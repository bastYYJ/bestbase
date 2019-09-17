package com.yyj.bestbase;

import android.content.Context;

import com.tencent.bugly.Bugly;
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

    /**
     * 网络异常
     */
    public static class NetErrorException extends RuntimeException {
        public NetErrorException(String message) {
            super(message);
        }
    }

    /**
     * 服务器异常
     */
    public static class ServerErrorException extends RuntimeException {
        public ServerErrorException(String message) {
            super(message);
        }
    }

    /**
     * 未登录或登录失效
     */
    public static class NoLoginException extends RuntimeException {
        public NoLoginException(String message) {
            super(message);
        }
    }

    /**
     * 其他异常
     */
    public static class OtherException extends RuntimeException {
        public OtherException(String message) {
            super(message);
        }
    }


    private static Context instance;

    public static Context getInstance() {
        return instance;
    }

    public static void init(Context context, String appId, boolean isDebug) {
        instance = context;
        Bugly.init(context, appId, isDebug);
    }

}
