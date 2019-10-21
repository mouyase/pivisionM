package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2015/12/17 0017.
 */
public class LoginDialog extends DialogFragment {
    private LoginListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_login, null);
        final EditText etAccount = view.findViewById(R.id.etAccount);
        final EditText etPassword = view.findViewById(R.id.etPassword);
        final TextInputLayout tilAccount = view.findViewById(R.id.tilAccount);
        final TextInputLayout tilPassword = view.findViewById(R.id.tilPassword);
        view.findViewById(R.id.button).setOnClickListener(v -> {
            tilAccount.setErrorEnabled(false);
            tilPassword.setErrorEnabled(false);
            String account = etAccount.getText().toString();
            String password = etPassword.getText().toString();
            if (account.equals("")) {
                tilAccount.setError(getString(R.string.cannot_be_null));
                tilAccount.setErrorEnabled(true);
                return;
            }
            if (password.equals("")) {
                tilPassword.setError(getString(R.string.cannot_be_null));
                tilPassword.setErrorEnabled(true);
                return;
            }
            listener.onLogin(account, password);
            dismiss();
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }

    public void setListener(LoginListener listener) {
        this.listener = listener;
    }

    public interface LoginListener {
        void onLogin(String account, String password);
    }
}
