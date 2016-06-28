package com.lettalk.gy.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lettalk.gy.R;
import com.lettalk.gy.bean.AddFriendMessage;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.config.Config;
import com.lettalk.gy.config.LetTalkApplication;
import com.lettalk.gy.ui.euclidui.EuclidActivity;
import com.lettalk.gy.ui.euclidui.EuclidItemButClickListener;
import com.lettalk.gy.ui.euclidui.EuclidListAdapter;
import com.lettalk.gy.ui.view.GifImageView;
import com.lettalk.gy.util.PhotoUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserInfoAcivityPro extends EuclidActivity implements EuclidItemButClickListener, View.OnClickListener {

    private static final String TAG = "UserInfoAcivityPro";
    User user, cruuentId;


    private final int PHOTO_AVATOR_CODE = 0X101;  //修改原图片标志码
    private final int PHOTO_CROP_CODE = 0X102;  //返回裁剪图片标志码
    private final int EDIT_NAME = 0X103;  //修改用户名
    private final int EDIT_PER = 0X104;  //修改签名


    private String sex[] = {"男", "女"};
    private String back_sex = "";
    private int sex_item = 0;

    private String back_name = "";

    private String tm_back = "";
    private int tm_ChoiceItem = 0;
    private String[] tm = {"机电系", "计算机系", "管理系", "财经系", "外语系", "热作系", "商务系",
            "艺术系", "德国F+U", "国际交流学院"};

    private EuclidListAdapter euclidListAdapter;
    private BmobIMUserInfo info;
    /**
     * 我的头像保存目录
     */
    public static String MyAvatarDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        user = (User) getBundle().getSerializable("u");
        cruuentId = BmobUser.getCurrentUser(this, User.class);
        initMap(user);
        super.onCreate(savedInstanceState);

        initData();

    }

    @Override
    public void initData() {
        MyAvatarDir = getFilesDir() + "/avatar/";
/*        Glide.with(this).load(R.drawable.load2).into(show_load);*/
        tv_userid.setText("(id:" + user.getUsername() + ")");
        tm_back = user.getSchool_tm();
        if (TextUtils.isEmpty(tm_back)) {
            tm_back = "尚未设置院系";
        }
        tv_tm.setText(tm_back);

        if (user.getSex() != null) {
            if (user.getSex()) {
                back_sex = "男";
                sex_item = 0;
                tv_sex.setText("男");
            } else {
                back_sex = "女";
                sex_item = 1;
                tv_sex.setText("女");
            }
        } else {
            back_sex = "";
            sex_item = 1;
            tv_sex.setText("女");
        }

        if (user.getObjectId().equals(cruuentId.getObjectId())) {

            /**
             * 修改头像
             */
            mButtonProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UserInfoAcivityPro.this, "修改资料", Toast.LENGTH_SHORT).show();
                    goGallery();
                }
            });
            ll_nickname.setOnClickListener(UserInfoAcivityPro.this);
            ll_pre.setOnClickListener(UserInfoAcivityPro.this);
            ll_sex.setOnClickListener(UserInfoAcivityPro.this);
            ll_tm.setOnClickListener(UserInfoAcivityPro.this);

        }
        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());


    }

    public void initMap(User user) {
        userInfoMap.clear();
        userInfoMap.put(NICKNAME, user.getNickname());
        userInfoMap.put(USERNAME, user.getUsername());
        userInfoMap.put(TM, user.getSchool_tm());
        if (user.getSex()) {
            userInfoMap.put(SEX, "男");
        } else {
            userInfoMap.put(SEX, "女");
        }
        userInfoMap.put(PERSONALIZED, user.getPersonalized());

        if (user.getObjectId().equals(cruuentId.getObjectId())) {
            userInfoMap.put(ISCURRENTID, "TRUE");
        } else {
            userInfoMap.put(ISCURRENTID, "FALSE");
        }

        if (LetTalkApplication.INSTANCE().getUser(user.getObjectId()) != null) {
            userInfoMap.put(ISFRIEND, "YES");
        } else {
            userInfoMap.put(ISFRIEND, "NO");
        }


    }

    @Override
    protected void setEuClidItemButtonListener() {
        euclidListAdapter.setButClickListener(this);
    }


    @Override
    protected BaseAdapter getAdapter() {
        Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();

        profileMap = new HashMap<>();
        profileMap.put(EuclidListAdapter.KEY_AVATAR, user.getAvatar());
        profileMap.put(EuclidListAdapter.KEY_NAME, user.getNickname());
        String per = "这个人很懒什么也没有留下..";
        if (!TextUtils.isEmpty(user.getPersonalized())) {
            per = user.getPersonalized();
        }
        String html;
        if (per.length() < 50) {
            //html 空白汉字占位符&#12288;
            html = "<p>" + "院系&#12288;" + user.getSchool_tm() + "</p>" + "<b style=\"font-size:130%\">签名&#12288;</b>" + per;
        } else {
            html = "<p>" + "院系&#12288;" + user.getSchool_tm() + "</p>" + "<b style=\"font-size:130%\">签名&#12288;</b>" + per.substring(0, 50) + "...";
        }
        profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, Html.fromHtml(html).toString());
        if (per.length() <= 50)
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, per);
        else
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, per.substring(0, 50));
        profilesList.add(profileMap);

        if (user.getObjectId().equals(cruuentId.getObjectId())) {

            euclidListAdapter = new EuclidListAdapter(this, R.layout.list_item, profilesList, EuclidListAdapter.IS_CURRENTUSER);
        } else {
            if (LetTalkApplication.INSTANCE().getUser(user.getObjectId()) != null) {

                euclidListAdapter = new EuclidListAdapter(this, R.layout.list_item, profilesList, EuclidListAdapter.NO_CURRENTUSER, EuclidListAdapter.IS_FRIEND);
            } else {
                euclidListAdapter = new EuclidListAdapter(this, R.layout.list_item, profilesList, EuclidListAdapter.NO_CURRENTUSER, 0);

            }
        }
        setEuClidItemButtonListener();
        return euclidListAdapter;
    }

    @Override
    public void euclidItemAddFriendOnclick(View v) {
        log("euclidItemAddFriendOnclick ===");
        onAddClick(v);

    }

    @Override
    public void euclidItemChatOnclick(View v) {
        log("euclidItemChatOnclick ===");
        onChatClick(v);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_nickname:
                User c = BmobUser.getCurrentUser(this, User.class);
                startActivityBudle("更改昵称", c.getNickname(), "好名字朋友更容易记住你。", EDIT_NAME);
                break;
            case R.id.ll_sex:
                onSexClick(v);
                Toast.makeText(this, "性别", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_pre:
                User c1 = BmobUser.getCurrentUser(this, User.class);
                if (c1.getPersonalized().length() > 50)
                    startActivityBudle("更改签名", c1.getPersonalized().substring(0, 50), "悟出心中的感慨（50字）", EDIT_PER);
                else
                    startActivityBudle("更改签名", c1.getPersonalized(), "悟出心中的感慨（50字）", EDIT_PER);
                break;
            case R.id.ll_tm:
                onTmClick(v);
                break;
        }
    }


    public void onTmClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoAcivityPro.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("农工商院系");

        builder.setSingleChoiceItems(tm, tm_ChoiceItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        if (tm_ChoiceItem != which && !tm_back.equals(tm[which])) {


                            User user = new User();
                            user.setSchool_tm(tm[which]);
                            tm_ChoiceItem = which;
                            updateUserData(user, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    toast("更新院系成功...");
                                    tv_tm.setText(tm[tm_ChoiceItem]);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    toast("更新院系失败...Error Code=" + i + s);
                                }
                            });
                        } else {
                            tv_tm.setText(tm_back);
                        }
                        dialog.dismiss();
                    }

                });

        builder.setNegativeButton("取消", null);

        builder.show();

    }


    public void onSexClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoAcivityPro.this);
        builder.setTitle("性别");

        builder.setSingleChoiceItems(sex, sex_item,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (sex_item != which && !back_sex.equals(sex[which])) {
                            sex_item = which;
                            User user = new User();
                            if (which == 0)
                                user.setSex(true);
                            else
                                user.setSex(false);
                            updateUserData(user, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    toast("更新性别成功...");
                                    tv_sex.setText(sex[sex_item]);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    toast("更新性别失败...Error Code=" + i + s);
                                }
                            });
                        } else {
                            tv_sex.setText(back_sex);
                        }

                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton("取消", null);

        builder.show();

    }


    public void onAddClick(View view) {
        /**
         * 以防为自己为好友
         */
        if (!user.getObjectId().equals(cruuentId.getObjectId()))
            sendAddFriendMessage();

        this.finish();
    }

    /**
     * 发送添加好友的请求
     */

    private void sendAddFriendMessage() {
        //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
        //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        AddFriendMessage msg = new AddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(this, User.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String, Object> map = new HashMap<>();
        map.put("name", currentUser.getNickname());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        map.put("avatar", currentUser.getAvatar());//发送者的头像
        map.put("uid", currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    Toast.makeText(UserInfoAcivityPro.this, "好友请求发送成功，等待验证", Toast.LENGTH_LONG).show();
                } else {//发送失败
                    Toast.makeText(UserInfoAcivityPro.this, "发送失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void onChatClick(View view) {
        //启动一个会话，设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, false, null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("c", c);
        startActivity(ChatActivity.class, bundle, true);
    }


    /**
     * 防止多次点击打开图库标志,在ActivityResult返回设置为false
     */
    private boolean OPEN_GALLERY = false;

    /**
     * 去图库获取图片修改头像或背景
     */
    public void goGallery() {

        if (!OPEN_GALLERY) {
            OPEN_GALLERY = true;
            /**
             * 去图库获取图像
             */
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, PHOTO_AVATOR_CODE);

        }


    }

    // 如果手机不是基于AOSP的话就是不能这么做的。可以直接通过出过来的uri来得到整张的图片
    Bitmap bitimage = null;

    /**
     * @return void
     * @throws
     * @Title: startImageAction
     */
    private void startImageAction(Uri uri, int outputX, int outputY,
                                  int requestCode, boolean isCrop) {

        if (uri == null) {
            return;
        }

        Intent intent = null;
        try {

            if (isCrop) {
                //intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent = new Intent("com.android.camera.action.CROP");
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            }
            /**
             * 附加选项 数据类型 描述
             crop String 发送裁剪信号
             aspectX int X方向上的比例
             aspectY int Y方向上的比例
             outputX int 裁剪区的宽
             outputY int 裁剪区的高
             scale boolean 是否保留比例
             return-data boolean 是否将数据保留在Bitmap中返回
             data Parcelable 相应的Bitmap数据
             circleCrop String 圆形裁剪区域？
             */
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("scaleUpIfNeeded", true); // 去掉黑边
            intent.putExtra("noFaceDetection", true); // 不需要人脸识别
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            bitimage = PhotoUtil.getBitmapFromBigImagByUri(this, uri, sScreenWidth / 3, sScreenHeight / 2);
        }
    }


    Bitmap newBitmap;
    boolean isFromCamera = false;// 区分拍照旋转
    int degree = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        OPEN_GALLERY = false;
        switch (requestCode) {
            //从图库选择图片的返回
            case PHOTO_AVATOR_CODE:// 本地修改头像

                Uri uri = null;
                if (data == null) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        return;
                    }
                    isFromCamera = false;
                    uri = data.getData();
                    startImageAction(uri, 300, 300, PHOTO_CROP_CODE, true);
                }
            case PHOTO_CROP_CODE:// 裁剪头像返回
                log("PHOTO_CROP_CODE:// 裁剪头像返回");
                // TODO sent to crop
                if (data == null) {
                    Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    ConstructorAvatorPath(data);
                    // 上传头像
                    uploadAvatar();
                }

                break;
            case EDIT_NAME:
            case EDIT_PER:
                if (data != null) {
                    String dec = data.getStringExtra("DEC");
                    User user = new User();
                    if (requestCode == EDIT_NAME) {

                        user.setNickname(dec);
                        mTextViewProfileName.setText(dec);
                    } else {
                        mTextViewProfileDescription.setText(dec);
                        user.setPersonalized(dec);
                    }

                    updateUserData(user, new UpdateListener() {
                        @Override
                        public void onSuccess() {

                            toast("更新成功...");
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });

                }

                break;
            default:
                Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    int color[] = {android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light};

    /**
     * 上传头像或背景图片到BMOB方法
     */

    private void uploadAvatar() {

        if (TextUtils.isEmpty(path)) {
            return;
        }


        startProgeress();
        Toast.makeText(this, "正在上传", Toast.LENGTH_SHORT).show();
        final BmobFile bmobFile = new BmobFile(new File(path));

        bmobFile.uploadblock(this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                stopProgeress();
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                String url = bmobFile.getFileUrl(UserInfoAcivityPro.this);
                // 更新BmobUser对象
                Toast.makeText(UserInfoAcivityPro.this, "上传成功", Toast.LENGTH_SHORT).show();
                updateUserAvatar(url);

            }

            @Override
            public void onProgress(Integer value) {
                pb_prohro.setProgress(value);
            }

            @Override
            public void onFailure(int code, String msg) {
                stopProgeress();
            }
        });
    }


    public void startProgeress() {

        show_load.setVisibility(GifImageView.VISIBLE);
        pb_prohro.setVisibility(ProgressBar.VISIBLE);
        pb_prohro.setProgress(0);
    }


    public void stopProgeress() {

        show_load.setVisibility(GifImageView.GONE);
        pb_prohro.setVisibility(ProgressBar.GONE);


    }

    private void updateUserAvatar(final String url) {
        User u = new User();
        u.setAvatar(url);
        updateUserData(u, new UpdateListener() {
            @Override
            public void onSuccess() {

                File tempFile = new File(MyAvatarDir);
                if (tempFile != null && tempFile.isDirectory()) {
                    tempFile.delete();
                }
                User currentUser = BmobUser.getCurrentUser(UserInfoAcivityPro.this, User.class);
                currentUser.update(UserInfoAcivityPro.this);
                UpdateAvator(url, null);
            }

            @Override
            public void onFailure(int code, String msg) {


            }
        });
    }


    String path;


    /**
     * 的头像
     *
     * @param data
     */
    private void ConstructorAvatorPath(Intent data) {
        path = "";
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            Log.i("life", "avatar - bitmap = " + bitmap);

            if (bitmap != null) {
                log(" 保存图片  bitmap != null");
                // 保存图片
                String filename = new SimpleDateFormat("yyMMddHHmmss")
                        .format(new Date()) + ".png";
                path = MyAvatarDir + filename;
                log("// 保存图片+" + path);
                PhotoUtil.saveBitmap(MyAvatarDir, filename, bitmap, true);
                UpdateAvator(path, bitmap);
                if (bitmap != null && bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }

        }

    }


    public void startActivityBudle(String title, String name, String dec, int COCE) {
        /**
         * 去图库获取图像
         */
        Intent intent = new Intent(this, EditInfoActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("code", COCE);
        intent.putExtra("title", title);
        intent.putExtra("dec", dec);
        startActivityForResult(intent, COCE);
    }

    /**
     * 更新用户资料到服务器
     *
     * @param user
     * @param listener
     */
    private void updateUserData(final User user, final UpdateListener listener) {
        //connect server
        final User current = BmobUser.getCurrentUser(this, User.class);
        user.setObjectId(current.getObjectId());
        user.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
                User c = BmobUser.getCurrentUser(UserInfoAcivityPro.this, User.class);
                BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(c.getObjectId(), c.getUsername(), c.getAvatar()));
                if (!TextUtils.isEmpty(user.getAvatar())) {
                    UpdateAvator(user.getAvatar(), null);
                } else {
                    UpdateAvator("", null);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
            }
        });

    }


    public void UpdateAvator(String uri, Bitmap bitmap) {

        if (mOverlayListItemView == null) {
            return;
        }

        if (bitmap != null) {
            ((ImageView) mOverlayListItemView.findViewById(R.id.image_view_reveal_avatar)).setImageBitmap(bitmap);
            ((ImageView) mOverlayListItemView.findViewById(R.id.image_view_avatar)).setImageBitmap(bitmap);
            return;
        }


        log("UpdateAvator: IamgePath==" + uri);

        User u = BmobUser.getCurrentUser(this, User.class);
        Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();
        profileMap = new HashMap<>();
        if (TextUtils.isEmpty(uri)) {
            profileMap.put(EuclidListAdapter.KEY_AVATAR, u.getAvatar());
        } else {
            profileMap.put(EuclidListAdapter.KEY_AVATAR, uri);
        }


        profileMap.put(EuclidListAdapter.KEY_NAME, u.getNickname());
        String per = "这个人很懒什么也没有留下..";
        if (!TextUtils.isEmpty(u.getPersonalized())) {
            per = u.getPersonalized();
        }
        String html;
        if (per.length() < 50) {
            //html 空白汉字占位符&#12288;
            html = "<p>" + "院系&#12288;" + u.getSchool_tm() + "</p>" + "<b style=\"font-size:130%\">签名&#12288;</b>" + per;
        } else {
            html = "<p>" + "院系&#12288;" + u.getSchool_tm() + "</p>" + "<b style=\"font-size:130%\">签名&#12288;</b>" + per.substring(0, 50) + "...";
        }
        profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_SHORT, Html.fromHtml(html).toString());
        if (per.length() <= 50)
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, per);
        else
            profileMap.put(EuclidListAdapter.KEY_DESCRIPTION_FULL, per.substring(0, 50));
        profilesList.add(profileMap);
        euclidListAdapter.UpdateData(profilesList);
        euclidListAdapter.notifyDataSetChanged();

    }

    /**
     * Log日志
     *
     * @param msg
     */
    public void log(String msg) {
        log(this.getPackageName(), msg);
    }

    /**
     * Log日志
     *
     * @param msg
     */
    public void log(String TDA, String msg) {
        if (Config.DEBUG) {
            Logger.i(TDA, msg);
        }
    }

    public void toast(String msg) {
        toast(msg, Toast.LENGTH_SHORT);
    }

    public void toast(String msg, int time) {
        Toast.makeText(this, msg, time).show();
    }

}