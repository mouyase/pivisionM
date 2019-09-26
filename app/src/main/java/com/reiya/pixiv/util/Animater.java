package com.reiya.pixiv.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/2/6.
 */
public class Animater {
    public static void fadeIn(final View view) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);

        animation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
        });

        view.startAnimation(animation);
    }

    public static void fadeOut(final View view) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out);

        animation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }
        });

        view.startAnimation(animation);
    }

    public static class AnimationListenerAdapter implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }
}
