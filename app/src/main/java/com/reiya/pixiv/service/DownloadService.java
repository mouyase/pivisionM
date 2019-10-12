package com.reiya.pixiv.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.reiya.pixiv.bean.Task;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.download.DownloadActivity;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;
import com.reiya.pixiv.util.IO;
import com.reiya.pixiv.util.ItemOperation;
import com.reiya.pixiv.util.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/2/23.
 */
public class DownloadService extends IntentService {
    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String TOTAL = "total";
    public static final String IS_PUBLIC = "is_public";
    public static final int TYPE_AUTHOR = 0;
    public static final int TYPE_BOOKMARK = 1;
    private static final int PARALLEL_NUM = 4;
    private static int sId = 0;
    private static boolean sIsExecuting = false;
    private static List<Task> sTasksDoing = new ArrayList<>();
    private final List<Task> mTasksToDo = new ArrayList<>();
    private int mNumWorks = 0;
    private int mNumPictures = 0;
    private long mSize = 0;
    private int mTotal = 0;
    private String mNextUrl;
    private String mPublicType;
    private Map<Integer, Integer> mMultiPageWorks;
    private ObservableWrapper mObservableWrapper;

    public DownloadService() {
        super("Download");
    }

    public static List<Task> getTasksDoing() {
        return sTasksDoing;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("download", "start");

        if (sIsExecuting) {
            Intent intent1 = new Intent("android.intent.action.UPDATE_PROGRESS");
            intent1.putExtra("info", "已有任务进行中");
            sendBroadcast(intent1);
        } else {
            sIsExecuting = true;
            int type = intent.getIntExtra(TYPE, 0);
            switch (type) {
                case TYPE_AUTHOR:
                    sId = intent.getIntExtra(ID, 0);
                    mObservableWrapper = new ObservableWrapper() {
                        @Override
                        public Observable getList() {
                            return NetworkRequest.getWorksOfAUser(sId);
                        }

                        @Override
                        public Observable getNextList(String nextUrl) {
                            return NetworkRequest.getWorksOfAUserNext(mNextUrl);
                        }
                    };
                    break;
                case TYPE_BOOKMARK:
                    boolean isPublic = intent.getBooleanExtra(IS_PUBLIC, true);
                    mPublicType = isPublic ? Value.PUBLIC : Value.PRIVATE;
                    mObservableWrapper = new ObservableWrapper() {
                        @Override
                        public Observable getList() {
                            return NetworkRequest.getBookmark(mPublicType, null);
                        }

                        @Override
                        public Observable getNextList(String nextUrl) {
                            return NetworkRequest.getBookmarkNext(nextUrl);
                        }
                    };
            }
            sTasksDoing = new ArrayList<>();
            sendNotification("初始化中");
            mMultiPageWorks = new HashMap<>();
            if (sId == 0) {


                mTotal = intent.getIntExtra(TOTAL, 0);

            }
            getList();
        }
    }

    private void getList() {
        mObservableWrapper.getList()
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        err();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse illustListResponse) {
                        mNextUrl = illustListResponse.getNextUrl();
                        List<Work> works = illustListResponse.getWorks();
                        addWorksToTasks(works);
                        getNextList();
                    }
                });

//        OldHttpClient.requestWithBearer(String.format(Value.URL_WORKS_BIG, userId, mPage), new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.e("err, list", e.getLocalizedMessage());
//                err();
//                isLoadingList = false;
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                String s = response.body().string();
//                if (mTotal == 0) {
//                    mTotal = JsonReader.getTotal(s);
//                    sendNotification(mNumWorks, mTotal);
//                }
//                final List<IllustItem> items = JsonReader.getWorksWithDetail(s);
//                Log.e("items", "" + items.mSize());
//                if (items.isEmpty() || mPage > 10) {
//                    sId = 0;
//                    isNoMoreList = true;
//                    return;
//                }
//                for (IllustItem item : items) {
////                    if (item.getPage() == 1) {
////                        mTasksToDo.add(new Task(item, 0));
////                        mNumTasks++;
////                    } else {
////                        for (int i = 0, l = item.getPage(); i < l; i++) {
////                            mTasksToDo.add(new Task(item, i));
////                            mNumTasks++;
////                        }
////                    }
//                }
//                int n = mTasksToDo.mSize() < PARALLEL_NUM ? mTasksToDo.mSize() : PARALLEL_NUM;
//                for (int i = 0; i < n; i++) {
//                    download();
//                }
//                isLoadingList = false;
//            }
//        });
    }

//    private void subscribe(Observable<HttpService.IllustListResponse> observable) {
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("err, list", e.getLocalizedMessage());
//                        err();
//                        isLoadingList = false;
//                    }
//
//                    @Override
//                    public void onNext(HttpService.IllustListResponse illustListResponse) {
//                        isLoadingList = false;
//                        List<Work> works = illustListResponse.getWorks();
//                        mNextUrl = illustListResponse.getNextUrl();
//                        Log.e("next", ":" + mNextUrl);
//                        if (mNextUrl == null) {
//                            sId = 0;
//                            isNoMoreList = true;
//                            checkDone();
//                        }
//                        addWorksToTasks(works);
//                        int n = mTasksToDo.size() < PARALLEL_NUM ? mTasksToDo.size() : PARALLEL_NUM;
//                        for (int i = 0; i < n; i++) {
//                            download();
//                        }
//                    }
//                });
//    }

    private void getNextList() {
        if (mNextUrl == null) {
            sendNotification(0, mTasksToDo.size());
            int n = mTasksToDo.size() < PARALLEL_NUM ? mTasksToDo.size() : PARALLEL_NUM;
            for (int i = 0; i < n; i++) {
                download();
            }
        } else {
            mObservableWrapper.getNextList(mNextUrl)
                    .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            err();
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(HttpService.IllustListResponse illustListResponse) {
                            mNextUrl = illustListResponse.getNextUrl();
                            List<Work> works = illustListResponse.getWorks();
                            addWorksToTasks(works);
                            getNextList();
                        }
                    });
        }
    }

    private void addWorksToTasks(List<Work> works) {
        for (Work work : works) {
            if (work.getPageCount() == 1) {
                mTasksToDo.add(new Task(work, 0));
            } else {
                for (int i = 0, l = work.getPageCount(); i < l; i++) {
                    mTasksToDo.add(new Task(work, i));
                }
                mMultiPageWorks.put(work.getId(), 0);
            }
        }
    }

    private void download() {
        Log.e("download", "" + mTasksToDo.size());
        if (mTasksToDo.isEmpty() && sTasksDoing.isEmpty()) {
            Log.e("done", ".");
            String info = "批量保存完成。\n\n" + mNumWorks + "件，\n" + mNumPictures + "P，\n" + IO.getFormattedSize(mSize);
            Intent intent1 = new Intent("android.intent.action.UPDATE_PROGRESS");
            intent1.putExtra("info", info);
            sendBroadcast(intent1);
            sendNotification(info);
            sIsExecuting = false;
        } else {
            Task task = mTasksToDo.remove(0);
            download(task);
        }
    }

    private void download(final Task task) {
        final Work work = task.work;
        final int index = task.index;
        ItemOperation.save(this, work, work.getPageCount() == 1, index, mPublicType,
                file -> {
                    mSize += file.length();
                    mNumPictures++;
                    sTasksDoing.remove(task);

                    Intent intent = new Intent("android.intent.action.UPDATE_PROGRESS");
                    intent.putExtra("info", getString(R.string.save_to) + file.getPath());
                    sendBroadcast(intent);

                    Intent intent2 = new Intent("android.intent.action.UPDATE_TASK");
                    sendBroadcast(intent2);

                    if (work.getPageCount() == 1) {
                        mNumWorks++;
                        sendNotification(mNumWorks, mTotal);
                    } else {
                        mMultiPageWorks.put(work.getId(), mMultiPageWorks.get(work.getId()) + 1);
                        if (mMultiPageWorks.get(work.getId()) == work.getPageCount()) {
                            mNumWorks++;
                            sendNotification(mNumWorks, mTotal);
                        }
                    }

                    Log.e("download", file.getPath());

                    download();
                },
                (file, exist) -> {
                    Log.e("exist", "" + exist);
                    if (exist) {
                        mSize += file.length();
                        mNumPictures++;

                        if (work.getPageCount() == 1) {
                            mNumWorks++;
                            sendNotification(mNumWorks, mTotal);
                        } else {
                            mMultiPageWorks.put(work.getId(), mMultiPageWorks.get(work.getId()) + 1);
                            if (mMultiPageWorks.get(work.getId()) == work.getPageCount()) {
                                mNumWorks++;
                                sendNotification(mNumWorks, mTotal);
                            }
                        }

                        download();
                    } else {
                        sTasksDoing.add(task);
                        Intent intent2 = new Intent("android.intent.action.UPDATE_TASK");
                        sendBroadcast(intent2);
                    }
                });
//        final IllustItem item = task.item;
//        final int index = task.index;
//        ItemOperation.download(this, item, index,
//                new ItemOperation.OnSavingDone() {
//                    @Override
//                    public void onDo(File file) {
//                        mSize += file.length();
//                        mNumPictures ++;
//                        sTasksDoing.remove(task);
//
//                        Intent intent = new Intent("android.intent.action.UPDATE_PROGRESS");
//                        intent.putExtra("info", getString(R.string.save_to) + file.getPath());
//                        sendBroadcast(intent);
//
//                        Intent intent2 = new Intent("android.intent.action.UPDATE_TASK");
//                        sendBroadcast(intent2);
//
//                        if (item.getPage() == index + 1) {
//                            mNumWorks ++;
//                            sendNotification(mNumWorks, mTotal);
//                        }
//
//                        if (mNumTasks == mNumPictures && isNoMoreList) {
//                            String info = "批量保存完成。\n\n" + mNumWorks + "件，\n" + mNumPictures + "P，\n" + IO.getFormattedSize(mSize);
//                            Intent intent1 = new Intent("android.intent.action.UPDATE_PROGRESS");
//                            intent1.putExtra("info", info);
//                            sendBroadcast(intent1);
//                            sendNotification(info);
//                        }
//
//                        Log.e("download", file.getPath());
//
//                        download();
//                    }
//                },
//                new ItemOperation.OnCheckExist() {
//                    @Override
//                    public void onDo(File file, boolean exist) {
//                        if (exist) {
//                            mSize += file.length();
//                            mNumPictures ++;
//
//                            if (item.getPage() == index + 1) {
//                                mNumWorks ++;
//                                sendNotification(mNumWorks, mTotal);
//                            }
//
//                            download();
//                        } else {
//                            sTasksDoing.add(task);
//                            Intent intent2 = new Intent("android.intent.action.UPDATE_TASK");
//                            sendBroadcast(intent2);
//                        }
//                    }
//                });
    }

    private void sendNotification(int progress, int total) {
        sendNotification(progress + "/" + total);
    }

    private void sendNotification(String info) {
        Intent intent = new Intent(this, DownloadActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(resultPendingIntent)
                .setContentTitle("批量保存")
                .setContentText(info)
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void err() {
        Intent intent = new Intent("android.intent.action.UPDATE_PROGRESS");
        intent.putExtra("info", "网络错误，保存终止");
        sendBroadcast(intent);
        sendNotification("网络错误，保存终止");
        sId = 0;
        sIsExecuting = false;
    }

    private interface ObservableWrapper {
        Observable getList();

        Observable getNextList(String nextUrl);
    }
}
