package com.reiya.pixiv.util;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.reiya.pixiv.dialog.FavoriteUserDialog;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;

import rx.Subscriber;
import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/10/1.
 */

public class UserOperation {
    public static void favourite(final FragmentActivity activity, final int id) {
        if (!UserData.isLoggedIn()) {
            Toast.makeText(activity, R.string.please_login_first, Toast.LENGTH_SHORT).show();
            return;
        }

        String defaultType = PreferenceManager.getDefaultSharedPreferences(activity).getString(activity.getString(R.string.key_favorite_user_option), "query");
        if (defaultType.equals("query")) {
            FavoriteUserDialog favoriteUserDialog = new FavoriteUserDialog();
            favoriteUserDialog.setToAdd(new FavoriteUserDialog.ToAdd() {
                @Override
                public void add(String type) {
                    favorite(activity, id, type);
                }
            });
            favoriteUserDialog.show(activity.getSupportFragmentManager(), "FavoriteUser");
        } else {
            favorite(activity, id, defaultType);
        }
    }

    public static void favorite(final Activity activity, int id, String type) {
        if (!UserData.isLoggedIn()) {
            Toast.makeText(activity, R.string.please_login_first, Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkRequest.addFollow(id, type)
                .subscribe(new Subscriber<HttpService.FollowResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(activity, R.string.fail_to_concern, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(HttpService.FollowResponse followResponse) {
                        Toast.makeText(activity, R.string.concern_successfully, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void outFavorite(final Context context, int id) {
        if (!UserData.isLoggedIn()) {
            Toast.makeText(context, R.string.please_login_first, Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkRequest.removeFollow(id)
                .subscribe(new Subscriber<HttpService.FollowResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, R.string.fail_to_remove_concern, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(HttpService.FollowResponse followResponse) {
                        Toast.makeText(context, R.string.removed_concern, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
