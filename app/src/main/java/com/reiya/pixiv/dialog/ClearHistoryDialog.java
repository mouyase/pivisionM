package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.reiya.pixiv.db.RecordDAO;

import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2015/12/17 0017.
 */
public class ClearHistoryDialog extends DialogFragment {
    private Runnable runnable;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.clear_history))
                .setPositiveButton(getString(R.string.positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RecordDAO(getActivity()).removeRecords(System.currentTimeMillis());
                        if (runnable != null) {
                            runnable.run();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.negative), null);
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        runnable = null;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
