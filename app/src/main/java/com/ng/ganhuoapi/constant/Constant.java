package com.ng.ganhuoapi.constant;

/**
 * 常量
 */

public class Constant {
    //http://v.juhe.cn/weixin/query?key=67d0a9b310379e16f1b47c67537f769d
    public static final int limit = 10;

    public static int start = 1;
    public static final String WEIXIN_APPKEY = "67d0a9b310379e16f1b47c67537f769d";
    public static final String GANKIO_HOSTURL = "http://gank.io/";
    public static final String HOMECONTENT_HOSTURL = "http://is.snssdk.com/";
    public static final String HOMECONTENT_HOSTURL2 = "http://lf.snssdk.com/";
    public final static String DOUBAN_HOSTURL = "Https://api.douban.com/";
    public final static String TOUTIAO_HOSTURL = "http://toutiao.com/";

    public static final String WEB_VIEW_LOAD_URL = "arg_key_web_view_load_url";
    public static final String WEB_VIEW_LOAD_TITLE = "arg_key_web_view_load_title";
    public static final String key_BITMAP_TRANSTION="key_bitmap_transtion";
    public static final String  key_BITMAP_WHO="key_bitmap_who";
    public static final String  KEY_HOME_DATA="key_home_data";
    public static final String  KEY_HOME_USER_IMGURL="key_home_user_img_url";
    public static final String  KEY_IS_GET_MORECOMMENTDATA="key_is_getMoreCommentData";
    public static final String  KEY_MOVIE_ID="key_of_movie_id";
    public static final String  KEY_MOVIE_IMGURL="key_movie_img_url";
    public static final String  KEY_BOOK_ITEM_BEAN="key_of_book_item_bean";
    public static final String KEY_MOVIE_TITLE ="key_movie_title" ;

    public static final String KEY_FROM_HOMEVIDEO ="key_from_homevidep" ;
    public static final String KEY_NO_FROM_HOMEVIDEO ="no_frome_home_video" ;
    public static final String KEY_IS_FROM_HOMEVIDEO ="frome_home_video" ;
    public static final String KEY_CurrentPositionWhenPlaying="CurrentPositionWhenPlaying" ;
    public static final String KEY_CurrentUrl="CurrentUrl" ;

    /**
     *
     /**
     * 豆瓣热映电影，每日更新
     * https://api.douban.com/v2/movie/in_theaters
     */
    /**
     * 获取电影详情
     *https://api.douban.com/v2/movie/subject/25941842
     * @param id 电影bean里的id
     * @GET("v2/movie/subject/{id}") Observable<MovieDetailBean> getMovieDetail(@Path("id") String id);
     */

/**
 * 获取豆瓣电影top250
 * https://api.douban.com/v2/movie/top250
 *
 @GET("v2/movie/top250") Observable<HotMovieBean> getMovieTop250(@Query("start") int start, @Query("count") int count);
 */
    /**
     * 根据tag获取图书
     *
     * @param tag   搜索关键字
     * @param start 从多少开始，如从"0"开始
     * @param count 一次请求的数目 最多100

    https://api.douban.com/v2/book/search?tag=文学
    @GET("v2/book/search")
    Observable<BookListBean> getBookListWithTag(@Query("tag") String tag, @Query("start") int
            start, @Query("count") int count);

    @GET("v2/book/{id}")
    Observable<BookDetailBean> getBookDetail(@Path("id") String id);
     */

    /**
     * 获取某几日干货网站数据:

     http://gank.io/api/history/content/2/1
     */

/**
 * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
 请求个数： 数字，大于0
 第几页：数字，大于0
 例：
 http://gank.io/api/data/Android/10/1
 http://gank.io/api/data/福利/10/1
 http://gank.io/api/data/iOS/20/2
 http://gank.io/api/data/all/20/2
 每日数据： http://gank.io/api/day/年/月/日

 例：
 http://gank.io/api/day/2015/08/06
 随机数据：http://gank.io/api/random/data/分类/个数

 数据类型：福利 | Android | iOS | 休息视频 | 拓展资源 | 前端
 个数： 数字，大于0
 例：
 http://gank.io/api/random/data/Android/20
 */

    /**
     * 头条号主页信息
     * https://is.snssdk.com/user/profile/homepage/v3/json/?media_id=4377795668&to_html=0&source=article_top_author&refer=all
     *
     * @param mediaId 头条号ID
     */
    /**
     * 获取头条号文章
     * https://is.snssdk.com/pgc/ma/?page_type=1&max_behot_time=1495181160&media_id=52445544609&output=json&is_json=1&count=10&from=user_profile_app&version=2&as=479BB4B7254C150&cp=585DB1871ED64E1
     *
     * @param mediaId      头条号ID
     * @param maxBehotTime 时间轴
     */
    /**
     * 获取头条号视频
     * https://is.snssdk.com/pgc/ma/?page_type=0&max_behot_time=1495181160&media_id=52445544609&output=json&is_json=1&count=10&from=user_profile_app&version=2&as=479BB4B7254C150&cp=585DB1871ED64E1
     *
     * @param mediaId      头条号ID
     * @param maxBehotTime 时间轴
     */
    /**
     * 获取头条号问答
     * https://is.snssdk.com/wenda/profile/wendatab/brow/?other_id=6619635172&format=json&from_channel=media_channel
     *
     * @param mediaId 头条号ID
     */
    /**
     * 获取头条号问答(加载更多)
     * http://is.snssdk.com/wenda/profile/wendatab/loadmore/?other_id=53294853357&format=json&from_channel=media_channel&cursor=6428177292098273538&count=10&offset=undefined
     *
     * @param mediaId 头条号ID
     * @param cursor  偏移量
     */
    /**
     * 获取头条号动态
     * https://is.snssdk.com/dongtai/list/v11/?user_id=6619635172&max_cursor=1494916016999
     *
     * @param mediaId   头条号ID
     * @param maxCursor 偏移量
     */
    /**
     * 获取个性化新闻
     * 深圳 http://is.snssdk.com/api/news/feed/v58/?iid=5034850950&device_id=6096495334&category=news_society
     * 深圳 http://lf.snssdk.com/api/news/feed/v58/?iid=12507202490&device_id=37487219424&category=news_society
     * 天津 http://ib.snssdk.com/api/news/feed/v58/?
     * 北京 http://iu.snssdk.com/api/news/feed/v58/?
     *
     * @param iid      用户ID
     * @param deviceId 设备ID
     * @param category 新闻/图片/视频栏目
     */
    /**
     * 获取新闻评论
     * 按热度排序
     * http://is.snssdk.com/article/v53/tab_comments/?group_id=6314103921648926977&offset=0&tab_index=0
     * 按时间排序
     * http://is.snssdk.com/article/v53/tab_comments/?group_id=6314103921648926977&offset=0&tab_index=1
     *
     * @param groupId 新闻ID
     * @param offset  偏移量
     */
}
