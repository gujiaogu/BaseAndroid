package com.yunshu.commonlib.okhttp;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Tyrese on 2017/12/14.
 *
 * 回调函数，运行在UI线程中
 */

interface OkHttpResultCallback {
    void onFailure(IOException e);
    void onResponse(Response response);
}
