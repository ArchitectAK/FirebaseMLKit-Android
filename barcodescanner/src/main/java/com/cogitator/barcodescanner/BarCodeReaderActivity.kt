package com.cogitator.barcodescanner

import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.View
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_barcode.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Ankit Kumar on 09/10/2018
 */
class BarCodeReaderActivity : BaseCameraActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet(R.layout.activity_barcode)
    }

    override fun onClick(v: View) {
        fabProgressCircle.show()
        cameraView.captureImage { cameraKitImage ->
            // Get the Bitmap from the captured shot
            getQRCodeDetailsFromBitmap(cameraKitImage.bitmap)
            runOnUiThread {
                showPreview()
                imagePreview.setImageBitmap(cameraKitImage.bitmap)
            }
        }
    }

    private fun getQRCodeDetailsFromBitmap(bitmap: Bitmap) {
        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                .build()
        val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        detector.detectInImage(image)


                .addOnSuccessListener {
                    for (firebaseBarcode in it) {

                        codeData.text = firebaseBarcode.displayValue //Display contents inside the barcode

                        when (firebaseBarcode.valueType) {
                            //Handle the URL here
                            FirebaseVisionBarcode.TYPE_URL -> firebaseBarcode.url
                            // Handle the contact info here, i.e. address, name, phone, etc.
                            FirebaseVisionBarcode.TYPE_CONTACT_INFO -> firebaseBarcode.contactInfo
                            // Handle the wifi here, i.e. firebaseBarcode.wifi.ssid, etc.
                            FirebaseVisionBarcode.TYPE_WIFI -> firebaseBarcode.wifi
                            //Handle more type of Barcodes
                        }

                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    Toast.makeText(baseContext, "Sorry, something went wrong!", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    fabProgressCircle.hide()
                    sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
    }

}