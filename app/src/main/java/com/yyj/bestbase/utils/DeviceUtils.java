package com.yyj.bestbase.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import static android.content.Context.TELEPHONY_SERVICE;

public class DeviceUtils {

    /**
     * 获取版本代码
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取版本名
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获得imei,需要权限READ_PHONE_STATE
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getIMEI(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return "";
        }
        return TelephonyMgr.getDeviceId();
    }

    //  判断手机的网络状态（是否联网）
    public static int getNetWorkInfo(Context context) {
        //通过上下文得到系统服务，参数为网络连接服务，返回网络连接的管理类
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //通过网络管理类的实例得到联网日志的状态，返回联网日志的实例
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //判断联网日志是否为空
        if (activeNetworkInfo == null) {
            //状态为空当前网络异常，没有联网
            return -1;
        }
        //不为空得到使用的网络类型
        return activeNetworkInfo.getType();
    }

}
