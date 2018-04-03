package com.yunshu.commonlib.okhttp;

import android.content.Context;

import com.liulishuo.filedownloader.FileDownloadListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OKHttp接口类
 *
 * Created by Tyrese on 2017/12/14.
 */

public class OkHttpService {

    private final OkHttpClientWrapper okHttpClientWrapper;

    public OkHttpService(Context context) {
        okHttpClientWrapper = new OkHttpClientWrapper(context);
    }

    /**
     * get方法
     * @param url url
     * @param callback 回调方法
     */
    public void get(String url, ResultDisposer callback) {
        okHttpClientWrapper.get(url, new OkHttpResultCallbackImpl(callback));
    }

    /**
     * post方法，只能post带参数的url
     * @param url url
     * @param param 参数
     * @param callback 回调方法
     */
    public void post(String url, String param, ResultDisposer callback) {
        okHttpClientWrapper.post(url, param, new OkHttpResultCallbackImpl(callback));
    }

    /**
     * 上传文件，不带参数的，带进度
     * @param url url
     * @param file 文件对象
     * @param callback 回调方法
     * @param progressCallBack 进度回调
     */
    public void postFile(String url, File file, ResultDisposer callback, ReqProgressCallBack progressCallBack) {
        okHttpClientWrapper.postFile(url, file, new OkHttpResultCallbackImpl(callback), progressCallBack);
    }

    /**
     * 上传文件，不带参数的，不带进度
     * @param url url
     * @param file 文件对象
     * @param callback 回调方法
     */
    public void postFile(String url, File file, ResultDisposer callback) {
        okHttpClientWrapper.postFile(url, file, new OkHttpResultCallbackImpl(callback));
    }

    /**
     * 多部分文件上传 可以带参数，比如文件名字等。(暂时未实现)
     * @param url url
     * @param paramsMap 参数，这里主要考虑是放文件和文件名，文件放入一个file对象，文件名string。目前只支持string和file对象
     * @param callback 回调方法
     */
    public void uploadMultipart(String url, HashMap<String, Object> paramsMap, ResultDisposer callback) {
        okHttpClientWrapper.uploadMultipart(url, paramsMap, new OkHttpResultCallbackImpl(callback));
    }

    /**
     * 带进度的多部分上传，可以带参数，带进度，比如文件名字等。(暂时未实现)
     * @param url url
     * @param paramsMap 参数，这里主要考虑是放文件和文件名，文件放入一个file对象，文件名string。目前只支持string和file对象
     * @param callback 回调方法
     * @param progressCallBack 进度回调
     */
    public void uploadMultipart(String url, HashMap<String, Object> paramsMap,
                                ResultDisposer callback, ReqProgressCallBack progressCallBack) {
        okHttpClientWrapper.uploadMultipart(url, paramsMap, new OkHttpResultCallbackImpl(callback), progressCallBack);
    }

    /**
     * 文件下载
     * @param url 下载地址
     * @param path 下载路径
     * @param listener 下载监听
     */
    public void downloadFile(String url, String path, FileDownloadListener listener) {
        okHttpClientWrapper.downloadFile(url, path, listener);
    }

    private class OkHttpResultCallbackImpl implements OkHttpResultCallback {

        ResultDisposer callback;

        public OkHttpResultCallbackImpl(ResultDisposer callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(IOException e) {
            callback.onException(e);
        }

        @Override
        public void onResponse(Response response) {
            disposeResponse(response, callback);
        }
    }

    private void disposeResponse(Response response, ResultDisposer callback) {
        if(response.isSuccessful()) {
            ResponseBody body = response.body();
            try {
                String resultStr = body.string();
                JSONObject object = new JSONObject(resultStr);
                if (object.getInt("status") == 0) {
                    String data = object.getString("data");
                    callback.onSuccess(data);
                } else {
                    callback.onFailure(object.getString("msg"));
                }
            } catch (IOException | JSONException e) {
                callback.onException(e);
            }
        } else {
            String msg = response.message();
            callback.onFailure("code: " + response.code() + ", msg: " + msg);
        }
    }
}
