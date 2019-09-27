package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reiya.pixiv.adapter.BookmarkTagAddAdapter;
import com.reiya.pixiv.bean.BookmarkTag;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;
import com.reiya.pixiv.util.ItemOperation;
import com.reiya.pixiv.util.Value;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import tech.yojigen.pivisionm.R;

/**
 * Created by zhengyirui on 2017/9/7.
 */

public class BookmarkAddDialog extends DialogFragment {
    private int mId;
    private boolean mIsBookmarked;
    private BookmarkTagAddAdapter mAdapter;
    private OnBookmarkChangedCallback mOnBookmarkChangedCallback;
    private int mTagCount;
    private TextView mTagCountTextView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bookmark_add, null);
        mTagCountTextView = (TextView) view.findViewById(R.id.tv_tag_count);
        mTagCountTextView.setText("0/10");
        View addTagBtn = view.findViewById(R.id.btn_add);
        final EditText tagEditText = (EditText) view.findViewById(R.id.et_tag);
        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = tagEditText.getText().toString();
                if (tag.length() > 0) {
                    tagEditText.setText("");
                    tagCountPlus();
                    mAdapter.addItem(0, new BookmarkTag(tag));
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BookmarkTagAddAdapter(getActivity(), new ArrayList<BookmarkTag>());
        mAdapter.setOnRegisterChangedCallback(new BookmarkTagAddAdapter.OnRegisterChangedCallback() {
            @Override
            public void onChanged(boolean isRegistered) {
                if (isRegistered) {
                    tagCountPlus();
                } else {
                    tagCountMinus();
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

        final SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.switch_compat);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.add_collection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> tags = new ArrayList<>();
                        for (BookmarkTag bookmarkTag : mAdapter.getItems()) {
                            if (bookmarkTag.isRegistered()) {
                                tags.add(bookmarkTag.getName());
                            }
                        }
                        ItemOperation.addBookmark(getActivity(), mId, switchCompat.isChecked() ? Value.PRIVATE : Value.PUBLIC, tags.toArray(new String[0]));
                        mOnBookmarkChangedCallback.onChanged(true);
                    }
                });
        if (mIsBookmarked) {
            builder.setNegativeButton(R.string.remove_collection, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ItemOperation.removeBookmark(getActivity(), mId);
                    if (mOnBookmarkChangedCallback != null) {
                        mOnBookmarkChangedCallback.onChanged(false);
                    }
                }
            });
        }

        NetworkRequest.getBookmarkDetail(mId)
                .subscribe(new Subscriber<HttpService.BookmarkDetailResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpService.BookmarkDetailResponse bookmarkDetailResponse) {
                        List<BookmarkTag> bookmarkTags = bookmarkDetailResponse.getBookmarkDetail().getBookmarkTags();
                        for (BookmarkTag bookmarkTag : bookmarkTags) {
                            if (bookmarkTag.isRegistered()) {
                                tagCountPlus();
                            }
                        }
                        mAdapter.addItems(bookmarkTags);
                        mAdapter.notifyDataSetChanged();
                    }
                });

        return builder.create();
    }

    private void tagCountPlus() {
        mTagCount++;
        mTagCountTextView.setText(mTagCount + "/10");
        if (mTagCount > 10) {
            Toast.makeText(getActivity(), R.string.too_many_tags, Toast.LENGTH_SHORT).show();
        }
    }

    private void tagCountMinus() {
        mTagCount--;
        mTagCountTextView.setText(mTagCount + "/10");
    }

    public void setId(int id) {
        mId = id;
    }

    public void setBookmarked(boolean bookmarked) {
        mIsBookmarked = bookmarked;
    }

    public void setOnBookmarkChangedCallback(OnBookmarkChangedCallback onBookmarkChangedCallback) {
        mOnBookmarkChangedCallback = onBookmarkChangedCallback;
    }

    public interface OnBookmarkChangedCallback {
        void onChanged(boolean isBookmarked);
    }
}
