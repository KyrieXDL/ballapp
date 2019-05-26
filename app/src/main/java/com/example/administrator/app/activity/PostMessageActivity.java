package com.example.administrator.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.example.administrator.app.R;
import com.example.administrator.app.bean.MessageUpdate;
import com.example.administrator.app.bean.TestImage;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.util.V;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

public class PostMessageActivity extends AppCompatActivity implements View.OnClickListener{
    private Context mContext = null;
    private EditText editText;
    private TextView textView;
    private LocationManager lm;
    private Button btn1;
    private final static int REQUEST_CODE_SELECT = 100;
    private ArrayList<ImageView> Images = new ArrayList<>();
    private ArrayList<BmobFile> bmobfile = new ArrayList<>();
    private TestImage testImage = new TestImage();
    private ArrayList<ImageItem> images;
    private ArrayList<String> imageFilePath = new ArrayList<>();
    private ImagePicker imagePicker;
    private static PermissionListener mListener;
    private static Activity activity ;
    private  SimpleDateFormat simpleDateFormat;
    private  Date date;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMERA"};
    private int size = 0;
    private UserInfo userInfo = JMessageClient.getMyInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);
        activity =this;
        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        date = new Date(System.currentTimeMillis());
        findImage();

        mContext = this;

        final MessageUpdate messageUpdate = new MessageUpdate();
        /*ImageView imageView = findViewById(R.id.im_add9);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopupWindow(v);

            }
        });*/
        textView = findViewById(R.id.tv);
        editText = findViewById(R.id.etContent);
        btn1 = findViewById(R.id.btn_1);

        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setMultiMode(true);
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状

        TextView textView3 = findViewById(R.id.tv_3);
        TextView textView1 = findViewById(R.id.tv_1);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"返回发现界面",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext,"上传数据",Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext,"准备进入onsuccess",Toast.LENGTH_SHORT).show();
                String[] listTemp = new String[10];
                for (int i=0;i<9;i++){
                    if (i<imageFilePath.size()) {
                        if (imageFilePath.get(i) != null) {
                            listTemp[i] = imageFilePath.get(i);
                        }
                    }
                }
                BmobFile.uploadBatch(listTemp, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> list1) {

                         if(list1.size() == 1)
                            {
                                testImage.setImage1(list.get(0));

                            }
                         if(list1.size()==2){
                             testImage.setImage2(list.get(1));
                         }
                         if(list1.size()==3){
                             testImage.setImage3(list.get(2));
                         }
                         if(list1.size()==4){
                             testImage.setImage4(list.get(3));
                         }
                         if(list1.size()==5){
                             testImage.setImage5(list.get(4));
                         }
                         if(list1.size()==6){
                             testImage.setImage6(list.get(5));
                         }
                         if(list1.size()==7){
                             testImage.setImage7(list.get(6));
                         }
                         if(list1.size()==8){
                             testImage.setImage8(list.get(7));
                         }
                         if(list1.size()==9){
                             testImage.setImage9(list.get(8));
                         }

                        if(list1.size()== images.size()){//如果数量相等，则代表文件全部上传完成
                            //Toast.makeText(mContext,"进入if2：",Toast.LENGTH_SHORT).show();
                            //do something
                           // TestImage testImage = new TestImage(list.get(0));
                            //testImage.setImage1(list.get(0));
                            testImage.setId(userInfo.getUserName());
                            testImage.setTime(simpleDateFormat.format(date));
                            testImage.setMessage(editText.getText().toString());

                            testImage.setName(userInfo.getUserName());
                            testImage.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e==null){
                                        Toast.makeText(mContext,"添加数据成功，返回objectId为："+s,Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(mContext,"创建数据失败"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(mContext,"创建数据失败"+ bmobFile.getFilename(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onProgress(int i, int i1, int i2, int i3) {

                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });

                finish();
            }

        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initPermission();

    }

    private void showPopupWindow(final View view)
    {
        final View contentView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow,null);
        TextView textView = contentView.findViewById(R.id.text_take_photo);
        TextView textView1 = contentView.findViewById(R.id.text_pick_photo);
        TextView textView2 = contentView.findViewById(R.id.text_cancle);
        RelativeLayout rl_layout = contentView.findViewById(R.id.rl_layout);

        final PopupWindow popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new poponDismissListener());
        //添加pop关闭事件
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ////点击空白处时，隐藏掉pop窗口
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
        backgroundAlpha(0.4f);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //if(v == view)
                //        {
                //        Toast.makeText(mContext,"111",Toast.LENGTH_SHORT).show();
                //popupWindow.dismiss();
                //    }
                //    else{Toast.makeText(mContext,"222",Toast.LENGTH_SHORT).show();}
                return false;
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Matisse
                        .from(PostMessageActivity.this)
                        .choose(MimeType.ofVideo())
                        .countable(true)
                        .maxSelectable(5)
                        .capture(false)
                        .captureStrategy(new CaptureStrategy(true, "com.example.administrator.app.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new GlideEngine())//图片加载引擎

                        .forResult(REQUEST_CODE_CHOOSE);//*/
                Intent intent = new Intent(PostMessageActivity.this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                popupWindow.dismiss();
                Toast.makeText(mContext,"照片",Toast.LENGTH_SHORT).show();
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(PostMessageActivity.this, ImageGridActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_SELECT);
       /*        Matisse .from(PostMessageActivity.this)
                        .choose(MimeType.ofImage())//图片类型
                        .countable(true)//true:选中后显示数字;false:选中后显示对号
                        .maxSelectable(5)//可选的最大数
                        .capture(true)//选择照片时，是否显示拍照
                        .captureStrategy(new CaptureStrategy(true, "com.example.administrator.app.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new GlideEngine())//图片加载引擎
                        .forResult(REQUEST_CODE_CHOOSE);//*/
                popupWindow.dismiss();
                Toast.makeText(mContext,"从相册",Toast.LENGTH_SHORT).show();
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        rl_layout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK)
                    popupWindow.dismiss();
                return false;
            }
        });
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }

    public void backgroundAlpha(float bgAlpha) {
        LogUtils.i("tag====backgroundAlpha==");
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
// TODO Auto-generated method stub
            LogUtils.i("tag====poponDismissListener==");
            backgroundAlpha(1f);
        }

    }
    /**
     * Request permission
     *
     */
    private void initPermission() {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE);
//        }


        if (Build.VERSION.SDK_INT >= 23) {//判断当前系统是不是Android6.0
            requestRuntimePermissions(PERMISSIONS_STORAGE, new PermissionListener() {
                @Override
                public void granted() {
                    //权限申请通过
                }

                @Override
                public void denied(List<String> deniedList) {
                    //权限申请未通过
                    for (String denied : deniedList) {
//                        if (denied.equals("android.permission.ACCESS_FINE_LOCATION")) {
//                            CustomToast.INSTANCE.showToast(SDK_WebApp.this, "定位失败，请检查是否打开定位权限！");
//                        } else {
//                            CustomToast.INSTANCE.showToast(SDK_WebApp.this, "没有文件读写权限,请检        查是否打开！");
//                        }
                    }
                }
            });
        }


    }

    public void findImage()
    {
        Images.add(findViewById(R.id.im_add1));
        Images.add(findViewById(R.id.im_add2));
        Images.add(findViewById(R.id.im_add3));
        Images.add(findViewById(R.id.im_add4));
        Images.add(findViewById(R.id.im_add5));
        Images.add(findViewById(R.id.im_add6));
        Images.add(findViewById(R.id.im_add7));
        Images.add(findViewById(R.id.im_add8));
        Images.add(findViewById(R.id.im_add9));

        for (int i=0;i<9;i++){
            Images.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_add1:
                if (size == 0 ){
                    showPopupWindow(v);
                }
                break;
            case R.id.im_add2:
                if (size == 1 ){
                    showPopupWindow(v);
                }
                break;
            case R.id.im_add3:
                if (size == 2 ){
                    showPopupWindow(v);
                }
                break;
            case R.id.im_add4:
                if (size == 3 ){
                    showPopupWindow(v);
                }
                break;
            case R.id.im_add5:
                if (size == 4 ){
                    showPopupWindow(v);
                }
                break;
            case R.id.im_add6:
                if (size == 5 ){
                    showPopupWindow(v);
                }
                break;
            case R.id.im_add7:
                if (size == 6 ){
                    showPopupWindow(v);
                }
                break;
            case R.id.im_add8:
                if (size == 7 ){
                    showPopupWindow(v);
                }
                break;
            case R.id.im_add9:
                if (size == 8 ){
                    showPopupWindow(v);
                }
                break;

        }
    }

    public void setBmobFile()
    {
        for(int i = 0;i<=8;i++) {
            if (i<imageFilePath.size()) {
                if (imageFilePath.get(i) != null) {
                    bmobfile.add(new BmobFile(new File(imageFilePath.get(i))));
                }
            }
        }
    }

   private void setAddImg(){
       size = imageFilePath.size();

       if (size<9) {
           ImageView imageView = (ImageView) Images.get(size);
           imageView.setVisibility(View.VISIBLE);
           imageView.setImageDrawable(getResources().getDrawable(R.drawable.addphoto));
       }
       //Glide.with(this).load(R.drawable.addphoto).into(Images.get(size+1));


   }
//    public void ifNull()
//    {
//        if(Images.get(0).getDrawable()==null) {
//            Images.get(0).setVisibility(View.GONE);
//        }
//        if(Images.get(1).getDrawable()==null) {
//            Images.get(1).setVisibility(View.GONE);
//        }
//        if(Images.get(2).getDrawable()==null) {
//            Images.get(2).setVisibility(View.GONE);
//        }
//        if(Images.get(3).getDrawable()==null) {
//            Images.get(3).setVisibility(View.GONE);
//        }
//        if(Images.get(3).getDrawable()==null) {
//            Images.get(3).setVisibility(View.GONE);
//        }
//        if(Images.get(4).getDrawable()==null) {
//            Images.get(4).setVisibility(View.GONE);
//        }
//        if(Images.get(5)==null) {
//            Images.get(5).setVisibility(View.GONE);
//        }
//        if(Images.get(6).getDrawable()==null) {
//            Images.get(6).setVisibility(View.GONE);
//        }
//        if(Images.get(7).getDrawable()==null) {
//            Images.get(7).setVisibility(View.GONE);
//        }
//        if(Images.get(8).getDrawable()==null) {
//            Images.get(8).setVisibility(View.GONE);
//        }
//
//    }
//    public void ifFull()
//    {
//        if(Images.get(0).getDrawable()!=null) {
//            Images.get(0).setVisibility(View.VISIBLE);
//        }
//        if(Images.get(1).getDrawable()!=null) {
//            Images.get(1).setVisibility(View.VISIBLE);
//        }
//        if(Images.get(2).getDrawable()!=null) {
//            Images.get(2).setVisibility(View.VISIBLE);
//        }
//        if(Images.get(3).getDrawable()!=null) {
//            Images.get(3).setVisibility(View.VISIBLE);
//        }
//        if(Images.get(4).getDrawable()!=null) {
//            Images.get(4).setVisibility(View.VISIBLE);
//        }
//        if(Images.get(5).getDrawable()!=null) {
//            Images.get(5).setVisibility(View.VISIBLE);
//        }
//        if(Images.get(6).getDrawable()!=null) {
//            Images.get(6).setVisibility(View.VISIBLE);
//        }
//        if(Images.get(7).getDrawable()!=null) {
//            Images.get(7).setVisibility(View.VISIBLE);
//        }
//        if(Images.get(8).getDrawable()!=null) {
//            Images.get(8).setVisibility(View.VISIBLE);
//        }
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(mContext,"测试是否进入result",Toast.LENGTH_SHORT).show();
        if(resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                /*Glide.with(PostMessageActivity.this)
                        .load(R.drawable.addphoto)
                        .into(Images.get(images.size()));*/
                for (int j = 0; j < 9; j++) {
                    if (j<images.size()) {
                        if (images.get(j) != null) {
                            Images.get(j).setVisibility(View.VISIBLE);
                            Glide.with(PostMessageActivity.this)
                                    .load(Uri.fromFile(new File(images.get(j).path)))
                                    .into(Images.get(j));
                            imageFilePath.add(images.get(j).path);
                            Toast.makeText(mContext, images.get(j).path, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Images.get(j).setVisibility(View.GONE);
                    }
                }

                setAddImg();

            }
        }
        setBmobFile();


    }
    public void requestPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permissions[i]);
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                        Toast.makeText(this, "没有开启权限将会导致部分功能不可使用", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ActivityCompat.requestPermissions(this, list.toArray(new String[permissions.length]), 0);
        }
    }

    /**
     * 申请权限
     */
    public static void requestRuntimePermissions(
            String[] permissions, PermissionListener listener) {
        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        // 遍历每一个申请的权限，把没有通过的权限放在集合中
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            } else {
                mListener.granted();
            }
        }
        // 申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionList.toArray(new String[permissionList.size()]), 1);
        }
    }

    /**
     * 申请后的处理
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            List<String> deniedList = new ArrayList<>();
            // 遍历所有申请的权限，把被拒绝的权限放入集合
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    mListener.granted();
                } else {
                    deniedList.add(permissions[i]);
                }
            }
            if (!deniedList.isEmpty()) {
                mListener.denied(deniedList);
            }
        }
    }
    public interface PermissionListener {
        void granted();
        void denied(List<String> deniedList);
    }

    @SuppressLint("ParcelCreator")
    class PicassoImageLoader implements ImageLoader, Serializable, Parcelable {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            Picasso.with(activity)
                    .load(Uri.fromFile(new File(path)))
                    .placeholder(R.drawable.ic_default_image)
                    .error(R.drawable.ic_default_image)
                    .resize(width, height)
                    .centerInside()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(imageView);
        }

        @Override
        public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
            Picasso.with(activity)
                    .load(Uri.fromFile(new File(path)))
                    .resize(width, height)
                    .centerInside()
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(imageView);
        }


        @Override
        public void clearMemoryCache() {
            //这里是清除缓存的方法,根据需要自己实现
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }


//    private void UploadImage() {
//        final String[] filePaths = new String[images.size()];
//        for (int i = 0; i < images.size(); i++) {
//            filePaths[i] = images.get(i).path;
//        }
//        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
//
//            @Override
//            public void onSuccess(List<BmobFile> files, List<String> urls) {
//                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
//                //2、urls-上传文件的完整url地址
//                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
//                    Toast.makeText(PostMessageActivity.this, "dwl", Toast.LENGTH_SHORT).show();
////                    UploadText(urls);
//                }
//            }
//
//            @Override
//            public void onError(int statuscode, String errormsg) {
//                Toast.makeText(PostMessageActivity.this, "错误码" + statuscode + ",错误描述：" + errormsg, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
//                //1、curIndex--表示当前第几个文件正在上传
//                //2、curPercent--表示当前上传文件的进度值（百分比）
//                //3、total--表示总的上传文件数
//                //4、totalPercent--表示总的上传进度（百分比）
//            }
//        });
//
//
//    }
}
