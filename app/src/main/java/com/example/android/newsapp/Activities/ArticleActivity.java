package com.example.android.newsapp.Activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.*;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.android.newsapp.Article;
import com.example.android.newsapp.R;
import com.example.android.newsapp.Utilities.Utilities;
import com.wang.avi.AVLoadingIndicatorView;

import org.parceler.Parcels;

import static com.example.android.newsapp.R.layout.article;

public class ArticleActivity extends AppCompatActivity {
    private ShareActionProvider shareActionProvider;
    Intent intent;
    String urlShare;
    int requestCode = 100;
    AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));
//        WebView webView = (WebView) findViewById(R.id.wvArticle);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//
//                return true;
//            }
//        });
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolbar color and/or setting custom actions before invoking build()
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        //CustomTabsIntent customTabsIntent = builder.build();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_white_48dp);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);

        CustomTabsIntent customTabsIntent = builder.build();
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (Utilities.isNetworkAvailable(this) && Utilities.isOnline()) {
        // and launch the desired Url with CustomTabsIntent.launchUrl()
            customTabsIntent.launchUrl(this, Uri.parse(article.getWebUrl()));
            //builder.setStartAnimations(this, android.R.anim.bounce_interpolator, 0);
            //webView.loadUrl(article.getWebUrl());
        } else {
            Snackbar.make(findViewById(R.id.articleActivity), "Make sure your device is connected to the internet", Snackbar.LENGTH_LONG).show();
        }

        urlShare = article.getWebUrl();
    }

    public void attachShareIntentAction() {
        if (shareActionProvider != null && intent != null)
            shareActionProvider.setShareIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.article_menu, menu);
        // Locate MenuItem with ShareActionProvider
        //MenuItem item = menu.findItem(R.id.action_share);
        // Fetch reference to the share action provider
        //shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //attachShareIntentAction();
        /*item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();
                shareIntent();
                return true;
            }
        });*/
        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareIntent();
                break;
        }
        return true;
    }

    public void shareIntent() {
        intent = new Intent(Intent.ACTION_SEND);
        //intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, urlShare);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);

        }
    }
}
