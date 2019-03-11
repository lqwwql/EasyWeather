package com.main.easyweather.network;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.main.easyweather.database.CityDao;
import com.main.easyweather.database.CountyDao;
import com.main.easyweather.database.ProvinceDao;
import com.main.easyweather.models.City;
import com.main.easyweather.models.County;
import com.main.easyweather.models.JsonInfo;
import com.main.easyweather.models.Province;
import com.main.easyweather.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2019/3/8.
 */

public class HttpConnectionManager {

    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private ProvinceDao provinceDao;
    private CityDao cityDao;
    private CountyDao countyDao;

    private HttpConnectionManager() {
        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
        provinceDao = new ProvinceDao();
        cityDao = new CityDao();
        countyDao = new CountyDao();
    }

    /**
     * 內部类懒汉加载单例模式
     */
    private static class LazyLoader {
        private static final HttpConnectionManager OK_HTTP_MANAGERS = new HttpConnectionManager();
    }

    public static final HttpConnectionManager getInstance() {
        return LazyLoader.OK_HTTP_MANAGERS;
    }

    public void getProvince(final String url, final GetProvinceCallBack callBack) {
        LogUtils.logInfo("getProvince");
        final Request request = new Request.Builder().url(url).build();

        //从数据库加载
        List<Province> provList = provinceDao.getAllProvince();
        if(provList!=null&&provList.size()>0){
            onGetProvince(1,provList,callBack);
            return;
        }

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.logInfo("getProvince onFailure: ", e);
                onGetProvince(1, null, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    List<JsonInfo> jsonInfoList = JSON.parseArray(response.body().string(), JsonInfo.class);
                    LogUtils.logInfo("getProvince:" + new Gson().toJson(jsonInfoList));
                    List<Province> provList = new ArrayList<>();
                    for (JsonInfo jsonInfo : jsonInfoList) {
                        Province province = new Province();
                        province.setiCode(jsonInfo.getId());
                        province.setsName(jsonInfo.getName());
                        if (!provinceDao.findProvince(jsonInfo.getId())) {
                            province.save();
                        }
                        provList.add(province);
                    }
                    onGetProvince(2, provList, callBack);
                } catch (Exception e) {
                    LogUtils.logInfo("getProvince onResponse Exception:" + e);
                }
            }
        });
    }

    public void getCity(final String url, final int iProvCode, final GetCityCallBack callBack) {
        final Request request = new Request.Builder().url(url + "/" + iProvCode).build();
        //从数据库加载
        List<City> cityList = cityDao.getCityByProvince(iProvCode);
        if(cityList!=null&&cityList.size()>0){
            onGetCity(1,cityList,callBack);
            return;
        }

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onGetCity(1, null, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    List<JsonInfo> jsonInfoList = JSON.parseArray(response.body().string(), JsonInfo.class);
                    LogUtils.logInfo("getCity:" + new Gson().toJson(jsonInfoList));
                    List<City> cityList = new ArrayList<>();
                    for (JsonInfo jsonInfo : jsonInfoList) {
                        City city = new City();
                        city.setiCode(jsonInfo.getId());
                        city.setsName(jsonInfo.getName());
                        city.setiProvCode(iProvCode);
                        if (!cityDao.findCity(jsonInfo.getId())) {
                            city.save();
                        }
                        cityList.add(city);
                    }
                    onGetCity(2, cityList, callBack);
                } catch (Exception e) {
                    LogUtils.logInfo("getCity onResponse Exception:" + e);
                }
            }
        });
    }

    public void getCounty(final String url, final int iCityCode, final int iProvCode, final GetCountyCallBack callBack) {
        final Request request = new Request.Builder().url(url + "/" + iProvCode + "/" + iCityCode).build();
        //从数据库加载
        List<County> countyList = CountyDao.getCountyByCity(iCityCode);
        if(countyList!=null&&countyList.size()>0){
            onGetCounty(1,countyList,callBack);
            return;
        }

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onGetCounty(1, null, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    List<JsonInfo> jsonInfoList = JSON.parseArray(response.body().string(), JsonInfo.class);
                    LogUtils.logInfo("getCounty:" + new Gson().toJson(jsonInfoList));
                    List<County> countyList = new ArrayList<>();
                    for (JsonInfo jsonInfo : jsonInfoList) {
                        County county = new County();
                        county.setiCode(jsonInfo.getId());
                        county.setsName(jsonInfo.getName());
                        county.setiCity(iCityCode);
                        if(countyDao.findCounty(jsonInfo.getId())){
                            county.save();
                        }
                        countyList.add(county);
                    }
                    onGetCounty(2, countyList, callBack);
                } catch (Exception e) {
                    LogUtils.logInfo("getCounty onResponse Exception:" + e);
                }
            }
        });
    }


    private void onGetProvince(final int result, final List<Province> provList, final GetProvinceCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(result, provList);
                }
            }
        });
    }

    private void onGetCity(final int result, final List<City> cityList, final GetCityCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(result, cityList);
                }
            }
        });
    }

    private void onGetCounty(final int result, final List<County> countyList, final GetCountyCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(result, countyList);
                }
            }
        });
    }

    public interface GetProvinceCallBack {
        void onResponse(int result, List<Province> provList);
    }

    public interface GetCityCallBack {
        void onResponse(int result, List<City> cityList);
    }

    public interface GetCountyCallBack {
        void onResponse(int result, List<County> countyList);
    }

}
