package com.reiya.pixiv.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.reiya.pixiv.base.BaseApplication;
import com.reiya.pixiv.main.MainActivity;
import com.reiya.pixiv.util.PixivOAuth;
import com.reiya.pixiv.view.RippleView;

import tech.yojigen.pivisionm.R;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private TextView mAccountView;
    private EditText mPasswordView;
    private View mClearAccountView;
    private View mClearPasswordView;
    private View mLoginFormView;
    private View mProcessingLoginView;
    private Handler mHandler;
    private Runnable mLoginRunnable = new Runnable() {
        @Override
        public void run() {
            mLoginFormView.setVisibility(View.GONE);
            mProcessingLoginView.setVisibility(View.VISIBLE);
        }
    };

    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mAccountView = findViewById(R.id.account);
        mPasswordView = findViewById(R.id.password);
        mClearAccountView = findViewById(R.id.iv_clear_account);
        mClearPasswordView = findViewById(R.id.iv_clear_password);

        mAccountView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mClearAccountView.setVisibility(View.GONE);
                } else {
                    mClearAccountView.setVisibility(View.VISIBLE);
                }
            }
        });
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mClearPasswordView.setVisibility(View.GONE);
                } else {
                    mClearPasswordView.setVisibility(View.VISIBLE);
                }
            }
        });
        mClearAccountView.setOnClickListener(v -> mAccountView.setText(""));
        mClearPasswordView.setOnClickListener(v -> mPasswordView.setText(""));

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
            attemptLogin();
            return true;
        });


        View.OnClickListener jumpToBrowserListener = view -> {
//            @SuppressLint("SimpleDateFormat")
//            String pixivTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.CHINA).format(new Date());
//            String pixivHash = MD5.convert(pixivTime + "28c1fdd170a5204386cb1313c7077b34f83e4aaf4aa829ce78c231e05b0bae2c");
//            Interceptor interceptor = chain -> {
//                Request request = chain.request().newBuilder()
//                        .header("User-Agent", BaseApplication.getUA())
//                        .header("Accept-Language", "zh_CN")
//                        .header("App-OS", "Android")
//                        .header("App-OS-Version", "" + Build.VERSION.RELEASE)
//                        .header("App-Version", "5.0.200")
//                        .header("X-Client-Time", pixivTime)
//                        .header("X-Client-Hash", pixivHash)
//                        .header("Referer", "https://www.pixiv.net")
//                        .build();
//                return chain.proceed(request);
//            };
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
//
//            OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                    .addInterceptor(interceptor)
//                    .addNetworkInterceptor(interceptor)
//                    .addInterceptor(logging);
//            OkHttpClient okHttpClient = builder.build();
//            FormBody formBody = new FormBody.Builder()
//                    .add("user_name", "四次元科技")
//                    .add("ref", "pixiv_android_app_provisional_account")
//                    .build();
//
//            Request request = new Request.Builder().url("https://accounts.128512.xyz/api/provisional-accounts/create")
//                    .header("Authorization", "Bearer l-f9qZ0ZyqSwRyZs8-MymbtWBbSxmCu1pmbOlyisou8")
//                    .post(formBody)
//                    .build();
//            okHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    LoginActivity.this.runOnUiThread(() -> Toast.makeText(LoginActivity.this, "获取账号失败", Toast.LENGTH_LONG).show());
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    try {
//                        String jsonString = new StringBuilder().append(response.body().string()).toString();
//                        System.out.println(jsonString);
//                        JSONObject jsonObject = new JSONObject(jsonString);
//                        String username = jsonObject.getJSONObject("body").getString("user_account");
//                        String password = jsonObject.getJSONObject("body").getString("password");
//                        LoginActivity.this.runOnUiThread(() -> {
//                                    Toast.makeText(LoginActivity.this, "获取账号成功", Toast.LENGTH_LONG).show();
//                                    mAccountView.setText(username);
//                                    mPasswordView.setText(password);
//                                    attemptLogin();
//                                }
//                        );
//                    } catch (JSONException e) {
//                        LoginActivity.this.runOnUiThread(() -> Toast.makeText(LoginActivity.this, "数据解析错误", Toast.LENGTH_LONG).show());
//                        e.printStackTrace();
//                    }
//                }
//            });
            String loginUrl = "https://app-api.pixiv.net/web/v1/login?code_challenge=" +
                    PixivOAuth.getInstance().getChallenge() +
                    "&code_challenge_method=S256&client=pixiv-android";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请选择模式")
                    .setMessage("是否使用代理模式(如果你无法打开登陆页面请选择是)")
                    .setPositiveButton("是", (dialog, which) -> {
                        Intent intent = new Intent(this, BrowserActivity.class);
                        intent.putExtra("isNeedProxy", true);
                        intent.putExtra("loginUrl", loginUrl);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("否", (dialog, which) -> {
                        Intent intent = new Intent(this, BrowserActivity.class);
                        intent.putExtra("isNeedProxy", false);
                        intent.putExtra("loginUrl", loginUrl);
                        startActivity(intent);
                        finish();
                    })
                    .create().show();
        };
        findViewById(R.id.sign_in_button).setOnClickListener(jumpToBrowserListener);
        findViewById(R.id.register_button).setOnClickListener(jumpToBrowserListener);
        findViewById(R.id.setting_button).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(intent, 1);
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProcessingLoginView = findViewById(R.id.login_progress_view);

        mHandler = new Handler(getMainLooper());

        String codeUrl = getIntent().getStringExtra("codeUrl");
        if (!TextUtils.isEmpty(codeUrl)){
            Uri codeUri = Uri.parse(codeUrl);
            if (codeUri != null && !TextUtils.isEmpty(codeUri.getQueryParameter("code"))) {
                code = codeUri.getQueryParameter("code");
                attemptLogin();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mLoginRunnable);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
//        String account = mAccountView.getText().toString();
//        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (TextUtils.isEmpty(password)) {
//            mPasswordView.setError(getString(R.string.error_field_required));
//            focusView = mPasswordView;
//            cancel = true;
//        }
//
        // Check for a valid email address.
//        if (TextUtils.isEmpty(account)) {
//            mAccountView.setError(getString(R.string.error_field_required));
//            focusView = mAccountView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            // Try to login
            BaseApplication.getInstance().login(code,
                    user -> {
                        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    },
                    () -> showProgress(false));
        }
    }

    private void showProgress(boolean show) {
        if (show) {
            int[] n = new int[2];
            View view = findViewById(R.id.sign_in_button);
            view.getLocationOnScreen(n);
            int x = n[0] + view.getWidth() / 2;
            int y = n[1] + view.getHeight() / 2;
            RippleView rippleView = findViewById(R.id.ripple_view);
            rippleView.start(x, y);
            mHandler.postDelayed(mLoginRunnable, 300);
        } else {
            RippleView rippleView = findViewById(R.id.ripple_view);
            rippleView.reset();
            mLoginFormView.setVisibility(View.VISIBLE);
            mProcessingLoginView.setVisibility(View.GONE);
        }
    }
}

