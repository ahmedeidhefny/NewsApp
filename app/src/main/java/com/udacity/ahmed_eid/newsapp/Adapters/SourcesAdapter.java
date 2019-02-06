package com.udacity.ahmed_eid.newsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.ahmed_eid.newsapp.Activities.AllNewsArticleActivity;
import com.udacity.ahmed_eid.newsapp.R;
import com.udacity.ahmed_eid.newsapp.Utilies.AppConstants;
import com.udacity.ahmed_eid.newsapp.Model.News;
import com.udacity.ahmed_eid.newsapp.Model.Source;

import java.util.ArrayList;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.sourceViewHolder> {

    private Context mContext;
    private ArrayList<Source> sources;

    public SourcesAdapter(Context mContext, ArrayList<Source> sources) {
        this.mContext = mContext;
        this.sources = sources;
    }

    @NonNull
    @Override
    public sourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView;
        myView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_news_source, parent, false);
        return new sourceViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull sourceViewHolder holder, final int position) {
        final Source source = sources.get(position);
        ArrayList<News> news = source.getNews();

        holder.sTitle.setText(source.getName());
        //holder.sTitle.setMovementMethod(LinkMovementMethod.getInstance());
        holder.sTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = source.getWebsiteLink();
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent mCustomTabsIntent = builder.build();
                builder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                mCustomTabsIntent.launchUrl(mContext, Uri.parse(link));
            }
        });

        holder.sImage.setImageResource(source.getImage());
        if (news.size() != 0) { //&& !news.isEmpty() && news != null
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            holder.newsArticlesRecyclerView.setLayoutManager(layoutManager);
            NewsAdapter newsAdapter = new NewsAdapter(mContext, news);
            newsAdapter.notifyDataSetChanged();
            holder.newsArticlesRecyclerView.setAdapter(newsAdapter);
        } else
            Log.e("Source Adapter", "news is empty");

        holder.moreLable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AllNewsArticleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String url = source.getAllNewsURL();
                intent.putExtra(AppConstants.INTENT_urlObjectKey, url);
                String name = source.getName();
                intent.putExtra(AppConstants.INTENT_SourceNameKey, name);
                String image = String.valueOf(source.getImage());
                intent.putExtra(AppConstants.INTENT_SourceImageKey, image);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    public class sourceViewHolder extends RecyclerView.ViewHolder {
        ImageView sImage;
        TextView sTitle, moreLable;
        RecyclerView newsArticlesRecyclerView;

        public sourceViewHolder(View itemView) {
            super(itemView);
            sTitle = itemView.findViewById(R.id.source_name);
            sImage = itemView.findViewById(R.id.source_image);
            newsArticlesRecyclerView = itemView.findViewById(R.id.recycler_source_news);
            moreLable = itemView.findViewById(R.id.more_label);
        }
    }
}
