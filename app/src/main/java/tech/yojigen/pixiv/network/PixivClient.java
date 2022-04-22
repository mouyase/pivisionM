package tech.yojigen.pixiv.network;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import tech.yojigen.common.MD5;
import tech.yojigen.common.util.SettingUtil;
import tech.yojigen.pivisionm.BuildConfig;
import tech.yojigen.pixiv.Pixiv;
import tech.yojigen.pixiv.PixivProperties;
import tech.yojigen.pixiv.network.fuckgfw.PixivDNS;
import tech.yojigen.pixiv.network.fuckgfw.PixivSSLSocketFactory;
import tech.yojigen.pixiv.network.fuckgfw.PixivTrustManager;

public class PixivClient {
    private static final PixivClient mPixivClient = new PixivClient();
    private final OkHttpClient mOkHttpClient;

    private PixivClient() {
        Interceptor interceptor = chain -> {
            String pixivTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.CHINA).format(new Date());
            String pixivHash = MD5.convert(pixivTime + PixivProperties.BASE_HASH);
            Request request = chain.request().newBuilder()
                    .header("User-Agent", PixivProperties.USER_AGENT)
                    .header("Accept-Language", PixivProperties.ACCEPT_LANGUAGE)
                    .header("App-OS", PixivProperties.APP_OS)
                    .header("App-OS-Version", PixivProperties.APP_OS_VERSION)
                    .header("App-Version", PixivProperties.APP_VERSION)
                    .header("Referer", PixivProperties.REFERER)
                    .header("X-Client-Time", pixivTime)
                    .header("X-Client-Hash", pixivHash)
                    .build();
            return chain.proceed(request);
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(interceptor);
        builder.addNetworkInterceptor(httpLoggingInterceptor);
        builder.dns(PixivDNS.getInstance());
        mOkHttpClient = builder.build();
    }

    public static PixivClient getClient() {
        return mPixivClient;
    }

    public void accountLogin(String username, String password, PixivListener pixivListener) {
        String device_token = SettingUtil.getSetting(Pixiv.getPixiv().getContext(), "account_device_token", "pixiv");
        String refresh_token = SettingUtil.getSetting(Pixiv.getPixiv().getContext(), "account_refresh_token", "");
        FormBody.Builder formBodyBuilder = new FormBody.Builder()
                .add("client_id", PixivProperties.CLIENT_ID)
                .add("client_secret", PixivProperties.CLIENT_SECRET)
                .add("device_token", device_token)
                .add("get_secure_url", "true")
                .add("include_policy", "true");
        if (StringUtils.isNoneEmpty(refresh_token)) {
            //如果检测到refresh token则使用refresh token刷新access token，防止收到登陆邮件
            formBodyBuilder
                    .add("refresh_token", refresh_token)
                    .add("grant_type", "refresh_token");
        } else {
            //如果未检测到refresh token则使用账号密码登陆
            formBodyBuilder
                    .add("username", username)
                    .add("password", password)
                    .add("grant_type", "password");
        }
        Request request = new Request.Builder().url(PixivApiUrls.OAUTH).post(formBodyBuilder.build()).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                pixivListener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.body() != null) {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.has("response")) {
                            JSONObject jsonResponse = new JSONObject(json).getJSONObject("response");
                            //存储三个token
                            SettingUtil.setSetting(Pixiv.getPixiv().getContext(), "account_device_token", jsonResponse.getString("device_token"));
                            SettingUtil.setSetting(Pixiv.getPixiv().getContext(), "account_refresh_token", jsonResponse.getString("refresh_token"));
                            SettingUtil.setSetting(Pixiv.getPixiv().getContext(), "account_access_token", jsonResponse.getString("access_token"));
                        }
                        if (jsonObject.has("has_error")) {
                            if (1508 == jsonObject.getJSONObject("errors").getJSONObject("system").getInt("code")) {
                                //Invalid refresh token
                                //refresh token 错误或过期，清除所有token重新使用密码登陆
//                                SettingUtil.delSetting(Pixiv.getPixiv().getContext(), "account_device_token");
//                                SettingUtil.delSetting(Pixiv.getPixiv().getContext(), "account_refresh_token");
//                                SettingUtil.delSetting(Pixiv.getPixiv().getContext(), "account_access_token");
                                Pixiv.getPixiv().accountLogout();
                                accountLogin(username, password, pixivListener);
                            }
                        }
                        pixivListener.onResponse(json);
                    } else {
                        pixivListener.onFailure(new IOException("response is null"));
                    }
                } catch (IOException e) {
                    pixivListener.onFailure(e);
                } catch (JSONException e) {
                    pixivListener.onFailure(new IOException("response is not json"));
                }
            }
        });
    }
}
