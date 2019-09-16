package com.yyj.bestbase.base.observer;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class MObserver implements Observer {
    @Override
    public void onSubscribe(Disposable d) {

    }


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
