package com.reiya.pixiv.network.fuckgfw;

import android.annotation.SuppressLint;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;


public class PixivTrustManager implements X509TrustManager {
    @SuppressLint("TrustAllX509TrustManager")
    public void checkClientTrusted(X509Certificate[] param1ArrayOfX509Certificate, String param1String) {
    }

    @SuppressLint("TrustAllX509TrustManager")
    public void checkServerTrusted(X509Certificate[] param1ArrayOfX509Certificate, String param1String) {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}