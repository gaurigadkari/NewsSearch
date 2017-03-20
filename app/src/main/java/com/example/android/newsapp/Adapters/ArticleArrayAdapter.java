package com.example.android.newsapp.Adapters;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.newsapp.Activities.ArticleActivity;
import com.example.android.newsapp.Article;
import com.example.android.newsapp.R;
import com.example.android.newsapp.Utilities.Utilities;


import org.parceler.Parcels;

import java.util.ArrayList;

import static com.example.android.newsapp.R.layout.article;

public class ArticleArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Article> articles;
    Context context;
    private final int articleWithImage = 0, articleWithoutImage = 1;
    String urlShare;
    int requestCode = 100;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case articleWithImage:
                View view1 = inflater.inflate(article, parent, false);
                viewHolder = new ArticleViewHolder(view1);

                break;
            case articleWithoutImage:
                View view2 = inflater.inflate(R.layout.article_without_image, parent, false);
                viewHolder = new ArticleNoImageViewHolder(view2);


        }
        return viewHolder;

//        View itemView = LayoutInflater.from(context).inflate(R.layout.article, parent, false);
//        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Article currentArticle = articles.get(position);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, ArticleActivity.class);
//                intent.putExtra("article", Parcels.wrap(currentArticle));
//                context.startActivity(intent);
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                // set toolbar color and/or setting custom actions before invoking build()
                // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
                //CustomTabsIntent customTabsIntent = builder.build();

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share_white_48dp);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, currentArticle.getWebUrl());

                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setActionButton(bitmap, "Share Link", pendingIntent, true);

                CustomTabsIntent customTabsIntent = builder.build();
                builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));

                if (Utilities.isNetworkAvailable(context) && Utilities.isOnline()) {
                    // and launch the desired Url with CustomTabsIntent.launchUrl()
                    customTabsIntent.launchUrl(context, Uri.parse(currentArticle.getWebUrl()));
                    //builder.setStartAnimations(this, android.R.anim.bounce_interpolator, 0);
                    //webView.loadUrl(article.getWebUrl());
                } else {
                    Snackbar.make(holder.itemView.findViewById(R.id.articleActivity), "Make sure your device is connected to the internet", Snackbar.LENGTH_LONG).show();
                }

                urlShare = currentArticle.getWebUrl();
            }
        });

        switch (holder.getItemViewType()) {
            case articleWithImage:
                ArticleViewHolder holderImage = (ArticleViewHolder) holder;

                Glide.clear(holderImage.thumbNail);
                holderImage.thumbNail.setImageResource(0);
                String imageUrl = currentArticle.getMultimedia().get(0).getUrl();

                holderImage.thumbNail.setImageURI(Uri.parse("http://www.nytimes.com/images/2017/02/12/arts/12KIDMAN/12KIDMAN-thumbWide-v2.jpg"));
                Glide.with(context).load(imageUrl).error(R.mipmap.ic_launcher).placeholder(R.drawable.placeholder_large).into(holderImage.thumbNail);
                holderImage.headline.setText(currentArticle.getHeadline().getMain());
                break;
            case articleWithoutImage:
                ArticleNoImageViewHolder holderNoImage = (ArticleNoImageViewHolder) holder;
                holderNoImage.headline.setText(currentArticle.getHeadline().getMain());
                holderNoImage.snippet.setText(currentArticle.getSnippet());
                break;

        }

//        final Article currentArticle = articles.get(position);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ArticleActivity.class);
//                intent.putExtra("article", Parcels.wrap(currentArticle));
//                context.startActivity(intent);
//            }
//        });
//        holder.headline.setText(currentArticle.getHeadline().getMain());
//        //holder.webUrl.setText(currentArticle.getWebUrl());
////        Log.d("DEBUG", currentArticle.getMultimedia().get(0).getUrl());
//        if(currentArticle.getMultimedia().size() > 0) {
//            String imageUrl = currentArticle.getMultimedia().get(0).getUrl();
//            //Glide.clear(holder.thumbNail);
//            //holder.thumbNail.setImageResource(0);
//
//            //holder.thumbNail.setImageURI(Uri.parse("http://www.nytimes.com/images/2017/02/12/arts/12KIDMAN/12KIDMAN-thumbWide-v2.jpg"));
//            Glide.with(context).load(imageUrl).error(R.mipmap.ic_launcher).into(holder.thumbNail);
//        }
//        else {
//            holder.thumbNail.setVisibility(View.GONE);
//        }
    }

    public ArticleArrayAdapter(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (articles.get(position).getMultimedia().size() > 0) {
            return 0;
        } else return 1;
    }


    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        protected TextView headline;
        //protected TextView webUrl;
        protected ImageView thumbNail;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            headline = (TextView) itemView.findViewById(R.id.headline);
            //webUrl = (TextView) itemView.findViewById(R.id.webUrl);
            thumbNail = (ImageView) itemView.findViewById(R.id.thumbnail);

        }
    }

    public static class ArticleNoImageViewHolder extends RecyclerView.ViewHolder {
        protected TextView headline;
        protected TextView snippet;

        public ArticleNoImageViewHolder(View itemView) {
            super(itemView);
            headline = (TextView) itemView.findViewById(R.id.headline);
            snippet = (TextView) itemView.findViewById(R.id.snippet);

        }
    }
}


