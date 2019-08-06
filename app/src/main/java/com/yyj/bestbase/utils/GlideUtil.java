package com.yyj.bestbase.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.yyj.bestbase.MApplication;
import com.yyj.bestbase.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

public final class GlideUtil {

    public static void sendImageLoader(String url, ImageView target) {
        sendImageLoader(url, target, R.drawable.loading, R.drawable.load_error);
    }

    public static void preload(String url, ImageView target) {
        Glide.with(MApplication.getInstance())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .preload();
    }


    public static void sendImageLoader(String url, ImageView target, int defaultRes, int errorRes) {
        Glide.with(MApplication.getInstance())
                .load(url).placeholder(defaultRes).error(errorRes)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//硬盘缓存机制
                .into(target);
    }

    public static void sendImageLoader(String url, Target<Drawable> target) {
        Glide.with(MApplication.getInstance()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//硬盘缓存机制
                .into(target);
    }

    public static void BlurTransformation(String url, ImageView target) {
        Glide.with(MApplication.getInstance())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(new BlurTransformation(10))
                .into(target);

    }

    public static void BlurTransformation(String url, Target<Drawable> target) {
        Glide.with(MApplication.getInstance())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(new BlurTransformation(10))
                .into(target);

    }

    public static SimpleTarget<Drawable> createSimpleTarget(View target) {
        return new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                target.setBackground(resource);
            }
        };
    }


}
