package com.reiya.pixiv.adapter;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class PathAdapter extends BaseAdapter<File> {
    private File mPath;
    private OnSelectListener mOnSelectListener;

    public PathAdapter(Context context, File path, List<File> files) {
        super(context, R.layout.item_file, files);
        mPath = path;
    }

    public static List<File> getFolders(File path) {
        File[] files = path.listFiles();
        List<File> folders = new ArrayList<>();
        if (files == null) {
            return folders;
        }
        Arrays.sort(files, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));
        for (File file : files) {
            if (file.isDirectory()) {
                folders.add(file);
            }
        }
        return folders;
    }

    @Override
    public void bindData(ViewHolder holder, File item, int position) {
        holder.setText(R.id.textView, item.getName()).setOnClickListener(item, position);
    }

    public void setListener(final OnSelectListener listener) {
        setOnItemClickListener((item, list, position) -> {
            File file = (File) item;
            if (listener != null) {
                listener.onSelect(file);
            }
            mPath = file;
            setItems(getFolders(file));
        });
        mOnSelectListener = listener;
    }

    public void up() {
        if (mPath.getParent() == null) {
            return;
        }
        mPath = mPath.getParentFile();
        setItems(getFolders(mPath));
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelect(mPath);
        }
    }

    public interface OnSelectListener {
        void onSelect(File file);
    }
}
