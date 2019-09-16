package com.yyj.bestbase.base.observer;

import com.yyj.bestbase.BestBase;
import com.yyj.bestbase.base.BaseActivity;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    private CompositeDisposable compositeDisposable;
    private BaseActivity activity;

    protected BaseObserver(CompositeDisposable compositeDisposable, BaseActivity activity) {
        this.compositeDisposable = compositeDisposable;
        this.activity = activity;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (compositeDisposable != null)
            compositeDisposable.add(d);
    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof BestBase.NetErrorException) {
            handleNetError(e);
        } else if (e instanceof BestBase.ServerErrorException) {
            handleServerError(e);
        } else if (e instanceof BestBase.OtherException) {
            handleOtherError(e);
        } else if (e instanceof BestBase.NoLoginException) {
            handleNoLoginError(e);
        } else {
            handleError(e);
        }
    }

    protected void handleNoLoginError(Throwable e) {
        activity.toast(e.getMessage());
    }

    protected void handleError(Throwable e) {
        activity.toast(e.getMessage());
    }

    protected void handleServerError(Throwable e) {
        activity.toast(e.getMessage());
    }

    protected void handleOtherError(Throwable e) {
        activity.toast(e.getMessage());
    }

    protected void handleNetError(Throwable e) {
        activity.toast(e.getMessage());
    }

    @Override
    public void onComplete() {

    }
}
