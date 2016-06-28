package com.lettalk.gy.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lettalk.gy.R;
import com.lettalk.gy.base.ParentWithNaviActivity;
import com.lettalk.gy.bean.AddFriendMessage;
import com.lettalk.gy.bean.User;
import com.lettalk.gy.config.LetTalkApplication;
import com.lettalk.gy.util.PhotoUtil;
import com.lettalk.gy.util.ViewUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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


/**
 * 用户资料
 */
public class UserInfoActivity extends ParentWithNaviActivity implements TextWatcher, View.OnClickListener {

    @Bind(R.id.iv_avator)
    ImageView iv_avator;

    @Bind(R.id.tv_name)
    EditText tv_name;

    @Bind(R.id.btn_add_friend)

    Button btn_add_friend;
    @Bind(R.id.btn_chat)
    Button btn_chat;

    User user;
    BmobIMUserInfo info;
    @Bind(R.id.tv_tm)
    TextView tvTm;
    @Bind(R.id.tv_sex)
    TextView tvSex;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.btn_edit)
    Button btnEdit;

    @Bind(R.id.iv_bg)
    ImageView iv_bg;

    @Bind(R.id.edit_bg)
    ImageView edit_bg;
    @Bind(R.id.progress_load)
    LinearLayout progressLoad;
    @Bind(R.id.tv_id)
    TextView tvId;


    @Override
    protected String title() {
        return "个人资料";
    }

    private final int BG_CODE = 0x101;
    private final int GALLERY_BG_CODE = 0X102;//修改头像图库标志码

    private final int PHOTO_AVATOR_CODE = 0X103;  //修改头像的图片标志码
    private final int PHOTO_BG_CODE = 0X104;  //修改背景的图片标志码
    private int PHOTO_CODE = 1;


    private String sex[] = {"男", "女"};
    private String back_sex = "";
    private int sex_item = 0;

    private String back_name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initNaviView();

        user = (User) getBundle().getSerializable("u");


        /**
         * 判断
         */
        if (user.getObjectId().equals(getCurrentUid())) {
            btn_add_friend.setVisibility(View.GONE);
            btn_chat.setVisibility(View.GONE);
            edit_bg.setVisibility(ImageView.VISIBLE);
            openViewSet();

            iv_avator.setOnClickListener(this);
            edit_bg.setOnClickListener(this);
            tvTm.setOnClickListener(this);
            tvSex.setOnClickListener(this);

        } else {

            if (LetTalkApplication.INSTANCE().getUser(user.getObjectId()) != null) {
                btn_add_friend.setVisibility(View.GONE);
            } else {
                btn_add_friend.setVisibility(View.VISIBLE);
            }
            btn_chat.setVisibility(View.VISIBLE);
            tvTm.setClickable(false);
            tvSex.setClickable(false);
        }


        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
        //设置头像
        ViewUtil.setImageView(user.getAvatar(), R.mipmap.head, iv_avator);
        //设置背景图
        ViewUtil.setImageView(user.getUserbg(), R.drawable.temp_bg, iv_bg, false);

        tvId.setText(user.getUsername());
        back_name = user.getNickname();
        tv_name.setText(back_name);
        tm_back = user.getSchool_tm();
        if (TextUtils.isEmpty(tm_back)) {
            tm_back = "尚未设置院系";
        }
        tvTm.setText(tm_back);

        if (user.getSex() != null) {
            if (user.getSex()) {
                back_sex = "男";
                sex_item = 0;
                tvSex.setText("男");
            } else {
                back_sex = "女";
                sex_item = 1;
                tvSex.setText("女");
            }
        } else {
            back_sex = "";
            sex_item = 1;
            tvSex.setText("女");
        }


        for (int i = 0; i < tm.length; i++) {
            if (tm_back.equals(tm[i])) {
                tm_ChoiceItem = i;
                break;
            }
        }

        if (tv_name.isEnabled()) {
            /**
             * 监听EditText文本改变事件，要设置在网络获取的信息改变之后开启
             */
            tv_name.addTextChangedListener(this);
        }


    }

    public void openViewSet() {
        iv_avator.setClickable(true);
        edit_bg.setClickable(true);
        tv_name.setEnabled(true);
        tvTm.setClickable(true);
        tvSex.setClickable(true);
    }

    public void closeViewSet() {
        iv_avator.setClickable(false);
        edit_bg.setClickable(false);
        tv_name.setEnabled(false);
        tvTm.setClickable(false);
        tvSex.setClickable(false);
    }

    private String tm_back = "";
    private int tm_ChoiceItem = 0;
    private String[] tm = {"机电系", "计算机系", "管理系", "财经系", "外语系", "热作系", "商务系",
            "艺术系", "德国F+U", "国际交流学院"};

    public void onTmClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("农工商院系");

        builder.setSingleChoiceItems(tm, tm_ChoiceItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        if (tm_ChoiceItem != which && !tm_back.equals(tm[which])) {

                            tvTm.setText(tm[which]);
                            openCommit(true);
                            tm_ChoiceItem = which;
                        } else {
                            tvTm.setText(tm_back);
                            openCommit(false);
                        }

                        dialog.dismiss();
                    }

                });

        builder.setNegativeButton("取消", null);

        builder.show();

    }


    public void onSexClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setTitle("性别");

        builder.setSingleChoiceItems(sex, sex_item,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (sex_item != which && !back_sex.equals(sex[which])) {
                            sex_item = which;
                            tvSex.setText(sex[which]);
                            openCommit(true);
                        } else {
                            tvSex.setText(back_sex);
                            openCommit(false);
                        }

                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton("取消", null);

        builder.show();

    }

    /**
     * 显示提交按钮
     */
    private void openCommit(boolean isopen) {

        if (isopen) {
            btnEdit.setVisibility(Button.VISIBLE);
        } else {
            if (back_name.equals(tv_name.getText().toString().trim()) && back_sex.equals(tvSex.getText().toString()) && tvTm.getText().toString().equals(tm_back)) {
                btnEdit.setVisibility(Button.GONE);
            }
        }

    }


    @OnClick(R.id.btn_add_friend)
    public void onAddClick(View view) {
        /**
         * 以防为自己为好友
         */
        if (!user.getObjectId().equals(getCurrentUid()))
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
                    toast("好友请求发送成功，等待验证");
                } else {//发送失败
                    toast("发送失败:" + e.getMessage());
                }
            }
        });
    }

    @OnClick(R.id.btn_chat)
    public void onChatClick(View view) {
        //启动一个会话，设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, false, null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("c", c);
        startActivity(ChatActivity.class, bundle, false);
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
            startActivityForResult(intent, GALLERY_BG_CODE);

        }


    }

    /**
     * 修改用户头像
     */

    public String filePath = "";

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
            if (PHOTO_CODE == PHOTO_AVATOR_CODE) {
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);

            } else if (PHOTO_CODE == PHOTO_BG_CODE) {
               /* 当图片很大的时候，这样还是会出现问题，一整张图片可能直接会使内存溢出，
               或者说裁剪的一张图片会使内存溢出（特别是对手机像素很高的来说）。所有呢，最好的情况是对图片进行压缩。
                图库截取图片后，通过intent的Bundle传送bitmap数据时，
                出现javabinder failed binder transaction，
                不能跳转到Contacts的AttachPhotoActivity,原因是通过Intent传送数据时，图片的size不能超过40k
                  */
                log("  scale boolean 是否保留比例 ==false");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", false);

            }
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("scaleUpIfNeeded", true); // 去掉黑边
            intent.putExtra("noFaceDetection", true); // 不需要人脸识别
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            bitimage = PhotoUtil.getBitmapFromBigImagByUri(this, uri, mScreenWidth / 3, mScreenHeight / 2);
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
            case GALLERY_BG_CODE:// 本地修改头像

                Uri uri = null;
                if (data == null) {
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        toast("SD不可用");
                        return;
                    }
                    isFromCamera = false;

                    /* 这样在很多机器上测试的时候都没有问题，但是这样就结束了吗？
                    还没有。当图片很大的时候，这样还是会出现问题，一整张图片可能直接会使内存溢出
                    ，或者说裁剪的一张图片会使内存溢出（特别是对手机像素很高的来说）。
                    所有呢，最好的情况是对图片进行压缩。
                     */
                    uri = data.getData();


                    log("开启进度条" + PHOTO_CODE);
                    /**
                     * 修改头像
                     */
                    if (PHOTO_CODE == PHOTO_AVATOR_CODE)
                        startImageAction(uri, 200, 200, BG_CODE, true);
                    else {
                        /**
                         * 修改背景
                         */
                        //  startImageAction(uri, 300, 300, BG_CODE, true);
                        bitimage = PhotoUtil.getBitmapFromBigImagByUri(this, uri, mScreenWidth, mScreenHeight);
                        NoCropImage(bitimage);

                    }

                } else {
                    toast("照片获取失败");
                }

                break;
            case BG_CODE:// 裁剪头像返回
                // TODO sent to crop
                log("裁剪头像返回");
                if (data == null) {
                    // Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    saveCropAvator(data);
                }
                // 初始化文件路径
                filePath = "";
                // 上传头像
                uploadAvatar();
                break;
            default:
                if (bitimage != null)
                    NoCropImage(bitimage);
                log(" ActivityResult default 默认");
                // progressBar.setVisibility(View.GONE);
                break;

        }
    }

    int color[] = {android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light};

    /**
     * 上传头像或背景图片到BMOB方法
     */
    private void uploadAvatar() {

        closeViewSet();
        //开启进度条
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        progressLoad.setVisibility(LinearLayout.VISIBLE);
        final BmobFile bmobFile = new BmobFile(new File(path));

        bmobFile.uploadblock(this, new UploadFileListener() {


            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                log("bmobFile.getFileUrl(context)--返回的上传文件的完整地址");
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                String url = bmobFile.getFileUrl(UserInfoActivity.this);

                // 更新BmobUser对象
                updateUserAvatar(url);
                progressBar.setVisibility(View.GONE);
                progressLoad.setVisibility(LinearLayout.GONE);
                openViewSet();
            }

            @Override
            public void onProgress(Integer value) {
                log("返回的上传进度（百分比）=" + value);
                // 返回的上传进度（百分比）
                progressBar.setProgress(value);

            }

            @Override
            public void onFailure(int code, String msg) {
                toast("上传文件失败：" + msg);
                progressBar.setVisibility(View.GONE);
                progressLoad.setVisibility(LinearLayout.GONE);
                openViewSet();
            }
        });
    }


    private void updateUserAvatar(final String url) {
        User u = new User();
        if (PHOTO_CODE == PHOTO_AVATOR_CODE) {
            u.setAvatar(url);
        } else if (PHOTO_CODE == PHOTO_BG_CODE) {
            log("保存用户背景");
            u.setUserbg(url);
        }
        log("updateUserAvatar方法" + PHOTO_CODE);
        updateUserData(u, new UpdateListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                if (PHOTO_CODE == PHOTO_AVATOR_CODE) {
                    toast("头像更新成功！");

                    /**
                     * 删除图片
                     */
                    File tempFile = new File(MyAvatarDir);
                    if (tempFile != null && tempFile.exists()) {
                        tempFile.delete();
                    }
                    User currentUser = BmobUser.getCurrentUser(UserInfoActivity.this, User.class);
                    currentUser.update(UserInfoActivity.this);
                    // 更新头像
               /* Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                iv_avator.setImageBitmap(bitmap);*/

                    ViewUtil.setImageView(url, R.mipmap.head, iv_avator);

                } else if (PHOTO_CODE == PHOTO_BG_CODE) {
                    toast("BG更新成功！");
                    ViewUtil.setImageView(url, R.drawable.temp_bg, iv_bg, false);
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                if (PHOTO_CODE == PHOTO_AVATOR_CODE) {
                    toast("头像更新失败：" + msg);
                    log("头像更新失败：" + msg);
                } else if (PHOTO_CODE == PHOTO_BG_CODE) {
                    toast("bg更新失败：" + msg);
                    log("bg更新失败：" + msg);
                }

            }
        });
    }


    String path;


    /**
     * 没有截图的方法
     */

    public void NoCropImage(Bitmap bit) {

        iv_bg.setImageBitmap(bit);


        // 保存图片
        String filename = new SimpleDateFormat("yyMMddHHmmss")
                .format(new Date()) + ".png";
        path = MyAvatarDir + filename;
        PhotoUtil.saveBitmap(MyAvatarDir, filename, bit, true);

        // 上传头像
        log("准备裁图失败后，使用压缩后的图片");

        if (bit != null && bit.isRecycled())

        {
            bit.recycle();
        }

        // 初始化文件路径
        filePath = "";
        // 上传头像
        uploadAvatar();

    }

    /**
     * 我的头像保存目录
     */
    public static String MyAvatarDir = "/sdcard/bmobimdemo/avatar/";

    /**
     * 保存裁剪的头像
     *
     * @param data
     */
    private void saveCropAvator(Intent data) {
        log("saveCropAvator保存裁剪的头像");

        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            Log.i("life", "avatar - bitmap = " + bitmap);
            if (bitmap != null) {
                if (PHOTO_CODE == PHOTO_AVATOR_CODE) {
                    //裁剪为圆角
                    bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
                    if (isFromCamera && degree != 0) {
                        bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
                    }
                }


                if (PHOTO_CODE == PHOTO_AVATOR_CODE) {
                    iv_avator.setImageBitmap(bitmap);
                } else {
                    iv_bg.setImageBitmap(bitmap);
                }

                // 保存图片
                String filename = new SimpleDateFormat("yyMMddHHmmss")
                        .format(new Date()) + ".png";
                path = MyAvatarDir + filename;
                PhotoUtil.saveBitmap(MyAvatarDir, filename, bitmap, true);
                // 上传头像
                log("准备上传图像");
                if (bitmap != null && bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
    }


    /**
     * 更新用户资料到服务器
     *
     * @param user
     * @param listener
     */
    private void updateUserData(User user, UpdateListener listener) {
        //connect server
        User current = BmobUser.getCurrentUser(this, User.class);
        log("updateUserData");
        user.setObjectId(current.getObjectId());
        user.update(this, listener);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    public void afterTextChanged(Editable s) {

        if (!s.toString().toString().equals(back_name)) {
            openCommit(true);
        } else {
            openCommit(false);
        }
    }

    @Override
    public void onClick(View v) {

        log("v  ID=" + v.getId() + "R =" + R.id.iv_avator);
        switch (v.getId()) {
            case R.id.tv_tm:
                onTmClick(v);
                break;
            case R.id.tv_sex:
                onSexClick(v);
                break;

            case R.id.edit_bg:
                PHOTO_CODE = PHOTO_BG_CODE;
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this).setTitle("是否修改个性背景").setPositiveButton("我要修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goGallery();
                    }
                });
                builder.setNegativeButton("取消", null).show();
                break;
            case R.id.iv_avator:
                log("onClick R.id.iv_avatar");
                PHOTO_CODE = PHOTO_AVATOR_CODE;
                goGallery();
                break;
        }
    }

    /**
     * 确定修改资料
     *
     * @param v
     */
    @OnClick(R.id.btn_edit)
    public void onCommit(View v) {
        User u = new User();

        if (TextUtils.isEmpty(tv_name.getText().toString().trim())) {
            toast("昵称不能为空哦");
            return;
        } else {
            if (!back_name.equals(tv_name.getText().toString().trim())) {
                u.setNickname(tv_name.getText().toString().trim());
                u.isEdit = true;
            }
        }
        if (!tm_back.equals(tvTm.getText().toString().trim())) {
            u.setSchool_tm(tvTm.getText().toString().trim());
            u.isEdit = true;
        }
        if (!back_sex.equals(tvSex.getText().toString().trim())) {

            String newsex = tvSex.getText().toString().trim();
            if (newsex.equals("男")) {
                u.setSex(true);
            } else {
                u.setSex(false);
            }
            u.isEdit = true;
        }

        if (u.isEdit) {
            updateUserData(u, new UpdateListener() {
                @Override
                public void onSuccess() {
                    log("修改个人资料成功");
                    toast("修改个人资料成功");
                    btnEdit.setVisibility(Button.GONE);
                }

                @Override
                public void onFailure(int i, String s) {
                    log("修改个人资料失败");
                    toast("修改个人资料失败");
                }
            });
        }
    }
}
