package com.kotlinextension.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.util.Log
import com.extensions.dialogs.toast
import com.extensions.network.hasConnection
import com.kotlinextension.BR
import com.kotlinextension.R
import com.kotlinextension.databinding.ActivityMainBinding
import com.kotlinextension.base.BaseActivity
import com.kotlinextension.networking.model.custom.ApiResponse
import com.kotlinextension.networking.model.success.Item
import com.kotlinextension.networking.model.success.ResSearchRepo
import com.kotlinextension.ui.map.MapsActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {
	private lateinit var adapter: RepoRecyclerAdapter

	override fun getBindingVariable(): Int = BR.viewModel

	override fun getLayoutId(): Int = R.layout.activity_main

	override fun getViewModel(): MainViewModel {
		return ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
	}

	override fun onBackPressed() {
		super.onBackPressed()
		finish()
	}

	override fun initObserve() {
		adapter = RepoRecyclerAdapter()
		adapter.setItemClickListener(object : OnItemClickListener {
			override fun onItemClick(item: Item) {
				openMapActivity()
			}
		})
		mViewDataBinding.recyclerRepo.adapter = adapter

		if (hasConnection) {
			showProgressDialog()
			mViewModel.searchRepositories()
				.observe(this, Observer {
					it?.let { it1 -> setData(it1) }
				})
		} else
            toast(R.string.disable_network)
	}

	private fun setData(response: ApiResponse<ResSearchRepo>) {
		hideProgressDialog()
		if (response.isSuccessful)
			response.body?.items?.let { adapter.updateAll(it) }
		else {
			Log.e("MainActivity", response.errorMessage)
			response.errorMessage?.let { toast(it) }
		}
	}

	override fun openMapActivity() {
		startActivity(MapsActivity.getActivity(this))
	}
}
