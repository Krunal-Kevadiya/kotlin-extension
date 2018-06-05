package com.widget.skeleton

import android.support.annotation.IntRange
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import io.supercharge.shimmerlayout.ShimmerLayout

class SkeletonAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mItemCount: Int = 0
    private var mLayoutReference: Int = 0
    private var mColor: Int = 0
    private var mShimmer: Boolean = false
    private var mShimmerDuration: Int = 0
    private var mShimmerAngle: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (mShimmer) {
            ShimmerViewHolder(inflater, parent, mLayoutReference)
        } else object : RecyclerView.ViewHolder(inflater.inflate(mLayoutReference, parent, false)) {

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mShimmer) {
            val layout = holder.itemView as ShimmerLayout
            layout.setShimmerAnimationDuration(mShimmerDuration)
            layout.setShimmerAngle(mShimmerAngle)
            layout.setShimmerColor(mColor)
            layout.startShimmerAnimation()
        }
    }

    override fun getItemCount(): Int {
        return mItemCount
    }

    fun setLayoutReference(layoutReference: Int) {
        this.mLayoutReference = layoutReference
    }

    fun setItemCount(itemCount: Int) {
        this.mItemCount = itemCount
    }

    fun setShimmerColor(color: Int) {
        this.mColor = color
    }

    fun shimmer(shimmer: Boolean) {
        this.mShimmer = shimmer
    }

    fun setShimmerDuration(shimmerDuration: Int) {
        this.mShimmerDuration = shimmerDuration
    }

    fun setShimmerAngle(@IntRange(from = 0, to = 30) shimmerAngle: Int) {
        this.mShimmerAngle = shimmerAngle
    }
}
