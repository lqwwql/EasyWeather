package com.main.easyweather.view.customer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.main.easyweather.R;
import com.main.easyweather.listener.OnRefreshListener;
import com.main.easyweather.utils.UtilTools;

/**
 * Created by Administrator on 2019/3/9.
 */

public class CustomerRefreshListView extends ListView implements OnScrollListener {

    //下拉头部
    private View headerView;
    //头部高度
    private int headerViewHeight;
    //头部旋转图片
    private ImageView iv_head_view_arrow;
    //头部下拉刷新时的状态描述与时间
    private TextView tv_head_view_refresh, tv_head_view_time;
    //刷新中的processbar
    private ProgressBar pb_refreshing;
    //按下Y坐标
    private int downY;

    //下拉，松开，正在刷新状态
    private final int PULL_REFRESH = 0;
    private final int RELEASE_REFRESH = 1;
    private final int REFRESHIING = 2;

    //当前下拉状态
    private int curRefreshState = PULL_REFRESH;

    //下拉头部箭头动画
    private RotateAnimation upAnimatin, downAnimation;

    //监听器
    private OnRefreshListener onRefreshListener;

    public CustomerRefreshListView(Context context) {
        super(context);
    }

    public CustomerRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomerRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        try {
            setOnScrollListener(this);
            initheaderView();
            initRotateAnimation();
        } catch (Exception e) {
            Log.i("weather", "customerlistview err : ", e);
        }
    }

    //初始化头部
    private void initheaderView() throws Exception {
        headerView = View.inflate(getContext(), R.layout.refresh_head_view, null);
        iv_head_view_arrow = headerView.findViewById(R.id.iv_head_view_arrow);
        tv_head_view_refresh = headerView.findViewById(R.id.tv_head_view_refresh);
        tv_head_view_time = headerView.findViewById(R.id.tv_head_view_time);
        pb_refreshing = headerView.findViewById(R.id.pb_refreshing);

        headerView.measure(0, 0);
        headerViewHeight = headerView.getMeasuredHeight();
        //获取高度设置padding为负的，则不显示headerview
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        addHeaderView(headerView);
        setHeaderDividersEnabled(false);//取消分割线
    }

    //初始化动画
    private void initRotateAnimation() throws Exception {
        upAnimatin = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimatin.setDuration(300);
        upAnimatin.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(300);
        downAnimation.setFillAfter(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (curRefreshState == REFRESHIING) {
                    //滑动中不做处理
                    break;
                }
                //获取滑动距离
                int deltaY = (int) (ev.getY() - downY);
                //获取padding值
                int paddingTop = -headerViewHeight + deltaY;
                if (paddingTop > -headerViewHeight && getFirstVisiblePosition() == 0) {
                    headerView.setPadding(0, paddingTop, 0, 0);
                    if (paddingTop >= 0 && curRefreshState == PULL_REFRESH) {
                        curRefreshState = RELEASE_REFRESH;
                        refreshHeaderView();
                    } else if (paddingTop < 0 && curRefreshState == RELEASE_REFRESH) {
                        curRefreshState = PULL_REFRESH;
                        refreshHeaderView();
                    }
                    return false;//拦截touchmove，否则listview会无法滑动
                }
                break;
            case MotionEvent.ACTION_UP:
                if (curRefreshState == PULL_REFRESH) {
                    //下拉距离不够，则复位
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                } else if (curRefreshState == RELEASE_REFRESH) {
                    //滑到一定距离，显示无padding的headview
                    headerView.setPadding(0, 0, 0, 0);
                    curRefreshState = REFRESHIING;
                    refreshHeaderView();
                    if (onRefreshListener != null) {
                        //接口回调，加载数据
                        onRefreshListener.onPullRefresh();
                    }
                }
                break;
            default:
                break;
        }


        return super.onTouchEvent(ev);
    }

    //根据curRefreshState来刷新头部
    private void refreshHeaderView() {
        switch (curRefreshState) {
            case PULL_REFRESH:
                tv_head_view_refresh.setText("下拉刷新");
                iv_head_view_arrow.startAnimation(downAnimation);
                break;
            case RELEASE_REFRESH:
                tv_head_view_refresh.setText("松开刷新");
                iv_head_view_arrow.setAnimation(upAnimatin);
                break;
            case REFRESHIING:
                iv_head_view_arrow.clearAnimation();
                iv_head_view_arrow.setVisibility(View.INVISIBLE);
                pb_refreshing.setVisibility(View.VISIBLE);
                tv_head_view_refresh.setText("正在刷新...");
                break;
            default:
                break;
        }
    }

    //完成刷新后重置headview
    private void completeRefresh() {
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        curRefreshState = PULL_REFRESH;
        pb_refreshing.setVisibility(View.INVISIBLE);
        iv_head_view_arrow.setVisibility(View.VISIBLE);
        tv_head_view_refresh.setText("下拉刷新");
        tv_head_view_time.setText("上次刷新:" + UtilTools.getCurrentTime());
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //上拉加载更多会用到
        /*if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && getLastVisiblePosition() == (getCount() - 1) && !isLoadingMore) {
            isLoadingMore = true;

            footerView.setPadding(0, 0, 0, 0);//显示出footerView
            setSelection(getCount());//让listview最后一条显示出来，在页面完全显示出底布局

            if (listener != null) {
                listener.onLoadingMore();
            }
        }*/
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
