package com.udacity.ahmed_eid.newsapp.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.ahmed_eid.newsapp.Fragments.NewsFragment;
import com.udacity.ahmed_eid.newsapp.Model.News;
import com.udacity.ahmed_eid.newsapp.Utilies.AppConstants;
import com.udacity.ahmed_eid.newsapp.R;
import com.udacity.ahmed_eid.newsapp.Utilies.VolleyManager;

import java.util.ArrayList;

public class AllNewsArticleActivity extends AppCompatActivity {

    private static final String TAG = "AllNewsArticleActivity";
    private FrameLayout frameLayout;

    //handle internet connection
    private TextView noInternetText;
    private ContentLoadingProgressBar loadingProgressBar;
    private RelativeLayout progressLayout;
    private ConnectivityManager conMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news_article);
        initializeUI();
        receiveDataFromHomeScreen();
    }

    private void initializeUI() {
        loadingProgressBar = findViewById(R.id.progress_bar);
        progressLayout = findViewById(R.id.progress_layout);
        noInternetText = findViewById(R.id.error_massage_display);
        conMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        progressLayout.setVisibility(View.VISIBLE);
        loadingProgressBar.show();
        frameLayout = findViewById(R.id.container_layout);
    }

    public void receiveDataFromHomeScreen() {
        Intent intent = getIntent();
        if (intent == null) {
            errorMassage();
            return;
        }
        if (!intent.hasExtra(AppConstants.INTENT_urlObjectKey)
                && !intent.hasExtra(AppConstants.INTENT_SourceNameKey)
                && !intent.hasExtra(AppConstants.INTENT_SourceImageKey)) {
            errorMassage();
        } else {
            String sourceName = intent.getStringExtra(AppConstants.INTENT_SourceNameKey);
            setTitle(sourceName);

            String sourceImage = intent.getStringExtra(AppConstants.INTENT_SourceImageKey);
            int image = Integer.parseInt(sourceImage);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(image);
            getSupportActionBar().setDisplayUseLogoEnabled(true);


            String url = intent.getStringExtra(AppConstants.INTENT_urlObjectKey);
            if (conMgr.getActiveNetworkInfo() == null
                    || !conMgr.getActiveNetworkInfo().isAvailable()
                    || !conMgr.getActiveNetworkInfo().isConnected()) {
                toggleError();
                noInternetText.setText(R.string.error_massage_no_internet);
            } else {
                populateUI(url);
            }
        }

    }

    public void populateUI(String url) {
        if (url != null) {
            VolleyManager volleyManager = new VolleyManager(this);
            volleyManager.getAllNewsArticles(url, new VolleyManager.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<News> news) {
                    setUpToggle(news);
                }
            });
        }
    }

    public void setUpToggle(ArrayList<News> news) {
        if (news != null && news.size() != 0) {
            toggleShowData();
            sendDataToFragment(news);
        } else {
            Log.e(TAG, "List of News is empty");
            toggleError();
            noInternetText.setText(R.string.error_massage_newsArticle);
        }
    }

    public void sendDataToFragment(ArrayList<News> news) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstants.BUNDLE_newsListKey, news);
        //Log.e(TAG, "sourceData " + news.get(0).getNTitle());
        FragmentManager fragmentManager = getSupportFragmentManager();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.container_layout, fragment).commit();
    }

    public void errorMassage() {
        finish();
        Toast.makeText(getApplicationContext(), R.string.error_massage_newsArticle, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_close) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleError() {
        progressLayout.setVisibility(View.GONE);
        loadingProgressBar.hide();
        frameLayout.setVisibility(View.GONE);
        noInternetText.setVisibility(View.VISIBLE);
    }

    private void toggleShowData() {
        progressLayout.setVisibility(View.GONE);
        loadingProgressBar.hide();
        noInternetText.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
    }

}
