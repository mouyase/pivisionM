package tech.yojigen.pixiu.network.fuckgfw;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Dns;

public class PixivDNS implements Dns {

    private static PixivDNS mPixivDNS;
    private static List<InetAddress> newDns = new ArrayList<>();
    private static final String[] addresses = {
            "210.140.131.200",
            "210.140.131.201",
            "210.140.131.202",
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