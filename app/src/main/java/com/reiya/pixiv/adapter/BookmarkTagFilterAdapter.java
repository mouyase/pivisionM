package com.reiya.pixiv.adapter;

import android.content.Context;

import com.reiya.pixiv.bean.BookmarkTag;

import java.util.List;

import tech.yojigen.pivisionm.R;

/**
 * Created by zhengyirui on 2017/9/8.
 */

public class BookmarkTagFilterAdapter extends BaseAdapter<BookmarkTag> {
    public BookmarkTagFilterAdapter(Context context, List<BookmarkTag> items) {
        super(context, R.layout.item_bookmark_tag_filter, items);
    }

    @Override
    public void bindData(ViewHolder holder, BookmarkTag item, int position) {
        holder.setText(R.id.tv_name, item.getName());
        holder.setText(R.id.tv_tag_count, String.valueOf(item.getCount()));
        holder.setOnClickListener(item, position);
    }
}
