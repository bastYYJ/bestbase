package com.yyj.bestbase.base;


import android.support.annotation.NonNull;

import com.yyj.bestbase.base.impl.IPresenter;
import com.yyj.bestbase.base.impl.IView;

public abstract class BasePresenterImpl<T extends IView> implements IPresenter {
    protected T mView;

    @Override
    public void attachView(@NonNull IView iView) {
        mView = (T) iView;
    }
}
