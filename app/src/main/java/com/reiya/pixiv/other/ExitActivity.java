package com.reiya.pixiv.other;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import tech.yojigen.common.util.SettingUtil;

public class ExitActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingUtil.delSetting(this, "account_refresh_token");
        getSharedPreferences("v2", MODE_PRIVATE).edit().clear().apply();
        Toast.makeText(this, "账号已登出", Toast.LENGTH_LONG).show();
        finish();
    }
}
