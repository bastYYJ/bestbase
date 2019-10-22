package com.yyj.bestbase.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.yyj.bestbase.R;
import com.yyj.bestbase.bean.UpdateBean;
import com.yyj.bestbase.help.UpdateManager;
import com.yyj.bestbase.utils.download.DownloadUtils;
import com.yyj.bestbase.utils.download.JsDownloadListener;

import java.io.File;
import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UpdateService extends Service {
    public static boolean isRunning = false;
    private static final String startDownload = "startDownload";
    private static final String stopDownload = "stopDownload";
    private UpdateBean updateBean;
    private Disposable disposableDown;
    private static ProgressDialog dialog;

    public static void startThis(Context context, UpdateBean updateBean, ProgressDialog dialog) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(startDownload);
        intent.putExtra("updateBean", updateBean);
        context.startService(intent);
        UpdateService.dialog = dialog;
    }

    public static void startThis(Context context, UpdateBean updateBean) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(startDownload);
        intent.putExtra("updateBean", updateBean);
        context.startService(intent);
        UpdateService.dialog = null;
    }

    public static void stopThis(Context context) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(stopDownload);
        context.startService(intent);
        UpdateService.dialog = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        //创建 Notification.Builder 对象
        updateNotification(0);
        RxBus.get().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if (disposableDown != null) {
            disposableDown.dispose();
        }
        stopForeground(true);
        RxBus.get().post("channel_download", -1);
        RxBus.get().unregister(this);
        dialog = null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action == null) {
                stopSelf();
            } else {
                switch (action) {
                    case startDownload:
                        updateBean = intent.getParcelableExtra("updateBean");
                        downloadApk(updateBean.getDownLoadUrl());
                        break;
                    case stopDownload:
                        stopDownload();
                        break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void stopDownload() {
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 更新通知
     */
    private void updateNotification(int state) {
        RxBus.get().post("channel_download", state);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_read_aloud")
                .setSmallIcon(R.drawable.ic_download)
                .setOngoing(true)
                .setContentTitle(getString(R.string.download_update))
                .setContentText(String.format(getString(R.string.progress_show), state, 100));
        //        .setContentIntent(getActivityPendingIntent());
        builder.addAction(R.drawable.ic_stop_black_24dp, getString(R.string.cancel), getThisServicePendingIntent());
        builder.setProgress(100, state, false);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        Notification notification = builder.build();
        int notificationId = 3425;
        startForeground(notificationId, notification);
        if (dialog!=null){
            dialog.setProgress(state);
        }
    }

   /* private PendingIntent getActivityPendingIntent() {
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.setAction("startActivity");
        intent.putExtra("updateBean", updateBean);
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }*/

    private PendingIntent getThisServicePendingIntent() {
        Intent intent = new Intent(this, this.getClass());
        intent.setAction(UpdateService.stopDownload);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void downloadApk(String apkUrl) {
        if (disposableDown != null) {
            return;
        }
        String apkFilePath = UpdateManager.getSavePath(apkUrl.substring(apkUrl.lastIndexOf("/")));
        File apkFile = new File(apkFilePath);
        DownloadUtils downloadUtils = new DownloadUtils("https://github.com", new JsDownloadListener() {
            @Override
            public void onStartDownload(long length) {

            }

            @Override
            public void onProgress(int progress) {
                updateNotification(progress);
            }

            @Override
            public void onFail(String errorInfo) {
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), errorInfo, Toast.LENGTH_SHORT).show());
                UpdateService.this.stopSelf();
            }
        });
        downloadUtils.download(apkUrl, apkFile, new Observer<InputStream>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposableDown = d;
            }

            @Override
            public void onNext(InputStream inputStream) {
            }

            @Override
            public void onError(Throwable e) {
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), "下载更新出错\n" + e.getMessage(), Toast.LENGTH_SHORT).show());
                UpdateService.this.stopSelf();
            }

            @Override
            public void onComplete() {
                if (dialog!=null){
                    UpdateManager.getInstance(dialog.getOwnerActivity()).installApk(apkFile);
                }
                UpdateService.this.stopSelf();
            }
        });

    }

}
