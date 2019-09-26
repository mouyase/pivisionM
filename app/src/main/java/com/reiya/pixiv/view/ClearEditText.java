package com.reiya.pixiv.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2015/11/30 0030.
 */
public class ClearEditText extends AutoCompleteWithNothingText implements OnTouchListener, TextWatcher {
    private Drawable mClearTextIcon;
    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_clear_black_24px);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable); //Wrap the drawable so that it can be tinted pre Lollipop
        DrawableCompat.setTint(wrappedDrawable, Color.WHITE);
        mClearTextIcon = wrappedDrawable;
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(), mClearTextIcon.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
//        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
//        setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((AutoCompleteTextView) v).showDropDown();
//            }
//        });
    }

    public void setClearIconVisible(final boolean visible) {
        mClearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]);
    }
//
//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        if (hasFocus) {
//            setClearIconVisible(getText().length() > 0);
//        } else {
//            setClearIconVisible(false);
//        }
//    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getX();
        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setText("");
                requestFocus();
                setClearIconVisible(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (isFocused()) {
            setClearIconVisible(text.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
