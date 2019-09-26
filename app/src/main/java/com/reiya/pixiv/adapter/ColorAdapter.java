package com.reiya.pixiv.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;

import tech.yojigen.pivisionm.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2016/1/27.
 */
public class ColorAdapter extends BaseAdapter<String> {
    private static final int[] COLORS = {R.color.red, R.color.pink, R.color.purple, R.color.indigo, R.color.blue, R.color.teal, R.color.green, R.color.lime, R.color.yellow, R.color.orange, R.color.grey, R.color.black};
    private static final String[] NAMES = {"red", "pink", "purple", "indigo", "blue", "teal", "green", "lime", "yellow", "orange", "grey", "black"};

    public ColorAdapter(Context context) {
        super(context, R.layout.item_color, Arrays.asList(NAMES));
    }

    @Override
    public void bindData(ViewHolder holder, String item, int position) {
        holder.setColor(R.id.iv, COLORS[position])
                .setOnClickListener(item, position);
    }

    public void setOnSelected(final OnColorSelected onSelected) {
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(Object item, List list, int position) {
                onSelected.onSelected(NAMES[position], ContextCompat.getColor(mContext, COLORS[position]));
            }
        });
    }

    public interface OnColorSelected {
        void onSelected(String color, int code);
    }
}
