package com.reiya.pixiv.adapter;

import android.content.Context;

import com.reiya.pixiv.bean.TrendTag;
import com.reiya.pixiv.image.ImageLoader;

import java.util.List;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/4/7.
 */
public class TrendTagAdapter extends BaseAdapter<TrendTag> {

    public TrendTagAdapter(Context context, List<TrendTag> trendTags) {
        super(context, R.layout.item_trend_tag, trendTags);
    }

    @Override
    public void bindData(ViewHolder holder, TrendTag item, int position) {
        holder.loadImage(R.id.imageView,
                ImageLoader.loadImage(mContext, item.getWork().getImageUrl(0))
                        .centerCrop()
        )
                .setText(R.id.tvTag, item.getTag())
                .setOnClickListener(item, position);
    }

    public void setOnSearchTag(final OnSearchTag onSearchTag) {
        setOnItemClickListener((item, list, position) -> {
            if (onSearchTag != null) {
                onSearchTag.onSearchTag(((TrendTag) item).getTag());
            }
        });
    }

    public interface OnSearchTag {
        void onSearchTag(String tag);
    }
}
