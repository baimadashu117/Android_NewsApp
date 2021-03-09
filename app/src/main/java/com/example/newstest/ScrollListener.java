package com.example.newstest;

import android.util.Log;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newstest.Adapter.NewsAdapter;

public abstract class ScrollListener implements NestedScrollView.OnScrollChangeListener {

    private static String TAG = "ScrollListener";

    private NewsAdapter adapter;
    //声明一个LinearLayoutManager
    private LinearLayoutManager mLinearLayoutManager;
    //已经加载出来的Item的数量
    private int totalItemCount;
    //主要用来存储上一个totalItemCount
    private int lastVisibleItem;
    //是否正在上拉数据
    private boolean loading = true;

    public ScrollListener(NewsAdapter adapter, LinearLayoutManager linearLayoutManager) {
        this.adapter = adapter;
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            totalItemCount = mLinearLayoutManager.getItemCount();
            lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
            if (adapter.isFadeTips() == false && lastVisibleItem + 1 == totalItemCount) {
                onLoadMore();
            }
            // 如果隐藏了提示条，我们又上拉加载时，那么最后一个条目就要比getItemCount要少2
            if (adapter.isFadeTips() == true && lastVisibleItem + 2 == totalItemCount) {
                onLoadMore();
            }
        }
    }

    /**
     * 提供一个抽闲方法，在Activity中监听到这个EndLessOnScrollListener
     * 并且实现这个方法
     */
    public abstract void onLoadMore();
}
