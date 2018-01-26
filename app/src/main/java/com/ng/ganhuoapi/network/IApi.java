package com.ng.ganhuoapi.network;

import com.ng.ganhuoapi.data.book.BookDetailBean;
import com.ng.ganhuoapi.data.book.BookListBean;
import com.ng.ganhuoapi.data.gankio.Gankio;
import com.ng.ganhuoapi.data.home.HomeContentBean;
import com.ng.ganhuoapi.data.movie.MovieContentBean;
import com.ng.ganhuoapi.data.movie.detial.MovieDetailBean;
import com.ng.ganhuoapi.data.movie.usboxmovie.UsBoxMoveBean;
import com.ng.ganhuoapi.data.video.CommentBean;
import com.ng.ganhuoapi.data.video.VideoContentBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/12/20.
 */

public interface IApi {
    /**
     * 干货api
     * http://gank.io/api/data/Android/10/1
     */
    @GET("api/data/{type}/{limit}/{start}")
    Observable<Gankio> getGanKioCall(@Path("type") String type, @Path("limit") int limit, @Path("start") int start);

    /**
     * @param category     新闻/图片/视频栏目
     * @param maxBehotTime 时间
     * @return
     */
//    @GET("api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13")
    @GET("api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&aid=13")
    Observable<HomeContentBean> getHometContentCall(
            @Query("category") String category,
            @Query("max_behot_time") String maxBehotTime,
            @Query("count") int count);

    @GET("api/news/feed/v62/?iid=12507202490&device_id=37487219424&refer=1&aid=13")
    Observable<HomeContentBean> getHometContentCall2(
            @Query("category") String category,
            @Query("max_behot_time") String maxBehotTime,
            @Query("count") int count);

    /**
     * 获取视频信息
     * 如：http://ib.365yg.com/video/urls/v/1/toutiao/mp4/143736b8929e42b2b1ad297f05794e69?r=1098289868929218&s=4222211407
     * http://ib.365yg.com/video/urls/v/1/toutiao/mp4/视频ID?r=17位随机数&s=加密结果
     */
    @GET
    Observable<VideoContentBean> getVideoResouCall(@Url String url);

    /**
     * 获取评论信息
     * http://is.snssdk.com/article/v53/tab_comments/?group_id=6495904721268638221&offset=0&tab_index=0
     *
     * @param groupId ID
     * @param offset  偏移量
     */
    @GET("article/v53/tab_comments/")
    Observable<CommentBean> getCommentBeanCall(@Query("group_id") String groupId, @Query("offset") int offset);

  /**
     * 头条号主页信息
     * https://is.snssdk.com/user/profile/homepage/v3/json/?media_id=4377795668&to_html=0&source=article_top_author&refer=all
     *
     * @param mediaId 头条号ID
     *//*
    @GET("https://is.snssdk.com/user/profile/homepage/v3/json/?to_html=0&source=article_top_author&refer=all&aid=13")
    Observable<MediaProfileBean> getMediaProfile(
            @Query("media_id") String mediaId);

   //**
     * 获取头条号文章
     * https://is.snssdk.com/pgc/ma/?page_type=1&max_behot_time=1495181160&media_id=52445544609&output=json&is_json=1&count=10&from=user_profile_app&version=2&as=479BB4B7254C150&cp=585DB1871ED64E1
     *
     * @param mediaId      头条号ID
     * @param maxBehotTime 时间轴
     *//*
    @GET("https://is.snssdk.com/pgc/ma/?page_type=1&output=json&is_json=1&count=20&from=user_profile_app&version=2")
    Observable<MultiMediaArticleBean> getMediaArticle(
            @Query("media_id") String mediaId,
            @Query("max_behot_time") String maxBehotTime,
            @Query("as") String as,
            @Query("cp") String cp);
    /**
     * 获取头条号视频
     * https://is.snssdk.com/pgc/ma/?page_type=0&max_behot_time=1495181160&media_id=52445544609&output=json&is_json=1&count=10&from=user_profile_app&version=2&as=479BB4B7254C150&cp=585DB1871ED64E1
     *
     * @param mediaId      头条号ID
     * @param maxBehotTime 时间轴
     */
   /* @GET("https://is.snssdk.com/pgc/ma/?page_type=0&output=json&is_json=1&count=10&from=user_profile_app&version=2")
    Observable<MultiMediaArticleBean> getMediaVideo(
            @Query("media_id") String mediaId,
            @Query("max_behot_time") String maxBehotTime,
            @Query("as") String as,
            @Query("cp") String cp);*/




    /**
     * 书籍类api
     *
     * @param tag   根据tag （文学 文化 生活）
     * @param start
     * @param count count最大为100，大于100的count会被置为100。
     * @return
     */
    @GET("v2/book/search")
    Observable<BookListBean> getBookListCall(@Query("tag") String tag, @Query("start") int
            start, @Query("count") int count);

    @GET("v2/book/{id}")
    Observable<BookDetailBean> getBookDetailCall(@Path("id") String id);
    /**
     * 获取电影详情
     *
     * @param id 电影bean里的id
     */
    @GET("v2/movie/subject/{id}")
    Observable<MovieDetailBean> getMovieDetail(@Path("id") String id);

    /**
     * movie 豆瓣
     * 正在热映 tab
     */
    @GET("v2/movie/in_theaters")
    Observable<MovieContentBean> getHotMovieCall();

    /**
     * 即将上映
     */
    @GET("v2/movie/coming_soon")
    Observable<MovieContentBean> getCommingSoonMovieCall();

    /**
     * Top 250
     */
    @GET("v2/movie/top250")
    Observable<MovieContentBean> getTop250MovieCall(@Query("start") int start, @Query("count") int count);

    /**
     * 热榜
     */
    @GET("v2/movie/weekly")
    Observable<MovieContentBean> getWeeklyMovieCall();

    /**
     * 北美票房榜
     */
    @GET("v2/movie/us_box")
    Observable<UsBoxMoveBean> getUsBoxMovieCall();

    /**
     * 新片榜
     */
    @GET("v2/movie/new_movies")
    Observable<MovieContentBean> getNewMoviesCall();
}
