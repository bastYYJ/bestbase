package com.yyj.bestbase.help;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yyj.bestbase.BuildConfig;
import com.yyj.bestbase.MApplication;
import com.yyj.bestbase.base.BaseActivity;
import com.yyj.bestbase.base.BaseModelImpl;
import com.yyj.bestbase.base.observer.MyObserver;
import com.yyj.bestbase.bean.UpdateBean;
import com.yyj.bestbase.model.analyzeRule.AnalyzeHeaders;
import com.yyj.bestbase.model.impl.IHttpGetApi;
import com.yyj.bestbase.service.UpdateService;
import com.yyj.bestbase.utils.DeviceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.DOWNLOAD_SERVICE;

public class UpdateManager {
    private Activity activity;

    public static UpdateManager getInstance(Activity activity) {
        return new UpdateManager(activity);
    }

    private UpdateManager(Activity activity) {
        this.activity = activity;
    }

    public void checkUpdate(boolean showMsg, String host, String url, BaseActivity updateActivity) {


        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("imei", DeviceUtils.getIMEI(activity));
        queryMap.put("code", DeviceUtils.getVersionCode(activity)+"");

        BaseModelImpl.getInstance().getRetrofitString(host)
                .create(IHttpGetApi.class)
                .getMap(url,queryMap, AnalyzeHeaders.getMap(new HashMap<>()))
                .flatMap(response -> analyzeLastReleaseApi(response.body()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<UpdateBean>() {
                    @Override
                    public void onNext(UpdateBean updateBean) {
                        if (DeviceUtils.getVersionCode(activity.getApplicationContext()) < updateBean.getVersionCode()){
                            if (updateBean.getIsForce() == MApplication.UpdateStatus.FORCE){
                                ProgressDialog dialog = ProgressDialog.show(activity, "提示", "正在更新。。");
                                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                dialog.setMax(100);
                                UpdateService.startThis(activity,updateBean,dialog);
                            }else {
                                if (showMsg) {
                                    Intent intent = new Intent(activity, updateActivity.getClass());
                                    intent.putExtra("updateBean",updateBean);
                                    activity.startActivity(intent);
                                }
                            }
                        }else {
                            if (showMsg) {
                                Toast.makeText(activity, "当前已是最新版了！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (showMsg) {
                            Toast.makeText(activity, "检测新版本出错", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Observable<UpdateBean> analyzeLastReleaseApi(String jsonStr) {
        return Observable.create(emitter -> {
            try {
                emitter.onNext(JSON.parseObject(jsonStr, UpdateBean.class));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
                emitter.onComplete();
            }
        });
    }

    /**
     * 安装apk
     */
    public void installApk(File apkFile) {
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            Log.d("wwd", "Failed to launcher installing activity");
        }
    }

    public static String getSavePath(String fileName) {
        return Environment.getExternalStoragePublicDirectory(DOWNLOAD_SERVICE).getPath() + fileName;
    }
}
