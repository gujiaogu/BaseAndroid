package com.yunshu.commonlib.okhttp;

/**
 * 进度响应回调接口
 * Created by Tyrese on 2017/12/15.
 */

public interface ReqProgressCallBack{
    /**
     * 响应进度更新
     */
    void onProgress(long total, long current);
}
