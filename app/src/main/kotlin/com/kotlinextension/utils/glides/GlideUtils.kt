package com.kotlinextension.utils.glides

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.extensions.validation.isNull
import com.kotlinextension.R
import com.kotlinextension.widget.AspectRatioFrameLayout

fun AspectRatioFrameLayout.loadPhoto(url: String?) =
    if (isNull(url)) {
        setProgressVisible(false)
        getImageView().setImageResource(R.drawable.ic_launcher_background)
    } else {
        val drawable:Drawable = ContextCompat.getDrawable(this.context, R.drawable.ic_launcher_background)!!
        val corner = context.resources.getDimensionPixelSize(R.dimen._2sdp)

        val options = RequestOptions()
            .centerCrop()
            .transform(RoundedCornersTransformation(corner, 0 , RoundedCornersTransformation.CornerType.TOP))
            .placeholder(drawable)
            .error(drawable)
            .priority(Priority.NORMAL)

        GlideImageLoader(getImageView(), getProgressBar()).load(url, options)
    }
