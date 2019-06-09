/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.google.assistant.embedded.v1alpha2.SpeechRecognitionResult
import com.google.auth.Credentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.OAuth2Credentials
import com.google.protobuf.ByteString

import org.json.JSONException

import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.nio.ByteBuffer
import java.util.ArrayList
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ClientInterceptors
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.auth.MoreCallCredentials
import io.grpc.stub.StreamObserver

//import com.google.assistant.embedded.v1alpha1.AudioInConfig;
//import com.google.assistant.embedded.v1alpha1.AudioOutConfig;
//import com.google.assistant.embedded.v1alpha1.ConverseConfig;
//import com.google.assistant.embedded.v1alpha1.ConverseRequest;
//import com.google.assistant.embedded.v1alpha1.ConverseResponse;
//import com.google.assistant.embedded.v1alpha1.ConverseState;
//import com.google.assistant.embedded.v1alpha1.EmbeddedAssistantGrpc;
//import com.google.assistant.embedded.v1alpha2.ConverseState;


class SpeechService : Service() {

    private val mBinder = SpeechBinder()
    private val mListeners = ArrayList<Listener>()
    private var mApi: EmbeddedAssistantGrpc.EmbeddedAssistantStub? = null

    private val DEFAULT_VOLUME = 100

    internal var deviceRegister: DeviceRegister? = null

    private val deviceRegisterConf: DeviceRegisterConf? = null

    internal lateinit var mAudioTrack: AudioTrack
    internal lateinit var mTextResponse: String//audiotracker

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
                        val userRequest = value.speechResultsList.stream()
                            .map<String>(Function<SpeechRecognitionResult, String> { it.getTranscript() })
                            .collect<String, *>(Collectors.joining(" "))

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
        /*
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
*/
    /**
     * Starts recognizing speech audio.
     *
     * @param sampleRate The sample rate of the audio.
     */

    private var vConversationState = ByteString.EMPTY

    private val mFetchAccessTokenRunnable = { fetchAccessToken() }

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

    override fun onCreate() {
        super.onCreate()
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
            mApi = null
        }
    }

    private fun fetchAccessToken() {


        val channel = ManagedChannelBuilder.forTarget(HOSTNAME).build()
        try {
            val cred = Credentials_.fromResource(getApplicationContext(), R.raw.credentials)
            /*
            Config root = ConfigFactory.load();
            deviceRegisterConf = ConfigBeanFactory.create(root.getConfig("reference.conf/deviceRegister"), DeviceRegisterConf.class);
            AssistantConf assistantConf = ConfigBeanFactory.create(root.getConfig("assistant"), AssistantConf.class);


            deviceRegister = new DeviceRegister(deviceRegisterConf, ((UserCredentials) cred).getRefreshToken());
            deviceRegister.register();
*/
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

        if (mApi == null) {
            Log.w(TAG, "API not ready. Ignoring the request.")
            return
        }
        //Log.d(TAG,"request sending");
        for (listener in mListeners) {
            listener.onRequestStart()
            // Log.d(TAG,"request sending");
        }
        // Configure the API
        //TODO: is it wrong?
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
                    //TODO: load device data
                    .setDeviceModelId("superproject-64d1c-super-project-b2szge")
                    .setDeviceId("superproject_dev_1")
                    //                        .setDeviceModelId(deviceRegister.getDeviceModel().getDeviceModelId())
                    //                        .setDeviceId(deviceRegister.getDevice().getId())
                    .build()
            )
        mRequestObserver!!.onNext(
            AssistRequest.newBuilder()
                .setConfig(assistConfigBuilder.build())
                .build()
        )
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


    /**
     * Authenticates the gRPC channel using the specified [GoogleCredentials].
     */
    private class GoogleCredentialsInterceptor internal constructor(private val mCredentials: Credentials) :
        ClientInterceptor {

        private var mCached: Metadata? = null

        private var mLastMetadata: Map<String, List<String>>? = null

        public override fun <ReqT, RespT> interceptCall(
            method: MethodDescriptor<ReqT, RespT>, callOptions: CallOptions,
            next: Channel
        ): ClientCall<ReqT, RespT> {
            return object : ClientInterceptors.CheckedForwardingClientCall<ReqT, RespT>(
                next.newCall<ReqT, RespT>(method, callOptions)
            ) {
                @Throws(StatusException::class)
                protected override fun checkedStart(responseListener: ClientCall.Listener<RespT>, headers: Metadata) {
                    val cachedSaved: Metadata?
                    val uri = serviceUri(next, method)
                    synchronized(this) {
                        val latestMetadata = getRequestMetadata(uri)
                        if (mLastMetadata == null || mLastMetadata !== latestMetadata) {
                            mLastMetadata = latestMetadata
                            mCached = toHeaders(mLastMetadata)
                        }
                        cachedSaved = mCached
                    }
                    headers.merge(cachedSaved!!)
                    delegate().start(responseListener, headers)
                }
            }
        }

        /**
         * Generate a JWT-specific service URI. The URI is simply an identifier with enough
         * information for a service to know that the JWT was intended for it. The URI will
         * commonly be verified with a simple string equality check.
         */
        @Throws(StatusException::class)
        private fun serviceUri(channel: Channel, method: MethodDescriptor<*, *>): URI {
            val authority = channel.authority()
            if (authority == null) {
                throw Status.UNAUTHENTICATED
                    .withDescription("Channel has no authority")
                    .asException()
            }
            // Always use HTTPS, by definition.
            val scheme = "https"
            val defaultPort = 443
            val path = "/" + MethodDescriptor.extractFullServiceName(method.getFullMethodName())!!
            var uri: URI
            try {
                uri = URI(scheme, authority, path, null, null)
            } catch (e: URISyntaxException) {
                throw Status.UNAUTHENTICATED
                    .withDescription("Unable to construct service URI for auth")
                    .withCause(e).asException()
            }

            // The default port must not be present. Alternative ports should be present.
            if (uri.port == defaultPort) {
                uri = removePort(uri)
            }
            return uri
        }

        @Throws(StatusException::class)
        private fun removePort(uri: URI): URI {
            try {
                return URI(
                    uri.scheme, uri.userInfo, uri.host, -1 /* port */,
                    uri.path, uri.query, uri.fragment
                )
            } catch (e: URISyntaxException) {
                throw Status.UNAUTHENTICATED
                    .withDescription("Unable to construct service URI after removing port")
                    .withCause(e).asException()
            }

        }

        @Throws(StatusException::class)
        private fun getRequestMetadata(uri: URI): Map<String, List<String>> {
            try {
                return mCredentials.getRequestMetadata(uri)
            } catch (e: IOException) {
                throw Status.UNAUTHENTICATED.withCause(e).asException()
            }

        }

        private fun toHeaders(metadata: Map<String, List<String>>?): Metadata {
            val headers = Metadata()
            if (metadata != null) {
                for (key in metadata!!.keys) {
                    val headerKey = Metadata.Key.of<String>(
                        key, Metadata.ASCII_STRING_MARSHALLER
                    )
                    for (value in metadata!!.get(key)!!) {
                        headers.put<String>(headerKey, value)
                    }
                }
            }
            return headers
        }
    }


}
