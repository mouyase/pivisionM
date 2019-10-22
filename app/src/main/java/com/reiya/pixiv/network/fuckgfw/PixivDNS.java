package com.reiya.pixiv.network.fuckgfw;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Dns;

public class PixivDNS implements Dns {

    private static PixivDNS mPixivDNS = null;
    private static List<InetAddress> newDns = new ArrayList<>();
    private static final String[] addresses = {
            "210.140.131.222",
            "74.120.148.208",
            "113.33.39.198",
            "52.219.68.34",
            "52.219.4.42",
            "210.129.120.56",
            "54.230.84.249",
            "210.129.120.56",
            "66.6.32.22",
            "52.222.234.208",
            "52.222.234.74",
            "122.208.114.218",
            "153.120.23.207",
            "210.140.170.179",
            "210.140.131.147",
            "210.140.131.144",
            "210.140.131.145",
            "210.140.131.145",
            "210.140.131.147",
    };

    private PixivDNS() {
        for (String address : addresses) {
            try {
                newDns.add(InetAddress.getByName(address));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    public static PixivDNS getInstance() {
        if (mPixivDNS == null) {
            mPixivDNS = new PixivDNS();
        }
        return mPixivDNS;
    }


    @NotNull
    public List<InetAddress> lookup(@NotNull String s) throws UnknownHostException {
        if (s.contains("pixiv.net")) {
            try {
                return newDns;
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
        return Dns.SYSTEM.lookup(s);
    }
}