package com.reiya.pixiv.adapter;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by lenovo on 2016/4/13.
 */
public class StringArrayAdapter extends BaseAdapter<String> {

    public StringArrayAdapter(Context context, String[] strings) {
        super(context, android.R.layout.simple_list_item_1, Arrays.asList(strings));
    }

    @Override
    public void bindData(ViewHolder holder, String item, int position) {
        holder.setText(android.R.id.text1, item);
    }
}
