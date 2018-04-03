package com.yunshu.baseandroid.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintContextWrapper;
import android.util.AttributeSet;

import com.umeng.analytics.MobclickAgent;
import com.yunshu.commonlib.util.LogWrapper;

import java.util.HashMap;

/**
 * Created by Tyrese on 2018/1/9.
 */

public class AnalyticsTextView extends AppCompatTextView {
    public AnalyticsTextView(Context context) {
        super(context);
    }

    public AnalyticsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnalyticsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        Context context = getContext();
        String pageName = "";
        if (context instanceof TintContextWrapper) {
            pageName = ((TintContextWrapper) context).getBaseContext().getClass().getName();
        } else {
            pageName = context.getClass().getName();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("buttonEvent", "class: " + pageName + ", text: " + getText());
        LogWrapper.d(pageName);
        MobclickAgent.onEvent(context.getApplicationContext(),"click", map);
        return super.performClick();
    }
}
