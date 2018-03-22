package com.kotlinextension.base

import android.app.Dialog
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.extensions.layout.inflateCustomBindView
import com.kotlinextension.R
import com.kotlinextension.di.Injectable
import javax.inject.Inject

abstract class BaseFragment<VDB : ViewDataBinding, BVM : BaseViewModel<*>> : Fragment(), Injectable {
	lateinit var mContext: Context
	lateinit var mViewDataBinding: VDB
	lateinit var mViewModel: BVM
	private lateinit var dialog: Dialog

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(false)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		mViewDataBinding = container!!.inflateCustomBindView(getLayoutId())
		return mViewDataBinding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mViewModel = getViewModel()
		mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
		mViewDataBinding.executePendingBindings()
		initProgressDialog()
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		mContext = context
	}

	override fun onPause() {
		super.onPause()
		hideProgressDialog()
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

	fun initProgressDialog() {
		dialog = Dialog(mContext, R.style.ThemeDialogFullscreen)
		dialog.setContentView(R.layout.layout_loading_view)
		dialog.setCancelable(false)
		dialog.setCanceledOnTouchOutside(false)
	}

	fun showProgressDialog() {
		if (!dialog.isShowing)
			dialog.show()
	}

	fun hideProgressDialog() {
		dialog.dismiss()
	}
}
