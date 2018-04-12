package com.kotlinextension.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.util.Log
import com.extensions.recyclerAdapter.RecyclerAdapter
import com.kotlinextension.BR
import com.kotlinextension.R
import com.kotlinextension.base.BaseActivity
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.databinding.ActivityMainBinding
import com.kotlinextension.databinding.RecyclerItemRepoBinding
import com.kotlinextension.ui.map.MapsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

    override fun initObserve() {
        adapter = RecyclerAdapter.create()
            .registerBinding<User, RecyclerItemRepoBinding>(R.layout.recycler_item_repo, BR.data) {_, _, binding ->
                binding.constraintLayout.setOnClickListener {
                    openMapActivity()
                }
            }
            .attachTo(mViewDataBinding.recyclerRepo)
        mViewDataBinding.recyclerRepo.adapter = adapter

        mViewModel.getAllUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                //this.taskList = items
                Log.e("Adapter", "" + items.size)
            }, {throwable ->
                Log.e("Adapter", "" + throwable.localizedMessage)
            })
    }

    private fun setData(response :MutableList<User>) {
        hideProgressDialog()
        //if(response.isNotNullOrEmpty())
        //adapter.addAll(response as List<Any>?)
    }

    override fun openMapActivity() {
        startActivity(MapsActivity.getActivity(this))
    }
}
