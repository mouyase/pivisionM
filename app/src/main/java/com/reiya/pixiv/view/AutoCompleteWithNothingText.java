package com.reiya.pixiv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by Administrator on 2015/11/30 0030.
 */
public class AutoCompleteWithNothingText extends AutoCompleteTextView {
    public AutoCompleteWithNothingText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }
}
