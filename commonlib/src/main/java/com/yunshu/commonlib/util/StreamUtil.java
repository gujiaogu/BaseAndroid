package com.yunshu.commonlib.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * 流工具类
 *
 * Created by Tyrese on 2017/12/13.
 */

public class StreamUtil {

    /**
     * 关闭流
     * */
    public static void close(Closeable is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
