package com.kotlinextension.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.util.Log
import com.kotlinextension.BR
import com.kotlinextension.R
import com.kotlinextension.base.BaseActivity
import com.kotlinextension.data.db.entity.User
import com.kotlinextension.databinding.ActivityMainBinding
import com.kotlinextension.databinding.RecyclerItemRepoBinding
import com.kotlinextension.ui.map.MapsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import com.kotlinextension.utils.GenericRecyclerAdapter

class MainActivity :BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator {
    private lateinit var adapter :GenericRecyclerAdapter
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
        callApiGetUsers()
    }
    override fun initObserve() {
        mViewDataBinding.recyclerRepo.layoutManager = WrapContentLinearLayoutManager(baseContext)
        (mViewDataBinding.recyclerRepo.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        adapter = GenericRecyclerAdapter.create()
            .registerBinding<User, RecyclerItemRepoBinding>(R.layout.recycler_item_repo, BR.data) { _,_,_ ->

            }
            /*.registerBinding<User, RecyclerItemRepoBinding>(R.layout.recycler_item_repo, BR.data) {_, bean, binding ->
                binding.imgProfile.loadPhoto(bean.picture)
                binding.constraintLayout.setOnClickListener {
                    openMapActivity()
                }
            }
            .enableDiff(object :RecyclerDiffUtil.Callback {
                override fun areItemsTheSame(oldItem :Any?, newItem :Any?) :Boolean {
                    return if(oldItem is User && newItem is User)
                        oldItem.id == newItem.id
                    else
                        false
                }

                override fun areContentsTheSame(oldItem :Any?, newItem :Any?) :Boolean {
                    return if(oldItem is User && newItem is User)
                        oldItem == newItem
                    else
                        false
                }
            })
            .enableLoadMore(false, object :RecyclerMoreLoader(baseContext, false) {
                override fun onLoadMore(handler :Handler?) {
                    callApiGetUsers()
                    handler?.loadCompleted(null)
                }

                override fun hasMore() :Boolean {
                    return adapter.itemList.size < 200
                }
            })*/
            .attachTo(mViewDataBinding.recyclerRepo)
        mViewDataBinding.recyclerRepo.adapter = adapter

        mViewModel.getAllDbUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({items ->
                adapter.addAll(items as MutableList<Any>)
            }, {throwable ->
                Log.e("Adapter", "error " + throwable.localizedMessage)
            })
    }

    fun callApiGetUsers() {
        mViewModel.getAllUsers()
    }

    override fun openMapActivity() {
        startActivity(MapsActivity.getActivity(this))
    }

    inner class WrapContentLinearLayoutManager(context :Context) :LinearLayoutManager(context) {
        override fun onLayoutChildren(recycler :RecyclerView.Recycler?, state :RecyclerView.State) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch(e :IndexOutOfBoundsException) {
                Log.e("Error", "IndexOutOfBoundsException in RecyclerView happens")
            }
        }
    }
}
