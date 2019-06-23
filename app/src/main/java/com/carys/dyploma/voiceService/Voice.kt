package com.carys.dyploma.voiceService

import android.app.Service
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.carys.dyploma.R

import com.google.assistant.embedded.v1alpha2.AssistConfig
import com.google.assistant.embedded.v1alpha2.AssistRequest
import com.google.assistant.embedded.v1alpha2.AssistResponse
import com.google.assistant.embedded.v1alpha2.AudioInConfig
import com.google.assistant.embedded.v1alpha2.AudioOutConfig
import com.google.assistant.embedded.v1alpha2.DeviceConfig
import com.google.assistant.embedded.v1alpha2.DialogStateIn
import com.google.assistant.embedded.v1alpha2.EmbeddedAssistantGrpc

import com.google.protobuf.ByteString

import org.json.JSONException

import java.io.IOException
import java.nio.ByteBuffer
import java.util.ArrayList
import java.util.Locale
import java.util.concurrent.TimeUnit

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.auth.MoreCallCredentials
import io.grpc.stub.StreamObserver



class SpeechService : Service() {

    private val mBinder = SpeechBinder()
    private val mListeners = ArrayList<Listener>()
    private lateinit var mApi: EmbeddedAssistantGrpc.EmbeddedAssistantStub

    private val DEFAULT_VOLUME = 100

    internal lateinit var mAudioTrack: AudioTrack
    internal lateinit var mTextResponse: String


    companion object {

        private val TAG = "SpeechService"

        val SCOPE = listOf<String>("https://www.googleapis.com/auth/assistant-sdk-prototype")
        private val HOSTNAME = "embeddedassistant.googleapis.com"
        private val PORT = 443
        private var mHandler: Handler? = null
        private val mVolumePercentage = 100 // for volume command

        fun from(binder: IBinder): SpeechService {
            return (binder as SpeechBinder).service
        }
    }

    override fun onCreate() {
        super.onCreate()
        println("runserv")
        mHandler = Handler()
        fetchAccessToken()

        val outputBufferSize = AudioTrack.getMinBufferSize(
            16000,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        try {
            mAudioTrack = AudioTrack(
                AudioManager.USE_DEFAULT_STREAM_TYPE,
                16000,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                outputBufferSize,
                AudioTrack.MODE_STREAM
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAudioTrack.setVolume(DEFAULT_VOLUME.toFloat())
            }
            mAudioTrack.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private val mResponseObserver = object : StreamObserver<AssistResponse> {
        override fun onNext(value: AssistResponse) {
            try {
                if (value.eventType != null && value.eventType != AssistResponse.EventType.EVENT_TYPE_UNSPECIFIED) {

                    Log.i("Event type : {}", value.eventType.name)
                }

                if (value.audioOut != null) {
                    //                    currentResponse.write(value.getAudioOut().getAudioData().toByteArray());
                    val data = value.audioOut.audioData.toByteArray()

                    val audioData = ByteBuffer.wrap(data)
                    //Log.d(TAG, "converse audio size: " + audioData.remaining());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mAudioTrack.write(audioData, audioData.remaining(), AudioTrack.WRITE_BLOCKING)
                    } else {
                        mAudioTrack.write(data, 0, data.size)
                    }
                }

                if (value.dialogStateOut != null) {
                    vConversationState = value.dialogStateOut.conversationState

                    if (value.speechResultsList != null) {
                        val userRequest = value.speechResultsList
                            .map { it.transcript }
                            .joinToString(" ")

                        if (!userRequest.isEmpty()) {
                            Log.i("Request Text : {}", userRequest)
                        }
                    }
                }

                if (value.dialogStateOut != null
                    && value.dialogStateOut.supplementalDisplayText != null
                    && !value.dialogStateOut.supplementalDisplayText.isEmpty()
                ) {

                    // Capturing string response for text query output
                    mTextResponse = value.dialogStateOut.supplementalDisplayText
                }

            } catch (e: Exception) {
            }
        }

        override fun onError(t: Throwable) {

            Log.e(TAG, "converse error:", t)
            for (listener in mListeners) {
                listener.onSpeechResponse("", false)
            }
        }

        override fun onCompleted() {

            Log.i(TAG, "assistant response finished")
            for (listener in mListeners) {
                listener.onSpeechResponse("", false)
            }
            mAudioTrack.play()
        }
    }


    private var mRequestObserver: StreamObserver<AssistRequest>? = null

    private//TODO: is it wrong?
    val defaultLanguageCode: String
        get() {
            val locale = Locale.getDefault()
            val language = StringBuilder(locale.getLanguage())
            val country = locale.getCountry()
            if (!TextUtils.isEmpty(country)) {
                language.append("-")
                language.append(country)
            }
            return language.toString()
        }

    /**
     * Starts recognizing speech audio.
     *
     * @param sampleRate The sample rate of the audio.
     */

    private var vConversationState = ByteString.EMPTY



    interface Listener {

        /**
         * Called when a new piece of text was recognized by the Speech API.
         *
         * @param text    The text.
         * @param isFinal `true` when the API finished processing audio.
         */
        fun onSpeechRecognized(text: String, isFinal: Boolean)

        fun onSpeechResponse(text: String, isFinal: Boolean)
        fun onRequestStart()

        fun onCredentialsSuccess()

    }



    override fun onDestroy() {
        super.onDestroy()
        mHandler!!.removeCallbacks(mFetchAccessTokenRunnable)
        mHandler = null
        // Release the gRPC channel.
        if (mApi != null) {
            val channel = mApi!!.getChannel() as ManagedChannel
            if (!channel.isShutdown()) {
                try {
                    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "Error shutting down the gRPC channel.", e)
                }

            }
            //TODO: destroy later
            //mApi = null
        }
    }

    private fun fetchAccessToken() {

        println("runserv")

        val channel = ManagedChannelBuilder.forTarget(HOSTNAME).build()
        try {
            val cred = Credentials_.fromResource(applicationContext, R.raw.credentials)
            mApi = EmbeddedAssistantGrpc.newStub(channel)
                .withCallCredentials(MoreCallCredentials.from(cred))


        } catch (e: IOException) {
            Log.e(TAG, "error creating assistant service:", e)
        } catch (e: JSONException) {
            Log.e(TAG, "error creating assistant service:", e)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }

        for (listener in mListeners) {
            listener.onCredentialsSuccess()

        }

    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    fun addListener(@NonNull listener: Listener) {
        mListeners.add(listener)
    }

    fun removeListener(@NonNull listener: Listener) {
        mListeners.remove(listener)
    }


    fun startRecognizing(sampleRate: Int) {

        try {
            //Log.d(TAG,"request sending");
            for (listener in mListeners) {
                listener.onRequestStart()
            }
            // Configure the API
            mRequestObserver = mApi!!.assist(mResponseObserver)
            Log.d("locale", Locale.getDefault().toString())
            val assistConfigBuilder = AssistConfig.newBuilder()
                .setAudioInConfig(
                    AudioInConfig.newBuilder()
                        .setEncoding(AudioInConfig.Encoding.LINEAR16)
                        .setSampleRateHertz(sampleRate)
                        .build()
                )
                .setAudioOutConfig(
                    AudioOutConfig.newBuilder()
                        .setEncoding(AudioOutConfig.Encoding.LINEAR16)
                        .setSampleRateHertz(sampleRate)
                        .setVolumePercentage(DEFAULT_VOLUME)
                        .build()
                )
                .setDialogStateIn(
                    DialogStateIn.newBuilder()
                        .setLanguageCode("en-US")
                        .setConversationState(vConversationState)
                        .build()
                )
                .setDeviceConfig(
                    DeviceConfig.newBuilder()
                        .setDeviceModelId("superproject-64d1c-super-project-b2szge")
                        .setDeviceId("superproject_dev_1")
                        .build()
                )
            mRequestObserver!!.onNext(
                AssistRequest.newBuilder()
                    .setConfig(assistConfigBuilder.build())
                    .build()
            )
        } catch(e: Exception) {
            Log.w(TAG, "API not ready. Ignoring the request.")
            return
        }
    }


    /**
     * Recognizes the speech audio. This method should be called every time a chunk of byte buffer
     * is ready.
     *
     * @param data The audio data.
     * @param size The number of elements that are actually relevant in the `data`.
     */
    fun recognize(data: ByteArray, size: Int) {
        if (mRequestObserver == null) {
            return
        }
        // Call the streaming recognition API
        mRequestObserver!!.onNext(
            AssistRequest.newBuilder()
                .setAudioIn(ByteString.copyFrom(data, 0, size))
                .build()
        )

    }

    /**
     * Finishes recognizing speech audio.
     */
    fun finishRecognizing() {
        if (mRequestObserver == null) {
            return
        }
        mRequestObserver!!.onCompleted()
        mRequestObserver = null
    }

    private inner class SpeechBinder : Binder() {

        internal val service: SpeechService
            get() {
                return this@SpeechService
            }
    }

    private val mFetchAccessTokenRunnable = Runnable { this.fetchAccessToken() }
}
