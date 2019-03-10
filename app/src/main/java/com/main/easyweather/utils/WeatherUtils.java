package com.main.easyweather.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.main.easyweather.models.WeatherBean;
import com.main.easyweather.models.WeatherDayBean;
import com.main.easyweather.models.WeatherDetailBean;
import com.main.easyweather.models.WeatherHourBean;
import com.main.easyweather.models.WeatherTipBean;
import com.main.easyweather.view.WeatherActivity;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.Hourly;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

/**
 * Created by Administrator on 2019/3/8.
 */

public class WeatherUtils {

    private WeatherActivity weatherActivity;

    private WeatherUtils() {
    }

    private static class LazyLoader {
        public static WeatherUtils WEATHER_UTILS = new WeatherUtils();
    }

    public final static WeatherUtils getInstance() {
        return LazyLoader.WEATHER_UTILS;
    }

    public void getWeatherBean(String cityName,boolean isRefresh) throws Exception {

        final List<WeatherBean> weatherBeanList = new ArrayList<>();
        final boolean refresh = isRefresh;
        HeWeather.getWeather(weatherActivity, cityName, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherDataListBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("weather", "getWeather onError:", throwable);
            }

            @Override
            public void onSuccess(List<Weather> list) {
                if (list != null && list.size() > 0) {
                    Log.i("weather", "onSuccess: " + new Gson().toJson(list));
                    WeatherBean detailWeatherBean = new WeatherBean();
                    WeatherBean hourWeatherBean = new WeatherBean();
                    WeatherBean dayWeatherBean = new WeatherBean();
                    WeatherBean tipWeatherBean = new WeatherBean();
                    //获取天气详细
                    WeatherDetailBean weatherDetailBean = new WeatherDetailBean();
                    NowBase nowBase = list.get(0).getNow();
                    weatherDetailBean.setTemp(nowBase.getTmp());
                    weatherDetailBean.setCondCode(nowBase.getCond_code());
                    weatherDetailBean.setCondTxt(nowBase.getCond_txt());
                    weatherDetailBean.setWindDir(nowBase.getWind_dir());
                    weatherDetailBean.setWindSc(nowBase.getWind_sc());

                    //获取小时天气
                    List<WeatherHourBean> weatherHourBeanList = new ArrayList<>();
                    List<HourlyBase> hourlyBaseList = list.get(0).getHourly();
                    for (HourlyBase hourlyBase : hourlyBaseList) {
                        WeatherHourBean weatherHourBean = new WeatherHourBean();
                        weatherHourBean.setHour(hourlyBase.getTime());
                        weatherHourBean.setImage(hourlyBase.getCond_code());
                        weatherHourBean.setPer(hourlyBase.getPop());
                        weatherHourBean.setTemp(hourlyBase.getTmp());
                        weatherHourBeanList.add(weatherHourBean);
                    }
                    //获取周天气
                    List<WeatherDayBean> weatherDayBeanList = new ArrayList<>();
                    List<ForecastBase> forecastBaseList = list.get(0).getDaily_forecast();
                    for (ForecastBase forecastBase : forecastBaseList) {
                        WeatherDayBean weatherDayBean = new WeatherDayBean();
                        weatherDayBean.setDay(forecastBase.getDate());
                        weatherDayBean.setImage(forecastBase.getCond_code_d());
                        weatherDayBean.setTempMax(forecastBase.getTmp_max());
                        weatherDayBean.setTempMin(forecastBase.getTmp_min());
                        weatherDayBeanList.add(weatherDayBean);
                    }
                    //获取生活指数
                    WeatherTipBean weatherTipBean = new WeatherTipBean();
                    List<LifestyleBase> lifestyleBaseList = list.get(0).getLifestyle();
                    weatherTipBean.setTip(getLifeStyle(lifestyleBaseList,null,0));
                    weatherDetailBean.setAir(getLifeStyle(lifestyleBaseList,"air",1));

                    detailWeatherBean.setWeatherDetailBean(weatherDetailBean);
                    detailWeatherBean.setType(1);
                    hourWeatherBean.setWeatherHourBeanList(weatherHourBeanList);
                    hourWeatherBean.setType(2);
                    dayWeatherBean.setWeatherDayBeanList(weatherDayBeanList);
                    dayWeatherBean.setType(3);
                    tipWeatherBean.setWeatherTipBean(weatherTipBean);
                    tipWeatherBean.setType(4);

                    weatherBeanList.add(detailWeatherBean);
                    weatherBeanList.add(hourWeatherBean);
                    weatherBeanList.add(dayWeatherBean);
                    weatherBeanList.add(tipWeatherBean);

                    weatherActivity.refreshDataChange(weatherBeanList,refresh);
                }
            }
        });
    }

    private String getLifeStyle(List<LifestyleBase> lifestyleBaseList, String type, int select) {
        String comf = "舒适度指数:";//舒适度指数
        String cw = "洗车指数:";//洗车指数
        String drsg = "穿衣指数:";//穿衣指数
        String flu = "感冒指数:";//感冒指数
        String sport = "运动指数:";//运动指数
        String trav = "旅游指数:";//旅游指数
        String uv = "紫外线指数:";//紫外线指数
        String air = "空气污染扩散条件指数:";//空气污染扩散条件指数
        for (LifestyleBase lifestyleBase : lifestyleBaseList) {
            switch (lifestyleBase.getType()) {
                case "comf":
                    if (type != null && type.equals("comf")) {
                        if (select != 0 && select == 1) {
                            return lifestyleBase.getBrf();
                        }else if(select != 0 && select == 2){
                            return lifestyleBase.getTxt();
                        }
                    }
                    comf += lifestyleBase.getTxt() + "\n\n";
                    break;
                case "cw":
                    if (type != null && type.equals("cw")) {
                        if (select != 0 && select == 1) {
                            return lifestyleBase.getBrf();
                        }else if(select != 0 && select == 2){
                            return lifestyleBase.getTxt();
                        }
                    }
                    cw += lifestyleBase.getTxt() + "\n\n";
                    break;
                case "drsg":
                    if (type != null && type.equals("drsg")) {
                        if (select != 0 && select == 1) {
                            return lifestyleBase.getBrf();
                        }else if(select != 0 && select == 2){
                            return lifestyleBase.getTxt();
                        }
                    }
                    drsg += lifestyleBase.getTxt() + "\n\n";
                    break;
                case "flu":
                    if (type != null && type.equals("flu")) {
                        if (select != 0 && select == 1) {
                            return lifestyleBase.getBrf();
                        }else if(select != 0 && select == 2){
                            return lifestyleBase.getTxt();
                        }
                    }
                    flu += lifestyleBase.getTxt() + "\n\n";
                    break;
                case "sport":
                    if (type != null && type.equals("sport")) {
                        if (select != 0 && select == 1) {
                            return lifestyleBase.getBrf();
                        }else if(select != 0 && select == 2){
                            return lifestyleBase.getTxt();
                        }
                    }
                    sport += lifestyleBase.getTxt() + "\n\n";
                    break;
                case "trav":
                    if (type != null && type.equals("trav")) {
                        if (select != 0 && select == 1) {
                            return lifestyleBase.getBrf();
                        }else if(select != 0 && select == 2){
                            return lifestyleBase.getTxt();
                        }
                    }
                    trav += lifestyleBase.getTxt() + "\n\n";
                    break;
                case "uv":
                    if (type != null && type.equals("uv")) {
                        if (select != 0 && select == 1) {
                            return lifestyleBase.getBrf();
                        }else if(select != 0 && select == 2){
                            return lifestyleBase.getTxt();
                        }
                    }
                    uv += lifestyleBase.getTxt() + "\n\n";
                    break;
                case "air":
                    if (type != null && type.equals("air")) {
                        if (select != 0 && select == 1) {
                            return lifestyleBase.getBrf();
                        }else if(select != 0 && select == 2){
                            return lifestyleBase.getTxt();
                        }
                    }
                    air += lifestyleBase.getTxt() + "\n\n";
                    break;
                default:
                    break;
            }
        }
        return comf + cw + drsg + flu + sport + trav + uv + air;
    }


    public WeatherActivity getWeatherActivity() {
        return weatherActivity;
    }

    public WeatherUtils setWeatherActivity(WeatherActivity weatherActivity) {
        this.weatherActivity = weatherActivity;
        return this;
    }
}
