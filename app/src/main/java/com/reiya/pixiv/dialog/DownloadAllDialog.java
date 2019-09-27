package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.reiya.pixiv.download.DownloadActivity;
import com.reiya.pixiv.service.DownloadService;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/2/23.
 */
public class DownloadAllDialog extends DialogFragment {
    public static final int TYPE_AUTHOR = DownloadService.TYPE_AUTHOR;
    public static final int TYPE_BOOKMARK = DownloadService.TYPE_BOOKMARK;
    private int mType;
    private int mUserId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int[] msg = {R.string.download_all_author_hint, R.string.download_all_bookmark_hint};
        builder.setMessage(msg[mType]);
        switch (mType) {
            case TYPE_AUTHOR:
                builder
                        .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), DownloadService.class);
                                intent.putExtra(DownloadService.TYPE, mType);
                                intent.putExtra(DownloadService.ID, mUserId);
                                getActivity().startService(intent);
                                Intent intent1 = new Intent(getActivity(), DownloadActivity.class);
                                startActivity(intent1);
                            }
                        })
                        .setNegativeButton(R.string.negative, null);
                break;
            case TYPE_BOOKMARK:
                builder
                        .setPositiveButton(R.string.pub, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), DownloadService.class);
                                intent.putExtra(DownloadService.TYPE, mType);
                                intent.putExtra(DownloadService.IS_PUBLIC, true);
                                getActivity().startService(intent);
                                Intent intent1 = new Intent(getActivity(), DownloadActivity.class);
                                startActivity(intent1);
                            }
                        })
                        .setNegativeButton(R.string.pri, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), DownloadService.class);
                                intent.putExtra(DownloadService.TYPE, mType);
                                intent.putExtra(DownloadService.IS_PUBLIC, false);
                                getActivity().startService(intent);
                                Intent intent1 = new Intent(getActivity(), DownloadActivity.class);
                                startActivity(intent1);
                            }
                        })
                        .setNeutralButton(R.string.negative, null);
                break;
        }

        return builder.create();
    }

    public void setType(int type) {
        mType = type;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
