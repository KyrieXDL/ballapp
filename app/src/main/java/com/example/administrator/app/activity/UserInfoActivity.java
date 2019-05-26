package com.example.administrator.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.administrator.app.R;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView cancel_img, save_img, add_addr_img, add_birth_img;
    private TextView add_addr_text, add_birth_text;
    private CircleImageView header_img;
    private RelativeLayout header_layout;
    private MaterialEditText nickname_edit, signature_edit;
    private PromptDialog promptDialog;
    private PromptButton cancle;
    private ImagePicker imagePicker;
    private final static int REQUEST_CODE_SELECT = 100;
    private CityPickerView mPicker=new CityPickerView();
    private UserInfo myInfo = JMessageClient.getMyInfo();
    private String signature = "", address = "";
    private RadioGroup radioGroup;
    private RadioButton male_radioButton, female_radioButton;
    private String img_path = "";
    private int isMale = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initViews();
        initData();
    }

    private void initViews(){
        mPicker.init(this);

        cancel_img = findViewById(R.id.cancel_img);
        cancel_img.setOnClickListener(this);
        save_img = findViewById(R.id.save_img);
        save_img.setOnClickListener(this);
        header_layout = findViewById(R.id.relative_layout);
        header_layout.setOnClickListener(this);
        nickname_edit = findViewById(R.id.editText_username);
        signature_edit = findViewById(R.id.editText_signature);
        header_img = findViewById(R.id.header_img);

        add_addr_img = findViewById(R.id.add_addr_img);
        add_addr_img.setOnClickListener(this);
        add_addr_text = findViewById(R.id.add_addr_text);
        add_addr_text.setOnClickListener(this);
        add_birth_img = findViewById(R.id.add_birth_img);
        add_birth_img.setOnClickListener(this);
        add_birth_text = findViewById(R.id.add_birth_text);
        add_birth_text.setOnClickListener(this);

        radioGroup = findViewById(R.id.radio_group);
        male_radioButton = findViewById(R.id.male_radio);
        male_radioButton.setOnClickListener(this);
        female_radioButton = findViewById(R.id.female_radio);
        female_radioButton.setOnClickListener(this);



        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        //imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状

        promptDialog = new PromptDialog(UserInfoActivity.this);
        cancle = new PromptButton("取消", null);
        cancle.setTextColor(Color.parseColor("#0076ff"));

        CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);

//监听选择点击事件及返回结果
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                address = province+" "+city+" "+district;
                //add_addr_img.setVisibility(View.GONE);
                add_addr_text.setText(address);
                //省份province
                //城市city
                //地区district
            }

            @Override
            public void onCancel() {
                //ToastUtils.showLongToast(this, "已取消");
            }
        });

    }

    private void updateUserInfo(){
        if (!img_path.equals("")) {
            JMessageClient.updateUserAvatar(new File(img_path), new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {

                }
            });
        }
        signature = signature_edit.getText().toString();
        UserInfo userInfo = JMessageClient.getMyInfo();
        if (!address.equals("")) {
            userInfo.setAddress(address);
        }
        if (!signature.equals("")) {
            userInfo.setSignature(signature);
        }

        if (isMale == 1){
            userInfo.setGender(UserInfo.Gender.male);
        }else if (isMale == 2){
            userInfo.setGender(UserInfo.Gender.female);
        }else{
            userInfo.setGender(UserInfo.Gender.unknown);
        }

        JMessageClient.updateMyInfo(UserInfo.Field.all, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                LogUtils.d("fuck: "+i+" "+s);
                ToastUtils.showShort("更新成功");
            }
        });



    }

    private void initData(){
        if (myInfo.getAvatarFile() != null) {

            Glide.with(UserInfoActivity.this)
                    .load(myInfo.getAvatarFile())
                    .into(header_img);
        }
        nickname_edit.setText(myInfo.getUserName());
        if (!myInfo.getSignature().equals("")){
            signature_edit.setText(myInfo.getSignature());
        }

        if (!myInfo.getAddress().equals("")){
            add_addr_text.setText(myInfo.getAddress());
        }
        if (myInfo.getGender() == UserInfo.Gender.female){
            female_radioButton.setChecked(true);
        }else if (myInfo.getGender() == UserInfo.Gender.male){
            male_radioButton.setChecked(true);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_img:
                finish();
                break;
            case R.id.save_img:
                updateUserInfo();
                break;
            case R.id.relative_layout:
                promptDialog.showAlertSheet("", true, cancle,
                        new PromptButton("打开相册", new PromptButtonListener(){
                            @Override
                            public void onClick(PromptButton promptButton) {
                                Intent intent1 = new Intent(UserInfoActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                            }
                        }), new PromptButton("打开相机", new PromptButtonListener(){
                            @Override
                            public void onClick(PromptButton promptButton) {
                                Intent intent = new Intent(UserInfoActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                            }
                        }));
                break;

            case R.id.add_addr_img:

                break;
            case R.id.add_addr_text:
                mPicker.showCityPicker( );
                break;
            case R.id.add_birth_img:

                break;
            case R.id.add_birth_text:

                break;
            case R.id.male_radio:
                isMale = 1;
                break;
            case R.id.female_radio:
                isMale = 2;
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if(data != null && requestCode == REQUEST_CODE_SELECT){
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                LogUtils.d(Uri.fromFile(new File(images.get(0).path))+"\n"+images.get(0).path);
                Glide.with(UserInfoActivity.this)
                        .load(Uri.fromFile(new File(images.get(0).path)))
                        .into(header_img);
                img_path = images.get(0).path;
                /*JMessageClient.updateUserAvatar(new File(images.get(0).path), new BasicCallback(){
                    @Override
                    public void gotResult(int i, String s) {

                    }
                });*/
            }
        }
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

}
