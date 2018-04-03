package com.yunshu.baseandroid.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.yunshu.baseandroid.app.MyApplication;
import com.yunshu.commonlib.util.GsonUtil;
import com.yunshu.baseandroid.util.SharePreferenceUtil;
import com.yunshu.commonlib.okhttp.OkHttpService;

import io.objectbox.Box;

/**
 * Created by Tyrese on 2017/12/14.
 *
 * Activity基类
 */

public class BaseActivity extends AppCompatActivity {

    protected OkHttpService okHttpService = null;
    protected MyApplication application = null;
    protected SharePreferenceUtil sharedUtil = null;
    protected ProgressDialog pd = null;
    protected GsonUtil gson = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        okHttpService = new OkHttpService(this);
        application = (MyApplication) getApplication();
        application.getRefWatcher().watch(this);
        sharedUtil = new SharePreferenceUtil(this);
        gson = new GsonUtil();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    protected <T> Box<T> getBox(Class<T> tClass) {
        return application.getBoxStore().boxFor(tClass);
    }

    protected void showSimpleProgressDialog(String message) {
        pd = new ProgressDialog(this);
        pd.setMessage(message);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    protected void showSimpleProgressDialog() {
        showSimpleProgressDialog("加载中...");
    }

    protected void dismiss() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
}
