package com.yunshu.baseandroid.app;

import com.mob.MobApplication;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.commonsdk.UMConfigure;
import com.yunshu.baseandroid.BuildConfig;
import com.yunshu.baseandroid.entity.MyObjectBox;

import cn.jpush.android.api.JPushInterface;
import io.objectbox.BoxStore;

/**
 *
 * Created by Tyrese on 2017/12/19.
 */

public class MyApplication extends MobApplication {

    private BoxStore boxStore;
    private RefWatcher mWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary();
        initUmeng();
        initJPush();
        boxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
    }

    /**
     * 初始化友盟统计
     */
    private void initUmeng() {
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        mWatcher = BuildConfig.DEBUG ? LeakCanary.install(this) : RefWatcher.DISABLED;
    }

    public RefWatcher getRefWatcher() {
        return mWatcher;
    }

}
