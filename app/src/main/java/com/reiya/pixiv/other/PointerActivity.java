package com.reiya.pixiv.other;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.reiya.pixiv.profile.ProfileActivity;
import com.reiya.pixiv.work.ViewActivity;

public class PointerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = getIntent().getDataString();
        Intent intent;
        if (url.contains("mode=medium")) {
            intent = new Intent(this, ViewActivity.class);
        } else {
            intent = new Intent(this, ProfileActivity.class);
        }
        intent.setData(Uri.parse(url));
        startActivity(intent);
        finish();
    }
}
