package com.reiya.pixiv.view;

import android.content.Context;
import android.preference.PreferenceManager;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/10/1.
 */

public class WorkGridLayoutManager extends StaggeredGridLayoutManager {
    public WorkGridLayoutManager(Context context) {
        super(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.key_columns_count), "3")), VERTICAL);
    }
}
