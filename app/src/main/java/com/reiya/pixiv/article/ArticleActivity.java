package com.reiya.pixiv.article;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.profile.ProfileActivity;
import com.reiya.pixiv.work.ViewActivity;

import java.util.HashMap;
import java.util.Map;

import tech.yojigen.pivisionm.R;

public class ArticleActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme());
        setContentView(R.layout.activity_article);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);
        WebView webView = findViewById(R.id.webView);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("member_illust.php") || url.contains("artworks")) {
                    Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                if (url.contains("member.php")) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        Map<String, String> header = new HashMap<>();
        header.put("Accept-Language", "zh_CN");
        header.put("Referer", "https://www.pixiv.net");
        webView.loadUrl(getIntent().getStringExtra("url"), header);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}