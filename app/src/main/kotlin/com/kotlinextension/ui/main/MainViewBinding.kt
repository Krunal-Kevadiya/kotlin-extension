package com.kotlinextension.ui.main

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.kotlinextension.R
import com.squareup.picasso.Picasso

object MainViewBinding {
	@JvmStatic
	@BindingAdapter("avatar_url")
	fun setTimeAgo(imageView: ImageView, url: String) {
		Picasso
			.with(imageView.context)
			.load(url)
			.placeholder(R.mipmap.ic_launcher)
			.error(R.mipmap.ic_launcher)
			.into(imageView)
	}
}
