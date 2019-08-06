package com.yyj.bestbase.utils;

import android.annotation.SuppressLint;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yyj.bestbase.MApplication;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class VolleyUtil {


    private final RequestQueue mRequestQueue = Volley.newRequestQueue(MApplication.getInstance());
    @SuppressLint("StaticFieldLeak")
    private static volatile VolleyUtil volleyUtil = null;

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


}
