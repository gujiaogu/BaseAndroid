package com.yunshu.commonlib.okhttp;

/**
 * OkHttpResult 回调函数，运行在UI线程中
 *
 * Created by Tyrese on 2017/12/26.
 */

public interface ResultDisposer {
    void onException(Exception e);
    void onFailure(String error);
    void onSuccess(String data);
}
