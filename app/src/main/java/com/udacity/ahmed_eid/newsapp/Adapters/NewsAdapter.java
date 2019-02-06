package com.udacity.ahmed_eid.newsapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.ahmed_eid.newsapp.Model.News;
import com.udacity.ahmed_eid.newsapp.R;

import java.util.ArrayList;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.newsViewHolder> {
    private Context mContext;
    private ArrayList<News> news;

    public NewsAdapter(Context mContext, ArrayList<News> news) {
        this.mContext = mContext;
        this.news = news;
    }

    @NonNull
    @Override
    public newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView;
        myView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_news_article, parent, false);
        return new newsViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull newsViewHolder holder, int position) {
        final News news1 = news.get(position);
        holder.nTitle.setText(news1.getNTitle());
        Picasso.with(mContext)
                .load(news1.getUrlToImage())
                .placeholder(R.drawable.loadimage)
                .error(R.drawable.news)
                .into(holder.nImage);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                String url= news1.getUrl();
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent mCustomTabsIntent = builder.build();
                builder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
               // Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_back_black_24dp);
                //builder.setCloseButtonIcon(bitmap);
                mCustomTabsIntent.launchUrl(mContext, Uri.parse(url));
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class newsViewHolder extends RecyclerView.ViewHolder {
        ImageView nImage;
        TextView nTitle;
        LinearLayout layout ;

        public newsViewHolder(View itemView) {
            super(itemView);
            nImage = itemView.findViewById(R.id.news_image);
            nTitle = itemView.findViewById(R.id.news_title);
            layout = itemView.findViewById(R.id.news_article_layout);
        }
    }
}
