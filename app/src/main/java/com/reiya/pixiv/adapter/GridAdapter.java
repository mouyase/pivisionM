package com.reiya.pixiv.adapter;

import android.content.Context;
import android.content.Intent;

import com.reiya.pixiv.group.GroupViewActivity;
import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/2/18.
 */
public class GridAdapter extends BaseAdapter<String> {
    private final Work mWork;

    public GridAdapter(Context context, Work work) {
        super(context, R.layout.item_grid, new ArrayList<String>());
        mWork = work;
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(Object item, List list, int position) {
                Intent intent = new Intent(mContext, GroupViewActivity.class);
                intent.putExtra("work", mWork);
                intent.putExtra("index", position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWork.getPageCount();
    }

    @Override
    protected String getItem(int position) {
        return mWork.getImageUrl(0, position);
    }

    @Override
    public void bindData(ViewHolder holder, String item, int position) {
        holder.loadImage(R.id.imageView,
                ImageLoader.loadImage(mContext, item)
                        .centerCrop()
                )
                .setOnClickListener(item, position);
    }
}
