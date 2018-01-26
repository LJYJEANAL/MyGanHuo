package com.ng.ganhuoapi.activity.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.custom.CustomAnimation;
import com.ng.ganhuoapi.data.video.CommentBean;
import com.ng.ganhuoapi.data.video.CommentData;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */

public class VideoCommentRVadapter extends BaseQuickAdapter <CommentBean.BeanData,BaseViewHolder>{
    public VideoCommentRVadapter(int layoutResId, @Nullable List<CommentBean.BeanData> data) {
        super(layoutResId, data);
        setEnableLoadMore(true);
        openLoadAnimation(new CustomAnimation());
    }

    @Override
    protected void convert(BaseViewHolder helper,CommentBean.BeanData item) {
        if (item!=null){
            CommentData commentData=item.getComment();
            if (commentData!=null){
                if (commentData.getUser_profile_image_url()!=null){
                    Glide.with(mContext).load(commentData.getUser_profile_image_url())
                            .asBitmap()
                            .centerCrop()
                            .into((ImageView) helper.getView(R.id.circle_iv));
                }
                helper.setText(R.id.userName_tv,item.getComment().getUser_name());
                helper.setText(R.id.digg_tv,item.getComment().getReply_count()+"赞");
                helper.setText(R.id.comment_tv,item.getComment().getText());
            }


        }

    }
}
