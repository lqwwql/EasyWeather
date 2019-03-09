package com.main.easyweather.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.easyweather.R;
import com.main.easyweather.models.WeatherBean;
import com.main.easyweather.models.WeatherDayBean;
import com.main.easyweather.models.WeatherDetailBean;
import com.main.easyweather.models.WeatherHourBean;
import com.main.easyweather.models.WeatherTipBean;
import com.main.easyweather.utils.GlideUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/3/8.
 */

public class WeatherListViewAdapter extends BaseAdapter {

    private List<WeatherBean> weatherBeanList;
    private Context context;

    public WeatherListViewAdapter(Context context, List<WeatherBean> weatherBeanList) {
        this.context = context;
        this.weatherBeanList = weatherBeanList;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return weatherBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return weatherBeanList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        //设置的数值要比实际的大，不然报数组越界。
        return 5;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        WeatherBean weatherBean = weatherBeanList.get(position);
        CurWeatherViewHolder curWeatherViewHolder;
        HourWeatherViewHolder hourWeatherViewHolder;
        TipViewHolder tipViewHolder;
        switch (type) {
            case 1:
                try{
                    if (convertView == null) {
                        convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_cur_weather, parent, false);
                        curWeatherViewHolder = new CurWeatherViewHolder(convertView);
                        convertView.setTag(curWeatherViewHolder);
                    } else {
                        curWeatherViewHolder = (CurWeatherViewHolder) convertView.getTag();
                    }
                    WeatherDetailBean weatherDetailBean = weatherBean.getWeatherDetailBean();
                    if(weatherDetailBean==null){
                        Log.i("weather", "weatherDetailBean = null");
                        break;
                    }
                    curWeatherViewHolder.tv_cur_temp.setText(weatherDetailBean.getTemp() + "℃");
                    curWeatherViewHolder.tv_weather.setText(weatherDetailBean.getCondTxt());
                    curWeatherViewHolder.tv_temp_range.setText(weatherDetailBean.getWindDir() + "/" + weatherDetailBean.getWindSc()+"级");
                    curWeatherViewHolder.tv_air.setText("空气质量:"+weatherDetailBean.getAir());
                    break;
                }catch(Exception e){
                    Log.i("weather","type 1 error : ",e);
                }
            case 2:
                try{
                    if (convertView == null) {
                        convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_hour_weather, parent, false);
                        hourWeatherViewHolder = new HourWeatherViewHolder(convertView);
                        convertView.setTag(hourWeatherViewHolder);
                    } else {
                        hourWeatherViewHolder = (HourWeatherViewHolder) convertView.getTag();
                    }
                    List<WeatherHourBean> weatherHourBeanList = weatherBean.getWeatherHourBeanList();
                    if (weatherHourBeanList == null) {
                        Log.i("weather", "weatherHourBeanList = null");
                        break;
                    }
                    HourWeatherRecycleViewAdapter hourWeatherRecycleViewAdapter = new HourWeatherRecycleViewAdapter(context, weatherHourBeanList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    hourWeatherViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
                    hourWeatherViewHolder.recyclerView.setAdapter(hourWeatherRecycleViewAdapter);
                    break;
                }catch(Exception e){
                    Log.i("weather","type 1 error : ",e);
                }
            case 3:
                try{
                    List<WeatherDayBean> weatherDayBeanList = weatherBean.getWeatherDayBeanList();
                    LinearLayout total = new LinearLayout(context);
                    total.setOrientation(LinearLayout.VERTICAL);
                    if (weatherDayBeanList == null) {
                        Log.i("weather", "weatherDayBeanList = null");
                        break;
                    }
                    Log.i("weather","weatherDayBeanList size = "+weatherDayBeanList.size());
                    for (WeatherDayBean weatherDayBean : weatherDayBeanList) {
                        View view = LayoutInflater.from(context).inflate(R.layout.lv_item_day_weather, parent, false);
                        TextView tv_date_day1 = view.findViewById(R.id.tv_date_day1);
                        ImageView iv_weather_day1 = view.findViewById(R.id.iv_weather_day1);
                        TextView tv_temp_day1 = view.findViewById(R.id.tv_temp_day1);
                        tv_date_day1.setText(weatherDayBean.getDay());
                        tv_temp_day1.setText(weatherDayBean.getTempMax() + "℃/" + weatherDayBean.getTempMin()+"℃");
                        int imageUrl = context.getResources().getIdentifier("w"+weatherDayBean.getImage(), "drawable", context.getPackageName());
                        GlideUtils.getInstence().setImageCacheResource(imageUrl, context, iv_weather_day1);
                        total.addView(view);
                    }
                    convertView = total;
                    break;
                } catch (Exception e) {
                    Log.i("weather", "type 1 error : ", e);
                }
            case 4:
                try{
                    if (convertView == null) {
                        convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_cur_tip, parent, false);
                        tipViewHolder = new TipViewHolder(convertView);
                        convertView.setTag(tipViewHolder);
                    } else {
                        tipViewHolder = (TipViewHolder) convertView.getTag();
                    }
                    WeatherTipBean weatherTipBean = weatherBean.getWeatherTipBean();
                    if(weatherTipBean==null){
                        Log.i("weather", "weatherTipBean = null");
                        break;
                    }
                    tipViewHolder.tv_cur_tip.setText(weatherTipBean.getTip());
                    break;
                }catch(Exception e){
                    Log.i("weather","type 4 error : ",e);
                }
            default:
                break;
        }
        return convertView;
    }

    class CurWeatherViewHolder {
        public TextView tv_cur_temp, tv_weather, tv_temp_range, tv_air;

        public CurWeatherViewHolder(View view) {
            this.tv_cur_temp = view.findViewById(R.id.tv_cur_temp);
            this.tv_weather = view.findViewById(R.id.tv_weather);
            this.tv_temp_range = view.findViewById(R.id.tv_temp_range);
            this.tv_air = view.findViewById(R.id.tv_air);
        }
    }

    class HourWeatherViewHolder {
        public RecyclerView recyclerView;

        public HourWeatherViewHolder(View view) {
            this.recyclerView = view.findViewById(R.id.rv_hour_weather);
        }
    }

    class TipViewHolder {
        public TextView tv_cur_tip;

        public TipViewHolder(View view) {
            this.tv_cur_tip = view.findViewById(R.id.tv_cur_tip);
        }
    }
}
