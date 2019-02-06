package com.udacity.ahmed_eid.newsapp.Utilies;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.udacity.ahmed_eid.newsapp.Model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolleyManager {
    private Context mContext;
    private RequestQueue requestQueue;
    private static final String TAG = "VolleyClass";

    public VolleyManager(Context mContext) {
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
                Log.e(TAG, "ErrorResponseVolley is...");
                //Log.e(TAG, "" + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    Log.e(TAG, "TimeoutError or NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Log.e(TAG, "AuthFailureError");
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Log.e(TAG, "ServerError");
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Log.e(TAG, "NetworkError");
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Log.e(TAG, "ParseError");
                }
            }
        });
        requestQueue.add(objectRequest);
    }

    public interface VolleyCallback {
        void onSuccess(ArrayList<News> news);
    }

}
