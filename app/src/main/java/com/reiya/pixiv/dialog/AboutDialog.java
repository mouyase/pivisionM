package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.reiya.pixiv.other.OpenSourceActivity;

import java.util.Locale;

import tech.yojigen.pivisionm.BuildConfig;
import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2016/1/1 0001.
 */
public class AboutDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final boolean isChina = Locale.getDefault().getCountry().equals("CN");
        builder.setTitle(getString(R.string.about))
                .setMessage("v" + BuildConfig.VERSION_NAME + "\n" + getString(R.string.app_update_log))
                .setPositiveButton(getString(R.string.positive), null)
                .setNegativeButton(isChina ? getString(R.string.donate) : getString(R.string.app_ranking), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isChina) {
                            String account = "reiya.zyr@gmail.com";
                            ClipboardManager cb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData data = ClipData.newPlainText("account", account);
                            cb.setPrimaryClip(data);
                            Toast.makeText(getActivity(), account + getString(R.string.clip_info_donate), Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                        }
                    }
                })
                .setNeutralButton(getString(R.string.open_source), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), OpenSourceActivity.class);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
}
