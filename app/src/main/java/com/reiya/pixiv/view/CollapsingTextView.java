package com.reiya.pixiv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/10/15.
 */

public class CollapsingTextView extends LinearLayout {
    private TextView mTextView;
    private TextView mTextView2;
    private boolean mCollapsed = true;

    public CollapsingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_collapsing_text_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView2 = (TextView) findViewById(R.id.textView2);
        mTextView2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCollapsed) {
                    mTextView.setMaxLines(99);
                    mTextView2.setText("收起");
                } else {
                    mTextView.setMaxLines(3);
                    mTextView2.setText("展开");
                }
                mCollapsed = !mCollapsed;
            }
        });
    }

    public void setText(CharSequence text) {
        mTextView.setText(text);
        Log.i("getLineCount", ":" + mTextView.getLineCount());
        if (mTextView.getLineCount() < 3) {
            mTextView2.setVisibility(GONE);
        }
    }
}
