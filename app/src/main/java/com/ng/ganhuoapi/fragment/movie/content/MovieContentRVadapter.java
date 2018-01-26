package com.ng.ganhuoapi.fragment.movie.content;

import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.custom.CustomAnimation;
import com.ng.ganhuoapi.data.movie.SubItemsBean;
import com.ng.ganhuoapi.util.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */

public class MovieContentRVadapter extends BaseMultiItemQuickAdapter<SubItemsBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MovieContentRVadapter(List<SubItemsBean> data) {
        super(data);
        setEnableLoadMore(false);
        //设置动画
        openLoadAnimation(new CustomAnimation());
        addItemType(SubItemsBean.MOVIE_VIEW_NORMAL, R.layout
                .item_book_content_recyview);
        addItemType(SubItemsBean.MOVIE_VIEW_CUSTOM, R.layout
                .item_movie_custom_recyview);
    }

    @Override
    protected void convert(BaseViewHolder helper, SubItemsBean item) {
        initTypeImage(helper, item);
    }

    private void initTypeImage(BaseViewHolder helper, SubItemsBean item) {
        String start = "";
        String moviceType = "";
        switch (helper.getItemViewType()) {
            case SubItemsBean.MOVIE_VIEW_NORMAL:
                //避免空指针异常
                CardView cardView = helper.getView(R.id.cardView);
                ImageView iv_item_image = helper.getView(R.id.iv_item_image);
                cardView.setTag(iv_item_image);
                Glide.with(mContext).load(item.getImages().getLarge()).crossFade().into(iv_item_image);
                helper.setText(R.id.tv_item_title, item.getTitle());

                for (int i = 0; i < item.getCasts().size(); i++) {
                    start = start + item.getCasts().get(i).getName();
                    if (i < item.getCasts().size() - 1) {
                        start += "/";
                    }

                }
                for (int i = 0; i < item.getGenres().length; i++) {
                    moviceType = moviceType + item.getGenres()[i];
                    if (i < item.getGenres().length - 1) {
                        moviceType += "/";
                    }
                }
                if (item.getDirectors().size()>0&& !TextUtils.isEmpty(item.getDirectors().get(0).getName())){
                    helper.setText(R.id.tv_item_author, "导演：" + item.getDirectors().get(0).getName() + "\n");
                }
                helper.setText(R.id.tv_item_rate, "主演：" + start + "\n" + "类型：" + moviceType);
                helper.setText(R.id.tv_item_summary, "年份：" + item.getYear() + "\n" + "评分：" + String.valueOf(item.getRating().getAverage()));
                break;
            case SubItemsBean.MOVIE_VIEW_CUSTOM:

                try {
                    //避免空指针异常
                    if (StringUtils.isEmpty(item.getImages().getLarge()))
                        return;
                    CardView customCardView = helper.getView(R.id.cardView);
                  final   ImageView movie_poster = helper.getView(R.id.movie_poster);
                    movie_poster.post(new Runnable() {
                        @Override
                        public void run() {
                            int measuredWidth=movie_poster.getMeasuredWidth();
//                            Log.i("信息")
                        }
                    });
                    customCardView.setTag(movie_poster);
                    Glide.with(mContext).load(item.getImages().getLarge()).crossFade().into(movie_poster);
                    helper.setText(R.id.movie_title, item.getTitle());
                    helper.setText(R.id.movie_rate, "评分：" + String.valueOf(item.getRating().getAverage()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
