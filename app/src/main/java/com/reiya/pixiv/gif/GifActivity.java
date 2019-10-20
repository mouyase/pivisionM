package com.reiya.pixiv.gif;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.network.HttpClient;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;
import com.reiya.pixiv.util.IO;
import com.reiya.pixiv.util.ItemOperation;

import java.io.File;

import okhttp3.ResponseBody;
import rx.Subscriber;
import tech.yojigen.pivisionm.R;

public class GifActivity extends AppCompatActivity {
    private VideoView mVideoView;
    private Work mWork;
    private String mUrl;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        mVideoView = findViewById(R.id.video_view);

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            if (mFile == null) {
                Toast.makeText(getApplicationContext(), R.string.please_wait, Toast.LENGTH_SHORT).show();
            } else {
                File newFile = ItemOperation.getNewFile(GifActivity.this, mWork, 0);
                IO.save(mFile, newFile);
                Toast.makeText(GifActivity.this, getString(R.string.save_to) + newFile.getPath(), Toast.LENGTH_LONG).show();
            }
        });

        mWork = getIntent().getParcelableExtra("work");
        int id = mWork.getId();
        NetworkRequest.getUgoira(id)
                .subscribe(new Subscriber<HttpService.UgoiraResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        err();
                    }

                    @Override
                    public void onNext(HttpService.UgoiraResponse ugoiraResponse) {
                        mUrl = ugoiraResponse.getUrl();
                        if (mUrl == null || mUrl.equals("")) {
                            err();
                        } else {
                            mFile = new File(getCacheDir(), mWork.getId() + ".webm");
                            NetworkRequest.download(mUrl)
                                    .subscribe(new Subscriber<ResponseBody>() {
                                        @Override
                                        public void onCompleted() {
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                        }

                                        @Override
                                        public void onNext(ResponseBody responseBody) {
                                            HttpClient.writeFileFromRequestBody(responseBody, mFile);
                                            Log.i("path", mFile.getPath());
                                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                                            mVideoView.setVideoPath(mFile.getPath());
                                            mVideoView.setOnPreparedListener(mp -> mp.setLooping(true));
                                            mVideoView.start();
                                        }
                                    });
                        }
                    }
                });
    }

    private void err() {
        Toast.makeText(this, R.string.fail_to_load, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFile != null) {
            mFile.delete();
        }
    }
}
