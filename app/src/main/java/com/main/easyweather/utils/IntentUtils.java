package com.main.easyweather.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Administrator on 2019/3/9.
 */

public class IntentUtils {

    public static String getStringData(Intent intent, String key) {
        String result = "";
        try {
            if (intent == null) {
                return null;
            }
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return null;
            }
            result = (String) bundle.get(key);
        } catch (Exception e) {
            Log.i("weather", "getStringData err: ", e);
        }
        return result;
    }

}
