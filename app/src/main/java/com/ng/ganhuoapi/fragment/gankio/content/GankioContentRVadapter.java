package com.ng.ganhuoapi.fragment.gankio.content;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.custom.CustomAnimation;
import com.ng.ganhuoapi.data.gankio.GankioInfo;

import java.util.List;

/**
 * 干货适配器
 */

public class GankioContentRVadapter extends BaseMultiItemQuickAdapter<GankioInfo, BaseViewHolder> {
    public GankioContentRVadapter(List<GankioInfo> data) {
        super(data);
        setEnableLoadMore(true);
        //z设置动画
        openLoadAnimation(new CustomAnimation());
        addItemType(GankioInfo.GANK_IO_DAY_ITEM_CUSTOM_NORMAL, R.layout
                .item_gank_io_custom_normal);
        addItemType(GankioInfo.GANK_IO_DAY_ITEM_CUSTOM_IMAGE, R.layout
                .item_gank_io_custom_image);
        addItemType(GankioInfo.GANK_IO_DAY_ITEM_CUSTOM_NO_IMAGE, R.layout
                .item_gank_io_custom_no_image);
    }

    /**
     * 根据实体类GankioInfo 中的temViewType()
     *
     * @param helper
     * @param item
     */
    @Override
    protected void convert(final BaseViewHolder helper, final GankioInfo item) {

        switch (helper.getItemViewType()) {
            case GankioInfo.GANK_IO_DAY_ITEM_CUSTOM_NORMAL:
                helper.setText(R.id.tv_item_title, item.getDesc());
                if (item.getImages() != null) {
                    if (item.getImages().size() > 0 && !TextUtils.isEmpty(item.getImages().get(0)))
                        Glide.with(mContext).load(item.getImages().get(0))
                                .asBitmap()
                                .centerCrop()
                                .into((ImageView) helper.getView(R.id.iv_item_image));
                }
                initTypeImage(helper, item);
                break;
            case GankioInfo.GANK_IO_DAY_ITEM_CUSTOM_IMAGE:
                helper.addOnClickListener(R.id.iv_item_image);
                Glide.with(mContext)
                        .load(item.getUrl())
                        .asBitmap()
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.iv_item_image));

                break;
            case GankioInfo.GANK_IO_DAY_ITEM_CUSTOM_NO_IMAGE:
                initTypeImage(helper, item);
                helper.setText(R.id.tv_item_title, item.getDesc());
                break;
            default:
                break;
        }
    }

    private void initTypeImage(BaseViewHolder helper, GankioInfo item) {
        helper.setText(R.id.tv_item_who_type, item.getType() + "-" + (TextUtils.isEmpty(item.getWho()) ? "佚名" : item.getWho()));
        helper.setText(R.id.tv_item_time, item.getCreatedAt().substring(0, 10));
        switch (item.getType()) {
           /* case "福利":
                helper.setImageResource(R.id.iv_type_item_title, R.drawable
                        .ic_vector_title_welfare);
                break;*/
            case "Android":
                helper.setImageResource(R.id.iv_type_item_title, R.drawable
                        .ic_vector_title_android);
                break;
            case "iOS":
                helper.setImageResource(R.id.iv_type_item_title, R.drawable.ic_vector_title_ios);
                break;
            case "前端":
                helper.setImageResource(R.id.iv_type_item_title, R.drawable.ic_vector_title_front);
                break;
            case "休息视频":
                helper.setImageResource(R.id.iv_type_item_title, R.drawable.ic_vector_title_video);
                break;
            case "瞎推荐":
                helper.setImageResource(R.id.iv_type_item_title, R.drawable.ic_vector_item_tuijian);
                break;
            case "拓展资源":
                helper.setImageResource(R.id.iv_type_item_title, R.drawable.ic_vector_item_tuozhan);
                break;
        }
    }
}
