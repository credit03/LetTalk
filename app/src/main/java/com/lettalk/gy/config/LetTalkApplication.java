package com.lettalk.gy.config;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.lettalk.gy.bean.Friend;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.model.UserModel;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.listener.FindListener;

/**
 * @author :smile
 * @project:BmobIMApplication
 * @date :2016-01-13-10:19
 */
public class LetTalkApplication extends Application {

    private static LetTalkApplication INSTANCE;

    private Map<String, User> users = new HashMap<String, User>();

    public static LetTalkApplication INSTANCE() {
        return INSTANCE;
    }

    private void setInstance(LetTalkApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(LetTalkApplication a) {
        LetTalkApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        Bmob.DEBUG = true;
        //初始化
        Logger.init("LetTalkApp");
        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }

        initBmobFileUploadCofing();
        //uil初始化
        initImageLoader(this);
    }

    /**
     * 设置文件分片上传时每片大小
     * <p/>
     * 自BmobSDKv3.4.6开始,新增BmobConfig类，允许开发者设置查询超时时间及文件分片上传时的每片大小。
     */
    public void initBmobFileUploadCofing() {
        //设置BmobConfig
        BmobConfig config = new BmobConfig.Builder()
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setBlockSize(500 * 1024)
                .build();
        Bmob.getInstance().initConfig(config);
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);
        config.memoryCache(new WeakMemoryCache());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        //将保存的时候的URI名称用MD5
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.diskCacheFileCount(200); //缓存的File数量
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void upDateUserListInfo(List<Friend> list) {

        Log.i("LetTalkApplication", "upDateUserListInfo------->>方法" + list.size());
        if (list != null && list.size() > 0) {
            users.clear();
            for (Friend f : list) {
                User friendUser = f.getFriendUser();
                users.put(friendUser.getObjectId(), friendUser);
            }

        }
    }

    /**
     * 从服务器获取好友列表
     */

    public void UpdateUserByBmobServer() {
        UserModel.getInstance().queryFriends(new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                upDateUserListInfo(list);
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }

    public User getUser(String id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        return null;


    }


}
