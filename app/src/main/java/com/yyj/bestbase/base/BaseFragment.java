package com.yyj.bestbase.base;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyj.bestbase.base.impl.IPresenter;
import com.yyj.bestbase.base.impl.IView;

import java.util.Objects;

import static com.yyj.bestbase.base.BaseActivity.START_SHEAR_ELE;

public abstract class BaseFragment<T extends IPresenter> extends Fragment implements IView {
    protected View view;
    protected Bundle savedInstanceState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        initSDK();
        view = createView(inflater, container);
        initData();
        bindView();
        bindEvent();
        firstRequest();
        return view;
    }

    /**
     * 事件触发绑定
     */
    protected void bindEvent() {

    }

    /**
     * 控件绑定
     */
    protected void bindView() {

    }

    /**
     * 数据初始化
     */
    protected void initData() {

    }

    /**
     * 首次逻辑操作
     */
    protected void firstRequest() {

    }

    /**
     * 加载布局
     */
    protected abstract View createView(LayoutInflater inflater, ViewGroup container);

    /**
     * 第三方SDK初始化
     */
    protected void initSDK() {

    }

    protected void startActivityByAnim(Intent intent, int animIn, int animExit) {
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(animIn, animExit);
    }

    protected void startActivityByAnim(Intent intent, @NonNull View view, @NonNull String transitionName, int animIn, int animExit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.putExtra(START_SHEAR_ELE, true);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, transitionName);
            startActivity(intent, options.toBundle());
        } else {
            startActivityByAnim(intent, animIn, animExit);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
