package com.reiya.pixiv.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;

import com.reiya.pixiv.base.BaseApplication;
import com.reiya.pixiv.network.fuckgfw.PixivDNS;
import com.reiya.pixiv.network.fuckgfw.PixivSSLSocketFactory;
import com.reiya.pixiv.network.fuckgfw.PixivTrustManager;
import com.reiya.pixiv.util.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.yojigen.common.MD5;
import tech.yojigen.pivisionm.BuildConfig;
import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2015/11/23 0023.
 */
public class HttpClient {
    private static OkHttpClient client;
    private static HttpService service;

    public static void init(Context context) {
        File cacheFile = new File(context.getCacheDir(), "/HttpCache/");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10); //10MB
        @SuppressLint("SimpleDateFormat")
        String pixivTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US).format(new Date());
        String pixivHash = MD5.convert(pixivTime + "28c1fdd170a5204386cb1313c7077b34f83e4aaf4aa829ce78c231e05b0bae2c");
        Interceptor interceptor = chain -> {
            Request request = chain.request().newBuilder()
                    .header("User-Agent", BaseApplication.getUA())
                    .header("Accept-Language", "zh_CN")
                    .header("App-OS", "Android")
                    .header("App-OS-Version", "" + Build.VERSION.RELEASE)
                    .header("App-Version", "5.0.156")
                    .header("X-Client-Time", pixivTime)
                    .header("X-Client-Hash", pixivHash)
                    .header("Referer", "https://www.pixiv.net")
                    .build();
            return chain.proceed(request);
        };
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(interceptor)
                .addInterceptor(logging);
        int connectMode = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.key_connect_mode), "0"));
        switch (connectMode) {
            case 0:
                builder.sslSocketFactory(PixivSSLSocketFactory.getInstance(), PixivTrustManager.getInstance()).dns(PixivDNS.getInstance());
                break;
            case 1:
                break;
            case 2:
                Value.URL_AUTH = Value.PROXY_URL_AUTH_YOJIGEN;
                Value.URL_PIXIV = Value.PROXY_URL_PIXIV_YOJIGEN;
                break;
        }
        client = builder.build();
        service = getRetrofit(Value.URL_PIXIV).create(HttpService.class);
    }

    private static Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static HttpService getService() {
        return service;
    }

    public static HttpService getService(String baseUrl) {
        return getRetrofit(baseUrl).create(HttpService.class);
    }

    public static void writeFileFromRequestBody(ResponseBody responseBody, File file) {
        try {
            byte[] bytes = responseBody.bytes();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void request(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Referer", "http://www.pixiv.net")
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static Call requestReturnCall(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Referer", "http://www.pixiv.net")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static void request(String url, Callback callback, ProgressListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Referer", "http://www.pixiv.net")
                .build();
        getProgressClient(listener).newCall(request).enqueue(callback);
    }

    public static void setListen(final ProgressListener listener) {
        client.networkInterceptors().add(chain -> {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), listener))
                    .build();
        });
    }

    private static OkHttpClient getProgressClient(final ProgressListener listener) {
        OkHttpClient clone = new OkHttpClient();
        if (listener != null) {
            clone.networkInterceptors().add(chain -> {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), listener))
                        .build();
            });
        }
        return clone;
    }

    public interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }
}
