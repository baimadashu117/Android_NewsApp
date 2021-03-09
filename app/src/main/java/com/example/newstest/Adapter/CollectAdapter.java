package com.example.newstest.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newstest.Beans.News;
import com.example.newstest.R;
import com.example.newstest.Tasks.DownloadPicTask;
import com.example.newstest.UserOpenHelper;
import com.example.newstest.ui.news.NewsFragment;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {

    private List<News> mNewsList;
    private static final String TAG = "NewsAdapter";
    private onItemClick mOnItemClick;
    private View root;
    SQLiteDatabase db;
    private Context context;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            HashMap<String, Object> hashMap = (HashMap<String, Object>) msg.obj;
            ImageView imageView = (ImageView) hashMap.get("imageview");
            Drawable drawable = (Drawable) hashMap.get("drawable");
            imageView.setImageDrawable(drawable);
        }
    };

    public CollectAdapter(List<News> newsList, View root, Context context) {
        mNewsList = newsList;
        this.root = root;
        db = new UserOpenHelper(context).getWritableDatabase();
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collect_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final News news = mNewsList.get(position);
        holder.newsTitle.setText(news.getTitle());
        ImageView imageView = holder.newsImage;
        DownloadPicTask task = new DownloadPicTask(imageView, handler);
        task.execute(news.getPic());
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClick.onclick(position);
                return true;
            }
        });
        holder.newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                News news = mNewsList.get(position);
                Log.d(TAG, "Date=" + news.getTime());
                Toast.makeText(view.getContext(), news.getTitle(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putParcelable("news", news);
                NewsFragment fragment = new NewsFragment();
                fragment.setArguments(bundle);
                NavController controller = Navigation.findNavController(root);
                controller.navigate(R.id.nav_news, bundle);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClick.onDelete(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }


    public interface onItemClick {
        void onclick(int p);

        void onDelete(int position);
    }

    public void setOnclick(onItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View newsView;
        ImageView newsImage;
        TextView newsTitle;
        TextView delete;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsView = itemView;
            newsImage = itemView.findViewById(R.id.newsImage);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            delete = itemView.findViewById(R.id.delete);
            item = itemView.findViewById(R.id.item);
        }
    }
}
