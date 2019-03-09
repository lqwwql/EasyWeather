package com.main.easyweather.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.main.easyweather.models.WeatherBean;
import com.main.easyweather.models.WeatherDayBean;
import com.main.easyweather.models.WeatherDetailBean;
import com.main.easyweather.models.WeatherHourBean;
import com.main.easyweather.models.WeatherTipBean;

import java.util.ArrayList;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
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

    private Context context;

    private WeatherUtils() {
    }

    private static class LazyLoader {
        public static WeatherUtils WEATHER_UTILS = new WeatherUtils();
    }

    public final static WeatherUtils getInstance() {
        return LazyLoader.WEATHER_UTILS;
    }

    public List<WeatherBean> getWeatherBean(String cityName) throws Exception {
        final List<WeatherBean> weatherBeanList = new ArrayList<>();
        final WeatherBean detailWeatherBean = new WeatherBean();
        final WeatherBean hourWeatherBean = new WeatherBean();
        final WeatherBean dayWeatherBean = new WeatherBean();
        final WeatherBean tipWeatherBean = new WeatherBean();

        HeWeather.getWeatherNow(context, cityName, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("weather", "getWeatherNow onError:", throwable);
            }

            @Override
            public void onSuccess(List<Now> list) {
                if (list != null && list.size() > 0) {
                    Log.i("weather", "getWeatherNow: " + new Gson().toJson(list));
                    WeatherDetailBean weatherDetailBean = new WeatherDetailBean();
                    NowBase nowBase = list.get(0).getNow();
                    weatherDetailBean.setTemp(nowBase.getTmp());
                    weatherDetailBean.setCondCode(nowBase.getCond_code());
                    weatherDetailBean.setCondTxt(nowBase.getCond_txt());
                    weatherDetailBean.setWindDir(nowBase.getWind_dir());
                    weatherDetailBean.setWindSc(nowBase.getWind_sc());
                    detailWeatherBean.setWeatherDetailBean(weatherDetailBean);
                    detailWeatherBean.setType(1);
                }
            }
        });

        HeWeather.getWeatherHourly(context, cityName, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherHourlyBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("weather", "getWeatherHourly onError:", throwable);
            }

            @Override
            public void onSuccess(List<Hourly> list) {
                if (list != null && list.size() > 0) {
                    Log.i("weather", "getWeatherHourly: " + new Gson().toJson(list));
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
                    hourWeatherBean.setWeatherHourBeanList(weatherHourBeanList);
                    hourWeatherBean.setType(2);
                }
            }
        });

        HeWeather.getWeatherForecast(context, cityName, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("weather", "getWeatherForecast onError:", throwable);
            }

            @Override
            public void onSuccess(List<Forecast> list) {
                if (list != null && list.size() > 0) {
                    Log.i("weather", "getWeatherForecast: " + new Gson().toJson(list));
                    List<WeatherDayBean> weatherDayBeanList = new ArrayList<>();
                    List<ForecastBase> forecastBaseList = list.get(0).getDaily_forecast();
                    Log.i("weather", "forecast size = " + forecastBaseList.size());
                    for (ForecastBase forecastBase : forecastBaseList) {
                        WeatherDayBean weatherDayBean = new WeatherDayBean();
                        weatherDayBean.setDay(forecastBase.getDate());
                        weatherDayBean.setImage(forecastBase.getCond_code_d());
                        weatherDayBean.setTempMax(forecastBase.getTmp_max());
                        weatherDayBean.setTempMin(forecastBase.getTmp_min());
                        weatherDayBeanList.add(weatherDayBean);
                    }
                    dayWeatherBean.setWeatherDayBeanList(weatherDayBeanList);
                    dayWeatherBean.setType(3);
                }
            }
        });

        HeWeather.getWeatherLifeStyle(context, cityName, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherLifeStyleBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("weather", "getWeatherLifeStyle onError:", throwable);
            }

            @Override
            public void onSuccess(List<Lifestyle> list) {
                if (list != null && list.size() > 0) {
                    Log.i("weather", "getWeatherLifeStyle: " + new Gson().toJson(list));
                    WeatherTipBean weatherTipBean = new WeatherTipBean();
                    List<LifestyleBase> lifestyleBaseList = list.get(0).getLifestyle();
                    weatherTipBean.setTip(getLifeStyle(lifestyleBaseList));
                    tipWeatherBean.setWeatherTipBean(weatherTipBean);
                    tipWeatherBean.setType(4);
                }
            }
        });

        Thread.sleep(500);

        weatherBeanList.add(detailWeatherBean);
        weatherBeanList.add(hourWeatherBean);
        weatherBeanList.add(dayWeatherBean);
        weatherBeanList.add(tipWeatherBean);

        return weatherBeanList;
    }

    private String getLifeStyle(List<LifestyleBase> lifestyleBaseList){
        String comf = "舒适度指数:";//舒适度指数
        String cw = "洗车指数:";//洗车指数
        String drsg = "穿衣指数:";//穿衣指数
        String flu = "感冒指数:";//感冒指数
        String sport = "运动指数:";//运动指数
        String trav = "旅游指数:";//旅游指数
        String uv = "紫外线指数:";//紫外线指数
        String air = "空气污染扩散条件指数:";//空气污染扩散条件指数
        for(LifestyleBase lifestyleBase : lifestyleBaseList){
            switch (lifestyleBase.getType()){
                case "comf":
                    comf += lifestyleBase.getTxt()+"\n\n";
                    break;
                case "cw":
                    cw += lifestyleBase.getTxt()+"\n\n";
                    break;
                case "drsg":
                    drsg += lifestyleBase.getTxt()+"\n\n";
                    break;
                case "flu":
                    flu += lifestyleBase.getTxt()+"\n\n";
                    break;
                case "sport":
                    sport += lifestyleBase.getTxt()+"\n\n";
                    break;
                case "trav":
                    trav += lifestyleBase.getTxt()+"\n\n";
                    break;
                case "uv":
                    uv += lifestyleBase.getTxt()+"\n\n";
                    break;
                case "air":
                    air += lifestyleBase.getTxt()+"\n\n";
                    break;
                default:
                    break;
            }
        }
        return comf+cw+drsg+flu+sport+trav+uv+air;
    }

    public Context getContext() {
        return context;
    }

    public WeatherUtils setContext(Context context) {
        this.context = context;
        return this;
    }
}
