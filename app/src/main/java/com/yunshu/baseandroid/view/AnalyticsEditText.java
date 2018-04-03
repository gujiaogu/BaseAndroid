package com.yunshu.baseandroid.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.TintContextWrapper;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.umeng.analytics.MobclickAgent;
import com.yunshu.commonlib.util.LogWrapper;

import java.util.HashMap;

/**
 * Created by Tyrese on 2018/1/5.
 */

public class AnalyticsEditText extends AppCompatEditText {
    public AnalyticsEditText(Context context) {
        super(context);
    }

    public AnalyticsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnalyticsEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && getMaxLines() == 1) {
            Context context = getContext();
            String pageName = "";
            if (context instanceof TintContextWrapper) {
                pageName = ((TintContextWrapper) context).getBaseContext().getClass().getName();
            } else {
                pageName = context.getClass().getName();
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("search_text", "text: " + getText());
            LogWrapper.d(pageName);
            MobclickAgent.onEvent(context.getApplicationContext(),"search", map);
        }
        return super.onKeyDown(keyCode, event);
    }
}
