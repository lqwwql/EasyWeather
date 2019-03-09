package com.main.easyweather.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.main.easyweather.network.HttpConnectUtil;
import com.main.easyweather.R;

import org.loader.autohideime.HideIMEUtil;

import java.util.List;

public class HomeActivity extends Activity {

    private String URL_PROVINCE="http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getRegionProvince?";
    private String URL_CITY=    "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getSupportCityString";
    private String URL_WEATHER= "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";
    private int weathericon[] = {R.drawable.w0,R.drawable.w1,R.drawable.w2,R.drawable.w3,R.drawable.w4
            ,R.drawable.w5,R.drawable.w6,R.drawable.w7,R.drawable.w8,R.drawable.w9
            ,R.drawable.w10,R.drawable.w11,R.drawable.w12,R.drawable.w13,R.drawable.w14
            ,R.drawable.w15,R.drawable.w16,R.drawable.w17,R.drawable.w18,R.drawable.w19
            ,R.drawable.w20,R.drawable.w21,R.drawable.w22,R.drawable.w23,R.drawable.w24
                ,R.drawable.w25,R.drawable.w26,R.drawable.w27,R.drawable.w28,R.drawable.w29,
            R.drawable.w30,R.drawable.w31,R.drawable.nothing};

    private List provincelist,citylist;
    //城市查找控件
    private EditText search;
    private Button check;
    private Spinner sp_province,sp_city;
    //城市天气内容控件
    private LinearLayout cityWeather;
    private TextView cityName,cityTerm,cityTermRange,cityWinRating,cityAir,cityUltravioletrays,
            cityGetCold,cityDressing,cityCarWash,cityExercise,afterday1,afterday2,afterday3,afterday4,
            afterday1TermRange,afterday2TermRange,afterday3TermRange,afterday4TermRange;
    private ImageView afterday1icon1,afterday1icon2,afterday2icon1,afterday2icon2,
            afterday3icon1,afterday3icon2,afterday4icon1,afterday4icon2;
    private String weatherUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        HideIMEUtil.wrap(this);

        search = (EditText) findViewById(R.id.et_search);
        check = (Button) findViewById(R.id.btn_check);
        sp_province = (Spinner) findViewById(R.id.sp_province);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        new GetProvince().execute(URL_PROVINCE);//获取所有省份和特区

        cityWeather = (LinearLayout) findViewById(R.id.city_weather);
        cityWeather.setVisibility(View.INVISIBLE);

        //所支持的中国所有省份和地区
        sp_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(provincelist!=null && provincelist.size() > 2 && position > 0){
                    String url = URL_CITY + "?theRegionCode=" + provincelist.get(position).toString();
                    new GetCity().execute(url);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //对应省份所有城市和地区
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(citylist!=null && citylist.size() > 2){
                    String city_name = citylist.get(position).toString();
                    weatherUrl = URL_WEATHER + "?theCityCode=" + city_name +"&theUserID=";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //优先级：输入框优先于选择栏
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search.getText().toString().equals("") && weatherUrl.equals("")){
                    new AlertDialog.Builder(HomeActivity.this).setTitle("温馨提示")
                            .setMessage("请输入或选择城市")
                            .setPositiveButton("确定",null).show();
                }else if(search.getText().toString().equals("") && !weatherUrl.equals("")) {
                    new GetWeather().execute(weatherUrl);
                }else{
                    String city_name = search.getText().toString();
                    String url = URL_WEATHER + "?theCityCode=" + city_name +"&theUserID=";
                    new GetWeather().execute(url);
                }
            }
        });

    }

    private void initCityWeatherControl(){
        cityName = (TextView) findViewById(R.id.tv_cityname);
        cityTerm = (TextView) findViewById(R.id.tv_cityterm);
        cityTermRange = (TextView) findViewById(R.id.tv_citytermrange);
        cityWinRating = (TextView) findViewById(R.id.tv_citywinrating);
        cityAir = (TextView) findViewById(R.id.tv_cityair);
        cityUltravioletrays = (TextView) findViewById(R.id.tv_city_ultravioletrays);
        cityGetCold = (TextView) findViewById(R.id.tv_city_getcold);
        cityDressing = (TextView) findViewById(R.id.tv_city_dressing);
        cityCarWash = (TextView) findViewById(R.id.tv_city_carwash);
        cityExercise = (TextView) findViewById(R.id.tv_city_exercise);
        afterday1 = (TextView) findViewById(R.id.aftertoday1);
        afterday2 = (TextView) findViewById(R.id.aftertoday2);
        afterday3 = (TextView) findViewById(R.id.aftertoday3);
        afterday4 = (TextView) findViewById(R.id.aftertoday4);
        afterday1TermRange = (TextView) findViewById(R.id.aftertoday1termrange);
        afterday2TermRange = (TextView) findViewById(R.id.aftertoday2termrange);
        afterday3TermRange = (TextView) findViewById(R.id.aftertoday3termrange);
        afterday4TermRange = (TextView) findViewById(R.id.aftertoday4termrange);

        afterday1icon1 = (ImageView) findViewById(R.id.aftertoday1icon1);
        afterday1icon2 = (ImageView) findViewById(R.id.aftertoday1icon2);
        afterday2icon1 = (ImageView) findViewById(R.id.aftertoday2icon1);
        afterday2icon2 = (ImageView) findViewById(R.id.aftertoday2icon2);
        afterday3icon1 = (ImageView) findViewById(R.id.aftertoday3icon1);
        afterday3icon2 = (ImageView) findViewById(R.id.aftertoday3icon2);
        afterday4icon1 = (ImageView) findViewById(R.id.aftertoday4icon1);
        afterday4icon2 = (ImageView) findViewById(R.id.aftertoday4icon2);

    }

    public class GetProvince extends AsyncTask<String ,Void,List>{

        @Override
        protected List doInBackground(String... params) {
            return HttpConnectUtil.getWeatherXml(params[0]);
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            if(list!=null&&list.size()>0){
                provincelist = list;
                provincelist.add(0,"请选择省份");
                ArrayAdapter arrayAdapter =
                        new ArrayAdapter(getBaseContext(),R.layout.spinner_item,provincelist);
                arrayAdapter.setDropDownViewResource(R.layout.down_style);
                sp_province.setDropDownVerticalOffset(80);
                sp_province.setAdapter(arrayAdapter);
                Log.d("weather","获取到所有省和地区");
            }
        }
    }

    public class GetCity extends AsyncTask<String,Void,List>{

        @Override
        protected List doInBackground(String... params) {
            return HttpConnectUtil.getWeatherXml(params[0]);
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            if(list!=null&&list.size()>0){
                citylist = list;
                Log.d("weather","获取该省的所支持的所有城市");
                //更新UI操作
                ArrayAdapter arrayAdapter =
                        new ArrayAdapter(getBaseContext(),R.layout.spinner_item,citylist);
                arrayAdapter.setDropDownViewResource(R.layout.down_style);
                sp_city.setDropDownVerticalOffset(80);
                sp_city.setAdapter(arrayAdapter);
            }
        }
    }

    public class GetWeather extends AsyncTask<String,Void,List>{

        @Override
        protected List doInBackground(String... params) {
            return HttpConnectUtil.getWeatherXml(params[0]);
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            if(list!=null&&list.size()>0){
                if (list.size()>2){
                    for(int i=0;i<list.size();i++){
                        Log.d("weather",i+" "+ list.get(i).toString());
                    }
                    initCityWeatherControl();

                    for(int i=0;i<list.size();i++){
                        Log.d("item:"+i,list.get(i).toString());
                    }

                    cityName.setText(list.get(1).toString());
                    String term = list.get(4).toString();
                    cityTerm.setText(term.substring(term.indexOf("气温：")+3,term.indexOf("℃")+1));
                    cityTermRange.setText(list.get(13).toString());
                    cityWinRating.setText(term.substring(term.indexOf("风向"),term.indexOf("湿度")-1));
                    String air = list.get(5).toString();
                    cityAir.setText(air.substring(air.indexOf("空气"),air.length()-1));
                    String advice = list.get(6).toString();
                    cityUltravioletrays.setText(advice.substring(0,advice.indexOf("健臻·血糖指数")-2));
                    cityGetCold.setText(advice.substring(advice.indexOf("健臻·血糖指数"),advice.indexOf("穿衣指数")-2));
                    cityDressing.setText(advice.substring(advice.indexOf("穿衣指数"),advice.indexOf("洗车指数")-2));
                    cityCarWash.setText(advice.substring(advice.indexOf("洗车指数"),advice.indexOf("空气污染指数")-2));
//                    cityExercise.setText(advice.substring(advice.indexOf("紫外线指数"),advice.indexOf("血糖指数")-2));
                    afterday1.setText(list.get(12).toString());
                    afterday1TermRange.setText(list.get(13).toString());
                    afterday1icon1.setBackgroundResource(weathericon[getIndexForImage(list.get(15).toString())]);
                    afterday1icon2.setBackgroundResource(weathericon[getIndexForImage(list.get(16).toString())]);
                    afterday2.setText(list.get(17).toString());
                    afterday2TermRange.setText(list.get(18).toString());
                    afterday2icon1.setBackgroundResource(weathericon[getIndexForImage(list.get(20).toString())]);
                    afterday2icon2.setBackgroundResource(weathericon[getIndexForImage(list.get(21).toString())]);
                    afterday3.setText(list.get(22).toString());
                    afterday3TermRange.setText(list.get(23).toString());
                    afterday3icon1.setBackgroundResource(weathericon[getIndexForImage(list.get(25).toString())]);
                    afterday3icon2.setBackgroundResource(weathericon[getIndexForImage(list.get(26).toString())]);
                    afterday4.setText(list.get(27).toString());
                    afterday4TermRange.setText(list.get(28).toString());
                    afterday4icon1.setBackgroundResource(weathericon[getIndexForImage(list.get(30).toString())]);
                    afterday4icon2.setBackgroundResource(weathericon[getIndexForImage(list.get(31).toString())]);

                    cityWeather.setVisibility(View.VISIBLE);
                }else {
                    new AlertDialog.Builder(HomeActivity.this).setTitle("温馨提示")
                            .setMessage("您所输入的城市不存在或不支持")
                            .setPositiveButton("确定",null).show();
                }
            }else{
                new AlertDialog.Builder(HomeActivity.this).setTitle("温馨提示")
                        .setMessage("因网络或服务器原因查询失败")
                        .setPositiveButton("确定",null).show();
            }
        }
    }

    //获取小图标下标
    private int getIndexForImage(String url){
        return Integer.parseInt(url.substring(0,url.indexOf(".gif")));
    }

}
