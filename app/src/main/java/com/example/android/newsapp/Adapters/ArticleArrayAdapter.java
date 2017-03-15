package com.example.android.newsapp.Adapters;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.newsapp.Article;
import com.example.android.newsapp.R;


import java.util.ArrayList;

public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ArticleViewHolder>{
    private ArrayList<Article> articles;
    Context context;
    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.article, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article currentArticle = articles.get(position);
        holder.headline.setText(currentArticle.getHeadline());
        //holder.webUrl.setText(currentArticle.getWebUrl());
        Log.d("DEBUG", currentArticle.getThumbNail());
        if(currentArticle.getThumbNail().length() > 0) {
            String imageUrl = currentArticle.getThumbNail();
            //holder.thumbNail.setImageURI(Uri.parse("http://www.nytimes.com/images/2017/02/12/arts/12KIDMAN/12KIDMAN-thumbWide-v2.jpg"));
            Glide.with(context).load(imageUrl).error(R.mipmap.ic_launcher).into(holder.thumbNail);
        }
        else {
            holder.thumbNail.setVisibility(View.GONE);
        }
    }
    public ArticleArrayAdapter(Context context, ArrayList<Article> articles){
        this.context = context;
        this.articles = articles;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        protected TextView headline;
        protected TextView webUrl;
        protected ImageView thumbNail;
        public ArticleViewHolder(View itemView) {
            super(itemView);
            headline = (TextView) itemView.findViewById(R.id.headline);
            //webUrl = (TextView) itemView.findViewById(R.id.webUrl);
            thumbNail = (ImageView) itemView.findViewById(R.id.thumbnail);

        }
    }
}

