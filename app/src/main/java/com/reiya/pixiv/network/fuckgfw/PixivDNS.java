package com.reiya.pixiv.network.fuckgfw;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Dns;

public class PixivDNS implements Dns {

    private static PixivDNS mPixivDNS;
    private static List<InetAddress> pixivDns = new ArrayList<>();
    private static List<InetAddress> pximgDns = new ArrayList<>();
    private static final String[] pixivAddresses = {
            "210.140.131.203",
            "210.140.131.204",
            "210.140.131.205",
            "210.140.131.206",
            "210.140.131.207",
            "210.140.131.208",
            "210.140.131.209",
            "210.140.131.210",
            "210.140.131.211",
            "210.140.131.212",
            "210.140.131.213",
            "210.140.131.214",
            "210.140.131.215",
            "210.140.131.216",
            "210.140.131.217",
            "210.140.131.218",
            "210.140.131.219",
            "210.140.131.220",
            "210.140.131.222",
            "210.140.131.223",
            "210.140.131.224",
            "210.140.131.225",
            "210.140.131.226",
            "210.140.131.180",
            "210.140.131.181",
            "210.140.131.182",
            "210.140.131.183",
            "210.140.131.184",
    };
    private static final String[] pximgAddresses = {
            "210.140.92.135",
    };

    private PixivDNS() {
        for (String address : pixivAddresses) {
            try {
                pixivDns.add(InetAddress.getByName(address));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        for (String address : pximgAddresses) {
            try {
                pximgDns.add(InetAddress.getByName(address));
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
                return pixivDns;
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        } else if (s.contains("pximg.net")) {
            try {
                return pximgDns;
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
        return Dns.SYSTEM.lookup(s);
    }
}