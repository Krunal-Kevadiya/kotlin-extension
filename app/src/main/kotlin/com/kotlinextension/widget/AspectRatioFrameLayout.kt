package com.kotlinextension.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.kotlinextension.R
import com.kotlinextension.utils.glides.CircleProgressBar
import com.widget.AspectRatioImageView

@Suppress("unused")
class AspectRatioFrameLayout : FrameLayout {
    private var widthRatio: Int = 1
    private var heightRatio: Int = 1

    private lateinit var imageView:AspectRatioImageView
    private lateinit var progressBar:CircleProgressBar

    private var imageParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER)
    private var progressParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER)

    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) {
            return
        }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioFrameLayout, defStyleAttr, defStyleRes)

        widthRatio = typedArray.getInteger(R.styleable.AspectRatioFrameLayout_ariv_widthRatio, DEFAULT_ASPECT_RATIO)
        heightRatio = typedArray.getInteger(R.styleable.AspectRatioFrameLayout_ariv_heightRatio, DEFAULT_ASPECT_RATIO)

        typedArray.recycle()

        validateRatio(widthRatio)
        validateRatio(heightRatio)

        setProgressAndImageView(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val sizePerRatio = width.toFloat() / widthRatio.toFloat()
        val height = Math.round(sizePerRatio * heightRatio)

        setMeasuredDimension(width, height)
    }

    fun getWidthRatio(): Int {
        return widthRatio
    }

    fun setWidthRatio(widthRatio: Int) {
        validateRatio(widthRatio)
        this.widthRatio = widthRatio
    }

    fun getHeightRatio(): Int {
        return heightRatio
    }

    fun setHeightRatio(heightRatio: Int) {
        validateRatio(heightRatio)
        this.heightRatio = heightRatio
    }

    private fun validateRatio(ratio: Int) {
        if (ratio <= 0) {
            throw IllegalArgumentException("ratio > 0")
        }
    }

    fun setProgressAndImageView(context :Context) {
        imageView = AspectRatioImageView(context)
        imageView.layoutParams = imageParams
        imageView.setWidthHeightRatio(getWidthRatio(), getHeightRatio())

        progressBar = CircleProgressBar(context)
        progressParams = LayoutParams(context.resources.getDimensionPixelSize(R.dimen._30sdp), context.resources.getDimensionPixelSize(R.dimen._30sdp), Gravity.CENTER)
        progressBar.layoutParams = progressParams

        addView(imageView)
        addView(progressBar)
    }

    fun getImageView(): ImageView = imageView

    fun getProgressBar(): CircleProgressBar = progressBar

    fun setProgressVisible(boolean :Boolean) {
        progressBar.visibility = if(boolean) View.VISIBLE else View.GONE
    }

    companion object {
        private const val DEFAULT_ASPECT_RATIO = 1

        fun getRoundedCroppedBitmap(bitmap:Bitmap, radius: Int):Bitmap {
            val finalBitmap:Bitmap = if (bitmap.width != radius || bitmap.height != radius)
                Bitmap.createScaledBitmap(bitmap, radius, radius,
                    false)
            else
                bitmap
            val output = Bitmap.createBitmap(finalBitmap.width,
                finalBitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)

            val paint = Paint()
            val rect = Rect(0, 0, finalBitmap.width,
                finalBitmap.height)

            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            paint.isDither = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.parseColor("#BAB399")
            canvas.drawCircle(finalBitmap.width / 2 + 0.7f,
                finalBitmap.height / 2 + 0.7f,
                finalBitmap.width / 2 + 0.1f, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(finalBitmap, rect, rect, paint)

            return output
        }
    }
}
