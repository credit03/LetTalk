package com.lettalk.gy.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.lettalk.gy.bean.ClickLike;
import com.lettalk.gy.bean.Comment;
import com.lettalk.gy.bean.FileDir;
import com.lettalk.gy.bean.FindBag;
import com.lettalk.gy.bean.Post;
import com.lettalk.gy.bean.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016-07-06.
 */
public class FindModel extends BaseModel {


    private static final String TAG = "FindModel";
    private static FindModel instance = null;


    /**
     * 安全创建单例
     *
     * @return
     */
    public static synchronized FindModel getInstance() {
        if (instance == null) {
            instance = new FindModel();
        }
        return instance;
    }

    /**
     * 对象为空
     */
    private static final int NULL_ERROR = 0x404;

    /**
     * 发表说说，添加一对一关联
     *
     * @param u
     * @param content
     * @param haveiamge
     * @param context
     */

    public void BindPostOneTone(User u, String content, Boolean haveiamge, Context context) {
        BindPostOneTone(u, content, haveiamge, context, null);
    }


    /**
     * 发表说说，添加一对一关联
     *
     * @param u
     * @param content
     * @param haveiamge
     * @param context
     * @param listener
     */
    public void BindPostOneTone(User u, String content, Boolean haveiamge, Context context, SaveListener listener) {
        if (u == null) {
            if (listener != null) {
                listener.onFailure(NULL_ERROR, "User Class Is Null");
            }

            return;
        }

        // 创建帖子信息
        Post post = new Post();
        post.setContent(content);
        post.setHaveimage(haveiamge);
        //添加一对一关联
        post.setAuthor(u);
        BindPostOneTone(post, context, listener);
    }

    /**
     * 发表说说，添加一对一关联
     *
     * @param post
     * @param context
     * @param listener
     */
    public void BindPostOneTone(Post post, Context context, SaveListener listener) {
        if (post == null) {
            if (listener != null) {
                listener.onFailure(NULL_ERROR, "Post Class Is Null");
            }
            return;
        }
        post.save(context, listener);
    }

    public void BindPostOneTone(String content, User u, Context context, SaveListener listener) {
        // 创建帖子信息
        Post post = new Post();
        post.setContent(content);
        //添加一对一关联
        post.setAuthor(u);
        BindPostOneTone(post, context, listener);

    }

    /**
     * 查询说说
     *
     * @param u
     * @param context
     * @param listener
     */
    public void QueryPostOneToOne(User u, Context context, FindListener<Post> listener) {
        if (u == null) {

            if (listener != null) {
                listener.onError(NULL_ERROR, "User Class Is Null");
            }
            return;
        }

        BmobQuery<Post> query = new BmobQuery<Post>();
        query.addWhereEqualTo("author", u);    // 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(context, listener);
    }

    /**
     * 更新说说，一对一关联
     *
     * @param u
     * @param postId
     * @param context
     * @param listener
     */
    public void UpdatePostOneToOne(User u, String postId, Context context, UpdateListener listener) {

        if (u == null) {

            if (listener != null) {
                listener.onFailure(NULL_ERROR, "User Class Is Null");
            }
            return;
        }
        Post p = new Post();
        //构造用户B，如果你知道用户B的objectId的话，可以使用这种方式进行关联，如果不知道的话，你需要将用户B查询出来
        // 这里假设已知用户B的objectId为aJyG2224
        p.setObjectId(postId);
        p.setAuthor(u);//重新设置帖子作者
        p.update(context, listener);

    }

    /**
     * 删除关联，一对一关联
     *
     * @param Postid
     * @param context
     * @param listener
     */
    public void DeletePostOneToOne(String Postid, Context context, UpdateListener listener) {
        Post p = new Post();
        p.remove("author");
        p.setObjectId(Postid);
        p.update(context, listener);
    }

    /**
     * 删除贴
     *
     * @param postId
     * @param context
     * @param deleteListener
     */
    public void DeletePost(String postId, Context context, DeleteListener deleteListener) {
        if (TextUtils.isEmpty(postId)) {
            if (deleteListener != null) {
                deleteListener.onFailure(NULL_ERROR, "PostId is NULL");
            }
            return;
        }
        Post post = new Post();
        post.setObjectId(postId);
        DeletePost(post, context, deleteListener);

    }

    public void DeletePost(Post post, Context context, DeleteListener deleteListener) {
        if (post == null) {
            if (deleteListener != null) {
                deleteListener.onFailure(NULL_ERROR, "PostId is NULL");
            }
            return;
        }
        post.delete(context, deleteListener);

    }

    /**
     * 评论关联，一对多，一个贴可以多个评论
     *
     * @param u
     * @param p
     * @param content
     * @param context
     * @param listener
     */
    public void BindCommentOneToMany(User u, Post p, String content, Context context, SaveListener listener) {

        if (u == null || p == null) {
            if (listener != null) {
                listener.onFailure(NULL_ERROR, "User OR Post Class Is Null");
            }
            return;
        }
        final Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(p);
        comment.setCommentuser(u);
        comment.save(context, listener);
        UpdateSynCount(p.getObjectId(), COMMENT_COUNT, ADD_MODE, context, null);
    }


    /**
     * 查询评论，一对多关联
     * <p/>
     * 我想查询出某个帖子的所有评论
     *
     * @param post
     * @param context
     * @param listener
     */
    public void QueryCommentOneToMany(Post post, Context context, FindListener<Comment> listener) {
        QueryCommentOneToMany(post, "commentuser,post.author", context, listener);
    }


    /**
     * 查询评论，一对多关联
     * <p/>
     * 我想查询出某个帖子的某一页评论
     *
     * @param post
     * @param context
     * @param listener
     */
    public void QueryCommentOneToMany(Post post, String include, Context context, FindListener<Comment> listener) {
        if (TextUtils.isEmpty(include)) {
            QueryCommentOneToMany(post, "commentuser", -1, context, listener);
        } else {
            QueryCommentOneToMany(post, include, -1, context, listener);

        }

    }

    /**
     * 查询评论，一对多关联
     * <p/>
     * 我想查询出某个帖子的某一页评论
     *
     * @param post
     * @param context
     * @param listener
     */
    public void QueryCommentOneToMany(Post post, String include, int crrentPage, Context context, FindListener<Comment> listener) {
        if (post == null) {

            if (listener != null) {
                listener.onError(NULL_ERROR, "Post Class Is Null");
            }
            return;
        }
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        query.addWhereEqualTo("post", new BmobPointer(post));
        if (crrentPage > -1) {
            query.setLimit(crrentPage);
        }

        query.order("-updatedAt");
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        // query.include("commentuser,post.author");
        query.include(include);
        query.findObjects(context, listener);
    }

    /**
     * 删除评论
     *
     * @param com
     * @param context
     * @param listener
     */
    public void DeleteComment(final Comment com, final Context context, DeleteListener listener) {

        if (com == null) {
            if (listener != null) {
                listener.onFailure(NULL_ERROR, "Comment is NULL");
            }
            return;
        }
        com.delete(context, new DeleteListener() {
            @Override
            public void onSuccess() {

                UpdateSynCount(com.getPost().getObjectId(), COMMENT_COUNT, SUB_MODE, context, null);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }


    /**
     * 点赞关联，一对多
     *
     * @param u
     * @param p
     * @param content
     * @param context
     * @param listener
     */
    public void BindClickLikeOneToMany(User u, Post p, String content, Context context, SaveListener listener) {

        if (u == null || p == null) {
            if (listener != null) {
                listener.onFailure(NULL_ERROR, "User OR Post Class Is Null");
            }
            return;
        }
        final ClickLike like = new ClickLike();
        like.setPost(p);
        like.setClickuser(u);
        like.save(context, listener);
        UpdateSynCount(p.getObjectId(), CLICK_COUNT, ADD_MODE, context, null);
    }


    public static final int STATE_REFRESH = 0;// 下拉刷新
    public static final int STATE_MORE = 1;// 加载更多

    public int limit = 15;        // 每页的数据是15条
    public int curPage = 0;       // 当前页的编号，从0开始
    public String lastTime = "0";

    /**
     * 建议使用QueryPostList方法获取，本方法数据不稳定，不同步，可能导致UI线程阻塞。
     * QueryPostList方法数据稳定，同步，但使用while实现同步的，耗性能。。
     *
     * @param page       第几页的数据
     * @param context
     * @param actionType STATE_REFRESH = 0;// 下拉刷新
     *                   STATE_MORE = 1;// 加载更多
     * @param listener   回调
     */
    public void queryPostData(int page, final Context context, final int actionType, final FindListener<FindBag> listener) {
        Log.i("bmob", "pageN:" + page + " limit:" + limit + " actionType:" + actionType);


        BmobQuery<Post> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        // 如果是加载更多
        if (actionType == STATE_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(page * limit + 1);
        } else {
            page = 0;
            query.setSkip(page);
        }
        query.include("author");
        // 设置每页数据个数
        query.setLimit(limit);

        // 查找数据
        final List<FindBag> finalBags = new ArrayList<>();
        query.findObjects(context, new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
                if (list.size() > 0) {
                    if (actionType == STATE_REFRESH) {
                        // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                        curPage = 0;
                        // 获取最后时间
                        lastTime = list.get(list.size() - 1).getCreatedAt();
                    }
                    finalBags.clear();
                    for (int i = 0; i < list.size(); i++) {
                        Post p = list.get(i);
                        final FindBag bag = new FindBag();
                        bag.setPost(p);
                        /**
                         * 是否有图片
                         */
                        if (p.getHaveimage()) {
                            bag.setFileDirs(new ArrayList<FileDir>());
                            /**
                             * 查询图片
                             */
                            Log.i(TAG, "开始查询图片啦: " + p.getObjectId());

                            if (i == list.size() - 1) {
                                QueryFileDirOneToMany(p, null, context, new FindListener<FileDir>() {
                                    @Override
                                    public void onSuccess(List<FileDir> list) {
                                        Log.i(TAG, "查询图片或文件，最后一个");
                                        if (list != null && list.size() > 0) {
                                            if (bag.getFileDirs() == null) {
                                                bag.setFileDirs(new ArrayList<FileDir>());
                                            }
                                            bag.getFileDirs().addAll(list);
                                        }

                                        finalBags.add(bag);

                                        if (listener != null) {
                                            Log.i(TAG, "这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了 ");
                                            listener.onSuccess(finalBags);
                                        }
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        Log.i(TAG, "查询图片或文件，onError: ");
                                        bag.AppendErrInfo("查询图片或文件", Integer.toString(i), s);
                                        finalBags.add(bag);
                                        if (listener != null) {
                                            Log.i(TAG, "这里在每次加载完数据后，最后Image的加载 ");
                                            listener.onSuccess(finalBags);
                                        }
                                    }
                                });

                            } else {

                                QueryFileDirOneToMany(p, null, context, bag);
                            }

                        } else {
                            Log.i(TAG, "没有图片" + p.getObjectId());
                        }
                        finalBags.add(bag);
                    }
                    // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                    curPage++;
                } else if (actionType == STATE_MORE) {
                    Log.i(TAG, "queryData  actionType == STATE_MORE 没有更多数据了: ");
                    if (listener != null)
                        listener.onError(STATE_REFRESH, "没有更多数据了");

                } else if (actionType == STATE_REFRESH) {
                    if (listener != null)
                        listener.onError(STATE_MORE, "没有数据");
                    Log.i(TAG, "queryData  actionType == STATE_REFRESH 没有更多数据了: ");
                }
            }

            @Override
            public void onError(int arg0, String arg1) {
                Log.i(TAG, "queryData  onError  ");
                if (listener != null)
                    listener.onError(arg0, arg1);
                finalBags.clear();
            }
        });
    }

    /**
     * 查询帖子数据
     *
     * @param page       页数 默认:0
     * @param actionType 获取数据类型，加载更多和刷新
     * @param context    上下文
     * @param listener   回调
     */
    private void QueryPost(int page, int actionType, Context context, FindListener<Post> listener) {


        BmobQuery<Post> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        // 如果是加载更多
        if (actionType == STATE_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(page * limit + 1);
            /**
             * 如果希望在查询帖子信息的同时也把该帖子的作者的信息查询出来，可以使用include方法
             */
            query.include("author");
        } else {
            page = 0;
            query.setSkip(page);
        }
        // 设置每页数据个数
        query.setLimit(limit);
        query.findObjects(context, listener);
    }

    /**
     * 查询帖子列表
     *
     * @param page       页数 默认:0
     * @param actionType 获取数据类型，加载更多和刷新，
     * @param context    上下文  !=NULL
     * @param listener   回调  !=NULL
     */
    public synchronized void QueryPostList(int page, int actionType, Context context, FindListener<FindBag> listener) {
        if (context == null) {
            if (listener != null)
                listener.onError(0x404, "Context == NULL");
            return;
        }
        new Asynload(page, actionType, context, listener).execute();
    }


    private final class Asynload extends AsyncTask<String, Integer, Map<String, List<FindBag>>> {

        private int page = 0;
        private int actionType = STATE_REFRESH;
        private Context context;
        private FindListener<FindBag> listener;
        private Map<String, List<FindBag>> data;
        private boolean loadsuccess = false;
        private int lastPost = -1;


        private final String LOAD_SUCCESS = "LOAD_SUCCESS";
        private final String LOAD_FAILED = "LOAD_FAILED";

        public Asynload(int page, int actionType, Context context, FindListener<FindBag> listener) {
            this.page = page;
            this.actionType = actionType;
            this.context = context;
            this.listener = listener;
            this.loadsuccess = false;
            data = new HashMap<>();
        }

        @Override
        protected Map<String, List<FindBag>> doInBackground(String... params) {


            QueryPost(page, actionType, context, new FindListener<Post>() {
                @Override
                public void onSuccess(List<Post> list) {
                    if (list != null && list.size() > 0) {
                        if (actionType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                            curPage = 0;
                            // 获取最后时间
                            lastTime = list.get(list.size() - 1).getCreatedAt();
                        }
                        List<FindBag> finalBags = new ArrayList<FindBag>();
                        finalBags.clear();


                        /**
                         * 因为Bmob并没有提供一对多查询接口，所以人为同步数据
                         */
                        /**
                         * 获取帖子列表中最后带图片的帖子下标
                         */
                        for (int i = list.size() - 1; i > 0; i--) {
                            Post p = list.get(i);
                            if (p.getHaveimage()) {
                                lastPost = i;
                                Log.i(TAG, " 获取帖子列表中最后带图片的帖子下标:  " + lastPost);
                                break;
                            }
                        }

                        for (int i = 0; i < list.size(); i++) {
                            Post p = list.get(i);
                            final FindBag bag = new FindBag();
                            bag.setPost(p);
                            /**
                             * 是否有图片
                             */
                            if (p.getHaveimage()) {
                                bag.setFileDirs(new ArrayList<FileDir>());
                                if (i == lastPost) {
                                    /**
                                     * 最后带有图片的帖子加载完成
                                     */
                                    QueryFileDirOneToMany(p, null, context, new FindListener<FileDir>() {
                                        @Override
                                        public void onSuccess(List<FileDir> list) {
                                            Log.i(TAG, "最后带有图片的帖子加载完成");
                                            if (list != null && list.size() > 0) {
                                                bag.getFileDirs().addAll(list);
                                            }
                                            loadsuccess = true;
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            bag.AppendErrInfo("加载图片失败", i + "", s);
                                            Log.i(TAG, "最后带有图片的帖子加载onError");
                                            loadsuccess = true;
                                        }
                                    });
                                } else {
                                    /**
                                     * 查询非最后帖子图片
                                     */
                                    Log.i(TAG, "开始查询图片啦: " + p.getObjectId());
                                    QueryFileDirOneToMany(p, null, context, bag);
                                }
                            }
                            /**
                             * 全部列表都没有图片
                             */
                            if (i == list.size() - 1) {
                                if (lastPost == -1) {
                                    loadsuccess = true;
                                }
                            }
                            /**
                             * 添加到集合中
                             */
                            finalBags.add(bag);
                        }
                        data.put(LOAD_SUCCESS, finalBags);

                        if (actionType == STATE_MORE)
                            // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                            curPage++;
                    } else if (actionType == STATE_MORE) {
                        data.put(LOAD_FAILED, ErrList(STATE_MORE + "", " 没有更多数据了"));
                        loadsuccess = true;
                        Log.i(TAG, "queryData  actionType == STATE_MORE 没有更多数据了: ");
                    } else if (actionType == STATE_REFRESH) {
                        loadsuccess = true;
                        data.put(LOAD_FAILED, ErrList(STATE_REFRESH + "", "没有更多数据了"));
                        Log.i(TAG, "queryData  actionType == STATE_REFRESH 没有更多数据了: ");
                    }
                }

                @Override
                public void onError(int arg0, String arg1) {
                    loadsuccess = true;
                    Log.i(TAG, "queryData  onError  ");
                    data.put(LOAD_FAILED, ErrList(arg0 + "", "加载失败" + arg1));
                }
            });


            Log.i(TAG, "doInBackground:等待数据加载完成 " + new Date().toLocaleString());
            /**
             * 等待数据加载完成
             */
            while (!loadsuccess) ;
            Log.i(TAG, "doInBackground:>>>数据加载完成 " + new Date().toLocaleString());
            return data;
        }

        @Override
        protected void onPostExecute(Map<String, List<FindBag>> data) {

            if (data.containsKey(LOAD_SUCCESS)) {
                Log.i(TAG, "onPostExecute: LOAD_SUCCESS list Size=" + data.get(LOAD_SUCCESS).size());
                if (listener != null) {
                    listener.onSuccess(data.get(LOAD_SUCCESS));
                }
            } else {
                Log.i(TAG, "onPostExecute: LOAD_FAILED" + data.get(LOAD_FAILED).get(0).getErrmsg());
                FindBag bag = data.get(LOAD_FAILED).get(0);
                if (listener != null) {
                    listener.onError(Integer.parseInt(bag.getErrcode()), bag.getErrmsg());
                }

            }


        }


    }


    /**
     * 构建一个加载失败的信息集合包
     *
     * @param code
     * @param msg
     * @return
     */
    public List<FindBag> ErrList(String code, String msg) {
        List<FindBag> bags = new ArrayList<>();
        FindBag bag = new FindBag();
        bag.setErrcode(code);
        bag.setErrmsg(msg);
        bags.add(bag);
        return bags;
    }

    /**
     * 查询图片或文件，一对多关联
     * <p/>
     * 我想查询出某个帖子的某一页评论
     *
     * @param post
     * @param context
     */
    public void QueryFileDirOneToMany(Post post, String include, Context context, final FindBag bag) {
        if (post == null) {
            return;
        }
        BmobQuery<FileDir> query = new BmobQuery<FileDir>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        query.addWhereEqualTo("post", new BmobPointer(post));
        query.order("-updatedAt");
        query.setLimit(10);
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        if (!TextUtils.isEmpty(include)) {

            query.include(include);
        }
        query.findObjects(context, new FindListener<FileDir>() {
            @Override
            public void onSuccess(List<FileDir> list) {

                Log.i(TAG, "查询图片或文件，一对多关联onSuccess:");
                if (list != null && list.size() > 0) {
                    if (bag.getFileDirs() == null) {
                        bag.setFileDirs(new ArrayList<FileDir>());
                    }
                    bag.getFileDirs().addAll(list);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "查询图片或文件，onError: ");
                bag.AppendErrInfo("查询图片或文件", Integer.toString(i), s);
            }
        });
    }

    public void QueryFileDirOneToMany(Post post, String include, Context context, final FindListener<FileDir> listener) {
        if (post == null) {
            return;
        }
        BmobQuery<FileDir> query = new BmobQuery<FileDir>();
        //用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        query.addWhereEqualTo("post", new BmobPointer(post));
        query.order("-updatedAt");
        query.setLimit(10);
        //希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        if (!TextUtils.isEmpty(include)) {
            query.include(include);
        }
        query.findObjects(context, listener);
    }

    /**
     * 评论次数与点击次数
     */
    public static final short COMMENT_COUNT = 0X100;
    public static final short CLICK_COUNT = 0X200;

    /**
     * 评论次数与点击次数的增加或减模式
     */
    public static final short SUB_MODE = 0x300;
    public static final short ADD_MODE = 0x400;


    public void UpdateSynCount(String postId, int Type, int mode, Context context, UpdateListener listener) {

        if (TextUtils.isEmpty(postId)) {
            if (listener != null) {
                listener.onFailure(NULL_ERROR, "Post Id Length =0 Or NULL");
            }
            return;
        }

        Post post = new Post();
        post.setObjectId(postId);
        UpdateSynCount(post, Type, mode, context, listener);

    }

    /**
     * 原子计数器
     * 很多应用可能会有计数器功能的需求，比如文章点赞的功能，如果大量用户并发操作，用普通的更新方法操作的话，会存在数据不一致的情况。
     * <p/>
     * 为此，Bmob提供了原子计数器来保证原子性的修改某一数值字段的值。注意：原子计数器只能对应用于Web后台的Number类型的字段，即JavaBeans数据对象中的Integer对象类型（不要用int类型）。
     *
     * @param post
     * @param Type
     * @param mode
     * @param context
     * @param listener
     */
    public void UpdateSynCount(final Post post, int Type, int mode, Context context, UpdateListener listener) {

        if (post == null) {
            if (listener != null) {
                listener.onFailure(NULL_ERROR, "Post class Is null");
            }
            return;
        }


        if (Type == COMMENT_COUNT) {

            if (mode == ADD_MODE) {
                // 分数递增1
                post.increment("commentcount");
                post.update(context, listener);
            } else if (mode == SUB_MODE) {
                post.increment("commentcount", -1);
                post.update(context, listener);
            }

        } else if (Type == CLICK_COUNT) {

            if (mode == ADD_MODE) {
                // 分数递增1
                post.increment("clickcount");
                post.update(context, listener);
            } else if (mode == SUB_MODE) {

                // 分数递减1
                post.increment("clickcount", -1);
                post.update(context, listener);
            }
        }


    }


    /*************************************************************************************************
     *                                                                                               *
     *                                                                                               *
     *         已经过时,耗流量，耗性能，低并发，非同步，不稳定                                           *                             *
     *           2016-6-10  21：00 author:credit毅                                                                                  *
     *                                                                                               *
     *                                                                                               *
     *                                                                                               *
     *************************************************************************************************/
    /**
     * 获取评论总数
     */
    @Deprecated
    public void getCommentCount(Context context, Post post, final FindBag findBag) {
    /*    BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("post", new BmobPointer(post));
        query.count(context, ClickLike.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (findBag != null) {
                    findBag.setClickCount(i);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                findBag.AppendErrInfo("获取评论总数", Integer.toString(i), s);
            }
        });*/

        getCount(Comment.class.getSimpleName(), context, post, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (findBag != null) {
                    findBag.setCommentCount(i);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                findBag.setCommentCount(0);
                findBag.AppendErrInfo("获取评论总数", Integer.toString(i), s);
            }
        });

    }

    @Deprecated
    private void getCount(String cname, Context context, Post post, CountListener countListener) {

        if (cname.equals(Comment.class.getSimpleName())) {

            BmobQuery<Comment> query = new BmobQuery();
            query.addWhereEqualTo("post", new BmobPointer(post));
            query.count(context, Comment.class, countListener);
        } else if (cname.equals(ClickLike.class.getSimpleName())) {
            BmobQuery<ClickLike> query = new BmobQuery();
            query.addWhereEqualTo("post", new BmobPointer(post));
            query.count(context, ClickLike.class, countListener);
        }


    }

    /**
     * 获取点赞总数
     */
    @Deprecated
    public void getCilckLikeCount(Context context, Post post, final FindBag findBag) {
       /* BmobQuery<ClickLike> query = new BmobQuery<ClickLike>();
        query.addWhereEqualTo("post", new BmobPointer(post));
        query.count(context, ClickLike.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (findBag != null) {
                    findBag.setClickCount(i);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                findBag.AppendErrInfo("获取点赞总数", Integer.toString(i), s);
            }
        });
*/

        getCount(Comment.class.getSimpleName(), context, post, new CountListener() {
            @Override
            public void onSuccess(int i) {
                if (findBag != null) {
                    findBag.setClickCount(i);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                findBag.setClickCount(0);
                findBag.AppendErrInfo("获取点赞总数", Integer.toString(i), s);
            }
        });


    }


}
