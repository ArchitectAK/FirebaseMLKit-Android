package com.cogitator.firebasemlkit.text_recognizer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.firebase.ml.vision.text.FirebaseVisionText
import android.graphics.RectF


/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 30/05/2018 (MM/DD/YYYY)
 */
class TextGraphic(overlay: GraphicOverlay, private val element: FirebaseVisionText.Element) : GraphicOverlay.Graphic(overlay) {

    private val TAG = "TextGraphic"
    private val TEXT_COLOR = Color.RED
    private val TEXT_SIZE = 54.0f
    private val STROKE_WIDTH = 4.0f

    private var rectPaint: Paint = Paint()
    private var textPaint: Paint = Paint()

    init {
        rectPaint = Paint()
        rectPaint.color = TEXT_COLOR
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = STROKE_WIDTH

        textPaint = Paint()
        textPaint.color = TEXT_COLOR
        textPaint.textSize = TEXT_SIZE
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()
    }

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    override fun draw(canvas: Canvas) {
        // Draws the bounding box around the TextBlock.
        val rect = RectF(element.boundingBox)
        canvas.drawRect(rect, rectPaint)

        // Renders the text at the bottom of the box.
        canvas.drawText(element.text, rect.left, rect.bottom, textPaint)
    }
}