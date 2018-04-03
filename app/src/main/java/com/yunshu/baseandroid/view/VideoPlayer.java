package com.yunshu.baseandroid.view;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * 播放器，基于ijkplayer
 * https://github.com/lipangit/JiaoZiVideoPlayer
 *
 * Created by Tyrese on 2017/11/16.
 */

public class VideoPlayer extends JZVideoPlayerStandard {

    private PlayerCallback callback;

    public VideoPlayer(Context context) {
        super(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void startVideo() {
        super.startVideo();
        if(callback != null) {
            callback.startVideo();
        }
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        if(callback != null) {
            callback.onStatePause();
        }
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        if(callback != null) {
            callback.onStatePreparing();
        }
    }

    @Override
    public void onStateError() {
        super.onStateError();
        if(callback != null) {
            callback.onStateError();
        }
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        if(callback != null) {
            callback.onStateAutoComplete();
        }
    }

    public void setCallback(PlayerCallback callback) {
        this.callback = callback;
    }
}
