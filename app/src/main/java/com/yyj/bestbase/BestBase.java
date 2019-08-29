package com.yyj.bestbase;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

public class BestBase {

    /**
     * 更新方式
     */
    public interface UpdateStatus {
        /**
         * 此版本强制更新
         */
        int FORCE = 0;
        /**
         * 此版本可选更新
         */
        int NO_FORCE = 1;
    }


    private static Context instance;

    public static Context getInstance() {
        return instance;
    }

    public static void init(Context context,String appId,boolean isDebug) {
        instance = context;
        CrashReport.initCrashReport(context, appId, isDebug);
    }

}
