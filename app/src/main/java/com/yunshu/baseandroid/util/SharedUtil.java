package com.yunshu.baseandroid.util;


import android.content.Context;
import android.text.Html;

import com.yunshu.commonlib.util.ToastUtil;

import java.io.File;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 分享工具类
 *
 * @author hp
 */
public class SharedUtil {
    public static void showShare(Context context, String title, String url, String content, String httpTitle, String imgUrl) {
//        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if (imgUrl == null || "".equals(imgUrl)) {
//            if (MyApplication.getInstance().getLogoPictureSdCaredPath() != null) {
//                oks.setImagePath(MyApplication.getInstance().getLogoPictureSdCaredPath());
//            }
            return;
        } else {
            oks.setImageUrl(imgUrl);
        }
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(Html.fromHtml(content) + "");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(content);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(httpTitle);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 分享图片
     * @param context
     * @param imgUrl
     */
    public static void shareOnlyImage(Context context, String imgUrl) {
//        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        File file = new File(imgUrl);
        if (!file.exists()) {
            ToastUtil.textToast(context, "图片不存在，分享失败");
            return;
        }
        oks.setImagePath(imgUrl);
        // 启动分享GUI
        oks.show(context);
    }

}
