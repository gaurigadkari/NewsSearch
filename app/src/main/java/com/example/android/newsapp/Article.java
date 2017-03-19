package com.example.android.newsapp;


import com.example.android.newsapp.Model.Headline;
import com.example.android.newsapp.Model.Multimedium;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Article {
    @SerializedName("web_url")
    @Expose
    String webUrl;
    @SerializedName("headline")
    @Expose
    Headline headline;
    @SerializedName("multimedia")
    @Expose
    List<Multimedium> multimedia = null;
//    String thumbNail;

    public Article() {
    }

    public String getWebUrl() {
        return webUrl;
    }

    public Headline getHeadline() {
        return headline;
    }

//    public String getThumbNail() {
//        return thumbNail;
//    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public List<Multimedium> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Multimedium> multimedia) {
        this.multimedia = multimedia;
    }



//    public Article (JSONObject jsonObject){
//
//        try {
//            this.webUrl = jsonObject.getString("web_url");
//            this.headline = jsonObject.getJSONObject("headline").getString("main");
//            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
//            if(multimedia.length() > 0){
//                JSONObject multimediaJSon = multimedia.getJSONObject(0);
//                this.thumbNail = "http://www.nytimes.com/" + multimediaJSon.getString("url");
//            }
//            else {
//                this.thumbNail = "";
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

//    public static ArrayList<Article> fromJSonArray(JSONArray array){
//        ArrayList<Article> results = new ArrayList<>();
//        for (int i = 0; i < array.length(); i++){
//            try {
//                results.add(new Article(array.getJSONObject(i)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//        return results;
//    }
}
