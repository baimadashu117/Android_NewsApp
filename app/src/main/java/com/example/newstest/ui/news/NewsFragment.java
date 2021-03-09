package com.example.newstest.ui.news;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newstest.Beans.News;
import com.example.newstest.CustomTextView;
import com.example.newstest.R;
import com.example.newstest.UserOpenHelper;
import com.example.newstest.Utils.HTMLUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class NewsFragment extends Fragment {

    private boolean collection = false;
    SQLiteDatabase db;
    News news;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_news, container, false);
        //----------- activity ---------
//        Intent intent = getActivity().getIntent();
//        News news = intent.getParcelableExtra("news");
        //------------- fragment ----------
        Bundle bundle = getArguments();
//        Bundle bundle = getActivity().getIntent().getParcelableExtra("news");
        if (bundle == null) {
            Log.d("Null", "No Object!!!");
        }
        news = bundle.getParcelable("news");

        TextView newsTitle = root.findViewById(R.id.news_title);
        CustomTextView textView = root.findViewById(R.id.newsView);
        newsTitle.setText(news.getTitle());
        ArrayList<HashMap<String, String>> data = HTMLUtil.parseHTML(news.getContent());
        Log.d("NewsContentActivity", "setText");
        textView.setText(data);
        db = new UserOpenHelper(getContext()).getWritableDatabase();

        String sql = String.format("select _HEAD,_MSG,_TITLE from res where _TITLE= ?");
        Cursor cursor = db.rawQuery(sql, new String[]{news.getTitle()});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                collection = true;
            }
        }
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (collection) {
            menu.add(0, 0, 0, "cancel").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        } else {
            menu.add(0, 0, 0, "collection").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        }
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                if (collection) {
                    collection = false;
                    String sql = String.format("delete from res where _TITLE= ?");
                    db.execSQL(sql, new String[]{news.getTitle()});
                } else {
                    ContentValues values = new ContentValues();
                    values.put("_HEAD", news.getContent());
                    values.put("_MSG", news.getPic());
                    values.put("_TITLE", news.getTitle());
                    SharedPreferences sp = getContext().getSharedPreferences("data", MODE_PRIVATE);
                    values.put("_UID", sp.getInt("id", -1));

                    db.insert("res", null, values);
                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT)
                            .show();
                    collection = true;
                }
                getActivity().invalidateOptionsMenu();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}