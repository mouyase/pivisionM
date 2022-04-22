package com.reiya.pixiv.other;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import PixivLocalReverseProxy.PixivLocalReverseProxy;
import tech.yojigen.pivisionm.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.ProxyConfig;
import androidx.webkit.ProxyController;
import androidx.webkit.WebViewClientCompat;
import androidx.webkit.WebViewFeature;

import java.util.HashMap;
import java.util.Map;

public class BrowserActivity extends AppCompatActivity {
    private WebView mWebView;
    private ProgressBar mProgressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        PixivLocalReverseProxy.startServer("12345");
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
            @SuppressLint("WebViewClientOnReceivedSslError")
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

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
        System.out.println(isNeedProxy);
        if (isNeedProxy) {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.PROXY_OVERRIDE)) {
                ProxyConfig proxyConfig = new ProxyConfig.Builder().addProxyRule("127.0.0.1:12345").addDirect().build();
                ProxyController.getInstance().setProxyOverride(proxyConfig, Runnable::run, () -> {
                    Log.w("PixivWebView", "WebView proxy init");
                    Map<String, String> header = new HashMap<>();
                    header.put("Accept-Language", "zh_CN");
                    header.put("App-Accept-Language", "zh-hans");
                    mWebView.loadUrl(loginUrl, header);
                });
            }
        }else{
            if (WebViewFeature.isFeatureSupported(WebViewFeature.PROXY_OVERRIDE)) {
                ProxyConfig proxyConfig = new ProxyConfig.Builder().addDirect().build();
                ProxyController.getInstance().setProxyOverride(proxyConfig, Runnable::run, () -> {
                    Map<String, String> header = new HashMap<>();
                    header.put("Accept-Language", "zh_CN");
                    header.put("App-Accept-Language", "zh-hans");
                    mWebView.loadUrl(loginUrl, header);
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        PixivLocalReverseProxy.stopServer();
        super.onDestroy();
    }
}