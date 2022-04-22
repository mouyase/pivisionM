package tech.yojigen.pixiv.network.fuckgfw;


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
    private static final PixivSSLSocketFactory mPixivSSLSocketFactory = new PixivSSLSocketFactory();

    public static PixivSSLSocketFactory getInstance() {
        return mPixivSSLSocketFactory;
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

    @Override
    public Socket createSocket(String host, int port) {
        return null;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return null;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return new String[0];
    }
}
