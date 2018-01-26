package com.ng.ganhuoapi.activity.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.custom.CustomAnimation;
import com.ng.ganhuoapi.data.movie.detial.child.PersonBean;
import com.ng.ganhuoapi.util.Public;

import java.util.List;

/**
 * Created by Administrator on 2018/1/8.
 */

public class MBDetailsRVadapter extends BaseQuickAdapter<PersonBean, BaseViewHolder> {
    private Context context;
    private RelativeLayout.LayoutParams layoutParams;

    public MBDetailsRVadapter(Context context, int layoutResId, @Nullable List<PersonBean> data) {
        super(layoutResId, data);
        this.context = context;
        setEnableLoadMore(true);
        openLoadAnimation(new CustomAnimation());
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonBean item) {
        int width = Public.getScreenWidthPixels(context);
        ImageView mb_avatars_img = (ImageView) helper.getView(R.id.mb_avatars_img);
        mb_avatars_img.getLayoutParams().width = width / 4;
        mb_avatars_img.getLayoutParams().height = width / 4;
        TextView avatarName_tv = helper.getView(R.id.avatarName_tv);
        layoutParams = (RelativeLayout.LayoutParams) avatarName_tv.getLayoutParams();
        layoutParams.setMargins(20, width / 20, 0, 0);
        TextView avatarName_post = helper.getView(R.id.avatarName_post);
        layoutParams = (RelativeLayout.LayoutParams) avatarName_post.getLayoutParams();
        layoutParams.setMargins(20, 20, 0, 0);
        if (item != null) {
            String imgUrl = null;
            if (item.getAvatars()!=null){
                if (item.getAvatars().getLarge() != null) {
                    imgUrl = item.getAvatars().getLarge();
                } else if (item.getAvatars().getMedium() != null) {
                    imgUrl = item.getAvatars().getMedium();
                } else if (item.getAvatars().getSmall() != null) {
                    imgUrl = item.getAvatars().getSmall();
                }
            }


            if (imgUrl != null) {
                Glide.with(mContext).load(imgUrl)
                        .asBitmap()
                        .centerCrop()
                        .into(mb_avatars_img);
            } else {
                mb_avatars_img.setImageResource(Public.getNodataLoadingId(mContext));
            }

            avatarName_tv.setText(item.getName());
            avatarName_post.setText(item.getType());

        }


    }
}
