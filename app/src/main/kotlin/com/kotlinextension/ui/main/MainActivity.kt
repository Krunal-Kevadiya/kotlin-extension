package com.kotlinextension.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.extensions.recyclerAdapter.RecyclerAdapter
import com.extensions.recyclerAdapter.ex.loadmore.RecyclerMoreLoader
import com.kotlinextension.BR
import com.kotlinextension.R
import com.kotlinextension.base.BaseActivity
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.databinding.ActivityMainBinding
import com.kotlinextension.databinding.RecyclerItemRepoBinding
import com.kotlinextension.ui.map.MapsActivity
import com.kotlinextension.utils.glides.loadPhoto

class MainActivity :BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {
    private lateinit var adapter :RecyclerAdapter
    override fun getBindingVariable() :Int = BR.viewModel

    override fun getLayoutId() :Int = R.layout.activity_main

    override fun getViewModel() :MainViewModel {
        return ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getAllApiUsers()
    }

    override fun initObserve() {
        mViewDataBinding.recyclerRepo.layoutManager = LinearLayoutManager(baseContext)
        (mViewDataBinding.recyclerRepo.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        adapter = RecyclerAdapter.create()
            .registerBinding<User, RecyclerItemRepoBinding>(R.layout.recycler_item_repo, BR.data)
            { position, bean, binding ->
                binding.imgProfile.loadPhoto(bean.picture)
                binding.imgDelete.setOnClickListener {
                    mViewModel.deleteDbUser(position, bean, adapter)
                }
                binding.constraintLayout.setOnClickListener {
                    openMapActivity()
                }
            }
            .enableLoadMore(false, object :RecyclerMoreLoader(baseContext, false) {
                override fun onLoadMore() {
                    mViewModel.getAllApiUsers()
                }

                override fun hasMore() :Boolean {
                    return adapter.itemList.size < 200
                }
            })
            .attachTo(mViewDataBinding.recyclerRepo)
        mViewDataBinding.recyclerRepo.adapter = adapter

        mViewModel.getAllDbUsers(adapter)
    }

    override fun openMapActivity() {
        startActivity(MapsActivity.getActivity(this))
    }
}

