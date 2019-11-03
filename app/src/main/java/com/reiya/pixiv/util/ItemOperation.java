package com.reiya.pixiv.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.reiya.pixiv.bean.Task;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.dialog.DetailDialog;
import com.reiya.pixiv.dialog.FavoriteWorkDialog;
import com.reiya.pixiv.dialog.MultiSelectDialog;
import com.reiya.pixiv.dialog.TagsDialog;
import com.reiya.pixiv.network.HttpClient;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/2/27.
 */
public class ItemOperation {
    public static void showDetailDialog(FragmentActivity activity, Work work) {
        DetailDialog detailDialog = new DetailDialog();
        detailDialog.setItem(work);
        detailDialog.show(activity.getSupportFragmentManager(), "Detail");
    }

    public static void showTagsDialog(FragmentActivity activity, Work work) {
        TagsDialog tagsDialog = new TagsDialog();
        tagsDialog.setItems(work.getTags());
        tagsDialog.show(activity.getSupportFragmentManager(), "Tags");
    }

    public static void addBookmark(final FragmentActivity activity, final Work work) {
        if (!UserData.isLoggedIn()) {
            Toast.makeText(activity, R.string.please_login_first, Toast.LENGTH_SHORT).show();
            return;
        }
        String defaultType = PreferenceManager.getDefaultSharedPreferences(activity).getString(activity.getString(R.string.key_favorite_work_option), "query");
        if (defaultType.equals("query")) {
            FavoriteWorkDialog favoriteWorkDialog = new FavoriteWorkDialog();
            favoriteWorkDialog.setToAdd(type -> addBookmark(activity, work.getId(), type));
            favoriteWorkDialog.show(activity.getSupportFragmentManager(), "FavoriteWork");
        } else {
            addBookmark(activity, work.getId(), defaultType);
        }
    }

    public static void removeBookmark(final Context context, int id) {
        if (!UserData.isLoggedIn()) {
            Toast.makeText(context, R.string.please_login_first, Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkRequest.removeBookmark(id)
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, R.string.fail_to_remove_collection, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse IllustListResponse) {
                        Toast.makeText(context, R.string.removed_collection, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static void addBookmark(final Activity activity, int id, String type, String... tags) {
        NetworkRequest.addBookmark(id, type, tags)
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(activity, R.string.fail_to_collect, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse IllustListResponse) {
                        Toast.makeText(activity, R.string.collect_successfully, Toast.LENGTH_SHORT).show();
                    }
                });
//        RequestBody body = new FormEncodingBuilder()
//                .add("publicity", type)
//                .add("work_id", "" + id)
//                .build();
//        OldHttpClient.requestWithBearer(Value.URL_FAVORITE_WORKS_ADD, "POST", body, new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.e("ERR", ":" + e.getMessage());
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(activity, R.string.fail_to_collect, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(activity, R.string.collect_successfully, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
    }

    public static void browser(Context context, Work work) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Value.URL_ILLUST_PAGE + work.getId()));
        context.startActivity(intent);
    }


//    public static void share(final Context context, Work work) {
//        GlideUrl glideUrl = new GlideUrl(work.getImageUrl(2), new LazyHeaders.Builder()
//                .addHeader("Referer", work.getImageUrl(2))
//                .build());
//        Glide.with(context)
//                .load(glideUrl)
//                .downloadOnly(new SimpleTarget<File>() {
//                    @Override
//                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
//                        File file = IO.getTemFile(resource);
//                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                        shareIntent.setType("image/*");
//                        Uri uri = Uri.fromFile(file);
//                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                        context.startActivity(shareIntent);
//                    }
//                });
//    }

    public static void save(final FragmentActivity activity, final Work work) {
        save(activity, work, -1);
    }

    public static void save(final FragmentActivity activity, final Work work, int index) {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWriteContactsPermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                int REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE = 111;
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE);
                return;
            }
        }
        if (work.getPageCount() == 1) {
            Toast.makeText(activity, R.string.saving_please_wait, Toast.LENGTH_SHORT).show();
            save(activity, work, true, 0,
                    file -> activity.runOnUiThread(() -> Toast.makeText(activity, activity.getString(R.string.save_to) + file.getPath(), Toast.LENGTH_LONG).show()),
                    (file, exist) -> {
                        if (exist) {
                            Toast.makeText(activity, R.string.file_already_exists, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            MultiSelectDialog multiSelectDialog = new MultiSelectDialog();
            multiSelectDialog.setPage(work.getPageCount());
            multiSelectDialog.setSelectedIndex(index);
            multiSelectDialog.setOnSelected(selected -> {
                Toast.makeText(activity, R.string.saving_please_wait, Toast.LENGTH_SHORT).show();
                List<Task> tasks = new ArrayList<>();
                for (int i = 1, l = selected.length; i < l; i++) {
                    if (selected[i]) {
                        tasks.add(new Task(work, i - 1));
                    }
                }
                if (tasks.size() > 0) {
                    new DownloadRunnable(activity, tasks).run();
                }
            });
            multiSelectDialog.show(activity.getSupportFragmentManager(), "Multi");
        }
    }

    public static void save(final Context context, final Work work, boolean single, int index,
                            final OnSavingDone onSavingDone, OnCheckExist onCheckExist) {
        save(context, work, single, index, null, onSavingDone, onCheckExist);
    }

    public static void save(final Context context, final Work work, boolean single, int index, String independentFolderName,
                            final OnSavingDone onSavingDone, OnCheckExist onCheckExist) {
        final File file = getFile(context, work, index, independentFolderName, onCheckExist);
        if (file == null) return;

//        if (work.isDynamic()) {
//            OldHttpClient.requestWithBearer(String.format(Value.URL_ILLUST_DETAIL, work.getId()), new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    String content = response.body().string();
//                    final IllustItem item = JsonReader.getItem(content);
//                    downloadGif(item.getId(), JsonReader.getUrl(item.getMetadata()),
//                            new OnGifDownloaded() {
//                                @Override
//                                public void onDo(int id) {
//                                    saveGif(context, work, file, onSavingDone);
//                                }
//                            }, new OldHttpClient.ProgressListener() {
//
//                                @Override
//                                public void update(long bytesRead, long contentLength, boolean done) {
//
//                                }
//                            });
//                }
//            });

//        } else {
        if (single) {
            downloadPic(context, work.getImageUrl(3), file, onSavingDone);
        } else {
            downloadPic(context, work.getImageUrl(3, index).replace("p0", "p" + index), file, onSavingDone);
        }
//        }
    }


    private static File getFile(Context context, Work work, int index, OnCheckExist onCheckExist) {
        return getFile(context, work, index, null, onCheckExist);
    }


    private static File getFile(Context context, Work work, int index, String independentFolderName, OnCheckExist onCheckExist) {
        final File file = getNewFile(context, work, index, independentFolderName);
        if (file.exists()) {
            onCheckExist.onDo(file, true);
            return null;
        } else {
            onCheckExist.onDo(file, false);
        }
        return file;
    }

    @NonNull
    public static File getNewFile(Context context, Work work, int index) {
        return getNewFile(context, work, index, null);
    }

    @NonNull
    public static File getNewFile(Context context, Work work, int index, String independentFolderName) {
        int mode = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.key_file_name_mode), "1"));
        String name = "";
        while (mode > 0) {
            int n = mode % 10;
            switch (n) {
                case 1:
                    name = work.getId() + "_" + name;
                    break;
                case 2:
                    name = work.getTitle() + "_" + name;
                    break;
                case 3:
                    name = work.getUser().getId() + "_" + name;
                    break;
                case 4:
                    name = work.getUser().getName() + "_" + name;
                    break;
            }
            mode /= 10;
        }
        String suffix = ".jpg";
        if (work.isDynamic()) {
            suffix = ".webm";
        }
        if (work.getPageCount() > 1) {
            name += index + suffix;
        } else {
            name += suffix;
            name = name.replace("_" + suffix, suffix);
        }
        name = name.replace("/", "|");

        String oldPath = Environment.getExternalStorageDirectory() + PreferenceManager.getDefaultSharedPreferences(context).getString("path", "/Pictures/PivisionM/");
        File path = new File(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.key_path), oldPath) + "/");
        String subfolderName = "_";
        if (independentFolderName == null || independentFolderName.equals("")) {
            int subfolderMode = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.key_auto_create_subfolder), "0"));
            if (subfolderMode != 0) {
                while (subfolderMode > 0) {
                    int n = subfolderMode % 10;
                    switch (n) {
                        case 1:
                            subfolderName = work.getUser().getId() + "_" + subfolderName;
                            break;
                        case 2:
                            subfolderName = work.getUser().getName() + "_" + subfolderName;
                            break;
                    }
                    subfolderMode /= 10;
                }
                subfolderName = subfolderName.replace("/", "|").replace("__", "/");
                path = new File(path, subfolderName);
            }
        } else {
            subfolderName = independentFolderName;
            path = new File(path, subfolderName);
        }
        if (!path.exists()) {
            path.mkdirs();
        }
        return new File(path, name);
    }

    public static void saveGif(final Context context, final Work work, String url, final OnSavingDone onSavingDone, OnCheckExist onCheckExist) {
        File file = getFile(context, work, 0, onCheckExist);
        downloadPic(context, url, file, onSavingDone);
    }

    public static void downloadPic(final Context context, final String url, final File file, final OnSavingDone onSavingDone) {
        Log.e("url", url);
        final Timer timer = new Timer();
        final Observable<ResponseBody> observable = NetworkRequest.download(url);
        observable.subscribe(new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                HttpClient.writeFileFromRequestBody(responseBody, file);
                timer.cancel();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
                onSavingDone.onDo(file);
            }
        });

//        final Call call = OldHttpClient.requestReturnCall(url, new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.e("download", "failed");
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                BufferedSink sink = Okio.buffer(Okio.sink(file));
//                sink.writeAll(response.body().source());
//                sink.close();
//
//                timer.cancel();
//                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri uri = Uri.fromFile(file);
//                intent.setData(uri);
//                context.sendBroadcast(intent);
//                onSavingDone.onDo(file);
//            }
//        });

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                observable.unsubscribeOn(Schedulers.io());
//                call.cancel();
                Log.e("cancel", url);
                downloadPic(context, url, file, onSavingDone);
            }
        }, 100000);
    }

    public interface OnCheckExist {
        void onDo(File file, boolean exist);
    }

//    public static void downloadGif(final int id, String url, final OnGifDownloaded onGifDownloaded, OldHttpClient.ProgressListener listener) {
//        OldHttpClient.request(url, new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Response response) throws IOException {
//                        File path = new File(IO.getGifCachePath() + id);
//                        path.mkdirs();
//                        BufferedSink sink = Okio.buffer(Okio.sink(new File(path, id + ".zip")));
//                        sink.writeAll(response.body().source());
//                        sink.close();
//
//                        Zip.unzip(id);
//                        onGifDownloaded.onDo(id);
//                    }
//                }, listener);
//    }
//
//    public static void saveGif(final Context context, final Work work, final File file, final OnSavingDone onSavingDone) {
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                Gif.getGif(file, work.getId(), null);
//                Intent intent = new Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri uri = Uri.fromFile(file);
//                intent.setData(uri);
//                context.sendBroadcast(intent);
//                onSavingDone.onDo(file);
//            }
//        });
//    }

    public interface OnSavingDone {
        void onDo(File file);
    }

    public interface OnGifDownloaded {
        void onDo(int id);
    }

    static class DownloadRunnable implements Runnable {
        private final static int PARALLEL_NUM = 4;
        Activity mActivity;
        List<Task> mTasks;

        public DownloadRunnable(Activity activity, List<Task> tasks) {
            mActivity = activity;
            mTasks = tasks;
        }

        @Override
        public void run() {
            int n = Math.min(mTasks.size(), PARALLEL_NUM);
            for (int i = 0; i < n && mTasks.size() > 0; i++) {
                saveMulti(mActivity);
            }
        }

        private void saveMulti(final Activity activity) {
            Task task = mTasks.remove(0);
            save(activity, task.work, false, task.index,
                    new OnSavingDone() {
                        @Override
                        public void onDo(final File file) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, activity.getString(R.string.save_to) + file.getPath(), Toast.LENGTH_LONG).show();
                                }
                            });
                            if (!mTasks.isEmpty()) {
                                saveMulti(mActivity);
                            }
                        }
                    },
                    new OnCheckExist() {
                        @Override
                        public void onDo(File file, boolean exist) {
                            if (exist) {
                                Toast.makeText(activity, R.string.file_already_exists, Toast.LENGTH_SHORT).show();
                                if (!mTasks.isEmpty()) {
                                    saveMulti(mActivity);
                                }
                            }
                        }
                    });
        }
    }
}
