package com.example.android.newsapp.Network;

import com.example.android.newsapp.Model.ResponseBody;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Gauri Gadkari on 3/18/17.
 */

public interface ApiInterface {

    @GET("articlesearch.json")
    Call<ResponseBody> getSearchResults(@Query("api-key") String apiKey, @Query("q") String query, @Query("page") int page);
            //, @Query("fq") String newsDesk);

    @GET("articlesearch.json")
    Call<ResponseBody> getSearchResultsWithFilter(@QueryMap Map<String, String> options);

//    @GET("articlesearch.json")
//    Call<ResponseBody> getSearchResultsNewsDesk(@Query("api-key") String apiKey, @Query("q") String query, @Query("page") int page, @Query("fq") String newsDesk);
//
//    @GET("articlesearch.json")
//    Call<ResponseBody> getSearchResultsDate(@Query("api-key") String apiKey, @Query("q") String query, @Query("page") int page, @Query("begin_date") String date);
//    //, @Query("fq") String newsDesk);
//
//    @GET("articlesearch.json")
//    Call<ResponseBody> getSearchResultsAllFilters(@Query("api-key") String apiKey, @Query("q") String query, @Query("page") int page, @Query("sort") String sort, @Query("fq") String newsDesk, @Query("begin_date") String date);
}
