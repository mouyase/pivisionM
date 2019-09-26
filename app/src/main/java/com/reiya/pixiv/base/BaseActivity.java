package com.reiya.pixiv.base;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by lenovo on 2016/5/31.
 */

public class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected T mPresenter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
