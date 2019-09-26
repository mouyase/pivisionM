package com.reiya.pixiv.adapter;

import android.content.Context;
import android.content.Intent;

import com.reiya.pixiv.profile.ProfileActivity;
import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.bean.UserPreview;
import com.reiya.pixiv.image.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2015/12/16 0016.
 */
public class UserAdapter extends BaseAdapter<UserPreview> {

    public UserAdapter(Context context, List<UserPreview> userPreviews) {
        super(context, R.layout.item_profile, userPreviews);
        setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(Object item, List list, int position) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("id", ((UserPreview) item).getUser().getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void bindData(ViewHolder holder, UserPreview item, int position) {
        holder.loadImage(R.id.ivProfile,
                ImageLoader.loadImage(mContext, item.getUser().getMediumImageUrl())
                        .centerCrop()
                )
                .setText(R.id.tvName, item.getUser().getName())
                .setOnClickListener(item, position)
                .setOnLongClickListener(item);
    }
}
