package com.yyj.bestbase.base;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.yyj.bestbase.base.impl.IPresenter;
import com.yyj.bestbase.base.impl.IView;

import java.util.Objects;

import static com.yyj.bestbase.base.BaseActivity.START_SHEAR_ELE;

public abstract class BaseFragment<T extends IPresenter> extends QMUIFragment implements IView {
    protected View view;
    protected Bundle savedInstanceState;
    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = initInjector();
        attachView();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        initSDK();
        view = createView(inflater, container);
        bindView();
        initData();
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
     * 第三方SDK初始化
     */
    protected void initSDK() {

    }

    /**
     * 加载布局
     */
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(createLayoutId(), container, false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * @return LayoutId
     */
    public abstract int createLayoutId();

    /**
     * P层绑定   若无则返回null;
     */
    protected abstract T initInjector();

    /**
     * P层绑定V层
     */
    private void attachView() {
        if (null != mPresenter) {
            mPresenter.attachView(this);
        }
    }

    /**
     * P层解绑V层
     */
    private void detachView() {
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }


    @Override
    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toast(int id) {
        toast(getResources().getString(id));
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

}
