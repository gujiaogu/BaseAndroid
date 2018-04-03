package com.yunshu.commonlib.util;

import android.util.Log;

import com.yunshu.commonlib.BuildConfig;

public class LogWrapper {

    private static final String TAG = "<<<<<<=====>>>>>>";

    public static void d(String info) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, info);
        }
    }

    public static void e(String info) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, info);
        }
    }

    public static void v(String info) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, info);
        }
    }

    public static void i(String info) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, info);
        }
    }

    public static void w(String info) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, info);
        }
    }

    public static void i(String tag, String msg){
        if (BuildConfig.DEBUG){
            Log.i(tag, msg);
        }
    }
    public static void v(String tag, String msg){
        if (BuildConfig.DEBUG){
            Log.v(tag, msg);
        }
    }
    public static void d(String tag, String msg){
        if (BuildConfig.DEBUG){
            Log.d(tag, msg);
        }
    }
    public static void w(String tag, String msg){
        if (BuildConfig.DEBUG){
            Log.w(tag, msg);
        }
    }
    public static void e(String tag, String msg){
        if (BuildConfig.DEBUG){
            Log.e(tag, msg);
        }
    }
}
