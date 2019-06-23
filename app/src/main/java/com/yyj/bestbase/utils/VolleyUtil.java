package com.yyj.bestbase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yyj.bestbase.MApplication;
import com.yyj.bestbase.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


public class VolleyUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context context = MApplication.getInstance();
    private final RequestQueue mRequestQueue = Volley.newRequestQueue(context);
    @SuppressLint("StaticFieldLeak")
    private static volatile VolleyUtil volleyUtil = null;
    private ImageLoader imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
        private LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024) / 8) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                File absoluteFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + key.substring(key.lastIndexOf("/") + 1));
                try {
                    absoluteFile.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(absoluteFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };

        @Override
        public Bitmap getBitmap(String imagePath) {
            Bitmap bitmap = mCache.get(imagePath);
            if (bitmap == null) {
                String absolutePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                File file = new File(absolutePath + "/" + imagePath.substring(imagePath.lastIndexOf("/") + 1));
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            }
            return bitmap;
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            mCache.put(s, bitmap);
        }
    });


    public static VolleyUtil getInstance() {
        synchronized (VolleyUtil.class) {
            if (volleyUtil == null) {
                synchronized (VolleyUtil.class) {
                    if (volleyUtil == null)
                        volleyUtil = new VolleyUtil();
                }
            }
        }
        return volleyUtil;
    }

    public interface ResultCall<T> {
        void handle(com.alibaba.fastjson.JSONObject jsonObject);
    }


    public final void sendStringRequest(int post, String url, Response.Listener<String> success, Response.ErrorListener error) {
        StringRequest stringRequest = new StringRequest(url, success, error);
        mRequestQueue.add(stringRequest);
    }

    public final void sendStringRequest(int method, String url, Response.Listener<String> success,
                                        Response.ErrorListener error, final Map<String, String> params, String... header) {
        StringRequest stringRequest = new StringRequest(method, url, success, error) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < header.length; i += 2) {
                    map.put(header[i], header[i] + 1);
                }
                return map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };

        mRequestQueue.add(stringRequest);
    }

    public final void sendJSONRequest(int method, String url, JSONObject jsonObject,
                                      Response.Listener<JSONObject> success, Response.ErrorListener error) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, jsonObject, success, error);
        mRequestQueue.add(jsonObjectRequest);
    }

    /**
     * @param decodeConfig 位图的属性配置
     */
    public final void sendImageRequest(String url, int maxWidth, int maxHeight, Bitmap.Config decodeConfig,
                                       Response.Listener<Bitmap> success, Response.ErrorListener errorListener) {
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                success.onResponse(bitmap);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ImageRequest imageRequest = new ImageRequest(url, success, maxWidth, maxHeight, decodeConfig, errorListener);
                mRequestQueue.add(imageRequest);
            }
        }, maxWidth, maxHeight);

    }

    /**
     * 默认的ImageRequest，最大宽、高不进行设定
     * Bitmap默认为占用最小配置
     */
    public final void sendImageRequest(String url, Response.Listener<Bitmap> success, Response.ErrorListener error) {
        sendImageRequest(url, 0, 0, Bitmap.Config.RGB_565, success, error);
    }

    /**
     * 拥有缓存机制的加载Image方式
     *
     * @param target     目标ImageView
     * @param defaultRes 默认显示图片
     * @param errorRes   失败时显示图片
     */
    public final void sendImageLoader(String url, ImageView target, int defaultRes, int errorRes) {


        ImageLoader.ImageListener imageListener = ImageLoader.
                getImageListener(target, defaultRes, errorRes);
        imageLoader.get(url, imageListener);
    }

    public final void sendImageLoader(String url, ImageView target) {
        if (url != null)
            sendImageLoader(url, target, R.drawable.loading, R.drawable.load_error);
    }


}
