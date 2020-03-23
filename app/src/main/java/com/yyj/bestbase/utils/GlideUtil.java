package com.yyj.bestbase.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.yyj.bestbase.BestBase;
import com.yyj.bestbase.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

public final class GlideUtil {

    public static int defaultRes = R.drawable.loading;
    public static int errorRes = R.drawable.load_error;

    public static void sendImageLoader(String url, ImageView target) {
        sendImageLoader(url, target, defaultRes, errorRes);
    }

    public static void preload(String url, ImageView target) {
        Glide.with(BestBase.getInstance())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .preload();
    }


    public static void sendImageLoader(String url, ImageView target, int defaultRes, int errorRes) {
        Glide.with(BestBase.getInstance())
                .load(url).placeholder(defaultRes).error(errorRes)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//硬盘缓存机制
                .into(target);
    }

    public static void sendImageLoader(String url, Target<Drawable> target) {
        Glide.with(BestBase.getInstance()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//硬盘缓存机制
                .into(target);
    }

    public static void BlurTransformation(String url, ImageView target) {
        Glide.with(BestBase.getInstance())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(new BlurTransformation(10))
                .into(target);

    }

    public static void BlurTransformation(String url, Target<Drawable> target) {
        Glide.with(BestBase.getInstance())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(new BlurTransformation(10))
                .into(target);

    }

    public static void CropCircleTransformation(String url, ImageView target) {
        CropCircleTransformation(url, target, defaultRes, errorRes);
    }


    public static void CropCircleTransformation(String url, ImageView target, int defaultRes, int errorRes) {
        Glide.with(BestBase.getInstance())
                .load(url)
                .placeholder(defaultRes).error(errorRes)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .apply(RequestOptions.circleCropTransform())
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
