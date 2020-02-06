package tech.yojigen.pixiv.network.fuckgfw;

import android.annotation.SuppressLint;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;


public class PixivTrustManager implements X509TrustManager {
    private static PixivTrustManager mPixivTrustManager;

    public static PixivTrustManager getInstance() {
        if (mPixivTrustManager == null) {
            mPixivTrustManager = new PixivTrustManager();
        }
        return mPixivTrustManager;
    }

    @SuppressLint("TrustAllX509TrustManager")
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @SuppressLint("TrustAllX509TrustManager")
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}