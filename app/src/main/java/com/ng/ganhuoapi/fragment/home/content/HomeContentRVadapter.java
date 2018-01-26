package com.ng.ganhuoapi.fragment.home.content;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.custom.CustomAnimation;
import com.ng.ganhuoapi.data.home.HomeContentDataBean;
import com.ng.ganhuoapi.util.Public;
import com.ng.ganhuoapi.util.TimeUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeContentRVadapter extends BaseQuickAdapter<HomeContentDataBean, BaseViewHolder> {
    public HomeContentRVadapter(int layoutResId, @Nullable List<HomeContentDataBean> data) {
        super(layoutResId, data);
        setEnableLoadMore(true);
        openLoadAnimation(new CustomAnimation());
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeContentDataBean item) {
        if (item != null) {


            helper.setText(R.id.title_tv, item.getTitle());
            if (item.getVideo_detail_info() != null) {
                String video_image = item.getVideo_detail_info().getDetail_video_large_image().getUrl();

                if (!TextUtils.isEmpty(video_image))
                    helper.setImageResource(R.id.iv_video_image, Public.getNodataLoadingId(mContext));
                    Glide.with(mContext).load(video_image)
                            .asBitmap()
                            .centerCrop()
                            .into((ImageView) helper.getView(R.id.iv_video_image));
            }
            if (null != item.getUser_info()) {
                String avatar_url = item.getUser_info().getAvatar_url();
                if (!TextUtils.isEmpty(avatar_url)) {
                    Glide.with(mContext).load(avatar_url)
                            .asBitmap()
                            .centerCrop()
                            .into((CircleImageView) helper.getView(R.id.avatar_iv));
                }
            }
            helper.setText(R.id.title_tv, item.getTitle());
            String tv_source = item.getSource();
            String tv_comment_count = item.getComment_count() + "评论";
            String tv_datetime = item.getBehot_time() + "";
            if (!TextUtils.isEmpty(tv_datetime)) {
                tv_datetime = TimeUtil.getTimeStampAgo(tv_datetime);
            }
            int video_duration = item.getVideo_duration();
            String min = String.valueOf(video_duration / 60);
            String second = String.valueOf(Math.round(video_duration % 10));
            if (Integer.parseInt(second) < 10) {
                second = "0" + second;
            }
            String tv_video_time = min + ":" + second;
            helper.setText(R.id.user_name, tv_source + " - " + tv_comment_count + " - " + tv_datetime);
            helper.setText(R.id.tv_video_time, tv_video_time);
        }
    }
}
