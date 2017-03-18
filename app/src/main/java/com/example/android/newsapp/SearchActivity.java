package com.example.android.newsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.newsapp.Adapters.ArticleArrayAdapter;
import com.example.android.newsapp.Fragment.FilterFragment;
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

import static android.R.attr.id;

public class SearchActivity extends AppCompatActivity {
    //@BindView(R.id.btnSearch) Button btnSearch;
    //@BindView(R.id.etSearchtext) EditText etQuery;
    @BindView(R.id.grdNewsResults) RecyclerView grdResults;
    Context context = null;
    private EndlessRecyclerViewScrollListener scrollListener;
    private String searchQuery;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        context = this;
        articles = new ArrayList<>();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        grdResults.setLayoutManager(staggeredGridLayoutManager);
        adapter = new ArticleArrayAdapter(this, articles);
        grdResults.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int newPage, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(newPage);
            }
        };
        grdResults.addOnScrollListener(scrollListener);




        //Picasso picasso = Picasso.with(context);
        //picasso.setIndicatorsEnabled(true);
        //picasso.setLoggingEnabled(true);

    }

    private void loadNextDataFromApi(int newPage) {
        networkCall(searchQuery, newPage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem filterItem = menu.findItem(R.id.action_settings);
        //final SearchView filterview = (SearchView) MenuItemCompat.getActionView(filterItem);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentManager fm = getSupportFragmentManager();

                FilterFragment filterFragment = new FilterFragment();
                filterFragment.show(fm,"Filters");
                //Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();
                //Open new Dialog Fragment for Filters
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                search(query);
                searchView.clearFocus();
                searchView.setQuery("",false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }



    //@OnClick(R.id.btnSearch)
    public void search(String query){
        //String query = etQuery.getText().toString();

        //Toast.makeText(this, "Search text is "+query, Toast.LENGTH_LONG).show();

        int page = 0;
        if (query.length() == 0) {
            Toast.makeText(this, "Search text can not be empty", Toast.LENGTH_LONG).show();
        }
        articles.clear();
        networkCall(query, page);

    }
    public void networkCall(String query,  int page){
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref",Context.MODE_PRIVATE);
        String date = sharedPreferences.getString("date","");
        String newsDesk = sharedPreferences.getString("newsDesk","");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        if(newsDesk!="") {
            params.put("fq", newsDesk);
        }
        params.put("api-key", "ad717c2ec12d4ec28f3463dc0e619bc2");
        params.put("page", page);
        params.put("q", query);

        if(date!="") {
            params.put("begin_date", date);
        }
        Log.d("URL",url);
        Log.d("Params", params.toString());
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