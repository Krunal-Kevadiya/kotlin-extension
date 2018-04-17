package com.extensions.recyclerAdapter.ex.loadmore;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.extensions.recyclerAdapter.RecyclerAdapter;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public abstract class RecyclerMoreLoader extends RecyclerView.OnScrollListener {
    private RecyclerLoadMoreView loadMoreView;
    private boolean loading;
    private LoadMoreViewCreator loadMoreViewCreator;
    private Context context;

    private RecyclerAdapter recyclerAdapter;

    private boolean isLoadMoreReverse;

    public RecyclerMoreLoader(Context context, boolean isLoadMoreReverse, LoadMoreViewCreator creator) {
        this.context = context;
        this.loadMoreViewCreator = creator;
        this.isLoadMoreReverse = isLoadMoreReverse;
        this.loadMoreViewCreator.attachLoader(this);
        initHandler();
    }

    public RecyclerMoreLoader(Context context, boolean isLoadMoreReverse) {
        this(context, isLoadMoreReverse, new SimpleLoadMoreViewCreator(context));
    }

    public void setRecyclerAdapter(RecyclerAdapter recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
    }

    private void initHandler() {
    }

    public RecyclerLoadMoreView getLoadMoreView() {
        if (loadMoreView == null) {
            loadMoreView = new RecyclerLoadMoreView(context, loadMoreViewCreator);
        }
        return loadMoreView;
    }

    public abstract void onLoadMore();

    public abstract boolean hasMore();

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                if(isLoadMoreReverse) {
                    int first = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    if (NO_POSITION == first)
                        break;
                    if (recyclerAdapter.getItem(first) == this && !loading)
                        loadMore();
                } else {
                    int last = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (NO_POSITION == last)
                        break;
                    if (recyclerAdapter.getItem(last) == this && !loading) {
                        loadMore();
                    }
                }
                break;
            default:
                break;
        }
    }

    void loadMore() {
        if (hasMore()) {
            loading = true;
            getLoadMoreView().visibleLoadingView(isLoadMoreReverse);
            onLoadMore();
        } else {
            reset();
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public void reset() {
        loading = false;
        if (!hasMore())
            getLoadMoreView().visibleNoMoreView(isLoadMoreReverse);

        getLoadMoreView().invisibleView(isLoadMoreReverse);
    }

    public void error() {
        loading = false;
        getLoadMoreView().invisibleView(isLoadMoreReverse);
        getLoadMoreView().visibleErrorView(isLoadMoreReverse);
    }
}
