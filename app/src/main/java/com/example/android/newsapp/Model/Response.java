package com.example.android.newsapp.Model;

/**
 * Created by Gauri Gadkari on 3/18/17.
 */
import java.util.List;

import com.example.android.newsapp.Article;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("docs")
    @Expose
    private List<Article> articles = null;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

}
