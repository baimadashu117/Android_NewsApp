package com.example.newstest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.newstest.ui.home.HomeFragment;
import com.example.newstest.ui.home.HomeViewModel;
import com.example.newstest.ui.home.HomeViewModelFactory;
import com.example.newstest.ui.news.NewsFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class Drawer_V1 extends AppCompatActivity {

    private static String TAG = "DrawerActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;
    private HomeViewModel homeViewModel;
    private NavController navController;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreated");
        setContentView(R.layout.activity_drawer__v1);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory(bundle)).get(HomeViewModel.class);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        final NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_collection, R.id.nav_contact, R.id.nav_set)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // head set
        final View headerView = navigationView.getHeaderView(0);
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        TextView header_title = headerView.findViewById(R.id.header_title);
        header_title.setText(sp.getString("name", ""));
        TextView textView = headerView.findViewById(R.id.textView);
        textView.setText(sp.getString("name", "") + "@android.com");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_log:
                        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                        sp.edit().remove("id");
                        startActivity(new Intent(Drawer_V1.this, LoginActivity.class));
                        finish();
                        break;
                    case R.id.nav_collection:
                        navController.navigate(R.id.nav_collection);
                        drawer.closeDrawers();
                        break;
                    case R.id.switch_btn:
                        setNightMode();
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_home:
                        navController.navigate(R.id.nav_home);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_contact:
                        navController.navigate(R.id.nav_contact);
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_set:
                        navController.navigate(R.id.nav_set);
                        drawer.closeDrawers();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer__v1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toutiao:
                Toast.makeText(this, "头条", Toast.LENGTH_SHORT).show();
                homeViewModel.downLoadNews(0, 15, "头条");
                break;
            case R.id.guonei:
                Toast.makeText(this, "国内", Toast.LENGTH_SHORT).show();
                homeViewModel.downLoadNews(0, 15, "国内");
                break;
            case R.id.guoji:
                Toast.makeText(this, "国际", Toast.LENGTH_SHORT).show();
                homeViewModel.downLoadNews(0, 15, "国际");
                break;
            case R.id.zhengzhi:
                Toast.makeText(this, "政治", Toast.LENGTH_SHORT).show();
                homeViewModel.downLoadNews(0, 15, "政治");
                break;
            case R.id.caijing:
                Toast.makeText(this, "财经", Toast.LENGTH_SHORT).show();
                homeViewModel.downLoadNews(0, 15, "财经");
                break;
            case R.id.tiyu:
                Toast.makeText(this, "体育", Toast.LENGTH_SHORT).show();
                homeViewModel.downLoadNews(0, 15, "体育");
                break;
            case R.id.junshi:
                Toast.makeText(this, "军事", Toast.LENGTH_SHORT).show();
                homeViewModel.downLoadNews(0, 15, "军事");
                break;
            case R.id.jiaoyu:
                Toast.makeText(this, "教育", Toast.LENGTH_SHORT).show();
                homeViewModel.downLoadNews(0, 15, "教育");
                break;
            case R.id.keji:
                Toast.makeText(this, "科技", Toast.LENGTH_SHORT).show();
                homeViewModel.downLoadNews(0, 15, "科技");
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setNightMode()
    {
        MyApplication application = (MyApplication)this.getApplication();
        boolean currentMode = application.getMode();
        if(!currentMode)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        application.setMode();
    }

}