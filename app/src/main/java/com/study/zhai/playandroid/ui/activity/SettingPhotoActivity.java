package com.study.zhai.playandroid.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.log.LogUtils;
import com.study.zhai.playandroid.utils.ConstantUtil;
import com.study.zhai.playandroid.utils.FileUtils;
import com.study.zhai.playandroid.utils.PhotoUtils;
import com.study.zhai.playandroid.utils.ToastUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;


public class SettingPhotoActivity extends BaseActivity {

    @BindView(R.id.photo)
    ImageView photo;

    private static final String TAG = "SettingPhotoActivity";
    private static final int CODE_GALLERY_REQUEST = 0xa0; //打开相册的请求吗
    private static final int CODE_CAMERA_REQUEST = 0xa1; //打开相机的请求码
    private static final int CODE_RESULT_REQUEST = 0xa2; //裁剪后返回图片的结果码
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;
    // 拍照的原图存储的路径
    private File fileUri;
    private Uri imageUri;
    // 裁剪后的图片的路径
    private File fileCropUri;
    private Uri cropImageUri;
    // 拍照的图片名称
    private static final String ORIGIN_FILE_NAME = "/photo.jpg";
    // 裁剪后的图片名称
    private static final String CROP_FILE_NAME = "/crop_photo.jpg";

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_photo;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        boolean dirIsSuccess = FileUtils.createOrExistsDir(ConstantUtil.USER_PHOTO);
        LogUtils.d(TAG, "dirIsSuccess = " +dirIsSuccess);
        if (dirIsSuccess) {
            fileUri = new File(ConstantUtil.USER_PHOTO + ORIGIN_FILE_NAME);
            fileCropUri = new File(ConstantUtil.USER_PHOTO + CROP_FILE_NAME);
            // 优先加载本地保持的头像
            initUserPhoto(fileCropUri);
        } else {
            ToastUtil.show(this, "创建文件夹失败");
        }

    }

    @OnClick({R.id.takePic, R.id.takeGallery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.takePic: //相机
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(this, "com.zhai.fileprovider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                break;
            case R.id.takeGallery: //相册
                PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //适配7.0
                        newUri = FileProvider.getUriForFile(this, "com.zhai.fileprovider", new File(newUri.getPath()));
                    }
                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                // 裁剪完成后的回调
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        photo.setImageBitmap(bitmap);
                    }
                    break;
                default:
            }
        }
    }

    private void initUserPhoto(File fileCropUri) {
        if (FileUtils.isFileExists(fileCropUri)) {
            cropImageUri = Uri.fromFile(fileCropUri);
            Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
            if (bitmap != null) {
                photo.setImageBitmap(bitmap);
            }
        } else {
            // 未设置过头像
        }
    }
}
