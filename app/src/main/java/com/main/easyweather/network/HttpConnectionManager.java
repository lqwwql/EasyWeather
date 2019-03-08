package com.main.easyweather.network;

import android.os.Handler;
import android.os.Looper;

import com.main.easyweather.models.City;
import com.main.easyweather.models.Province;

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

    private HttpConnectionManager(){
        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 內部类懒汉加载单例模式
     * */
    private static class LazyLoader{
        private static final HttpConnectionManager OK_HTTP_MANAGERS = new HttpConnectionManager();
    }

    public static final HttpConnectionManager getInstance() {
        return LazyLoader.OK_HTTP_MANAGERS;
    }

    public void getProvince(final String url,final GetProvinceCallBack callBack){
        final Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onGetProvince(1,null,callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<Province> provList = new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Province province = new Province();
                        province.setiCode(jsonObject.getInt("id"));
                        province.setsName(jsonObject.getString("name"));
                        province.save();
                        provList.add(province);
                    }
                    onGetProvince(2,provList,callBack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getCity(final String url,final int iProvCode,final GetCityCallBack callBack){
        final Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onGetCity(1,null,callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<City> cityList = new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        City city = new City();
                        city.setiCode(jsonObject.getInt("id"));
                        city.setsName(jsonObject.getString("name"));
                        city.setiProvCode(iProvCode);
                        city.save();
                        cityList.add(city);
                    }
                    onGetCity(2,cityList,callBack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void onGetProvince(final int result,final List<Province> provList,final GetProvinceCallBack callBack){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack!=null) {
                    callBack.onResponse(result,provList);
                }
            }
        });
    }

    public interface GetProvinceCallBack{
        void onResponse(int result, List<Province> provList);
    }

    private void onGetCity(final int result, final List<City> cityList, final GetCityCallBack callBack){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack!=null) {
                    callBack.onResponse(result,cityList);
                }
            }
        });
    }

    public interface GetCityCallBack{
        void onResponse(int result, List<City> cityList);
    }

}
