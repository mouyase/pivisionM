package com.reiya.pixiv.network.fuckgfw;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Dns;

public class PixivDNS implements Dns {

    private static PixivDNS mPixivDNS = null;
    private static List<InetAddress> newDns = new ArrayList<>();
    private static final String[] addresses = {
            "210.140.131.224",
            "210.140.131.219",
            "210.140.131.223",
            "54.192.151.59",
            "54.192.151.106",
            "54.192.151.22",
            "54.192.151.42",
            "122.208.114.218",
            "52.219.0.208",
            "52.84.225.11",
            "52.84.225.87",
            "52.84.225.95",
            "52.84.225.186",
            "66.6.32.22",
            "66.6.33.22",
            "52.84.225.193",
            "52.84.225.8",
            "52.84.225.131",
            "52.84.225.132",
            "52.84.225.230",
            "52.84.225.206",
            "52.84.225.214",
            "52.84.225.221",
            "210.140.170.179",
            "210.140.174.37",
            "210.140.175.130",
            "210.140.131.153",
            "210.140.131.144",
            "210.140.131.145",
            "210.140.131.147"
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


    public List<InetAddress> lookup(String s) throws UnknownHostException {
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