package tech.yojigen.pixiv.network.fuckgfw;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Dns;

public class PixivDNS implements Dns {

    private static PixivDNS mPixivDNS;
    private static List<InetAddress> pixivDns = new ArrayList<>();
    private static final String[] PIXIV_ADDRESSES = {
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
    private static final List<InetAddress> pximgDns = new ArrayList<>();
    private static final String[] PXIMG_ADDRESSES = {
            "210.140.92.138",
            "210.140.92.139",
            "210.140.92.140",
            "210.140.92.141",
            "210.140.92.142",
            "210.140.92.143",
            "210.140.92.144",
            "210.140.92.145",
            "210.140.92.146",
            "210.140.92.147",
    };

    private PixivDNS() {
        for (String address : PIXIV_ADDRESSES) {
            try {
                pixivDns.add(InetAddress.getByName(address));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        for (String address : PXIMG_ADDRESSES) {
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
        }
        if (s.contains("pximg.net")) {
            try {
                return pximgDns;
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
        return Dns.SYSTEM.lookup(s);
    }
}