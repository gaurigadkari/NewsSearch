package com.example.android.newsapp.Model;

/**
 * Created by Gauri Gadkari on 3/18/17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Headline {

    @SerializedName("main")
    @Expose
     String main;
    @SerializedName("print_headline")
    @Expose
     String printHeadline;
    @SerializedName("kicker")
    @Expose
     String kicker;
    @SerializedName("content_kicker")
    @Expose
     String contentKicker;

    public Headline() {
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getPrintHeadline() {
        return printHeadline;
    }

    public void setPrintHeadline(String printHeadline) {
        this.printHeadline = printHeadline;
    }

    public String getKicker() {
        return kicker;
    }

    public void setKicker(String kicker) {
        this.kicker = kicker;
    }

    public String getContentKicker() {
        return contentKicker;
    }

    public void setContentKicker(String contentKicker) {
        this.contentKicker = contentKicker;
    }

}

