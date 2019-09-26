package com.reiya.pixiv.article;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.reiya.pixiv.profile.ProfileActivity;
import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.work.ViewActivity;

import java.util.HashMap;
import java.util.Map;

public class ArticleActivity extends AppCompatActivity {

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
        if (webView != null) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains("member_illust.php")) {
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
            webView.loadUrl(getIntent().getStringExtra("url"), header);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
