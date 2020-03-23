package com.yyj.bestbase.base.observer;

import com.yyj.bestbase.BestBase;
import com.yyj.bestbase.base.BaseActivity;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class HttpObserver<T> implements Observer<T> {

    private CompositeDisposable compositeDisposable;
    private BaseActivity activity;

    protected HttpObserver(CompositeDisposable compositeDisposable, BaseActivity activity) {
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
        defaultHandleError(e);
    }

    protected void handleError(Throwable e) {
        defaultHandleError(e);
    }

    protected void handleServerError(Throwable e) {
        defaultHandleError(e);
    }

    protected void handleOtherError(Throwable e) {
        defaultHandleError(e);
    }

    protected void handleNetError(Throwable e) {
        defaultHandleError(e);
    }


    protected void defaultHandleError(Throwable e) {
        activity.toast(e.getMessage());
    }

    @Override
    public void onComplete() {

    }
}
