package com.reiya.pixiv.work;

import android.content.Context;

import com.reiya.pixiv.base.BasePresenter;
import com.reiya.pixiv.base.BaseView;
import com.reiya.pixiv.bean.Work;

/**
 * Created by lenovo on 2016/5/31.
 */

public interface WorkContract {
    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void saveRecord(Context context, Work work);
    }
}
