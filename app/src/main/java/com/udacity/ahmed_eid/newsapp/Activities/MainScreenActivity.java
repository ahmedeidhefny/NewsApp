package com.udacity.ahmed_eid.newsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.ahmed_eid.newsapp.Adapters.SourcesAdapter;
import com.udacity.ahmed_eid.newsapp.Model.News;
import com.udacity.ahmed_eid.newsapp.Model.Source;
import com.udacity.ahmed_eid.newsapp.R;
import com.udacity.ahmed_eid.newsapp.Utilies.AppConstants;
import com.udacity.ahmed_eid.newsapp.Utilies.GetNewsArticlesList;

import java.util.ArrayList;

public class MainScreenActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ArrayList<String> allNewsURLs;
    private RecyclerView sourceRecycler;
    GetNewsArticlesList newsArticlesList;
    private static final String TAG = "MainScreenActivity";

    //handle internet connection
    private TextView noInternetText;
    private ContentLoadingProgressBar loadingProgressBar;
    private RelativeLayout progressLayout;
    private ConnectivityManager conMgr;

    //data of your favorite news source
    private String sourceName = null;
    private int sourceImage;
    private String sourceTopUrl = null;
    private String sourceAllUrl = null;
    private String sourceWebsite = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        initializeUI();
        getDataFromSharedPreference();
        if (savedInstanceState != null) {
            sourceName = savedInstanceState.getString(AppConstants.BUNDLE_SourcesNameKey);
            sourceTopUrl = savedInstanceState.getString(AppConstants.BUNDLE_SourcesTopURLSKey);
            sourceAllUrl = savedInstanceState.getString(AppConstants.BUNDLE_SourcesAllURLKey);
            sourceWebsite = savedInstanceState.getString(AppConstants.BUNDLE_SourcesWebsiteKey);
            sourceImage = savedInstanceState.getInt(AppConstants.BUNDLE_SourcesImageKey);
        }

        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewSourceActivity.class);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_ADD);
            }
        });
        if (conMgr.getActiveNetworkInfo() == null
                || !conMgr.getActiveNetworkInfo().isAvailable()
                || !conMgr.getActiveNetworkInfo().isConnected()) {
            toggleError();
            noInternetText.setText(R.string.error_massage_no_internet);
        } else {
            getData();
        }

    }

    private void initializeUI() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        allNewsURLs = new ArrayList<>();
        sourceRecycler = findViewById(R.id.recycler_sources);
        newsArticlesList = new GetNewsArticlesList(this);
        loadingProgressBar = findViewById(R.id.progress_bar);
        progressLayout = findViewById(R.id.progress_layout);
        noInternetText = findViewById(R.id.error_massage_display);
        conMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        progressLayout.setVisibility(View.VISIBLE);
        loadingProgressBar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_CODE_ADD) {
            if (data != null) {
                int radioButtonId = data.getIntExtra(AppConstants.INTENT_RadioBtnKey, 0);
                Toast.makeText(this, R.string.loading_massage, Toast.LENGTH_SHORT).show();
                checkedRadioButton(radioButtonId);
                if (conMgr.getActiveNetworkInfo() == null
                        || !conMgr.getActiveNetworkInfo().isAvailable()
                        || !conMgr.getActiveNetworkInfo().isConnected()) {
                    Snackbar.make(toolbar, R.string.check_internet_massage, Snackbar.LENGTH_LONG).show();
                } else {
                    getData();
                    saveDataInSharedPreference();
                }
            }
        }
    }

    public void checkedRadioButton(int radioButtonId) {
        if (radioButtonId == R.id.mail_radioBtn) {
            sourceName = "Daily Mail";
            sourceImage = R.drawable.mail;
            sourceAllUrl = AppConstants.MailUrl;
            sourceTopUrl = AppConstants.sMailurl;
            sourceWebsite = AppConstants.MailSite;

        } else if (radioButtonId == R.id.bleacher_radioBtn) {
            sourceName = "Bleacher Report";
            sourceImage = R.drawable.bleacher;
            sourceAllUrl = AppConstants.sBleacherurl;
            sourceTopUrl = AppConstants.BleacherUrl;
            sourceWebsite = AppConstants.BleacherSite;

        } else if (radioButtonId == R.id.alJazeera_radioBtn) {
            sourceName = "RT News";
            sourceImage = R.drawable.rt;
            sourceAllUrl = AppConstants.sRTurl;
            sourceTopUrl = AppConstants.RTUrl;
            sourceWebsite = AppConstants.RTsite;
        }
    }

    public void saveDataInSharedPreference() {
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = app_preferences.edit();
        editor.putString(AppConstants.ShardPreference_SourcesNameKey, sourceName);
        editor.putString(AppConstants.ShardPreference_SourcesTopURLSKey, sourceTopUrl);
        editor.putString(AppConstants.ShardPreference_SourcesAllURLKey, sourceAllUrl);
        editor.putInt(AppConstants.ShardPreference_SourcesImageKey, sourceImage);
        editor.putString(AppConstants.ShardPreference_SourcesWebsiteKey, sourceWebsite);
        editor.commit();
    }

    public void getDataFromSharedPreference() {
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sourceName = app_preferences.getString(AppConstants.ShardPreference_SourcesNameKey, null);
        sourceTopUrl = app_preferences.getString(AppConstants.ShardPreference_SourcesTopURLSKey, null);
        sourceAllUrl = app_preferences.getString(AppConstants.ShardPreference_SourcesAllURLKey, null);
        sourceImage = app_preferences.getInt(AppConstants.ShardPreference_SourcesImageKey, 0);
        sourceWebsite = app_preferences.getString(AppConstants.ShardPreference_SourcesWebsiteKey, null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.BUNDLE_SourcesNameKey, sourceName);
        outState.putString(AppConstants.BUNDLE_SourcesTopURLSKey, sourceTopUrl);
        outState.putString(AppConstants.BUNDLE_SourcesAllURLKey, sourceAllUrl);
        outState.putString(AppConstants.BUNDLE_SourcesWebsiteKey, sourceWebsite);
        outState.putInt(AppConstants.BUNDLE_SourcesImageKey, sourceImage);

    }

    public void getData() {
        newsArticlesList.getAllNewsArticles(AppConstants.BBCurl, new GetNewsArticlesList.VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<News> news) {
                getData(news);
            }
        });


    }

    public void getData(final ArrayList<News> news1) {
        newsArticlesList.getAllNewsArticles(AppConstants.ABCurl, new GetNewsArticlesList.VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<News> news2) {
                getData(news1, news2);
            }
        });
    }

    public void getData(final ArrayList<News> news1, final ArrayList<News> news2) {
        final ArrayList<Source> sources = new ArrayList<>();
        newsArticlesList.getAllNewsArticles(AppConstants.CNNul, new GetNewsArticlesList.VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<News> news3) {
                if (sourceName == null && sourceAllUrl == null && sourceTopUrl == null && sourceWebsite == null) {
                    sources.add(0, new Source("BBC News", R.drawable.bbc, AppConstants.BBCsite, AppConstants.sBBCurl, news1));
                    sources.add(1, new Source("ABC News", R.drawable.abc, AppConstants.ABCsite, AppConstants.sABCurl, news2));
                    sources.add(2, new Source("CNN News", R.drawable.cnn, AppConstants.CNNsite, AppConstants.sCNNurl, news3));
                    setUpSourceRecyclerView(sources);
                    Log.e(TAG, "sourceData " + sources.get(0).getName());
                } else
                    getData(news1, news2, news3);
            }
        });
    }

    public void getData(final ArrayList<News> news1, final ArrayList<News> news2, final ArrayList<News> news3) {
        if (sourceName != null && sourceAllUrl != null && sourceTopUrl != null && sourceWebsite != null) {
            final ArrayList<Source> sources = new ArrayList<>();
            newsArticlesList.getAllNewsArticles(sourceTopUrl, new GetNewsArticlesList.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<News> news4) {
                    sources.add(0, new Source("BBC News", R.drawable.bbc, AppConstants.BBCsite, AppConstants.sBBCurl, news1));
                    sources.add(1, new Source("ABC News", R.drawable.abc, AppConstants.ABCsite, AppConstants.sABCurl, news2));
                    sources.add(2, new Source("CNN News", R.drawable.cnn, AppConstants.CNNsite, AppConstants.sCNNurl, news3));
                    sources.add(3, new Source(sourceName, sourceImage, sourceWebsite,sourceAllUrl, news4));
                    setUpSourceRecyclerView(sources);
                }
            });
        } else
            Log.e(TAG, "sourceFavorite is empty");
    }

    public void setUpSourceRecyclerView(ArrayList<Source> sources) {
        if (sources.size() != 0 && !sources.isEmpty() && sources != null) {
            toggleShowData();
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            sourceRecycler.setLayoutManager(layoutManager);
            SourcesAdapter sourcesAdapter = new SourcesAdapter(this, sources);
            sourcesAdapter.notifyDataSetChanged();
            sourceRecycler.setAdapter(sourcesAdapter);
        } else {
            Log.e(TAG, "List of sources is empty");
            toggleError();
            noInternetText.setText(R.string.error_massage_newsArticle);
        }
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
        sourceRecycler.setVisibility(View.GONE);
        noInternetText.setVisibility(View.VISIBLE);
    }

    private void toggleShowData() {
        progressLayout.setVisibility(View.GONE);
        loadingProgressBar.hide();
        noInternetText.setVisibility(View.GONE);
        sourceRecycler.setVisibility(View.VISIBLE);
    }
}
