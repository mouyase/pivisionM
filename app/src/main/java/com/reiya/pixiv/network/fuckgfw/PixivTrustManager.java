package com.reiya.pixiv.network.fuckgfw;

import android.annotation.SuppressLint;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;


public class PixivTrustManager implements X509TrustManager {
    @SuppressLint("TrustAllX509TrustManager")
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
    }

    @SuppressLint("TrustAllX509TrustManager")
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}