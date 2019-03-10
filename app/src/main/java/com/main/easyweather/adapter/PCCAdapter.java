package com.main.easyweather.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/10.
 */

public abstract class PCCAdapter<T> extends ArrayAdapter {

    private List<T> mList = new ArrayList<>();
    private int mLayout;

    public PCCAdapter(Context context,List<T> list, int mLayout) {
        super(context,mLayout);
        this.mList = list;
        this.mLayout = mLayout;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerViewHolder spinnerViewHolder = SpinnerViewHolder.bind(parent.getContext(),convertView,parent,mLayout,position);
        convert(spinnerViewHolder,getItem(position));
        return spinnerViewHolder.view;
    }

    public abstract void convert(SpinnerViewHolder spinnerViewHolder, T t);

    public static class SpinnerViewHolder {
        private SparseArray<View> mView;
        private View view;
        private int position;
        private Context context;

        public SpinnerViewHolder(Context context,ViewGroup parent,int layoutRes) {
            this.mView = new SparseArray<>();
            this.context = context;
            View converView = LayoutInflater.from(context).inflate(layoutRes,parent,false);
            converView.setTag(this);
            this.view = converView;
        }

        public static SpinnerViewHolder bind(Context mContext, View convertView, ViewGroup partent, int mLayoutRes, int position) {
            SpinnerViewHolder holder;
            if (convertView == null) {
                holder = new SpinnerViewHolder(mContext, partent, mLayoutRes);
            } else {
                holder = (SpinnerViewHolder) convertView.getTag();
                holder.view = convertView;
            }
            holder.position = position;
            return holder;
        }

        public View getView() {
            return view;
        }

        public <T extends View> T getView(int id) {
            T t = (T) mView.get(id);
            if (t == null) {
                t = view.findViewById(id);
                mView.put(id, t);
            }
            return t;
        }

        //设置内容
        public SpinnerViewHolder setTitle(int id,String Title){
            View view = getView(id);
            if(view instanceof TextView){
                ((TextView) view).setText(Title);
            }
            return this;
        }
    }
}
