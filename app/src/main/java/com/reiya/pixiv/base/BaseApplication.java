package com.reiya.pixiv.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.mob.MobSDK;
import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.bean.User;
import com.reiya.pixiv.db.RecordDAO;
import com.reiya.pixiv.network.HttpClient;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;
import com.reiya.pixiv.other.LoginActivity;
import com.reiya.pixiv.util.IO;
import com.reiya.pixiv.util.PixivOAuth;
import com.reiya.pixiv.util.Serializer;
import com.reiya.pixiv.util.StringHelper;
import com.reiya.pixiv.util.UserData;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import rx.Subscriber;
import tech.yojigen.common.util.SettingUtil;
import tech.yojigen.pivisionm.R;
import tech.yojigen.pixiv.Pixiv;

/**
 * Created by Administrator on 2015/11/21 0021.
 */
public class BaseApplication extends Application {
    public static final long AUTH_EXPIRE_TIME = 1000 * 60 * 30;
    private static BaseApplication sInstance;

    public static BaseApplication getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static long getTime(String name) {
        return sInstance.getSharedPreferences(name, MODE_PRIVATE).getLong("time", 0);
    }

    public static String getCache(String name) {
        return sInstance.getSharedPreferences(name, MODE_PRIVATE).getString("content", "");
    }

    public static void writeCache(String name, long time, String content) {
        SharedPreferences.Editor editor = sInstance.getSharedPreferences(name, MODE_PRIVATE).edit();
        editor.putLong("time", time);
        editor.putString("content", content);
        editor.apply();
    }

    public static String[] getHistory() {
        String s = sInstance.getSharedPreferences("search", MODE_PRIVATE).getString("history", "");
        if (s.equals("")) {
            return new String[]{};
        }
        return s.split("##");
    }

    public static void writeHistory(String[] s) {
        StringBuilder h = new StringBuilder(s[0]);
        for (int i = 1; i < s.length; i++) {
            h.append("##");
            h.append(s[i]);
        }
        SharedPreferences.Editor editor = sInstance.getSharedPreferences("search", MODE_PRIVATE).edit();
        editor.putString("history", h.toString());
        editor.apply();
    }

    public static long getAuthTime() {
        return sInstance.getSharedPreferences("v2", MODE_PRIVATE).getLong("time", 0);
    }

    public static String getToken() {
        return sInstance.getSharedPreferences("v2", MODE_PRIVATE).getString("token", "");
    }

    private static void writeToken(String s) {
        SharedPreferences.Editor editor = sInstance.getSharedPreferences("v2", MODE_PRIVATE).edit();
        editor.putString("token", s);
        editor.putLong("time", System.currentTimeMillis());
        editor.apply();
    }

    public static User getUser() {
        String str = sInstance.getSharedPreferences("v2", MODE_PRIVATE).getString("user", "");
        if (str.equals("")) {
            return null;
        }
        return (User) Serializer.deserialize(str, User.class);
    }

    private static void writeUser(User user) {
        SharedPreferences.Editor editor = sInstance.getSharedPreferences("v2", MODE_PRIVATE).edit();
        editor.putString("user", Serializer.serialize(user));
        editor.apply();
    }

    private static String getPID() {
        SharedPreferences sp = sInstance.getSharedPreferences("v1", MODE_PRIVATE);
        String s = sp.getString("pid", "");
        if (s.equals("")) {
            s = StringHelper.getRandomString();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("pid", s);
            editor.apply();
            return s;
        }
        return s;
    }

    private static String getDeviceInfo() {
        return "(Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + ")";
    }

    public static String getUA() {
        return "PixivAndroidApp/5.0.200 " + getDeviceInfo();
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static int getSystemVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //判断是否为Debug版本
        ApplicationInfo applicationInfo = getApplicationInfo();
        if ((applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            Bugly.init(getApplicationContext(), "0fc124925c", true);
        } else {
            Bugly.init(getApplicationContext(), "0fc124925c", false);
        }
        Beta.autoCheckUpgrade = false;

        Pixiv.init(this);
        HttpClient.init(this);
        MobSDK.init(this);//初始化MobSDK
        new RecordDAO(this).removeRecords(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7);

        if (IO.getImageCacheSizeMB() > Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.key_cache_limit), "100"))) {
            AsyncTask.execute(() -> Glide.get(getApplicationContext()).clearDiskCache());
        }

        Theme.init(this);
    }

    public void tryLogin(Activity activity, boolean force, final OnLoginDone onLoginDone) {
        if (UserData.isLoggedIn()) {
            return;
        }
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.key_auto_login), false) && !force) {
            return;
        }
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String account = sharedPreferences.getString(getString(R.string.key_account), "");
//        String password = sharedPreferences.getString(getString(R.string.key_password), "");
//        if (account.equals("") || password.equals("") && force) {
//            LoginDialog loginDialog = new LoginDialog();
//            loginDialog.setListener((account1, password1) -> login(account1, password1, true, onLoginDone));
//            loginDialog.show(((FragmentActivity) activity).getSupportFragmentManager(), "Login");
//        } else {
//            login(account, password, false, onLoginDone);
//        }
    }

    public void login(final String code, final OnLoginDone onLoginDone, final OnLoginFailed onLoginFailed) {
//        if (BaseApplication.getAuthTime() + AUTH_EXPIRE_TIME > System.currentTimeMillis()) {
//            UserData.setBearer(BaseApplication.getToken());
////            Log.e("token", UserData.token);
//            User user = BaseApplication.getUser();
//            UserData.user = user;
//            UserData.setLoggedInState();
//            onLoginDone.onLoginDone(user);
//        } else {
//            Toast.makeText(this, R.string.logining, Toast.LENGTH_SHORT).show();

        Toast.makeText(this, R.string.processing_login, Toast.LENGTH_SHORT).show();
        String refresh_token = SettingUtil.getSetting(this, "account_refresh_token", "");
        if (!StringUtils.isEmpty(refresh_token)) {
            NetworkRequest.getAuth(refresh_token)
                    .subscribe(new Subscriber<HttpService.AuthResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            SettingUtil.delSetting(BaseApplication.this, "account_refresh_token");
//                            login(account, password, save, onLoginDone, onLoginFailed);
                            Intent intent = new Intent(BaseApplication.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onNext(HttpService.AuthResponse authResponse) {
                            String token = authResponse.getToken();
                            if (token == null || token.equals("")) {
                                Toast.makeText(getApplicationContext(), R.string.fail_to_login_account_err, Toast.LENGTH_SHORT).show();
                                if (onLoginFailed != null) {
                                    onLoginFailed.onLoginFailed();
                                }
                            } else {
                                User user = authResponse.getUser();
                                SettingUtil.setSetting(BaseApplication.this, "account_refresh_token", authResponse.getRefreshToken());
                                BaseApplication.writeToken(token);
                                BaseApplication.writeUser(user);
                                UserData.setBearer(token);
//                                Log.e("token", token);
                                UserData.user = user;
                                UserData.setLoggedInState();
                                Toast.makeText(getApplicationContext(), R.string.login_successfully, Toast.LENGTH_SHORT).show();
                                if (onLoginDone != null) {
                                    onLoginDone.onLoginDone(user);
                                }
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
//                                    editor.putString(getString(R.string.key_account), account);
//                                    editor.putString(getString(R.string.key_password), password);
                                editor.putBoolean(getString(R.string.key_auto_login), true);
                                editor.apply();
                            }
                        }
                    });
        } else {
            NetworkRequest.getAuthNew(code, PixivOAuth.getInstance().getCodeVerifier())
                    .subscribe(new Subscriber<HttpService.AuthResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (e instanceof IOException) {
                                Toast.makeText(getApplicationContext(), "网络错误，可能需要科学上网环境", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.fail_to_login_network_err, Toast.LENGTH_LONG).show();
                            }
                            if (onLoginFailed != null) {
                                onLoginFailed.onLoginFailed();
                            }
                        }

                        @Override
                        public void onNext(HttpService.AuthResponse authResponse) {
                            String token = authResponse.getToken();
                            if (token == null || token.equals("")) {
                                Toast.makeText(getApplicationContext(), R.string.fail_to_login_account_err, Toast.LENGTH_SHORT).show();
                                if (onLoginFailed != null) {
                                    onLoginFailed.onLoginFailed();
                                }
                            } else {
                                User user = authResponse.getUser();
                                SettingUtil.setSetting(BaseApplication.this, "account_refresh_token", authResponse.getRefreshToken());
                                BaseApplication.writeToken(token);
                                BaseApplication.writeUser(user);
                                UserData.setBearer(token);
//                                Log.e("token", token);
                                UserData.user = user;
                                UserData.setLoggedInState();
                                Toast.makeText(getApplicationContext(), R.string.login_successfully, Toast.LENGTH_SHORT).show();
                                if (onLoginDone != null) {
                                    onLoginDone.onLoginDone(user);
                                }
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
//                                    editor.putString(getString(R.string.key_account), account);
//                                    editor.putString(getString(R.string.key_password), password);
                                editor.putBoolean(getString(R.string.key_auto_login), true);
                                editor.apply();
                            }
                        }
                    });
//        }
        }
    }

    public interface OnLoginDone {
        void onLoginDone(User user);
    }

    public interface OnLoginFailed {
        void onLoginFailed();
    }
}
