package com.reiya.pixiv.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.reiya.pixiv.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/5/29.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private List<T> mItems;
    private int[] mLayoutId;
    private String mNextUrl;
    private boolean mLoading = false;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseAdapter(Context context, int layoutId, List<T> items) {
        mContext = context;
        mLayoutId = new int[]{layoutId};
        mItems = items;
    }

    public BaseAdapter(Context context, int[] layoutId, List<T> items) {
        mContext = context;
        mLayoutId = layoutId;
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutId[viewType], parent, false);
        view.setOnClickListener(v -> {

        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindData((ViewHolder) holder, getItem(position), holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    protected T getItem(int position) {
        return mItems.get(position);
    }

    public abstract void bindData(ViewHolder holder, T item, int position);

    public void clearItems() {
        mItems = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addItems(List<T> list) {
        if (list == null) {
            return;
        }
        int old = mItems.size();
        int n = list.size();
        mItems.addAll(list);
        notifyItemRangeInserted(old, n);
    }

    public void addItem(int index, T item) {
        mItems.add(index, item);
        notifyItemInserted(index);
    }

    public void removeItem(int id) {
        for (int i = 0; i < mItems.size(); i++) {
            if (getItemId(mItems.get(i)) == id) {
                mItems.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public List<T> getItems() {
        return mItems;
    }

    public void setItems(List<T> list) {
        mItems = list;
        notifyDataSetChanged();
    }

    protected int getItemId(T item) {
        return 0;
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }

    public String getNextUrl() {
        return mNextUrl;
    }

    public void setNextUrl(String nextUrl) {
        mNextUrl = nextUrl;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onClick(Object item, List list, int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(Object item, View view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mViews = new SparseArray<>();
        }

        public <T extends View> T getView(int id) {
            View view = mViews.get(id);
            if (view == null) {
                view = mView.findViewById(id);
                mViews.put(id, view);
            }
            return (T) view;
        }

        public ViewHolder setText(int id, String text) {
            TextView textView = getView(id);
            textView.setText(text);
            return this;
        }

        public ViewHolder loadImage(int id, ImageLoader.Builder builder) {
            ImageView imageView = getView(id);
            builder.load(imageView);
            return this;
        }

        public ViewHolder setVisible(int id, boolean visible) {
            View view = getView(id);
            if (visible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
            return this;
        }

        public ViewHolder setColor(int id, int color) {
            ImageView imageView = getView(id);
            imageView.setImageDrawable(new ColorDrawable(ContextCompat.getColor(mContext, color)));
            return this;
        }

        public ViewHolder setOnClickListener(final T item, final int position) {
            mView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(item, mItems, position);
                }
            });
            return this;
        }

        public ViewHolder setOnLongClickListener(final T item) {
            mView.setOnLongClickListener(v -> {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onLongClick(item, mView);
                }
                return true;
            });
            return this;
        }
    }
}
