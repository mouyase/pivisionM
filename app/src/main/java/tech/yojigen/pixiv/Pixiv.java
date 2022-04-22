package tech.yojigen.pixiv;

import android.content.Context;

import tech.yojigen.common.util.SettingUtil;
import tech.yojigen.pixiv.network.PixivClient;
import tech.yojigen.pixiv.network.PixivListener;

public class Pixiv {
    private static Pixiv mPixiv = new Pixiv();
    private static Context mContext;
    private PixivClient mPixivClien = PixivClient.getClient();

    public static Pixiv getPixiv() {
        return mPixiv;
    }

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public Context getContext() {
        return mContext;
    }

    public void accountLogin(String username, String password, PixivListener pixivListener) {
        mPixivClien.accountLogin(username, password, pixivListener);
    }

    public void accountLogout() {
        SettingUtil.delSetting(Pixiv.getPixiv().getContext(), "account_device_token");
        SettingUtil.delSetting(Pixiv.getPixiv().getContext(), "account_refresh_token");
        SettingUtil.delSetting(Pixiv.getPixiv().getContext(), "account_access_token");
    }
}
