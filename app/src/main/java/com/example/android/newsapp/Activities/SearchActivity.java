package com.example.android.newsapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.databinding.DataBindingUtil;
import android.provider.BaseColumns;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.SearchView;

import com.example.android.newsapp.Adapters.ArticleArrayAdapter;
import com.example.android.newsapp.Adapters.SuggestionAdapter;
import com.example.android.newsapp.Article;
import com.example.android.newsapp.Utilities.EndlessRecyclerViewScrollListener;
import com.example.android.newsapp.Fragment.FilterFragment;
import com.example.android.newsapp.Model.ResponseBody;
import com.example.android.newsapp.Network.ApiInterface;
import com.example.android.newsapp.R;
import com.example.android.newsapp.Utilities.Utilities;
import com.example.android.newsapp.databinding.ActivitySearchBinding;
import com.google.common.collect.Lists;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    //@BindView(R.id.btnSearch) Button btnSearch;
    //@BindView(R.id.etSearchtext) EditText etQuery;
    RecyclerView grdResults;
    Context context = null;
    private EndlessRecyclerViewScrollListener scrollListener;
    private String searchQuery;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    SimpleCursorAdapter cursorAdapter;
    ArrayList<String> suggestions;
    List<String> items;
    SuggestionAdapter<String> stringSuggestionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        Toolbar toolbar;
        toolbar = binding.toolbar;
        //= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        ButterKnife.bind(this);
        grdResults = binding.grdNewsResults;

        //grdResults = (RecyclerView) findViewById(R.id.grdNewsResults);
        context = this;
        articles = new ArrayList<>();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        grdResults.setLayoutManager(staggeredGridLayoutManager);
        adapter = new ArticleArrayAdapter(this, articles);
        grdResults.setAdapter(adapter);
        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
        //cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to);

        scrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int newPage, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(newPage);
            }
        };
        grdResults.addOnScrollListener(scrollListener);
        if (Utilities.isNetworkAvailable(context) && Utilities.isOnline()) {
            retroNetworkCall("", 0);
        } else {
            Snackbar.make(findViewById(R.id.searchActivity), "Make sure your device is connected to the internet", Snackbar.LENGTH_LONG).show();
        }


        //Picasso picasso = Picasso.with(context);
        //picasso.setIndicatorsEnabled(true);
        //picasso.setLoggingEnabled(true);

    }


    private void loadNextDataFromApi(int newPage) {
        //networkCall(searchQuery, newPage);
        if (Utilities.isNetworkAvailable(context) && Utilities.isOnline()) {
            retroNetworkCall(searchQuery, newPage);
        } else {
            Snackbar.make(findViewById(R.id.searchActivity), "Make sure your device is connected to the internet", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem filterItem = menu.findItem(R.id.action_settings);
        //final SearchView filterview = (SearchView) MenuItemCompat.getActionView(filterItem);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchView.SearchAutoComplete searchSrcTextView = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //List<String> items = Lists.newArrayList(suggestions);
        items = new ArrayList<String>();
        stringSuggestionAdapter = new SuggestionAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
        searchSrcTextView.setThreshold(0);
        searchSrcTextView.setAdapter(stringSuggestionAdapter);
        //SearchView.SearchAutoComplete();
        //searchView.setSuggestionsAdapter(cursorAdapter);
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentManager fm = getSupportFragmentManager();

                FilterFragment filterFragment = new FilterFragment();
                filterFragment.show(fm, "Filters");
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
                searchView.setQuery("", false);
                items.add(query);
                stringSuggestionAdapter.notifyDataSetChanged();
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
    public void search(String query) {
        //String query = etQuery.getText().toString();

        //Toast.makeText(this, "Search text is "+query, Toast.LENGTH_LONG).show();

        int page = 0;
        if (query.length() == 0) {
            Toast.makeText(this, "Search text can not be empty", Toast.LENGTH_LONG).show();
        }
        articles.clear();
        //networkCall(query, page);
        if (Utilities.isNetworkAvailable(context) && Utilities.isOnline()) {
            retroNetworkCall(query, page);
        } else {
            Snackbar.make(findViewById(R.id.searchActivity), "Make sure your device is connected to the internet", Snackbar.LENGTH_LONG).show();
        }

    }

    public void retroNetworkCall(String query, final int page) {
        final String BASE_URL = "https://api.nytimes.com/svc/search/v2/";
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String date = sharedPreferences.getString("date", "");
        //String newsDesk = sharedPreferences.getString("newsDesk","");
        Boolean newsDeskBoolean = sharedPreferences.getBoolean("newsDeskBoolean", false);
        Boolean art = sharedPreferences.getBoolean("art", false);
        Boolean fashion = sharedPreferences.getBoolean("fashion", false);
        Boolean sports = sharedPreferences.getBoolean("sports", false);
        Boolean education = sharedPreferences.getBoolean("education", false);
        Boolean health = sharedPreferences.getBoolean("health", false);
        String newsDesk = "news_desk:(";
        if (art) {
            newsDesk = newsDesk + "\"Art\" ";
        }
        if (fashion) {
            newsDesk = newsDesk + "\"Fashion\" ";
        }
        if (sports) {
            newsDesk = newsDesk + "\"Sports\" ";
        }
        if (education){
            newsDesk = newsDesk + "\"Education\" ";
        }
        if (health){
            newsDesk = newsDesk + "\"Health\" ";
        }
        newsDesk = newsDesk + ")";
        String sortString;
        int sort = sharedPreferences.getInt("sort", 0);
        if (sort == 0) {
            sortString = "Newest";
        } else {
            sortString = "Oldest";
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Map<String, String> params = new HashMap<>();
        if (newsDeskBoolean) {
            params.put("fq", newsDesk);
        }
        params.put("api-key", "ad717c2ec12d4ec28f3463dc0e619bc2");
        params.put("page", page + "");
        if (query != "") {
            params.put("q", query);
        } else {
            searchQuery = "";
        }
        params.put("sort", sortString);

        if (date != "") {
            params.put("begin_date", date);
        }


        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getSearchResultsWithFilter(params);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("DEBUG", response.toString());
                //Log.d("DEBUG", response.body().toString());
                if (response.body() != null) {
                    if (response.body().getStatus().equals("OK")) {
                        //if (!(response.body().getResponse().equals(null))) {
                        List<Article> responseArticles = response.body().getResponse().getArticles();
                        if (responseArticles.size() != 0) {
                            articles.addAll(responseArticles);
                            adapter.notifyDataSetChanged();
                        } else {
                            if (page == 0) {
                                Snackbar.make(findViewById(R.id.searchActivity), "No results found!!", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(findViewById(R.id.searchActivity), "No more Search results", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Snackbar.make(findViewById(R.id.searchActivity), "Oops something went wrong!!", Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    Snackbar.make(findViewById(R.id.searchActivity), "Oops something went wrong!!", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("DEBUG", t.toString());
            }
        });
    }

    public void networkCall(String query, int page) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String date = sharedPreferences.getString("date", "");
        String newsDesk = sharedPreferences.getString("newsDesk", "");
        int sort = sharedPreferences.getInt("sort", 0);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        if (newsDesk != "") {
            params.put("fq", newsDesk);
        }
        params.put("api-key", "ad717c2ec12d4ec28f3463dc0e619bc2");
        params.put("page", page);
        params.put("q", query);

        if (date != "") {
            params.put("begin_date", date);
        }
        Log.d("URL", url);
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

//                    articles.addAll(Article.fromJSonArray(articleJsonResults));
//                    adapter.notifyDataSetChanged();
//                    Log.d("DEBUG",articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}