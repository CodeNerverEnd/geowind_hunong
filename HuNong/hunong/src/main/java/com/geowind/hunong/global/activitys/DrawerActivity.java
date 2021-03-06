package com.geowind.hunong.global.activitys;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geowind.hunong.R;
import com.geowind.hunong.application.HunongApplication;
import com.geowind.hunong.drawer.AboutActivity;
import com.geowind.hunong.drawer.feedback.Credit.CreditActivity;
import com.geowind.hunong.drawer.HistoryTaskActivity;
import com.geowind.hunong.drawer.MeInfoActivity;
import com.geowind.hunong.drawer.SettingActivity;
import com.geowind.hunong.utils.BitmapLoader;
import com.geowind.hunong.utils.DialogCreator;
import com.geowind.hunong.utils.FileHelper;
import com.geowind.hunong.utils.MyConstants;
import com.geowind.hunong.utils.NativeImageLoader;
import com.geowind.hunong.utils.SpTools;
import com.geowind.hunong.view.CircleImageView;


import java.io.File;

/**
 * Created by zhangwen on 16-7-24.
 */
public class DrawerActivity extends AppCompatActivity {


    private static  final String TAG="DrawerActitvity";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    private TextView mTv_userName;
    private TextView mTv_exit;
    private CircleImageView mTakePhotoBtn;
    private TextView mUserNameTv;
    private TextView mNickNameTv;
    private TextView mTv_historTask;
    private TextView mTv_credit;
    private TextView mTv_presonalInfol;
    private TextView mTv_help;
    private TextView mTv_about;
    private ImageView mIv_atavar;
    private View.OnClickListener mListener;
    private final MyHandler myHandler = new MyHandler();
    private Dialog mDialog;
    private ProgressDialog mProgressDialog;
    private String mPath;
    private boolean mIsShowAvatar = false;
    private boolean mIsGetAvatar = false;
    private Uri mUri;
    // 裁剪后图片的宽(X)和高(Y), 720 X 720的正方形。
    private static int OUTPUT_X = 720;
    private static int OUTPUT_Y = 720;
    private TextView mTv_setting;
    protected float mDensity;
    protected int mDensityDpi;
    protected int mAvatarSize;
    protected int mWidth;
    protected int mHeight;
    private ImageView mIv_usertype;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
        mDensityDpi = dm.densityDpi;
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mAvatarSize = (int) (50 * mDensity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDrawer = (DrawerLayout) findViewById(R.id.dl_main);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        initView();
        initData();
        initEvnet();
    }

    private void initData() {
        String userType=SpTools.getString(DrawerActivity.this,MyConstants.USER_TYPE,"");

        if("0".equals(userType)){
            mIv_usertype.setImageResource(R.mipmap.status);
        }else {
            mIv_usertype.setImageResource(R.mipmap.status2);
        }
    }

    private void initEvnet() {
        mListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.iv_avatar://头像
                        View.OnClickListener listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.jmui_take_photo_btn:
                                        mDialog.cancel();
                                        takePhoto();
                                        break;
                                    case R.id.jmui_pick_picture_btn:
                                        mDialog.cancel();
                                        selectImageFromLocal();
                                        break;
                                }
                            }
                        };
                        mDialog = DialogCreator.createSetAvatarDialog(DrawerActivity.this, listener);
                        mDialog.show();
                        mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
                        break;
                    case R.id.tv_personal_info://个人信息
                       startOntherActivity(MeInfoActivity.class);
                        break;
                    case R.id.tv_setting://系统设置
                       startOntherActivity(SettingActivity.class);
                        break;
//			//退出登录 清除Notification，清除缓存
                    case R.id.tv_exit://注销
                        listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switch (view.getId()) {
                                    case R.id.jmui_cancel_btn:
                                        mDialog.cancel();
                                        break;
                                    case R.id.jmui_commit_btn:
                                        Logout();
                                        cancelNotification();
                                        NativeImageLoader.getInstance().releaseCache();
                                        finish();
                                        mDialog.cancel();
                                        break;
                                }
                            }
                        };
                        mDialog = DialogCreator.createLogoutDialog(DrawerActivity.this, listener);
                        mDialog.show();
                        mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
                        break;
                    case R.id.tv_credit://信用记录
                        startOntherActivity(CreditActivity.class);
                        break;
                    case R.id.tv_about://关于
                        startOntherActivity(AboutActivity.class);
                        break;
                    case R.id.tv_historyTask://历史任务
                        startOntherActivity(HistoryTaskActivity.class);
                        break;
                    case R.id.tv_help://帮助
                        startOntherActivity(GuideActivity.class);
                        break;
                }
            }
        };
        mTakePhotoBtn.setOnClickListener(mListener);
        mTv_about.setOnClickListener(mListener);
        mTv_historTask.setOnClickListener(mListener);
        mTv_credit.setOnClickListener(mListener);
        mTv_presonalInfol.setOnClickListener(mListener);
        mTv_help.setOnClickListener(mListener);
        mTv_about.setOnClickListener(mListener);
        mTv_exit.setOnClickListener(mListener);
        mTv_setting.setOnClickListener(mListener);

    }

    private void initView() {
        mTv_userName = (TextView)mDrawer.findViewById(R.id.tv_userName);
        mTv_exit = (TextView)mDrawer.findViewById(R.id.tv_exit);
        mTv_historTask = (TextView)mDrawer.findViewById(R.id.tv_historyTask);
        mTv_credit = (TextView)mDrawer.findViewById(R.id.tv_credit);
        mTv_presonalInfol = (TextView)mDrawer.findViewById(R.id.tv_personal_info);
        mTv_help = (TextView)mDrawer.findViewById(R.id.tv_help);
        mTv_about = (TextView) mDrawer.findViewById(R.id.tv_about);
        mTv_userName.setText(SpTools.getString(getApplicationContext(), MyConstants.USERNAME,""));
        mTakePhotoBtn= (CircleImageView) mDrawer.findViewById(R.id.iv_avatar);
        mTv_setting = (TextView) mDrawer.findViewById(R.id.tv_setting);
        mIv_usertype = (ImageView) findViewById(R.id.iv_userType);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == HunongApplication.REQUEST_CODE_TAKE_PHOTO) {
            String path = getPhotoPath();
            if (path != null) {
                File file = new File(path);
                if (file.isFile()) {
                    mUri = Uri.fromFile(file);
                    //拍照后直接进行裁剪
                    cropRawPhoto(mUri);
                    Intent intent = new Intent();
                    intent.putExtra("filePath", mUri.getPath());
                    intent.setClass(this, CropImageActivity.class);
                    startActivityForResult(intent, HunongApplication.REQUEST_CODE_CROP_PICTURE);
                }
            }
        } else if (requestCode == HunongApplication.REQUEST_CODE_SELECT_PICTURE) {
            if (data != null) {
                Uri selectedImg = data.getData();
                if (selectedImg != null) {
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = this.getContentResolver()
                            .query(selectedImg, filePathColumn, null, null, null);
                    if (null == cursor) {
                        String path = selectedImg.getPath();
                        File file = new File(path);
                        if (file.isFile()) {
                            copyAndCrop(file);
                            return;
                        } else {
                            Toast.makeText(this, this.getString(R.string.picture_not_found),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else if (!cursor.moveToFirst()) {
                        Toast.makeText(this, this.getString(R.string.picture_not_found),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String path = cursor.getString(columnIndex);
                    if (path != null) {
                        File file = new File(path);
                        if (!file.isFile()) {
                            Toast.makeText(this, this.getString(R.string.picture_not_found),
                                    Toast.LENGTH_SHORT).show();
                            cursor.close();
                        } else {
                            //如果是选择本地图片进行头像设置，复制到临时文件，并进行裁剪
                            copyAndCrop(file);
                            cursor.close();
                        }
                    }
                }
            }
        } else if (requestCode == HunongApplication.REQUEST_CODE_CROP_PICTURE) {
//            uploadUserAvatar(mUri.getPath());
            String path = data.getStringExtra("filePath");
            if (path != null) {
                uploadUserAvatar(path);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {

        super.onResume();
    }
    public void showPhoto(final Bitmap bitmap) {
        mTakePhotoBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mTakePhotoBtn.setImageBitmap(bitmap);
    }

    public void showPhoto(final String path) {

        Log.i("DrawActivity", "updated path:  " + path);
        final Bitmap bitmap = BitmapLoader.getBitmapFromFile(path, (int) (100 * mDensity),
                (int) (100 * mDensity));
        mTakePhotoBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mTakePhotoBtn.setImageBitmap(bitmap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //退出登录
    public void Logout() {
        SpTools.setBoolean(getApplicationContext(),MyConstants.ISLOGIN,false);
        SpTools.setString(getApplicationContext(),MyConstants.USER_TYPE,"");
        SpTools.setString(getApplicationContext(),MyConstants.USERNAME,"");
       startOntherActivity(LoginActivity.class);
    }

    public void cancelNotification() {
        NotificationManager manager = (NotificationManager) this.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }
    /**
     * 复制后裁剪文件
     * @param file 要复制的文件
     */
    private void copyAndCrop(final File file) {
        FileHelper.getInstance().copyAndCrop(file, this, new FileHelper.CopyFileCallback() {
            @Override
            public void copyCallback(Uri uri) {
                mUri = uri;
                cropRawPhoto(mUri);
                Intent intent = new Intent();
                intent.putExtra("filePath", mUri.getPath());
                intent.setClass(DrawerActivity.this, CropImageActivity.class);
                startActivityForResult(intent, HunongApplication.REQUEST_CODE_CROP_PICTURE);
            }
        });
    }

    /**
     * 裁剪图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", OUTPUT_X);
        intent.putExtra("outputY", OUTPUT_Y);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, HunongApplication.REQUEST_CODE_CROP_PICTURE);
    }

    public void uploadUserAvatar(final String path) {
        mProgressDialog = new ProgressDialog(DrawerActivity.this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(getString(R.string.updating_avatar_hint));
        mProgressDialog.show();
//        JMessageClient.updateUserAvatar(new File(path), new BasicCallback() {
//            @Override
//            public void gotResult(final int status, final String desc) {
//                mProgressDialog.dismiss();
//                if (status == 0) {
//                    Log.i(TAG, "Update avatar succeed path " + path);
//                    loadUserAvatar(path);
//                    //如果头像上传失败，删除剪裁后的文件
//                }else {
//                    HandleResponseCode.onHandle(getApplicationContext(), status, false);
//                    File file = new File(path);
//                    if (file.delete()) {
//                        Log.d(TAG, "Upload failed, delete cropped file succeed");
//                    }
//                }
//            }
//        });
    }

    //照相
    public void takePhoto() {
        if (FileHelper.isSdCardExist()) {
            mPath = FileHelper.createAvatarPath(SpTools.getString(getApplicationContext(),MyConstants.USERNAME,""));
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPath)));
            try {
                startActivityForResult(intent, HunongApplication.REQUEST_CODE_TAKE_PHOTO);
            } catch (ActivityNotFoundException anf) {
                Toast.makeText(this, this.getString(R.string.camera_not_prepared), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, this.getString(R.string.jmui_sdcard_not_exist_toast), Toast.LENGTH_SHORT).show();
        }
    }

    public String getPhotoPath() {
        return mPath;
    }

    //选择本地图片
    public void selectImageFromLocal() {
        if (FileHelper.isSdCardExist()) {
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
            } else {
                intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            startActivityForResult(intent, HunongApplication.REQUEST_CODE_SELECT_PICTURE);
        } else {
            Toast.makeText(this, getString(R.string.jmui_sdcard_not_exist_toast),
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void loadUserAvatar(String path) {
            mIsGetAvatar = true;
            showPhoto(path);
    }

    public  void startOntherActivity(Class c){
        Intent intent=new Intent(this,c);
        startActivity(intent);
    }
    public void refreshNickname(String newName) {
        showNickName(newName);
    }
    public void showNickName(String nickname) {
        mTv_userName.setText(nickname);
    }
    private static class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
