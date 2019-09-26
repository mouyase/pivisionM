package com.reiya.pixiv.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by Administrator on 2016/1/17 0017.
 */
public class StringHelper {
    private static final String STRINGS = "1234567890abcdefghijklmnopqrstuvwxyz";

    public static String getRandomString() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        int l = STRINGS.length();
        for (int i = 0; i < 4; i++) {
            int num = random.nextInt(l);
            builder.append(STRINGS.charAt(num));
        }
        return builder.toString();
    }

    public static String getFormattedDate(int year, int month, int day, String separator) {
        DecimalFormat format = new DecimalFormat("00");
        return year + separator + format.format(month + 1) + separator + format.format(day);
    }

    public static String getFormattedTime(String time) {
//        2016-04-07T00:23:20+09:00
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
