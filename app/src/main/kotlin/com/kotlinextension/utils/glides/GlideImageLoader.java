package com.kotlinextension.utils.glides;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideImageLoader {
    private ImageView mImageView;
    private CircleProgressBar mProgressBar;

    public GlideImageLoader(ImageView imageView, CircleProgressBar progressBar) {
        mImageView = imageView;
        mProgressBar = progressBar;
    }

    public void load(final String url, RequestOptions rOptions) {
        if (url == null || rOptions == null)
            return;

        onConnecting();

        ProgressAppGlideModule.expect(url, new ProgressAppGlideModule.UIonProgressListener() {
            @Override
            public void onProgress(long bytesRead, long expectedLength) {
                if (mProgressBar != null) {
                    mProgressBar.setProgressWithAnimation((int) (100 * bytesRead / expectedLength));
                }
            }

            @Override
            public float getGranualityPercentage() {
                return 1.0f;
            }
        });
        //noinspection unchecked
        Glide.with(mImageView.getContext())
            .load(url)
            .apply(rOptions)
            .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    ProgressAppGlideModule.forget(url);
                    onFinished();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    ProgressAppGlideModule.forget(url);
                    onFinished();
                    return false;
                }
            })
            .into(mImageView);
    }

    private void onConnecting() {
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    private void onFinished() {
        if (mProgressBar != null && mImageView != null) {
            mProgressBar.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        }
    }
}
