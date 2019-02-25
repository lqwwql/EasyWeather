package com.main.easyweather;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 * xml解析类
 */

public class PullXmlUtil {

    private InputStream is;

    public PullXmlUtil(InputStream is){
        this.is  = is;
    }

    public List getAllElement() throws XmlPullParserException, IOException {
        List myData = null;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(is,"UTF-8");
        int eventType = xpp.getEventType();
        String elementName = "";
        while(eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    myData = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    elementName = xpp.getName();
                    if(elementName.equals("string")){
                        String data = xpp.nextText();
                        Log.d("weather1",data);
                        if(data.indexOf(",")!=-1){
                            myData.add(data.substring(0,data.indexOf(",")));
                        }else{
                            myData.add(data);
                        }
                    }
                    break;
                default:
                    break;
            }
            eventType = xpp.next();
        }
        return myData;
    }

}
