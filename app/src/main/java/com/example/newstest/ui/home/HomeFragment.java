package com.example.newstest.ui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.newstest.Adapter.NewsAdapter;
import com.example.newstest.Beans.News;
import com.example.newstest.R;
import com.example.newstest.ScrollListener;
import com.example.newstest.SlideRecyclerView;
import com.example.newstest.Utils.RecyclerViewDecoration;
import com.example.newstest.ui.news.NewsFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnBannerListener {

    private static final String TAG = "HomeFragment";
    private SwipeRefreshLayout refreshLayout;
    private NestedScrollView nestedScrollView;
    private NewsAdapter adapter;
    private List<News> newsList;
    private int PAGE_COUNT = 15;
    private Banner banner;
    private SlideRecyclerView slideRecyclerView;
    private View root;
    private Toolbar toolbar;
    public String category;
    public static HomeViewModel homeViewModel;
    private LiveData<List<News>> liveNews;
    private long exitTime = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreated");
        root = inflater.inflate(R.layout.fragment_home, container, false);
        initNewsList();
        initBanner();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(category);
        nestedScrollView = root.findViewById(R.id.nestedScrollView);
        refreshLayout = root.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews(true);
            }
        });
        slideRecyclerView = root.findViewById(R.id.news_list_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        slideRecyclerView.setLayoutManager(layoutManager);
        slideRecyclerView.addItemDecoration(new RecyclerViewDecoration(this.getContext(), RecyclerViewDecoration.VERTICAL_LIST));
        adapter = new NewsAdapter(newsList, getActivity(), newsList.size() > 0 ? true : false, root);
        setFooterView(slideRecyclerView);
        slideRecyclerView.setAdapter(adapter);
        slideRecyclerView.setNestedScrollingEnabled(false);
        nestedScrollView.setOnScrollChangeListener(new ScrollListener(adapter, layoutManager) {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "LoadMore");
                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT, category);
            }
        });
        liveNews.observe(getActivity(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                Log.d(TAG, "Data changed");
                newsList = liveNews.getValue();
//                Log.d(TAG, String.valueOf(liveNews.getValue().size()));
                refreshNews(false);
                category = homeViewModel.getCategory();
                toolbar.setTitle(category);
            }
        });
        return root;
    }

    private void initBanner() {
        List<String> bannerList = new ArrayList<>();
        for (News item : newsList.subList(0, 5)) {
            bannerList.add(item.getPic());
        }
        List<String> bannerList_Title = new ArrayList<>();
        for (News title : newsList.subList(0, 5)) {
            bannerList_Title.add(title.getTitle());
        }
        banner = root.findViewById(R.id.banner);
        banner.setDelayTime(4500);//间隔时间
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context).load(path).into(imageView);
            }
        });   //设置图片加载器
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//Style
        banner.setImages(bannerList);  //设置banner中显示图片
        banner.isAutoPlay(true);
        banner.setBannerTitles(bannerList_Title);
        banner.setOnBannerListener(this);
        banner.start();  //设置完毕后调用
    }

    private void setFooterView(RecyclerView recyclerView) {
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.footview, recyclerView, false);
        adapter.setFooterView(footer);
    }

    private void initNewsList() {
//        Log.d(TAG, getActivity().toString());
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        liveNews = homeViewModel.getNews();
        newsList = liveNews.getValue();
        category = homeViewModel.getCategory();
    }

    @Override
    public void OnBannerClick(int position) {
        News clickedNews = newsList.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("news", clickedNews);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        NavController controller = Navigation.findNavController(root);
        controller.navigate(R.id.nav_news, bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    private void refreshNews(boolean isRefresh) {
        refreshLayout.setRefreshing(true);
        if (isRefresh) {
            homeViewModel.downLoadNews(0, PAGE_COUNT, category);
            Log.d(TAG, "category = " + category);
        }
        adapter.resetData();
        if (newsList.size() > 0) {
            // 然后传给Adapter，并设置hasMore为true
            adapter.updateList(newsList, true);
            adapter.notifyDataSetChanged();
        } else {
            adapter.updateList(null, false);
        }
        resetBanner();
        refreshLayout.setRefreshing(false);
//        nestedScrollView.scrollTo(0, 0);
    }

    private void resetBanner() {
        List<String> bannerList = new ArrayList<>();
        for (News item : newsList.subList(0, 5)) {
            bannerList.add(item.getPic());
        }
        List<String> bannerList_Title = new ArrayList<>();
        for (News title : newsList.subList(0, 5)) {
            bannerList_Title.add(title.getTitle());
        }
        banner.setImages(bannerList);  //设置banner中显示图片
        banner.setBannerTitles(bannerList_Title);
        banner.start();  //设置完毕后调用
    }

    public void updateRecyclerView(int fromIndex, int number, String category) {
        homeViewModel.downLoadNews(fromIndex, number, category);
        List<News> newData = newsList;
        Log.d(TAG, "First Title: " + newsList.get(0).getTitle());
        if (newData.size() > 0) {
            // 然后传给Adapter，并设置hasMore为true
            adapter.updateList(newData, true);
            adapter.notifyDataSetChanged();
        } else {
            adapter.updateList(null, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // ToDo
                    if ((System.currentTimeMillis() - exitTime) > 2000) {
                        Toast.makeText(getActivity(), "Press again to exit", Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        getActivity().finish();
                        System.exit(0);
                    }
                    return true;
                }
                return false;
            }
        });
    }



}