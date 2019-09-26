package com.reiya.pixiv.work;

import android.content.Context;

import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.db.RecordDAO;
import com.reiya.pixiv.util.Serializer;

/**
 * Created by lenovo on 2016/5/31.
 */

public class WorkPresenter extends WorkContract.Presenter {

    @Override
    void saveRecord(Context context, Work work) {
        String record = new RecordDAO(context).getContent(work.getId());
        if (record.equals("")) {
            new RecordDAO(context).addRecord(work.getId(), Serializer.serialize(work), System.currentTimeMillis());
        } else {
            new RecordDAO(context).updateRecord(work.getId(), System.currentTimeMillis());
        }
    }
}
