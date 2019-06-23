package com.yyj.bestbase;

import android.app.Application;
import android.content.Context;

public class MApplication extends Application {

    /**
     * 更新方式
     */
    public interface UpdateStatus{
        /**
         * 此版本强制更新
         */
        int FORCE = 0;
        /**
         * 此版本可选更新
         */
        int NO_FORCE=1;
    }


    private static Context instance;
    public static Context getInstance() {
        return  instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
