package com.kotlinextension.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.extensions.collections.isNotNullOrEmpty
import com.extensions.dialogs.toast
import com.extensions.network.hasConnection
import com.extensions.recyclerAdapter.RecyclerAdapter
import com.kotlinextension.BR
import com.kotlinextension.R
import com.kotlinextension.base.BaseActivity
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.databinding.ActivityMainBinding
import com.kotlinextension.databinding.RecyclerItemRepoBinding
import com.kotlinextension.ui.map.MapsActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {
	private lateinit var adapter: RecyclerAdapter

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
        adapter = RecyclerAdapter.create()
            .registerBinding<User, RecyclerItemRepoBinding>(R.layout.recycler_item_repo, BR.data) { _, _, binding ->
                binding.constraintLayout.setOnClickListener{
                    openMapActivity()
                }
            }
            .attachTo(mViewDataBinding.recyclerRepo)
		mViewDataBinding.recyclerRepo.adapter = adapter

		if (hasConnection) {
			showProgressDialog()
			mViewModel.loadUsers()
				.observe(this, Observer {
					it?.let { it1 -> setData(it1) }
				})
		} else
            toast(R.string.disable_network)
	}

	private fun setData(response: List<User>) {
		hideProgressDialog()
        if(response.isNotNullOrEmpty())
            adapter.addAll(response)
	}

	override fun openMapActivity() {
		startActivity(MapsActivity.getActivity(this))
	}
}
