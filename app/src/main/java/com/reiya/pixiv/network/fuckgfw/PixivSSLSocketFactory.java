package com.reiya.pixiv.network.fuckgfw;


import android.net.SSLCertificateSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public class PixivSSLSocketFactory extends SSLSocketFactory {
    private static PixivSSLSocketFactory mPixivSSLSocketFactory;

    public static PixivSSLSocketFactory getInstance() {
        if (mPixivSSLSocketFactory == null) {
            mPixivSSLSocketFactory = new PixivSSLSocketFactory();
        }
        return mPixivSSLSocketFactory;
    }


    public Socket createSocket(String paramString, int paramInt) {
        return null;
    }


    public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2) {
        return null;
    }


    public Socket createSocket(InetAddress paramInetAddress, int paramInt) {
        return null;
    }


    public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2) {
        return null;
    }


    public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean) throws IOException {
        if (paramSocket == null)
            Intrinsics.throwNpe();
        InetAddress inetAddress = paramSocket.getInetAddress();
        Intrinsics.checkExpressionValueIsNotNull(inetAddress, "address");
        if (paramBoolean)
            paramSocket.close();
        SocketFactory socketFactory = SSLCertificateSocketFactory.getDefault(0);
        if (socketFactory != null) {
            Socket socket = socketFactory.createSocket(inetAddress, paramInt);
            if (socket != null) {
                ((SSLSocket) socket).setEnabledProtocols(((SSLSocket) socket).getSupportedProtocols());
                SSLSession sSLSession = ((SSLSocket) socket).getSession();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Established ");
                Intrinsics.checkExpressionValueIsNotNull(sSLSession, "session");
                stringBuilder.append(sSLSession.getProtocol());
                stringBuilder.append(" connection with ");
                stringBuilder.append(sSLSession.getPeerHost());
                stringBuilder.append(" using ");
                stringBuilder.append(sSLSession.getCipherSuite());
                return socket;
            }
            throw new TypeCastException("null cannot be cast to non-null type javax.net.ssl.SSLSocket");
        }
        throw new TypeCastException("null cannot be cast to non-null type android.net.SSLCertificateSocketFactory");
    }


    public String[] getDefaultCipherSuites() {
        return new String[0];
    }


    public String[] getSupportedCipherSuites() {
        return new String[0];
    }
}
