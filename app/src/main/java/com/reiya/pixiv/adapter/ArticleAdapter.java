package com.reiya.pixiv.adapter;

import android.content.Context;
import android.content.Intent;

import com.reiya.pixiv.article.ArticleActivity;
import com.reiya.pixiv.bean.Article;
import com.reiya.pixiv.image.ImageLoader;

import java.util.List;

import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2016/1/12 0012.
 */
public class ArticleAdapter extends BaseAdapter<Article> {

    public ArticleAdapter(Context context, List<Article> list) {
        super(context, R.layout.item_spotlight_article, list);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(Object item, List list, int position) {
                Intent intent = new Intent(mContext, ArticleActivity.class);
                intent.putExtra("url", ((Article) item).getArticleUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void bindData(ViewHolder holder, Article item, int position) {
        holder.setText(R.id.tvTitle, item.getTitle())
                .loadImage(R.id.iv,
                        ImageLoader.loadImage(mContext, item.getThumbnail())
                                .centerCrop()
                )
                .setOnClickListener(item, position);
    }
//    private void setPalette(final View view, Bitmap bitmap) {
//        Palette.from(bitmap)
//                .generate(new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(Palette palette) {
//                        Palette.Swatch swatch = palette.getMutedSwatch();
//                        if (swatch == null) {
//                            return;
//                        }
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            view.setBackground(new ColorDrawable(swatch.getRgb()));
//                        } else {
//                            view.setBackgroundDrawable(new ColorDrawable(swatch.getRgb()));
//                        }
//                    }
//                });
//
//    }
}
