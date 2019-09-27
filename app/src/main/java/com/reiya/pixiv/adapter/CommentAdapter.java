package com.reiya.pixiv.adapter;

import android.content.Context;

import com.reiya.pixiv.bean.Comment;
import com.reiya.pixiv.image.ImageLoader;

import java.util.List;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/2/17.
 */
public class CommentAdapter extends BaseAdapter<Comment> {

    public CommentAdapter(Context context, List<Comment> list) {
        super(context, R.layout.item_comment, list);
    }

    @Override
    public void bindData(ViewHolder holder, Comment item, int position) {
        holder.loadImage(R.id.ivProfile,
                ImageLoader.loadImage(mContext, item.getUser().getMediumImageUrl())
                        .centerCrop()
        )
                .setText(R.id.tvName, item.getUser().getName())
                .setText(R.id.tvText, item.getComment())
                .setText(R.id.tvTime, item.getDate());
    }
}
