package com.carys.dyploma.activities

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.carys.dyploma.R
import com.carys.dyploma.dataModels.HomeSystem
import com.carys.dyploma.presenters.AuthPresenter
import com.carys.dyploma.voiceService.MessageDialogFragment
import com.carys.dyploma.voiceService.SpeechService
import com.carys.dyploma.voiceService.VoiceRecorder_
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class AuthActivity : AppCompatActivity(), MessageDialogFragment.MDListener, SpeechService.Listener, VoiceRecorder_.Callback {
    override fun onSpeechRecognized(text: String, isFinal: Boolean) {
        if (isFinal) {
            mVoiceRecorder.dismiss()
        }
    }

    override fun onSpeechResponse(text: String, isFinal: Boolean) {
        if (isFinal) {
            mVoiceRecorder.dismiss()
        }
    }

    override fun onRequestStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCredentialsSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mSpeechService: SpeechService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthActivityUI().setContentView(this)
        //mSpeechService = SpeechService()
    }

    private val AUDIO_PERMISSION = 1

    override fun onMessageDialogDismissed() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.RECORD_AUDIO), AUDIO_PERMISSION
        )
    }


    private var mVoiceRecorder = VoiceRecorder_(this)


    override fun onVoiceStart() {
        showStatus(true)
        mSpeechService.startRecognizing(mVoiceRecorder.sampleRate)
    }

    override fun onVoice(data: ByteArray, size: Int) {
        mSpeechService.recognize(data, size)
    }

    override fun onVoiceEnd() {
        showStatus(false)
        mSpeechService.finishRecognizing()
    }


    private fun showStatus(hearingVoice: Boolean) {
        println("I HEAR VOICES")
        //runOnUiThread { mStatus.setTextColor(if (hearingVoice) mColorHearing else mColorNotHearing) }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            toast("land")
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            toast("port")
        }
    }


    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, binder: IBinder) {
            mSpeechService = SpeechService.from(binder)
            mSpeechService.addListener(this@AuthActivity)
            //mStatus.setVisibility(View.VISIBLE)

            if (mSpeechService != null) {
                //you can even read from file and set it to google assistant
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            // mSpeechService = null
        }

    }

    /*override fun onStart() {

        super.onStart()


        bindService(Intent(this, SpeechService::class.java), mServiceConnection, BIND_AUTO_CREATE)

        // Start listening to voices
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            //startVoiceRecorder()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            showPermissionMessageDialog()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                AUDIO_PERMISSION
            )
        }
    }*/

    private val FRAGMENT_MESSAGE_DIALOG = "message_dialog"

    private fun showPermissionMessageDialog() {
        MessageDialogFragment
            .newInstance("This app needs to record audio and recognize your speech.")
            .show(supportFragmentManager, FRAGMENT_MESSAGE_DIALOG)
    }

    private fun startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop()
        }
        mVoiceRecorder = VoiceRecorder_(this)
        mVoiceRecorder.start()
    }

    private fun stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop()
            //mVoiceRecorder = null
        }
    }

    class AuthActivityUI() : AnkoComponent<AuthActivity> {

        private val presenter = AuthPresenter(this)
        lateinit var myui: AnkoContext<AuthActivity>
        lateinit var username: EditText
        lateinit var password: EditText
        lateinit var image: ImageView
        lateinit var login: Button
        lateinit var progress: ProgressBar

        private val customStyle = { v: Any ->
            when (v) {
                is Button -> v.textSize = myui.resources.getDimension(R.dimen.auth_button)
                is EditText -> v.textSize = myui.resources.getDimension(R.dimen.auth_text)
            }
        }

        override fun createView(ui: AnkoContext<AuthActivity>) = with(ui) {

            myui = ui
            verticalLayout {
                padding = dip(resources.getDimension(R.dimen.auth_margin))

                image = imageView(R.mipmap.bro).lparams {
                    margin = dip(resources.getDimension(R.dimen.auth_margin))
                    gravity = Gravity.CENTER
                }

                username = editText {
                    hintResource = R.string.username
                }
                password = editText {
                    hintResource = R.string.password
                    inputType = TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
                }
                login = button {
                    text = resources.getText(R.string.login_button)
                    onClick() {
                        presenter.authorize()
                    }
                }
                progress = progressBar {
                    incrementProgressBy(5)
                    visibility = ProgressBar.INVISIBLE
                }
            }.applyRecursively(customStyle)
        }

        private fun launchActivity(home: HomeSystem) = with(myui) {
            val intent = Intent(ctx, MainViewActivity::class.java)
            intent.putExtra("HomeSystem", home)
            ctx.startActivity(intent)
            (ctx as Activity).finish()
        }

        fun toast(msg: String?) = with(myui) {
            if (msg != null) {
                ctx.toast(msg)
            }
        }

        fun messageSystems(list: ArrayList<HomeSystem>) = with(myui) {
            GlobalScope.launch(Dispatchers.Main) {
                selector(resources.getText(R.string.system_selector), list.map { it.name })
                { _, i -> launchActivity(list[i]) }
            }
        }
    }
}