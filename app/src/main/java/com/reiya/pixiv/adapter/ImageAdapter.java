package com.reiya.pixiv.adapter;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.image.ImageLoader;
import com.reiya.pixiv.ranking.RankingActivity;
import com.reiya.pixiv.util.TempData;
import com.reiya.pixiv.util.WorkFilter;
import com.reiya.pixiv.view.RatioImageView;
import com.reiya.pixiv.work.ViewActivity;

import java.util.ArrayList;
import java.util.List;

import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2015/11/21 0021.
 */
public class ImageAdapter extends BaseAdapter<Work> {
    private boolean mAdjustHeight = false;
    private WorkFilter mWorkFilter = new WorkFilter();
    private boolean mHasHeader = false;
    private Work mHeaderItem;

    public ImageAdapter(Context context) {
        this(context, new ArrayList<Work>());
    }

    public ImageAdapter(Context context, List<Work> works) {
        super(context, new int[]{R.layout.item_image, R.layout.item_header}, works);
        setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(Object item, List list, int position) {
                if (mHasHeader && position == 0) {
                    Intent intent = new Intent(mContext, RankingActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, ViewActivity.class);
                    intent.putExtra("id", ((Work) item).getId());
                    TempData.put("works", list);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }

            }
        });

        setStyle();
    }

    @Override
    public void bindData(ViewHolder holder, Work item, int position) {
        if (mHasHeader && position == 0) {
            holder.loadImage(R.id.imageView,
                    ImageLoader.loadImage(mContext, mHeaderItem.getImageUrl(1))
                            .fitCenter()
            )
                    .setOnClickListener(item, position)
                    .setOnLongClickListener(item);
            holder.setText(R.id.tvTitle, mHeaderItem.getTitle());
        } else {
            if (mAdjustHeight) {
//                View iv = holder.getView(R.id.imageView);
//                ViewGroup.LayoutParams params = iv.getLayoutParams();
//                int width = iv.getMeasuredWidth();
//                Log.e("width", iv + "/" + width);
//                int height = width * item.getHeight() / item.getWidth();
//                params.height = height;
//                iv.setLayoutParams(params);
                RatioImageView iv = holder.getView(R.id.imageView);
                iv.setOriginalSize(item.getWidth(), item.getHeight());

                holder.loadImage(R.id.imageView,
                        ImageLoader.loadImage(mContext, item.getImageUrl(1))
//                                .override(item.getWidth(), item.getHeight())
                )
                        .setOnClickListener(item, position)
                        .setOnLongClickListener(item);
            } else {
                RatioImageView iv = holder.getView(R.id.imageView);
                iv.setOriginalSize(1, 1);

                holder.loadImage(R.id.imageView,
                        ImageLoader.loadImage(mContext, item.getImageUrl(0))
                                .centerCrop()
                )
                        .setOnClickListener(item, position)
                        .setOnLongClickListener(item);
            }

            int page = item.getPageCount();
            if (page > 1) {
                holder.setVisible(R.id.tvPage, true).setText(R.id.tvPage, page + "P");
            } else {
                holder.setVisible(R.id.tvPage, false);
            }
            if (item.isDynamic()) {
                holder.setVisible(R.id.ivDyn, true);
            } else {
                holder.setVisible(R.id.ivDyn, false);
            }
        }
    }

    public void setItems(List<Work> list) {
        super.setItems(mWorkFilter.getFilteredList(list));
    }

    public void addItems(List<Work> list) {
        if (list == null) {
            return;
        }
        super.addItems(mWorkFilter.getFilteredList(list));
    }

    @Override
    protected int getItemId(Work item) {
        return item.getId();
    }

    @Override
    public int getItemCount() {
        if (mHasHeader) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHasHeader && position == 0) {
            return 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    protected Work getItem(int position) {
        if (mHasHeader && position > 0) {
            return super.getItem(position - 1);
        }
        return super.getItem(position);
    }

    public void setBlackList(String[] blackList) {
        mWorkFilter.setBlacklist(blackList);
    }

    public void setHasHeader(boolean hasHeader) {
        mHasHeader = hasHeader;
    }

    public void setHeaderItem(Work headerItem) {
        mHeaderItem = headerItem;
    }

    public void setStyle() {
        mAdjustHeight = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(mContext.getString(R.string.key_list_style), "1").equals("2");
        notifyDataSetChanged();
    }
}
