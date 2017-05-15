package com.ampm.camerano;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button open_camera;
    private SurfaceView mySurfaceView;
    private SurfaceHolder myHolder;
    private Camera myCamera;
    private ImageView mImageView01;
    int page = 0;
    ArrayList<String > list = new ArrayList<>();
    private String fileName;
    private int MSG_DO_AUTO_FOCUS=1;
    private final Handler mCameraAutoFocusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (myCamera != null) {
                        //自动对焦
                        myCamera.autoFocus(myAutoFocus);
                    }
                    break;
            }
        }
    };
    private boolean isPreving =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initview();
    }

    private void initview() {
        //初始化surfaceview
        mImageView01 = (ImageView) findViewById(R.id.mImageView01);
        mySurfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);
        //初始化surfaceholder
        myHolder = mySurfaceView.getHolder();
        myHolder.setFixedSize(320, 240);
        myHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
;
        open_camera = (Button) findViewById(R.id.open_camera);

        new Thread(new Runnable() {
            @Override
            public void run() {
                open_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        list.add((1+page)+".jpg");
                        page++;
                        camer_open();
                        myCamera.startPreview();
                        isPreving = true;
                        mCameraAutoFocusHandler.sendEmptyMessageDelayed(MSG_DO_AUTO_FOCUS,3000);
                        //对焦后拍照
//                        myCamera.takePicture(null, null, myPicCallback);


                    }
                });
            }
        }).start();


    }

    private void camer_open() {
        myCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        //这里的myCamera为已经初始化的Camera对象
        Camera.Parameters parameters = myCamera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);//设置照片的输出格式
        parameters.set("jpeg-quality", 85);//照片质量
        myCamera.setParameters(parameters);
        try {
            myCamera.setPreviewDisplay(myHolder);
//自动对焦
//            myCamera.autoFocus(myAutoFocus);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFocusing =true;
    //自动对焦回调函数(空实现)
    private Camera.AutoFocusCallback myAutoFocus = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success&&isPreving==true) {
                myCamera.cancelAutoFocus();
                myCamera.takePicture(null, null, myPicCallback);
            } else {
                mCameraAutoFocusHandler.sendEmptyMessageDelayed(MSG_DO_AUTO_FOCUS,3000);
            }
        }
    };
    private String picname;
    //拍照成功回调函数
    private Camera.PictureCallback myPicCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
                /* onPictureTaken传入的第一个参数即为相片的byte */
            Bitmap bm = BitmapFactory.decodeByteArray
                    (data, 0, data.length);
            saveBitmap(bm);
       /* 创建新文件 */
//            picname = "sdcard/1234566.jpg";//要保存在哪里，路径你自己设
//            File myCaptureFile = new File(picname);
//            try
//            {
//                BufferedOutputStream bos = new BufferedOutputStream
//                        (new FileOutputStream(myCaptureFile));
//
//         /* 采用压缩转档方法 */
//                bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//
//         /* 调用flush()方法，更新BufferStream */
//                bos.flush();
//
//         /* 结束OutputStream */
//                bos.close();

         /* 将拍照下来且存储完毕的图文件，显示出来 */
                mImageView01.setImageBitmap(bm);

         /* 显示完图文件，立即重置相机，并关闭预览 */
//                resetCamera();

//            }
//            catch (Exception e)
//            {
////                Log.e(TAG, e.getMessage());
//            }
        }
    };
    public void saveBitmap(Bitmap bm) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "LOL");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        for(int i = 0; i < list.size(); i++) {

            fileName = list.get(i);
        }
//        fileName = fileName.substring(fileName.length()-18,fileName.length());
        File pictureFile = new File(appDir, fileName);
        if (pictureFile.exists()) {
            pictureFile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(pictureFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
//            Snackbar.make(findViewById(android.R.id.content), "保存图片成功"+pictureFile.getAbsolutePath(), Snackbar.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    pictureFile .getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = Uri.fromFile(pictureFile);intent.setData(uri);
        sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //完成拍照后关闭Activity
        finish();
        myCamera.stopPreview();
        myCamera.release();
        myCamera = null;
    }
}
