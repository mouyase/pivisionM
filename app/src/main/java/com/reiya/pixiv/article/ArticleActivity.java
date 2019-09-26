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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);

        WebView webView = (WebView) findViewById(R.id.webView);
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


//        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
//        TextView tvDate = (TextView) findViewById(R.id.tvDate);
//        TextView tvCaption = (TextView) findViewById(R.id.tvCaption);
//
//        tvTitle.setText(getIntent().getStringExtra("title"));
//        tvDate.setText(getIntent().getStringExtra("date"));
//        tvCaption.setText(Html.fromHtml(getIntent().getStringExtra("caption")));
//
//        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        ArticleDetailAdapter adapter = new ArticleDetailAdapter(this, JsonReader.getWorksInArticle(getIntent().getStringExtra("works")));
//        adapter.setOnItemClickListener(new ArticleDetailAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(IllustItem item, int type) {
//                if (type == 0) {
//                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                    intent.putExtra("id", item.getUserId());
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
//                    intent.putExtra("id", item.getId());
//                    startActivity(intent);
//                }
//            }
//        });
//        adapter.setHeader(findViewById(R.id.header));
//        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
