package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.text.Html;

import com.reiya.pixiv.comment.CommentActivity;
import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.bean.Work;

/**
 * Created by Administrator on 2015/11/26 0026.
 */
public class DetailDialog extends DialogFragment {
    private Work mWork;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence info = mWork.getTitle() + " (" + mWork.getId() + ")\n"
                + mWork.getUser().getName() + " (" + mWork.getUser().getId() + ")\n"
                + mWork.getTime() + "\n\n" + Html.fromHtml(mWork.getCaption());
        builder.setMessage(info)
            .setPositiveButton(getString(R.string.positive), null)
            .setNegativeButton(getString(R.string.show_comments), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getContext(), CommentActivity.class);
                    intent.putExtra("id", mWork.getId());
                    getActivity().startActivity(intent);
                }
            });
        return builder.create();
    }

    public void setItem(Work work) {
        mWork = work;
    }
}
