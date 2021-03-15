package com.reiya.pixiv.other;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.ProxyConfig;
import androidx.webkit.ProxyController;
import androidx.webkit.WebViewClientCompat;
import androidx.webkit.WebViewFeature;

import tech.yojigen.pivisionm.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BrowserActivity extends AppCompatActivity {
    private WebView mWebView;
    private ProgressBar mProgressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mWebView = findViewById(R.id.webview);
        mProgressBar = findViewById(R.id.bar);

        findViewById(R.id.close).setOnClickListener(v -> {
            Intent intent = new Intent(BrowserActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        String loginUrl = getIntent().getStringExtra("loginUrl");
        boolean isNeedProxy = getIntent().getBooleanExtra("isNeedProxy", false);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        mWebView.setWebViewClient(new WebViewClientCompat() {
            @Override
            public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
                if (request.getUrl().getScheme().equals("pixiv")) {
                    Intent intent = new Intent(BrowserActivity.this, LoginActivity.class);
                    intent.putExtra("codeUrl", request.getUrl().toString());
                    startActivity(intent);
                    finish();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        if (isNeedProxy) {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.PROXY_OVERRIDE)) {
                ProxyConfig proxyConfig = new ProxyConfig.Builder()
                        .addProxyRule("socks://113.31.126.172:7891")
                        .addDirect().build();
                ProxyController.getInstance().setProxyOverride(proxyConfig, command -> {
                }, () -> {
                });
            }
        }
        mWebView.loadUrl(loginUrl);
    }
}