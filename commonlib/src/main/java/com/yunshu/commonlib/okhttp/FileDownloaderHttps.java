package com.yunshu.commonlib.okhttp;

import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.liulishuo.filedownloader.util.FileDownloadHelper;

/**
 * 支持Https的FileDownloader
 *
 * Created by Tyrese on 2017/12/18.
 */

class FileDownloaderHttps extends FileDownloader {

    static DownloadMgrInitialParams.InitCustomMaker setupOnApplicationOnCreate(Context application) {
        final Context context = application.getApplicationContext();
        FileDownloadHelper.holdContext(context);

        DownloadMgrInitialParams.InitCustomMaker customMaker = new DownloadMgrInitialParams.InitCustomMaker();
        CustomComponentHolder.getImpl().setInitCustomMaker(customMaker);

        return customMaker;
    }
}
