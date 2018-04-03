package com.yunshu.commonlib.okhttp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;

import com.liulishuo.filedownloader.FileDownloadListener;
import com.yunshu.commonlib.BuildConfig;
import com.yunshu.commonlib.util.LogWrapper;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import cn.dreamtobe.filedownloader.OkHttp3Connection;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by Tyrese on 2017/12/13.
 *
 * OKHttp 客户端执行程序
 */

class OkHttpClientWrapper {

    private static final int MSG_WHAT_RESPONSE = 0xcc;
    private static final int MSG_WHAT_FAILURE = 0xdd;
    private static final String KEY_RANDOM = "key_random";
    final private static MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");

    private static final ArrayMap<String, OkHttpResultCallback> callbackArrayMap = new ArrayMap<>();

    private OkHttpClient client = null;
    final private Context context;
    private OkHttpHandler mHandler = null;
    final private Handler progressHandler = new Handler();

    OkHttpClientWrapper(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        this.context = context;
        if (BuildConfig.USE_SSL == 2) { //双向证书
            builder.sslSocketFactory(SSLSocketFactoryWrapper.getSSLFactory(context.getApplicationContext(),
                    BuildConfig.CLIENT_CERT_PATH, BuildConfig.SERVER_CERT_PATH));
        } else if (BuildConfig.USE_SSL == 1) { //单向证书
            builder.sslSocketFactory(SSLSocketFactoryWrapper.buildSSLSocketFactory(context, BuildConfig.SERVER_CERT_PATH));
        }
        mHandler = new OkHttpHandler(this.context);
        FileDownloaderHttps.setupOnApplicationOnCreate(context)
                .connectionCreator(new OkHttp3Connection.Creator(builder));
        this.client = builder.build();
    }

    /**
     * get方法
     * @param url url
     * @param callback 回调方法
     */
    void get(final String url, OkHttpResultCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        String randomKey = UUID.randomUUID().toString();
        callbackArrayMap.put(randomKey, callback);
        client.newCall(request).enqueue(new CallBackHandler(randomKey));
    }

    /**
     * post方法，只能post带参数的url
     * @param url url
     * @param param 参数
     * @param callback 回调方法
     */
    void post(final String url, String param, OkHttpResultCallback callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, param);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        String randomKey = UUID.randomUUID().toString();
        callbackArrayMap.put(randomKey, callback);
        client.newCall(request).enqueue(new CallBackHandler(url));
    }

    /**
     * 上传文件，不带参数的，带进度
     * @param url url
     * @param file 文件对象
     * @param callback 回调方法
     * @param progressCallBack 进度回调
     */
    void postFile(final String url, File file, OkHttpResultCallback callback, final ReqProgressCallBack progressCallBack) {
        MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)

                //这里三个header仅仅用于科技厅服务器特殊情况
//                .addHeader("uploaded_file", file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.')))
//                .addHeader("user_id", "995")
//                .addHeader("Connection", "Keep-Alive")
                //这里三个header仅仅用于科技厅服务器特殊情况
                ;
        if (progressCallBack == null) {
            requestBuilder.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
        } else {
            requestBuilder.post(createProgressRequestBody(MEDIA_OBJECT_STREAM, file, progressCallBack));
        }

        Request request = requestBuilder.build();
        String randomKey = UUID.randomUUID().toString();
        callbackArrayMap.put(randomKey, callback);
        client.newCall(request).enqueue(new CallBackHandler(url));
    }

    /**
     * 上传文件，不带参数的，不带进度
     * @param url url
     * @param file 文件对象
     * @param callback 回调方法
     */
    void postFile(final String url, File file, OkHttpResultCallback callback) {
        postFile(url, file, callback, null);
    }

    /**
     * 多部分文件上传 可以带参数，比如文件名字等。(暂时未实现)
     * @param url url
     * @param paramsMap 参数，这里主要考虑是放文件和文件名，文件放入一个file对象，文件名string。目前只支持string和file对象
     * @param callback 回调方法
     */
    void uploadMultipart(final String url, HashMap<String, Object> paramsMap, OkHttpResultCallback callback) {
        MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");
        try {
            //补全请求地址
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (object instanceof String) {
                    builder.addFormDataPart(key, object.toString());
                } else if(object instanceof File) {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(url)
                    //这里三个header仅仅用于科技厅服务器特殊情况
//                    .addHeader("uploaded_file", "hehe.jpg")
//                    .addHeader("user_id", "995")
//                    .addHeader("Connection", "Keep-Alive")
                    //这里三个header仅仅用于科技厅服务器特殊情况
                    .post(body).build();
            //单独设置参数 比如读取超时时间
            final Call call = client.newBuilder().writeTimeout(500, TimeUnit.SECONDS).build().newCall(request);
            String randomKey = UUID.randomUUID().toString();
            callbackArrayMap.put(randomKey, callback);
            call.enqueue(new CallBackHandler(url));
        } catch (Exception e) {
            LogWrapper.e(e.toString());
        }
    }

    /**
     * 带进度的多部分上传，可以带参数，带进度，比如文件名字等。(暂时未实现)
     * @param url url
     * @param paramsMap 参数，这里主要考虑是放文件和文件名，文件放入一个file对象，文件名string。目前只支持string和file对象
     * @param callback 回调方法
     * @param progressCallBack 进度回调
     */
    void uploadMultipart(String url, HashMap<String, Object> paramsMap,
                         OkHttpResultCallback callback, final ReqProgressCallBack progressCallBack) {
        try {
            //补全请求地址
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
//            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (object instanceof String) {
                    builder.addFormDataPart(key, object.toString());
                } else if(object instanceof File) {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), createProgressRequestBody(MEDIA_OBJECT_STREAM, file, progressCallBack));
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(url)
                    //这里三个header仅仅用于科技厅服务器特殊情况
//                    .addHeader("uploaded_file", "1513574957088.jpg")
//                    .addHeader("user_id", "995")
//                    .addHeader("Connection", "Keep-Alive")
                    //这里三个header仅仅用于科技厅服务器特殊情况
                    .post(body).build();
            final Call call = client.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
            String randomKey = UUID.randomUUID().toString();
            callbackArrayMap.put(randomKey, callback);
            call.enqueue(new CallBackHandler(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件下载
     * @param url 下载地址
     * @param path 下载路径
     * @param listener 下载监听
     */
    void downloadFile(String url, String path, FileDownloadListener listener) {
        FileDownloaderHttps.getImpl().create(url)
                .setPath(path)
                .setListener(listener).start();
    }

    /**
     * 创建带进度的RequestBody
     * @param contentType MediaType
     * @param file  准备上传的文件
     * @param callBack 回调
     * @return ProgressRequestBody
     */
    private RequestBody createProgressRequestBody(final MediaType contentType, final File file, final ReqProgressCallBack callBack) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    final long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        progressCallBack(remaining, current/1024, callBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void progressCallBack(final long total, final long current, final ReqProgressCallBack callBack) {
        progressHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onProgress(total, current);
                }
            }
        });
    }

    private void sendFailureMessage(String randomKey, IOException e) {
        sendMessage(MSG_WHAT_FAILURE, randomKey, e);
    }

    private void sendResponseMessage(String randomKey, Response response) {
        sendMessage(MSG_WHAT_RESPONSE, randomKey, response);
    }

    private void sendMessage(int what, String randomKey, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        Bundle data = new Bundle();
        data.putString(KEY_RANDOM, randomKey);
        msg.obj = obj;
        msg.setData(data);
        mHandler.sendMessage(msg);
    }

    private class CallBackHandler implements Callback {

        private String randomKey = "";

        private CallBackHandler(String randomKey) {
            this.randomKey = randomKey;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            sendFailureMessage(randomKey, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            sendResponseMessage(randomKey, response);
        }
    }

    private static class OkHttpHandler extends Handler {

        private final WeakReference<Context> contextReference;

        private OkHttpHandler(Context context) {
            this.contextReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Context context = contextReference.get();
            if (context != null) {
                disposeCallback(msg);
            }
        }

        private void disposeCallback(Message msg) {
            Bundle data = msg.getData();
            String randomKey = data.getString(KEY_RANDOM);
            OkHttpResultCallback callback = callbackArrayMap.get(randomKey);
            switch (msg.what) {
                case MSG_WHAT_RESPONSE:
                    Response response = (Response) msg.obj;
                    callback.onResponse(response);
                    break;
                case MSG_WHAT_FAILURE:
                    IOException e = (IOException) msg.obj;
                    callback.onFailure(e);
                    break;
                default:
                    break;
            }
            callbackArrayMap.remove(randomKey);
        }
    }
}
