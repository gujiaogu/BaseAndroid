package com.yunshu.baseandroid.view;

/**
 * Created by Tyrese on 2017/11/16.
 */

public interface PlayerCallback {
    void startVideo();
    void onStatePause();
    void onStatePreparing();
    void onStateError();
    void onStateAutoComplete();
}
