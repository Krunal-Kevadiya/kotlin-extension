package com.kotlinextension.base

import android.app.Dialog
import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.kotlinextension.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity<VDB : ViewDataBinding, BVM :BaseViewModel<*>> : AppCompatActivity(), HasSupportFragmentInjector {
	lateinit var mViewDataBinding: VDB
	lateinit var mViewModel: BVM
	private lateinit var dialog: Dialog

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> = fragmentInjector

	override fun onCreate(@Nullable savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		performDataBinding()
		initProgressDialog()
		initObserve()
	}

	private fun performDataBinding() {
		mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
		mViewModel = getViewModel()
		mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
		mViewDataBinding.executePendingBindings()
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
	abstract fun initObserve()

	fun initProgressDialog() {
		dialog = Dialog(this, R.style.ThemeDialogFullscreen)
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
