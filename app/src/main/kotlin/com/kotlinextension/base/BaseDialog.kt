package com.kotlinextension.base

import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.extensions.layout.inflateCustomBindView

abstract class BaseDialog<VDB : ViewDataBinding, BVM :BaseViewModel<*>> : DialogFragment() {
	lateinit var mContext: Context
	lateinit var mViewDataBinding: VDB
	lateinit var mViewModel: BVM

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		mViewDataBinding = container!!.inflateCustomBindView(getLayoutId())
		return mViewDataBinding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mViewModel = getViewModel()
		mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
		mViewDataBinding.executePendingBindings()
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		mContext = context
	}

	override fun onDestroy() {
		super.onDestroy()
		clearMemory()
	}

	override fun onLowMemory() {
		super.onLowMemory()
		clearMemory()
	}

	private fun clearMemory() {
		try {
			System.gc()
			Runtime.getRuntime().gc()
		} catch (e: Exception) {
		}
	}

	@LayoutRes
	abstract fun getLayoutId(): Int
	abstract fun getViewModel(): BVM
	abstract fun getBindingVariable(): Int
}
