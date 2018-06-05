package com.cogitator.facedetection

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.activity_face_detection.*


/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 04/06/2018 (MM/DD/YYYY)
 */
class FaceDetectionActivity : AppCompatActivity() {

    private lateinit var mCurrentPhotoPath: String
    private val CAMERA_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)
        FirebaseApp.initializeApp(this@FaceDetectionActivity)
        title = "Face Detection"


        button_capture_.setOnClickListener({
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                }
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(this@FaceDetectionActivity,
                            "com.cogitator.facedetection.fileprovider",
                            photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageFile = File(mCurrentPhotoPath)
            val photo = BitmapFactory.decodeFile(imageFile.absolutePath)
            addEmojis(photo)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun addEmojis(photo: Bitmap) {
        val emojiPhoto = photo.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(emojiPhoto)

        val options = FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setMinFaceSize(0.1f)
                .setTrackingEnabled(false)
                .build()
        val image = FirebaseVisionImage.fromBitmap(photo)
        val detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options)
        var result = detector.detectInImage(image)
                .addOnSuccessListener({


                    // Task completed successfully
                    for (face in it) {

                        val leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)
                        val rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)
                        val nose = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE)
                        val mouth = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_MOUTH)

                        if (leftEye != null) {
                            val leftEyePosition = leftEye.position
                            var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_remove_red_eye_black_24dp)
                            val width = bitmap.width * 3
                            val height = bitmap.height * 3
                            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
                            canvas.drawBitmap(bitmap, leftEyePosition.x - width / 2, leftEyePosition.y - height / 2, null)
                        }

                        if (rightEye != null) {
                            val rightEyePosition = rightEye.position
                            var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_remove_red_eye_black_24dp)
                            val width = bitmap.width * 3
                            val height = bitmap.height * 3
                            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
                            canvas.drawBitmap(bitmap, rightEyePosition.x - width / 2, rightEyePosition.y - height / 2, null)
                        }

                        if (nose != null) {
                            val nosePosition = nose.position
                            var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_music_note_black_24dp)
                            val width = bitmap.width * 3
                            val height = bitmap.height * 3
                            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
                            canvas.drawBitmap(bitmap, nosePosition.x - width / 2, nosePosition.y - height, null)
                        }

                        if (mouth != null) {
                            val mouthPosition = mouth.position
                            var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_mouse_black_24dp)
                            val width = bitmap.width * 4
                            val height = bitmap.height * 4
                            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
                            canvas.drawBitmap(bitmap, mouthPosition.x - width, mouthPosition.y - height / 2, null)
                        }
                    }
                    canvas.save()
                    image_view_.setImageBitmap(emojiPhoto)

                })
    }
}