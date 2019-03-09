package com.main.easyweather.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.main.easyweather.R;
import com.main.easyweather.adapter.WeatherListViewAdapter;
import com.main.easyweather.common.Common;
import com.main.easyweather.listener.OnRefreshListener;
import com.main.easyweather.models.WeatherBean;
import com.main.easyweather.utils.IntentUtils;
import com.main.easyweather.utils.WeatherUtils;
import com.main.easyweather.view.customer.CustomerRefreshListView;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class WeatherActivity extends AppCompatActivity implements OnRefreshListener{

    private CustomerRefreshListView weatherListView;
    private TextView tv_current_city;
    private ImageView iv_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //全局调用一次就够了,和风天气配置
        HeConfig.init(Common.ID,Common.KEY);
        HeConfig.switchToFreeServerNode();

        init(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        init(intent);
    }

    private void init(Intent intent){
        try{
            String param = IntentUtils.getStringData(intent,"cityName");
            String cityName = "高州";
            if(param!=null&&param.length()>0){
                cityName = param;
            }
            tv_current_city = findViewById(R.id.tv_current_city);
            iv_settings = findViewById(R.id.iv_settings);
            tv_current_city.setText(cityName);

            weatherListView = findViewById(R.id.body_listView);
            List<WeatherBean> weatherBeanList = WeatherUtils.getInstance().setContext(this).getWeatherBean(cityName);
            WeatherListViewAdapter weatherListViewAdapter = new WeatherListViewAdapter(this,weatherBeanList);
            weatherListView.setOnRefreshListener(this);
            weatherListView.setAdapter(weatherListViewAdapter);

            iv_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(WeatherActivity.this,SettingsActivity.class));
                }
            });
        }catch(Exception e){
            Log.i("weather","init Exception : ",e);
        }
    }

    @Override
    public void onPullRefresh() {
        init(getIntent());
    }
}
