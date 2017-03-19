package com.example.android.newsapp.Model;

/**
 * Created by Gauri Gadkari on 3/18/17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ResponseBody {
    @SerializedName("response")
    @Expose
    private Response response;
    public Response getResponse() {
        return response;
    }
    public void setResponse(Response response) {
        this.response = response;
    }
}