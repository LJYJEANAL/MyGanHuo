package com.ng.ganhuoapi.fragment.book.content;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.custom.CustomAnimation;
import com.ng.ganhuoapi.data.book.BookItemBean;
import com.ng.ganhuoapi.util.Public;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25.
 */

public class BookContentRVadapter extends BaseQuickAdapter<BookItemBean, BaseViewHolder> {

    private BaseViewHolder helper;
    public BookContentRVadapter(int layoutResId, @Nullable List<BookItemBean> data) {
        super(layoutResId, data);
        setEnableLoadMore(true);
        //z设置动画
        openLoadAnimation(new CustomAnimation());
    }



    @Override
    protected void convert(BaseViewHolder helper, BookItemBean item) {
        helper.addOnClickListener(R.id.cardView);
        CardView cardView = helper.getView(R.id.cardView);
        ImageView iv_item_image = helper.getView(R.id.iv_item_image);
        cardView.setTag(iv_item_image);
        helper.setText(R.id.tv_item_title, "《" + item.getTitle() + "》");
        helper.setText(R.id.tv_item_author, "作者：" + item.getAuthorsString());
        helper.setText(R.id.tv_item_rate, "评分：" + item.getRating().getAverage());
        TextView tv_item_summary = (TextView) helper.getView(R.id.tv_item_summary);
        tv_item_summary.setEllipsize(TextUtils.TruncateAt.END);
        tv_item_summary.setSingleLine(true);
        tv_item_summary.setText("简介：" + item.getSummary());
        iv_item_image.setImageResource(Public.getNodataLoadingId(mContext));
        Glide.with(mContext).load(item.getImage()).crossFade(300).into(iv_item_image);

    }
}
