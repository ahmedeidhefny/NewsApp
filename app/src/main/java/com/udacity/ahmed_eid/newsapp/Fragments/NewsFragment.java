package com.udacity.ahmed_eid.newsapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.ahmed_eid.newsapp.Adapters.AllNewsAdapter;
import com.udacity.ahmed_eid.newsapp.Model.News;
import com.udacity.ahmed_eid.newsapp.R;
import com.udacity.ahmed_eid.newsapp.Utilies.AppConstants;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";
    private RecyclerView allNewsRecycler;
    private ArrayList<News> news;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_news, container, false);
        allNewsRecycler = myView.findViewById(R.id.moreRecycler);
        news = new ArrayList<>();

        if (savedInstanceState != null) {
            news = savedInstanceState.getParcelableArrayList(AppConstants.BUNDLE_fragment_newsListKey);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            news = bundle.getParcelableArrayList(AppConstants.BUNDLE_newsListKey);
            if (news != null && news.size() != 0 && !news.isEmpty())
                setUpRecyclerView();
            else
                Log.e(TAG, "newsList is empty");
        } else
            Log.e(TAG, "bundle of Arguments is empty");

        return myView;
    }

    public void setUpRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        allNewsRecycler.setLayoutManager(layoutManager);
        AllNewsAdapter newsAdapter = new AllNewsAdapter(getActivity(), news);
        allNewsRecycler.setAdapter(newsAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(AppConstants.BUNDLE_fragment_newsListKey, news);
    }
}
