package com.reiya.pixiv.bean;

import android.content.Context;
import android.preference.PreferenceManager;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/1/27.
 */
public class Theme {
    private static int theme;

    private static int getTheme(String color) {
        switch (color) {
            case "red":
                return R.style.ThemeRed;
            case "pink":
                return R.style.ThemePink;
            case "purple":
                return R.style.ThemePurple;
            case "indigo":
                return R.style.ThemeIndigo;
            case "blue":
                return R.style.ThemeBlue;
            case "teal":
                return R.style.ThemeTeal;
            case "green":
                return R.style.ThemeGreen;
            case "lime":
                return R.style.ThemeLime;
            case "yellow":
                return R.style.ThemeYellow;
            case "orange":
                return R.style.ThemeOrange;
            case "grey":
                return R.style.ThemeGrey;
            case "black":
                return R.style.ThemeBlack;
        }
        return R.style.ThemeIndigo;
    }

    public static int getTheme() {
        return theme;
    }

    public static void init(Context context) {
        theme = getTheme(PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getString(context.getString(R.string.key_theme_color), ""));
    }

    public static void set(String color) {
        theme = getTheme(color);
    }
}
