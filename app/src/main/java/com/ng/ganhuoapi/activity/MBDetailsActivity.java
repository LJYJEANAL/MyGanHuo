package com.ng.ganhuoapi.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ng.ganhuoapi.R;
import com.ng.ganhuoapi.activity.adapter.MBDetailsRVadapter;
import com.ng.ganhuoapi.constant.Constant;
import com.ng.ganhuoapi.data.book.BookItemBean;
import com.ng.ganhuoapi.data.movie.detial.MovieDetailBean;
import com.ng.ganhuoapi.data.movie.detial.child.PersonBean;
import com.ng.ganhuoapi.network.IApi;
import com.ng.ganhuoapi.network.OkHttpCreateHelper;
import com.ng.ganhuoapi.util.DisplayUtils;
import com.ng.ganhuoapi.util.Public;
import com.ng.ganhuoapi.util.SettingUtil;
import com.ng.ganhuoapi.util.StatusBarUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MBDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_book_photo)
    ImageView iv_book_photo;
    @BindView(R.id.iv_header_bg)
    ImageView iv_header_bg;
    @BindView(R.id.info_relayout)
    RelativeLayout info_relayout;
    @BindView(R.id.collapsing_Layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.info_text)
    TextView info_text;

    @BindView(R.id.mb_aka_layout)
    LinearLayout mb_aka_layout;
    @BindView(R.id.tv_book_aka)
    TextView aka_tv;
    @BindView(R.id.tv_book_author_intro)
    TextView tvBookAuthorIntro;

    @BindView(R.id.mb_summary_layout)
    LinearLayout summary_layout;
    @BindView(R.id.summary_tv)
    TextView summary_tv;
    @BindView(R.id.tv_book_summary)
    TextView tvBookSummary;

    @BindView(R.id.mb_avatars_layout)
    LinearLayout mb_avatars_layout;
    @BindView(R.id.avatars_tv)
    TextView avatars_tv;
    @BindView(R.id.tv_book_detail)
    TextView tvBookDetail;

    @BindView(R.id.mb_recyclerView)
    RecyclerView recyclerView;

    private String titleLoad;
    private String altUrl;

    private void setLayoutShowOrHind(int VISIBLE) {
        mb_aka_layout.setVisibility(VISIBLE);
        summary_layout.setVisibility(VISIBLE);
        mb_avatars_layout.setVisibility(VISIBLE);
        recyclerView.setVisibility(VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!SettingUtil.getInstance().getIsDayOrNighTheme()){
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }
        }
        setContentView(R.layout.activity_mbdetails);

        unbinder = ButterKnife.bind(this);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) info_relayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, toolbar.getLayoutParams().height + StatusBarUtils.getStatusBarHeight(this));
        final BookItemBean bookItemBean = (BookItemBean) getIntent().getSerializableExtra(Constant.KEY_BOOK_ITEM_BEAN);

        final String movieId = getIntent().getStringExtra(Constant.KEY_MOVIE_ID);
        String largeImage = getIntent().getStringExtra(Constant.KEY_MOVIE_IMGURL);
        String movieTitle = getIntent().getStringExtra(Constant.KEY_MOVIE_TITLE);
        if (bookItemBean != null) {
            altUrl = bookItemBean.getAlt();
            titleLoad = bookItemBean.getTitle();
            showBookDetail(bookItemBean);
        } else if (movieId != null && largeImage != null && movieTitle != null) {
            toolbar.setTitle(movieTitle);
            Glide.with(this).load(largeImage).asBitmap().into(iv_book_photo);
            DisplayUtils.displayBlurImg(this, largeImage, iv_header_bg);
            showMovieDetail(movieId);
        }
        collapsingToolbarLayout.setOnClickListener(this);
        tvBookAuthorIntro.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(MBDetailsActivity.this);
            }
        });


    }

    private Disposable disposable;

    private void showMovieDetail(final String movieId) {
        iv_book_photo.setImageResource(Public.getNodataLoadingId(this));


        io.reactivex.Observable<MovieDetailBean> beanObservable = OkHttpCreateHelper.createApi(IApi.class, Constant.DOUBAN_HOSTURL).getMovieDetail(movieId);
        if (beanObservable != null) {
            beanObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MovieDetailBean>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(MovieDetailBean movieDetailBean) {
                            for (PersonBean bean1 : movieDetailBean.getCasts()) {
                                bean1.setType("主演");
                            }
                            for (PersonBean bean2 : movieDetailBean.getDirectors()) {
                                bean2.setType("导演");
                            }
                            showMovieDetail(movieDetailBean);
                        }

                        @Override
                        public void onError(Throwable e) {
//                            ActivityCompat.finishAfterTransition(MBDetailsActivity.this);
                            Snackbar snackbar = Snackbar.make(recyclerView, e.toString(), Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            snackbar.show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void showMovieDetail(MovieDetailBean movieDetailBean) {

        if (movieDetailBean != null) {
            setLayoutShowOrHind(View.VISIBLE);
            altUrl = movieDetailBean.getAlt();
            titleLoad = movieDetailBean.getTitle();

            String start = "";
            if (movieDetailBean.getCasts().size() > 0 & movieDetailBean.getCasts() != null) {
                for (int i = 0; i < movieDetailBean.getCasts().size(); i++) {
                    start = start + movieDetailBean.getCasts().get(i).getName();
                    if (i < movieDetailBean.getCasts().size() - 1) {
                        start += "/";
                    }
                }
            }
            String moviceType = "";
            if (movieDetailBean.getGenres().size() > 0 & movieDetailBean.getGenres() != null) {
                for (int i = 0; i < movieDetailBean.getGenres().size(); i++) {
                    moviceType = moviceType + movieDetailBean.getGenres().get(i);
                    if (i < movieDetailBean.getGenres().size() - 1) {
                        moviceType += "/";
                    }
                }
            }
            String akaName = "";
            if (movieDetailBean.getAka().size() > 0 & movieDetailBean.getAka() != null) {
                for (int i = 0; i < movieDetailBean.getAka().size(); i++) {
                    akaName = akaName + movieDetailBean.getAka().get(i);
                    if (i < movieDetailBean.getAka().size() - 1) {
                        akaName += "\n";
                    }
                }
            }
            info_text.setText("导演：" + movieDetailBean.getDirectors().get(0).getName() + "\n评分：" + movieDetailBean.getRating().getAverage() + "\n" +
                    movieDetailBean.getRatings_count() + "人评分" + "\n主演：" + start + "\n类型：" + moviceType + "\n上映日期：" + movieDetailBean.getYear() + "\n制片国家/地区：" + movieDetailBean.getCountries());
            aka_tv.setText("别名：");
            tvBookAuthorIntro.setText(akaName);
            summary_tv.setText("剧情简介：");
            tvBookSummary.setText(movieDetailBean.getSummary());
            avatars_tv.setText("导演 & 演员");
            tvBookDetail.setVisibility(View.GONE);
            List<PersonBean> list = movieDetailBean.getDirectors();
            list.addAll(movieDetailBean.getCasts());
            MBDetailsRVadapter mbDetailsRVadapter = new MBDetailsRVadapter(this, R.layout.item_mb_directors_avatars, list);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mbDetailsRVadapter);
            mbDetailsRVadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    PersonBean personBean = (PersonBean) adapter.getItem(position);
                    Intent intent = new Intent(MBDetailsActivity.this, GanWebDetailsActivity.class);
                    intent.putExtra(Constant.WEB_VIEW_LOAD_URL, personBean.getAlt());
                    intent.putExtra(Constant.WEB_VIEW_LOAD_TITLE, personBean.getName());
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MBDetailsActivity.this).toBundle());
                }
            });

        }


    }

    private void showBookDetail(BookItemBean bookItemBean) {
        if (bookItemBean != null) {
            setLayoutShowOrHind(View.VISIBLE);
            aka_tv.setText("作者简介：");
            tvBookAuthorIntro.setText(bookItemBean.getAuthor_intro());
            summary_tv.setText("摘要：");
            tvBookSummary.setText(bookItemBean.getSummary());
            avatars_tv.setText("目录：");
            tvBookDetail.setText(bookItemBean.getCatalog());
            toolbar.setTitle(bookItemBean.getTitle());
            info_text.setText("作者：" + bookItemBean.getAuthorsString() + "\n评分：" + bookItemBean.getRating().getAverage() + "\n" +
                    bookItemBean.getRating().getNumRaters() + "人评分" + "\n价格：" + bookItemBean.getPrice() + "\n出版社：" + bookItemBean.getPublisher() + "\n出版日期：" + bookItemBean.getPubdate()
            );
            iv_book_photo.setImageResource(Public.getNodataLoadingId(this));

            Glide.with(this).load(bookItemBean.getImages().getLarge()).asBitmap().into(iv_book_photo);
            DisplayUtils.displayBlurImg(this, bookItemBean.getImages().getLarge(), iv_header_bg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (disposable != null)
            disposable.dispose();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_book_author_intro:
            case R.id.collapsing_Layout:
            case R.id.mb_summary_layout:
                Intent intent = new Intent(MBDetailsActivity.this, GanWebDetailsActivity.class);
                if (altUrl != null && titleLoad != null) {
                    intent.putExtra(Constant.WEB_VIEW_LOAD_URL, altUrl);
                    intent.putExtra(Constant.WEB_VIEW_LOAD_TITLE, titleLoad);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MBDetailsActivity.this).toBundle());
                }
                break;
        }
    }
}
