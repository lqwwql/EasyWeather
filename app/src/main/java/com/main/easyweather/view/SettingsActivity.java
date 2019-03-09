package com.main.easyweather.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.main.easyweather.R;
import com.main.easyweather.adapter.CityListViewAdapter;
import com.main.easyweather.listener.CityListViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private TextView tv_addcity;
    private ImageView iv_turnback;
    private ListView lv_citys;
    private int RESULT_CODE = 1004;
    private CityListViewAdapter cityListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }


    private void init(){
        tv_addcity = findViewById(R.id.tv_addcity);
        iv_turnback = findViewById(R.id.iv_turnback);
        lv_citys = findViewById(R.id.lv_citys);
        final List<String> cityList = new ArrayList<>();
        cityList.add("广州");
        cityList.add("茂名");
        cityList.add("湛江");
        cityListViewAdapter = new CityListViewAdapter(this, cityList, new CityListViewItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                //数据库删除


                cityList.remove(position);
                notifyDataChanged();
            }
        });
        lv_citys.setAdapter(cityListViewAdapter);
        iv_turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_addcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SettingsActivity.this,AddCityActivity.class),RESULT_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




    }

    private void notifyDataChanged(){
        if(cityListViewAdapter!=null){
            cityListViewAdapter.notifyDataSetChanged();
        }
    }
}
