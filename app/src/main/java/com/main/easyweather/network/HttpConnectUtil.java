package com.main.easyweather.network;

import android.util.Log;

import com.main.easyweather.utils.PullXmlUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 * 网络链接类
 */

public class HttpConnectUtil {

    public static List getWeatherXml(String wurl){
        HttpURLConnection con = null;
        InputStream is = null;
        List list = null;
        try{
            URL url = new URL(wurl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5*1000);
            con.setReadTimeout(5*1000);
            con.setRequestMethod("GET");
            if(con.getResponseCode()==200){
                Log.d("getxml","success");
                is = con.getInputStream();
                PullXmlUtil pullXmlUtil = new PullXmlUtil(is);
                list = pullXmlUtil.getAllElement();
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            try{
                if (is!=null){
                    is.close();
                }
                if(con!=null){
                    con.disconnect();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
