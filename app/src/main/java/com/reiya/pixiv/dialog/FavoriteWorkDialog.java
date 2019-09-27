package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.reiya.pixiv.util.Value;

import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2015/12/16 0016.
 */
public class FavoriteWorkDialog extends DialogFragment {
    private ToAdd toAdd;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.collection))
                .setPositiveButton(getString(R.string.pub), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toAdd.add(Value.PUBLIC);
                    }
                })
                .setNegativeButton(getString(R.string.pri), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toAdd.add(Value.PRIVATE);
                    }
                })
                .setNeutralButton(getString(R.string.negative), null);
        return builder.create();
    }

    public void setToAdd(ToAdd toAdd) {
        this.toAdd = toAdd;
    }

    public interface ToAdd {
        void add(String type);
    }
}
