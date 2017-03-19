package com.example.android.newsapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
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

import org.parceler.Parcels;

import static com.example.android.newsapp.R.layout.article;

public class ArticleActivity extends AppCompatActivity {
    private ShareActionProvider shareActionProvider;
    Intent intent;
    String urlShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));
        WebView webView = (WebView) findViewById(R.id.wvArticle);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        if(Utilities.isNetworkAvailable(this) && Utilities.isOnline()) {
            webView.loadUrl(article.getWebUrl());
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
    public void shareIntent(){
        intent = new Intent(Intent.ACTION_SEND);
        //intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, urlShare);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);

        }
    }
}
