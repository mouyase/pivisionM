package com.reiya.pixiv.comment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.reiya.pixiv.adapter.CommentAdapter;
import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;
import com.reiya.pixiv.view.LoaderRecyclerView;

import rx.Subscriber;
import tech.yojigen.pivisionm.R;

public class CommentActivity extends AppCompatActivity {
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme());
        setContentView(R.layout.activity_comment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);

        final LoaderRecyclerView recyclerView = (LoaderRecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final int id = getIntent().getIntExtra("id", 0);
        NetworkRequest.getComment(id)
                .subscribe(new Subscriber<HttpService.CommentResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpService.CommentResponse commentResponse) {
                        adapter = new CommentAdapter(getApplicationContext(), commentResponse.getComments());
                        adapter.setNextUrl(commentResponse.getNextUrl());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setOnLoadMoreListener(new LoaderRecyclerView.OnLoadMoreListener() {
                            @Override
                            public void load() {
                                if (adapter.getNextUrl() != null) {
                                    NetworkRequest.getComment(adapter.getNextUrl())
                                            .subscribe(new Subscriber<HttpService.CommentResponse>() {
                                                @Override
                                                public void onCompleted() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Log.e("err", e.getLocalizedMessage());
                                                }

                                                @Override
                                                public void onNext(HttpService.CommentResponse commentResponse) {
                                                    adapter.addItems(commentResponse.getComments());
                                                    adapter.setNextUrl(commentResponse.getNextUrl());
                                                    recyclerView.finishLoading();
                                                }
                                            });
                                }
                            }
                        });
                    }
                });

//        HttpClient.requestWithBearer(String.format(Value.URL_COMMENT, id, 1), new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.e("comments", "err");
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                adapter = new CommentAdapter(getApplicationContext(), JsonReader.getComments(response.body().string()));
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        recyclerView.setAdapter(adapter);
//                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            int lastVisibleItem;
//
//                            @Override
//                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                                super.onScrollStateChanged(recyclerView, newState);
//                                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                                        && lastVisibleItem + 1 == adapter.getItemCount() && !adapter.isLoading()) {
//                                    adapter.setLoading(true);
//                                    HttpClient.requestWithBearer(String.format(Value.URL_COMMENT, id, adapter.getPage() + 1), new Callback() {
//                                        @Override
//                                        public void onFailure(Request request, IOException e) {
//
//                                        }
//
//                                        @Override
//                                        public void onResponse(Response response) throws IOException {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                                if (isDestroyed()) {
//                                                    return;
//                                                }
//                                            }
//                                            ResponseBody responseBody = response.body();
//                                            final String s = responseBody.string();
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    adapter.addItems(JsonReader.getComments(s));
//                                                }
//                                            });
//                                            adapter.setLoading(false);
//                                        }
//                                    });
//                                }
//                            }
//
//                            @Override
//                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                                super.onScrolled(recyclerView, dx, dy);
//                                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//                            }
//                        });
//                    }
//                });
//            }
//        });
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
