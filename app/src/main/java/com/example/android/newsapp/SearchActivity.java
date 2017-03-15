package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.newsapp.Adapters.ArticleArrayAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.etSearchtext) EditText etQuery;
    @BindView(R.id.grdNewsResults) RecyclerView grdResults;
    Context context = null;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        context = this;
        articles = new ArrayList<>();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        grdResults.setLayoutManager(staggeredGridLayoutManager);
        adapter = new ArticleArrayAdapter(this, articles);
        grdResults.setAdapter(adapter);
        //Picasso picasso = Picasso.with(context);
        //picasso.setIndicatorsEnabled(true);
        //picasso.setLoggingEnabled(true);

    }
    @OnClick(R.id.btnSearch)
    public void search(){
        String query = etQuery.getText().toString();

        //Toast.makeText(this, "Search text is "+query, Toast.LENGTH_LONG).show();
        if (query.length() == 0) {
            Toast.makeText(this, "Search text can not be empty", Toast.LENGTH_LONG).show();
        }
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
            RequestParams params = new RequestParams();
            params.put("api-key", "ad717c2ec12d4ec28f3463dc0e619bc2");
            params.put("page", 0);
            params.put("q", query);

            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(context, "Failing", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray articleJsonResults = null;
                    try {
                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");


                        articles.addAll(Article.fromJSonArray(articleJsonResults));
                        adapter.notifyDataSetChanged();
                        Log.d("DEBUG",articles.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

    }
}