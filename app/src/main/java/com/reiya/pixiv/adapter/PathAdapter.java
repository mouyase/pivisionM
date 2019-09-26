package com.reiya.pixiv.adapter;

import android.content.Context;
import android.os.Environment;

import tech.yojigen.pivisionm.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class PathAdapter extends BaseAdapter<File> {
    private final String ROOT = Environment.getRootDirectory().getPath();
    private File mPath;
    private OnSelectListener mOnSelectListener;

    public PathAdapter(Context context, File path, List<File> files) {
        super(context, R.layout.item_file, files);
        mPath = path;
    }

    @Override
    public void bindData(ViewHolder holder, File item, int position) {
        holder.setText(R.id.textView, item.getName())
                .setOnClickListener(item, position);
    }

    public interface OnSelectListener {
        void onSelect(File file);
    }

    public void setListener(final OnSelectListener listener) {
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(Object item, List list, int position) {
                File file = (File) item;
                if (listener != null) {
                    listener.onSelect(file);
                }
                mPath = file;
                setItems(getFolders(file));
            }
        });
        mOnSelectListener = listener;
    }

    public static List<File> getFolders(File path) {
        File[] files = path.listFiles();
        List<File> folders = new ArrayList<>();
        if (files == null) {
            return folders;
        }
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        for (File file : files) {
            if (file.isDirectory()) {
                folders.add(file);
            }
        }
        return folders;
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
}
