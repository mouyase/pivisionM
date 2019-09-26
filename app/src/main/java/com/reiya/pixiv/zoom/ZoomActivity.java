package com.reiya.pixiv.zoom;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.image.ImageLoader;

public class ZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        final ImageView iv = (ImageView) findViewById(R.id.iv);

        String url = getIntent().getStringExtra("url");
        ImageLoader.loadImage(this, url)
                .fitCenter()
                .thumbnail(0.1f)
                .load(iv);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
