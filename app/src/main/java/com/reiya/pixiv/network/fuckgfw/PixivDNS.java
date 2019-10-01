package com.reiya.pixiv.network.fuckgfw;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Dns;

public class PixivDNS implements Dns {
    private static final String[] addresses = {
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
            "18.211.61.121",
            "210.140.131.213",
            "210.140.131.214",
            "210.140.131.215",
            "210.140.131.216",
            "210.140.131.217",
            "66.6.32.22",
            "210.140.131.218",
            "210.140.131.219",
            "122.208.114.218",
            "13.35.19.186",
            "210.140.131.220",
            "13.35.19.88",
            "210.140.131.222",
            "210.140.131.223",
            "210.140.131.224",
            "210.140.131.225",
            "210.140.131.226",
            "13.35.19.46",
            "210.140.131.180",
            "52.219.68.134",
            "210.140.131.181",
            "122.208.114.218",
            "210.140.131.182",
            "210.140.131.183",
            "210.140.131.184"
    };
    private static PixivDNS sHttpDns = null;
    private static List<InetAddress> newDns = new ArrayList<>();

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
        if (sHttpDns == null) {
            sHttpDns = new PixivDNS();
        }
        return sHttpDns;
    }


    @NotNull
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