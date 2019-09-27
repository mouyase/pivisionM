package com.reiya.pixiv.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.reiya.pixiv.bean.BookmarkTag;

import java.util.List;

import tech.yojigen.pivisionm.R;

/**
 * Created by zhengyirui on 2017/9/7.
 */

public class BookmarkTagAddAdapter extends BaseAdapter<BookmarkTag> {
    private OnRegisterChangedCallback mOnRegisterChangedCallback;

    public BookmarkTagAddAdapter(Context context, List<BookmarkTag> items) {
        super(context, R.layout.item_bookmark_tag_add, items);
    }

    @Override
    public void bindData(ViewHolder holder, final BookmarkTag item, int position) {
        holder.setText(R.id.tv, item.getName());
        CheckBox checkBox = holder.getView(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(item.isRegistered());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setRegistered(isChecked);
                if (mOnRegisterChangedCallback != null) {
                    mOnRegisterChangedCallback.onChanged(isChecked);
                }
            }
        });
    }

    public void setOnRegisterChangedCallback(OnRegisterChangedCallback onRegisterChangedCallback) {
        mOnRegisterChangedCallback = onRegisterChangedCallback;
    }

    public interface OnRegisterChangedCallback {
        void onChanged(boolean isRegistered);
    }
}
