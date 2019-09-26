package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.widget.ListView;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/2/15.
 */
public class MultiSelectDialog extends DialogFragment {
    private int page = 1;
    private int selectedIndex = -1;
    private OnMultiSelected onSelected;
    private ListView lv;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = new String[page + 1];
        items[0] = getActivity().getString(R.string.select_all);
        for (int i = 1; i < page + 1; i++) {
            items[i] = "" + i;
        }
        final boolean[] selected = new boolean[page + 1];
        if (selectedIndex >= 0) {
            selected[selectedIndex + 1] = true;
        }
        builder.setMultiChoiceItems(items, selected, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if (which == 0) {
                        for (int i = 1; i < page + 1; i++) {
                            selected[i] = isChecked;
                            lv.setItemChecked(i, isChecked);
                        }
                    } else {
                        selected[which] = isChecked;
                    }
                }
            })
            .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onSelected.onSelected(selected);
                }
            });
        AlertDialog dialog = builder.create();
        lv = dialog.getListView();
        return dialog;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public void setOnSelected(OnMultiSelected onSelected) {
        this.onSelected = onSelected;
    }

    public interface OnMultiSelected {
        void onSelected(boolean[] selected);
    }
}
