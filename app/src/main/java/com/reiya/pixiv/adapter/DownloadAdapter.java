package com.reiya.pixiv.adapter;

import android.content.Context;

import com.reiya.pixiv.bean.Task;
import com.reiya.pixiv.image.ImageLoader;

import java.util.List;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/3/19.
 */
public class DownloadAdapter extends BaseAdapter<Task> {

    public DownloadAdapter(Context context, List<Task> tasks) {
        super(context, R.layout.item_download, tasks);
    }

    @Override
    public void bindData(ViewHolder holder, Task item, int position) {
        holder.loadImage(R.id.imageView,
                ImageLoader.loadImage(mContext, item.work.getImageUrl(0))
                        .centerCrop()
        )
                .setText(R.id.textView, item.work.getTitle() + "\n" + item.work.getId() + "   p" + item.index);
    }
}
