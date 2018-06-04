package com.cogitator.firebasemlkit.textRecognizer

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.cogitator.firebasemlkit.R
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.wonderkiln.camerakit.*
import kotlinx.android.synthetic.main.activity_text_recog.*


/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 25/05/2018 (MM/DD/YYYY)
 */
class TextRecognizerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recog)



        camView.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) {

            }

            override fun onError(cameraKitError: CameraKitError) {

            }

            override fun onImage(cameraKitImage: CameraKitImage) {

                var bitmap = cameraKitImage.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, camView.width, camView.height, false)
                camView.stop()
                runTextRecognition(bitmap)

            }

            override fun onVideo(cameraKitVideo: CameraKitVideo) {

            }
        })

        cameraBtn.setOnClickListener {
            graphic_overlay_.clear()
            camView.start()
            camView.captureImage()
        }

    }

    private fun runTextRecognition(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance()
                .visionTextDetector
        detector.detectInImage(image)
                .addOnSuccessListener(
                        { texts -> processTextRecognitionResult(texts as FirebaseVisionText) })
                .addOnFailureListener(
                        { e ->
                            // Task failed with an exception
                            e.printStackTrace()
                        })
    }

    private fun processTextRecognitionResult(texts: FirebaseVisionText) {
        val blocks = texts.blocks
        if (blocks.size == 0) {
            Log.d("TAG", "No text found")
            return
        }
        graphic_overlay_.clear()
        for (i in blocks.indices) {
            val lines = blocks.get(i).lines
            for (j in lines.indices) {
                val elements = lines[j].elements
                for (k in elements.indices) {
                    val textGraphic = TextGraphic(graphic_overlay_, elements[k])
                    graphic_overlay_.add(textGraphic)

                }
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        camView.start()
    }

    public override fun onPause() {
        camView.stop()
        super.onPause()
    }


}