package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.reiya.pixiv.search.SearchActivity;
import com.reiya.pixiv.bean.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/3 0003.
 */
public class TagsDialog extends DialogFragment {
    private String[] items;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("tag", items[which]);
                getActivity().startActivity(intent);
            }
        });
        return builder.create();
    }

    public void setItems(List<Tag> tags) {
        List<String> list = new ArrayList<>();
        for (Tag tag : tags) {
            list.add(tag.getName());
        }
        items = list.toArray(new String[list.size()]);
    }
}
