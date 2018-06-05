package com.widget.skeleton

import android.support.v7.widget.RecyclerView
import android.view.View

object Skeleton {

    fun bind(recyclerView: RecyclerView): RecyclerViewSkeletonScreen.Builder {
        return RecyclerViewSkeletonScreen.Builder(recyclerView)
    }

    fun bind(view: View): ViewSkeletonScreen.Builder {
        return ViewSkeletonScreen.Builder(view)
    }

}
