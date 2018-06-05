package com.widget.skeleton

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.extensions.R

class ShimmerViewHolder(inflater: LayoutInflater, parent: ViewGroup, innerViewResId: Int) : RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_shimmer, parent, false)) {

    init {
        val layout = itemView as ViewGroup
        inflater.inflate(innerViewResId, layout, true)
    }
}
