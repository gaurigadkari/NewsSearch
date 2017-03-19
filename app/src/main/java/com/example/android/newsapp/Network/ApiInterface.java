package com.example.android.newsapp.Network;

import com.example.android.newsapp.Model.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Gauri Gadkari on 3/18/17.
 */

public interface ApiInterface {
    @GET("articlesearch.json")
    Call<ResponseBody> getSearchResults(@Query("api-key") String apiKey, @Query("q") String query, @Query("page") int page);
            //, @Query("fq") String newsDesk);

}
