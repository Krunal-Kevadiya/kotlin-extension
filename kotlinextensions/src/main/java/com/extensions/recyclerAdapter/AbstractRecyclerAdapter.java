package com.extensions.recyclerAdapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

abstract class AbstractRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            /*MediaWrapper media = mVideos.get(position);
            for (Object data : payloads) {
                switch ((int) data) {
                    case UPDATE_THUMB:
                        AsyncImageLoader.loadPicture(holder.thumbView, media);
                        break;
                    case UPDATE_TIME:
                        fillView(holder, media);
                        break;
                }
            }*/
        }

        /*if(holder instanceof RecyclerBindingViewHolder)
            ((RecyclerBindingViewHolder) holder).bind(position, getItem(position));
        else
            ((RecyclerViewHolder) holder).bind(position, getItem(position));*/
    }

    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof RecyclerBindingViewHolder)
            ((RecyclerBindingViewHolder) holder).bind(position, getItem(position));
        else
            ((RecyclerViewHolder) holder).bind(position, getItem(position));
    }

    protected abstract Object getItem(int position);
}
