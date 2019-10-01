package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import tech.yojigen.pivisionm.R;
import tech.yonjigen.common.util.SettingUtil;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class ConnectModeSelectDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_mode, null);
        Button button_direct = view.findViewById(R.id.button_direct);
        button_direct.setOnClickListener(v -> {
            SettingUtil.setSetting(getContext(), "connect_mode", "direct");
            Toast.makeText(getContext(), "已切换为 直连模式 ，重启后生效", Toast.LENGTH_SHORT).show();
        });
        Button button_global = view.findViewById(R.id.button_global);
        button_global.setOnClickListener(v -> {
            SettingUtil.setSetting(getContext(), "connect_mode", "global");
            Toast.makeText(getContext(), "已切换为 全球模式 ，重启后生效", Toast.LENGTH_SHORT).show();
        });
        Button button_proxy = view.findViewById(R.id.button_proxy);
        button_proxy.setOnClickListener(v -> {
            SettingUtil.setSetting(getContext(), "connect_mode", "proxy");
            Toast.makeText(getContext(), "已切换为 代理模式 ，重启后生效", Toast.LENGTH_SHORT).show();
        });
        builder.setTitle(getString(R.string.string_connect_mode))
                .setView(view);
        return builder.create();
    }
}
