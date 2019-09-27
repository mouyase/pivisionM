package com.reiya.pixiv.gif;

import android.media.MediaPlayer;
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

        mVideoView = (VideoView) findViewById(R.id.video_view);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFile == null) {
                    Toast.makeText(getApplicationContext(), R.string.please_wait, Toast.LENGTH_SHORT).show();
                } else {
                    File newFile = ItemOperation.getNewFile(GifActivity.this, mWork, 0);
                    IO.save(mFile, newFile);
                    Toast.makeText(GifActivity.this, getString(R.string.save_to) + newFile.getPath(), Toast.LENGTH_LONG).show();
                }
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
//                            ImageLoader
//                                    .download(GifActivity.this, mUrl, new SimpleTarget<File>() {
//                                        @Override
//                                        public void onResourceReady(File resource, Transition<? super File> transition) {
//                                            mFile = resource;
//                                            ImageLoader.loadImage(GifActivity.this, mFile)
//                                                    .load(iv);
//                                        }
//                                    });
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
                                            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                @Override
                                                public void onPrepared(MediaPlayer mp) {
                                                    mp.setLooping(true);
                                                }
                                            });
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

//    private void showMenu() {
//        MenuDialog menuDialog = new MenuDialog();
//        final Work work = getIntent().getParcelableExtra("work");
//        menuDialog.setListener(new String[]{getString(R.string.detail), getString(R.string.tag), getString(R.string.save_gif), getString(R.string.open_in_browser), getString(R.string.collection)}, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case 0:
//                        ItemOperation.showDetailDialog(GifActivity.this, work);
//                        break;
//                    case 1:
//                        ItemOperation.showTagsDialog(GifActivity.this, work);
//                        break;
//                    case 2:
//                        if (mUrl == null) {
//                            Toast.makeText(getApplicationContext(), R.string.please_wait, Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                        ItemOperation.saveGif(GifActivity.this, mWork, mUrl,
//                                new ItemOperation.OnSavingDone() {
//                                    @Override
//                                    public void onDo(final File file) {
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Toast.makeText(GifActivity.this, getString(R.string.save_to) + file.getPath(), Toast.LENGTH_LONG).show();
//
//                                            }
//                                        });
//                                    }
//                                },
//                                new ItemOperation.OnCheckExist() {
//                                    @Override
//                                    public void onDo(File file, boolean exist) {
//
//                                    }
//                                });
//                        break;
//                    case 3:
//                        ItemOperation.browser(GifActivity.this, work);
//                        break;
//                    case 4:
//                        ItemOperation.addBookmark(GifActivity.this, work);
//                }
//            }
//        });
//        menuDialog.show(getSupportFragmentManager(), "Menu");
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFile != null) {
            mFile.delete();
        }
    }
}
