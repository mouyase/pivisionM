package com.reiya.pixiv.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * Created by lenovo on 2016/5/29.
 */

public class ImageLoader {
    public static Builder loadImage(Context context, String url) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Referer", url)
                .build());
        return new Builder(GlideApp.with(context).load(glideUrl));
    }

    public static Builder loadImage(Context context, File file) {
        return new Builder(GlideApp.with(context).load(file));
    }

    public static Builder loadImageFromCache(Context context, String url) {
        return new Builder(GlideApp.with(context)
                .load(url).onlyRetrieveFromCache(true));
    }

    public static void download(Context context, String url, SimpleTarget<File> target) {
        Glide.with(context).load(url).downloadOnly(target);
    }

    public static class Builder {
        GlideRequest mRequest;

        public Builder(GlideRequest request) {
            mRequest = request;
        }

        public Builder centerCrop() {
            mRequest.centerCrop();
            return this;
        }

        public Builder fitCenter() {
            mRequest.fitCenter();
            return this;
        }

        public Builder thumbnail(float sizeMultiplier) {
            mRequest.thumbnail(sizeMultiplier);
            return this;
        }

        public void load(ImageView imageView) {
            mRequest.into(imageView);
        }
    }
}
