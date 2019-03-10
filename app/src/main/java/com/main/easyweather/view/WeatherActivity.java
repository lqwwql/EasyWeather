package com.main.easyweather.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.easyweather.R;
import com.main.easyweather.adapter.WeatherListViewAdapter;
import com.main.easyweather.common.Common;
import com.main.easyweather.listener.OnRefreshListener;
import com.main.easyweather.models.CacheCity;
import com.main.easyweather.models.WeatherBean;
import com.main.easyweather.utils.CacheUtils;
import com.main.easyweather.utils.LogUtils;
import com.main.easyweather.utils.NetWorkUtils;
import com.main.easyweather.utils.WeatherUtils;
import com.main.easyweather.view.customer.CustomerRefreshListView;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class WeatherActivity extends AppCompatActivity implements OnRefreshListener {

    private CustomerRefreshListView weatherListView;
    private TextView tv_current_city;
    private ImageView iv_settings;
    private WeatherListViewAdapter weatherListViewAdapter;
    private String cityName;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 0x123) {
                    refreshListView();
                    weatherListView.completeRefresh();
                }
            } catch (Exception e) {
                LogUtils.logInfo("weatherListView completeRefresh err : ", e);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //全局调用一次就够了,和风天气配置
        HeConfig.init(Common.ID, Common.KEY);
        HeConfig.switchToFreeServerNode();
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(Common.TAG, "onNewIntent call");
        setIntent(intent);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            cityName = bundle.getString("cityName");
        }
        refreshListView();
    }

    private void init() {
        try {
            cityName = CacheUtils.getCacheCityName().getCityName();
            if (cityName == null || cityName.equals("")) {
                cityName = "广州";
            }
            tv_current_city = findViewById(R.id.tv_current_city);
            iv_settings = findViewById(R.id.iv_settings);
            tv_current_city.setText(cityName);

            weatherListView = findViewById(R.id.body_listView);
            weatherListView.setOnRefreshListener(this);
            weatherListViewAdapter = new WeatherListViewAdapter(this);

            //判断网络状态
            if((NetWorkUtils.isMobileConnected(this)||NetWorkUtils.isNetworkConnected(this)||NetWorkUtils.isWifiConnected(this))&&NetWorkUtils.ping()){
                //网络可用，则更新
                LogUtils.logInfo("network is ok");
                WeatherUtils.getInstance().setWeatherActivity(this).getWeatherBean(cityName, false);
            }else{
                //加载缓存数据
                List<WeatherBean> weatherBeanList = CacheUtils.getCacheData();
                if(weatherBeanList!=null&&weatherBeanList.size()>0){
                    LogUtils.logInfo("load cache data is ok");
                    refreshDataChange(weatherBeanList,false);
                }
            }

            iv_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(WeatherActivity.this, SettingsActivity.class));
                }
            });
        } catch (Exception e) {
            LogUtils.logInfo("init err : ", e);
        }
    }

    @Override
    public void onPullRefresh() {
        Message message = new Message();
        message.what = 0x123;
        handler.sendMessage(message);
    }

    private void refreshListView() {
        try {
            if (tv_current_city != null) {
                tv_current_city.setText(cityName);
            }
            WeatherUtils.getInstance().setWeatherActivity(this).getWeatherBean(cityName, true);
        } catch (Exception e) {
            LogUtils.logInfo("refreshListView err : ", e);
        }

    }

    public void refreshDataChange(List<WeatherBean> weatherBeanList, boolean isRefresh) {
        try {
            if (weatherListViewAdapter != null && isRefresh) {
                LogUtils.logInfo("weatherListViewAdapter not null,weatherBeanList size = " + weatherBeanList.size());
                weatherListViewAdapter.setWeatherBeanList(weatherBeanList);
                weatherListViewAdapter.notifyDataSetChanged();
                CacheUtils.saveToCache(weatherBeanList,cityName);
            } else {
                LogUtils.logInfo("weatherListViewAdapter not null,weatherBeanList size = " + weatherBeanList.size());
                weatherListViewAdapter.setWeatherBeanList(weatherBeanList);
                weatherListView.setAdapter(weatherListViewAdapter);
                CacheUtils.saveToCache(weatherBeanList,cityName);
            }
        } catch (Exception e) {
            LogUtils.logInfo("refreshDataChange err : ", e);
        }
    }



}
