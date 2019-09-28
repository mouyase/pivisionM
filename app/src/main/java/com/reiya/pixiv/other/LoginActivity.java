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

import androidx.appcompat.app.AppCompatActivity;

import com.reiya.pixiv.base.BaseApplication;
import com.reiya.pixiv.bean.User;
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

        findViewById(R.id.sign_in_button).setOnClickListener(view -> attemptLogin());
        findViewById(R.id.register_button).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.pixiv.net/signup"));
            startActivity(browserIntent);
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProcessingLoginView = findViewById(R.id.login_progress_view);

        mHandler = new Handler(getMainLooper());
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
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            // Try to login
            BaseApplication.getInstance().login(account, password, true,
                    new BaseApplication.OnLoginDone() {
                        @Override
                        public void onLoginDone(User user) {
                            finish();
                        }
                    },
                    new BaseApplication.OnLoginFailed() {
                        @Override
                        public void onLoginFailed() {
                            showProgress(false);
                        }
                    });
        }
    }

    private void showProgress(boolean show) {
        if (show) {
            int[] n = new int[2];
            View view = findViewById(R.id.sign_in_button);
            view.getLocationOnScreen(n);
            int x = n[0] + view.getWidth() / 2;
            int y = n[1] + view.getHeight() / 2;
            RippleView rippleView = (RippleView) findViewById(R.id.ripple_view);
            rippleView.start(x, y);
            mHandler.postDelayed(mLoginRunnable, 300);
        } else {
            RippleView rippleView = (RippleView) findViewById(R.id.ripple_view);
            rippleView.reset();
            mLoginFormView.setVisibility(View.VISIBLE);
            mProcessingLoginView.setVisibility(View.GONE);
        }
    }
}

