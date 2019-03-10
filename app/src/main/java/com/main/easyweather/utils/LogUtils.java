package com.main.easyweather.utils;

import android.util.Log;

import com.main.easyweather.common.Common;

/**
 * Created by Administrator on 2019/3/10.
 */

public class LogUtils {
    public static void logInfo(String common){
        Log.i(Common.TAG,common);
    }
    public static void logInfo(String common,Exception e){
        Log.i(Common.TAG,common,e);
    }
}
