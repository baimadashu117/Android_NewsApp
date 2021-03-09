package com.example.newstest.ui.collection;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newstest.Adapter.CollectAdapter;
import com.example.newstest.Beans.News;
import com.example.newstest.R;
import com.example.newstest.SlideRecyclerView;
import com.example.newstest.UserOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CollectionFragment extends Fragment {

    private CollectionViewModel collectionViewModel;
    private Button bt;
    private TextView tv;
    private int num;
    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        collectionViewModel =
                new ViewModelProvider(getActivity()).get(CollectionViewModel.class);
        root = inflater.inflate(R.layout.fragment_collection, container, false);
        return root;
    }

    SharedPreferences sp;
    private List<News> newsList = new ArrayList<>();
    UserOpenHelper helper;
    SQLiteDatabase db;


    @Override
    public void onResume() {
        super.onResume();
        getCollectList();
    }

    public void getCollectList() {
        newsList.clear();
        sp = getContext().getSharedPreferences("data", MODE_PRIVATE);
        helper = new UserOpenHelper(getContext());
        db = helper.getWritableDatabase();
        String sql = String.format("select _HEAD,_MSG,_TITLE from res where _UID=" + sp.getInt("id", -1) + " ");
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                News a = new News();
                a.setContent(cursor.getString(0));
                a.setPic(cursor.getString(1));
                a.setTitle(cursor.getString(2));
                a.setTime(new Date(System.currentTimeMillis()));
                newsList.add(a);
            }
        }
        SlideRecyclerView slideRecyclerView = root.findViewById(R.id.news_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        slideRecyclerView.setLayoutManager(layoutManager);
        CollectAdapter adapter = new CollectAdapter(newsList, root,getContext());
        adapter.setOnclick(new CollectAdapter.onItemClick() {
            @Override
            public void onclick(int p) {

            }

            @Override
            public void onDelete(int position) {
                News news = newsList.get(position);
                String sql = String.format("delete from res where _TITLE= ?");
                db.execSQL(sql, new String[]{news.getTitle()});
                getCollectList();
            }
        });
        slideRecyclerView.setAdapter(adapter);
    }

}