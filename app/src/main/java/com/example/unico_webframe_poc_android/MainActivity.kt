package com.example.unico_webframe_poc_android

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.acesso.acessobio_android.AcessoBioListener
import com.acesso.acessobio_android.iAcessoBioSelfie
import com.acesso.acessobio_android.onboarding.AcessoBio
import com.acesso.acessobio_android.onboarding.camera.UnicoCheckCamera
import com.acesso.acessobio_android.onboarding.camera.UnicoCheckCameraOpener
import com.acesso.acessobio_android.onboarding.camera.selfie.SelfieCameraListener
import com.acesso.acessobio_android.services.dto.ErrorBio
import com.acesso.acessobio_android.services.dto.ResultCamera

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var unicoCheckCamera: UnicoCheckCamera
    lateinit var cameraListener: iAcessoBioSelfie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val callback = object : AcessoBioListener {
            override fun onErrorAcessoBio(errorBio: ErrorBio?) {
                Log.d("Response", "onErrorAcessoBio")
            }

            override fun onUserClosedCameraManually() {
                Log.d("Response", "userClosedCameraManually")
            }

            override fun onSystemClosedCameraTimeoutSession() {
                Log.d("Response", "systemClosedCameraTimeoutSession")
            }

            override fun onSystemChangedTypeCameraTimeoutFaceInference() {
                Log.d("Response", "systemChangedTypeCameraTimeoutFaceInference")
            }
        }

        val acessoBioBuilder = AcessoBio(this, callback)

        unicoCheckCamera = acessoBioBuilder
            .setAutoCapture(true)
            .setSmartFrame(true)
            .build()

        cameraListener = object : iAcessoBioSelfie {
            override fun onSuccessSelfie(result: ResultCamera?) {}

            override fun onErrorSelfie(errorBio: ErrorBio?) {}
        }
    }

    fun openCamera(view: View){
        Thread(Runnable {
            unicoCheckCamera.prepareSelfieCamera(
                "android-sem-liveness.json",
                object : SelfieCameraListener {
                    override fun onCameraReady(cameraOpener: UnicoCheckCameraOpener.Selfie?) {
                        cameraOpener?.open(cameraListener)
                    }

                    override fun onCameraFailed(message: String?) {
                        if (message != null) {
                            Log.e(TAG, message)
                        }
                    }
                })
        }).start()
    }

    fun openLivenessCamera(view: View){
        Thread(Runnable {
            unicoCheckCamera.prepareSelfieCamera(
                "android-com-liveness.json",
                object : SelfieCameraListener {
                    override fun onCameraReady(cameraOpener: UnicoCheckCameraOpener.Selfie?) {
                        cameraOpener?.open(cameraListener)
                    }

                    override fun onCameraFailed(message: String?) {
                        if (message != null) {
                            Log.e(TAG, message)
                        }
                    }
                })
        }).start()
    }
}