package com.reiya.pixiv.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.reiya.pixiv.bean.User;
import com.reiya.pixiv.collection.FavoriteWorksActivity;
import com.reiya.pixiv.concern.FavoriteUsersActivity;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/4/4.
 */
public class UserData {
    public static User user;
    private static String bearer;
    private static boolean mSpecialMode = false;
    private static UserState mUserState = new DefaultState();

    public static boolean isLoggedIn() {
        return bearer != null;
    }

    public static String getBearer() {
        return bearer;
    }

    public static void setBearer(String token) {
        bearer = "Bearer " + token;
    }

    public static boolean getSpecialMode() {
        return mSpecialMode;
    }

    public static void setSpecialMode(boolean b) {
        mSpecialMode = b;
    }

    public static void openCollection(Context context) {
        mUserState.openCollection(context);
    }

    public static void openConcern(Context context) {
        mUserState.openConcern(context);
    }

    public static void openNewWorks(Context context) {
        mUserState.openNewWorks(context);
    }

    public static void setLoggedInState() {
        mUserState = new LoggedInState();
    }

    interface UserState {
        void openCollection(Context context);

        void openConcern(Context context);

        void openNewWorks(Context context);
    }

    private static class DefaultState implements UserState {

        @Override
        public void openCollection(Context context) {
            showToast(context);
        }

        @Override
        public void openConcern(Context context) {
            showToast(context);
        }

        @Override
        public void openNewWorks(Context context) {
            showToast(context);
        }

        void showToast(Context context) {
            Toast.makeText(context, R.string.please_login_first, Toast.LENGTH_SHORT).show();
        }
    }

    private static class LoggedInState implements UserState {

        @Override
        public void openCollection(Context context) {
            Intent intent = new Intent(context, FavoriteWorksActivity.class);
            context.startActivity(intent);
        }

        @Override
        public void openConcern(Context context) {
            Intent intent = new Intent(context, FavoriteUsersActivity.class);
            context.startActivity(intent);
        }

        @Override
        public void openNewWorks(Context context) {
//            Intent intent = new Intent(context, SubscriptionActivity.class);
//            context.startActivity(intent);
        }
    }
}
