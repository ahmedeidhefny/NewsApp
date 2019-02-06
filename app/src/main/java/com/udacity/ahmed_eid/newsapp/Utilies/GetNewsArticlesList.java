package com.udacity.ahmed_eid.newsapp.Utilies;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.udacity.ahmed_eid.newsapp.Model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetNewsArticlesList {
    private Context mContext;
    private RequestQueue requestQueue;

    public GetNewsArticlesList(Context mContext) {
        this.mContext = mContext;
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public void getAllNewsArticles(String url, final VolleyCallback callback) {
        final ArrayList<News> newsList = new ArrayList<>();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray articles = response.getJSONArray("articles");
                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject article = articles.getJSONObject(i);
                        String title = article.getString("title");
                        String author = article.getString("author");
                        String description = article.getString("description");
                        String url = article.getString("url");
                        String urlToImage = article.getString("urlToImage");
                        String publishedAt = article.getString("publishedAt");
                        String content = article.getString("content");
                        News news = new News(title, author, description, url, urlToImage, publishedAt, content);
                        newsList.add(i, news);
                    }
                    callback.onSuccess(newsList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Volley Class", "Error Response" );
                Log.e("Volley Class", "" + error.getMessage());
            }
        });
        requestQueue.add(objectRequest);
    }

    public interface VolleyCallback {
        void onSuccess(ArrayList<News> news);
    }

}
