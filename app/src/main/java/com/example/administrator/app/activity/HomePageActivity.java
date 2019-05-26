package com.example.administrator.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.app.R;
import com.example.administrator.app.activity.fragment_home_page.FragmentDynamic;
import com.example.administrator.app.activity.fragment_home_page.FragmentHome;
import com.example.administrator.app.activity.fragment_home_page.FragmentLike;
import com.example.administrator.app.adapter.NewsAdapter;
import com.example.administrator.app.adapter.RankFragmentAdapter;
import com.example.administrator.app.bean.News;
import com.example.administrator.app.bean.Relation;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import devlight.io.library.ntb.NavigationTabBar;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener{
    private CircleImageView header_img;
    private final static int IMAGE_PICKER = 0;
    private final static int REQUEST_CODE_SELECT = 100;
    private ImagePicker imagePicker;
    private PromptDialog promptDialog;
    private PromptButton cancle;

    private ImageView bg_img,back_img;
    private TextView username_text,friends_text,signature_text,username_title_text, edit_userinfo, chat_text;

    private ViewPager viewpager;
    private SmartRefreshLayout refreshLayout;
    private ButtonBarLayout buttonBarLayout;
    private CollapsingToolbarLayout collapsing_toolbar;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private int mOffset = 0;
    boolean isblack = false;//状态栏字体是否是黑色
    boolean iswhite = true;//状态栏字体是否是亮色
    private NavigationTabStrip navigationTabBar ;
    private String username = "";  //当前主页用户的用户名
    protected ImmersionBar mImmersionBar;
    private int fans = 0,likes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initImmersionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initView();
        initListener();

    }

    private void initView() {

        appbar = (AppBarLayout) findViewById(R.id.appbar);
        viewpager = (ViewPager) findViewById(R.id.view_pager);
        bg_img = (ImageView) findViewById(R.id.bg_img);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        buttonBarLayout = (ButtonBarLayout) findViewById(R.id.buttonBarLayout);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back_img = (ImageView) findViewById(R.id.iv_back);
        back_img.setOnClickListener(this);

        header_img = findViewById(R.id.header_img);
        header_img.setOnClickListener(this);
        username_text = (TextView) findViewById(R.id.username);
        username_title_text = (TextView) findViewById(R.id.username_title_text);
        friends_text = (TextView) findViewById(R.id.friends);
        signature_text = (TextView) findViewById(R.id.signature);
        edit_userinfo = (TextView) findViewById(R.id.edit_userinfo);
        edit_userinfo.setOnClickListener(this);
        chat_text = (TextView) findViewById(R.id.chat_text);
        chat_text.setOnClickListener(this);

        //初始化沉浸式
        mImmersionBar.titleBar(toolbar).init();

        navigationTabBar = (NavigationTabStrip) findViewById(R.id.navigationTabStrip);
        ArrayList<Fragment> listfragment=new ArrayList<Fragment>(); //new一个List<Fragment>
        Fragment f1 = new FragmentHome();
        Fragment f2 = new FragmentDynamic();
        Fragment f3 = new FragmentLike();

        listfragment.add(f1);
        listfragment.add(f2);
        listfragment.add(f3);

        FragmentManager fm = getSupportFragmentManager();
        RankFragmentAdapter mfpa=new RankFragmentAdapter(fm, listfragment); //new myFragmentPagerAdater记得带上两个参数

        viewpager.setAdapter(mfpa);
        viewpager.setCurrentItem(0); //设置当前页是第一页
        navigationTabBar.setViewPager(viewpager);

        initData();

        ((FragmentDynamic) f2).setUsername(username);

        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        //imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        //imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        //imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        //imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        //imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

        promptDialog = new PromptDialog(HomePageActivity.this);
        cancle = new PromptButton("取消", null);
        cancle.setTextColor(Color.parseColor("#0076ff"));


    }

    @Override
    protected void onResume() {
        super.onResume();
        //initData();
    }


    private void initData(){
        username = getIntent().getStringExtra("name");
        UserInfo myInfo = JMessageClient.getMyInfo();
        JMessageClient.getUserInfo(username, "c08a6fa3ea3bb53fccdcd60d", new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (userInfo.getAvatarFile() != null) {
                    Glide.with(HomePageActivity.this)
                            .load(userInfo.getAvatarFile())
                            .apply(bitmapTransform(new BlurTransformation(14, 2)))
                            .into(bg_img);

                    Glide.with(HomePageActivity.this)
                            .load(userInfo.getAvatarFile())
                            .into(header_img);
                }else{
                    Glide.with(HomePageActivity.this)
                            .load(R.drawable.header_default)
                            .apply(bitmapTransform(new BlurTransformation(14, 2)))
                            .into(bg_img);

                    Glide.with(HomePageActivity.this)
                            .load(R.drawable.header_default)
                            .into(header_img);
                }
            }
        });


        username_text.setText(username);
        username_title_text.setText(username);
        if (!username.equals(myInfo.getUserName())){
            //当前处于访问别人的主页状态
            edit_userinfo.setVisibility(View.GONE);
            chat_text.setVisibility(View.VISIBLE);
        }else{
            edit_userinfo.setVisibility(View.VISIBLE);
            chat_text.setVisibility(View.GONE);
        }

        if (!myInfo.getSignature().equals("")){
            signature_text.setText(myInfo.getSignature());
        }

        getFanData();
        getLikeData();

    }

    private void getLikeData(){
        BmobQuery<Relation> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("fan",username);
        bmobQuery.findObjects(new FindListener<Relation>(){

            @Override
            public void done(List<Relation> list, BmobException e) {
                if(e==null){
                    likes = list.size();
                    friends_text.setText("关注 "+likes+" | 粉丝 "+fans);

                }else{
                    ToastUtils.showShort("失败");

                }
            }
        });
    }

    private void getFanData(){
        BmobQuery<Relation> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("like",username);
        bmobQuery.findObjects(new FindListener<Relation>(){

            @Override
            public void done(List<Relation> list, BmobException e) {
                if(e==null){
                    fans = list.size();
                    friends_text.setText("关注 "+likes+" | 粉丝 "+fans);

                }else{
                    ToastUtils.showShort("失败");

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.edit_userinfo:
                ToastUtils.showShort("编辑资料");
                Intent intent0 = new Intent(HomePageActivity.this, UserInfoActivity.class);
                startActivity(intent0);
                break;
            case R.id.chat_text:
                Intent intent = new Intent(HomePageActivity.this, ChatActivity.class);
                intent.putExtra("chatusername",username);
                startActivity(intent);
                break;
            case R.id.header_img:
                promptDialog.showAlertSheet("", true, cancle,
                        new PromptButton("打开相册", new PromptButtonListener(){
                            @Override
                            public void onClick(PromptButton promptButton) {
                                Intent intent1 = new Intent(HomePageActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                            }
                        }), new PromptButton("打开相机", new PromptButtonListener(){
                            @Override
                            public void onClick(PromptButton promptButton) {
                                Intent intent = new Intent(HomePageActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                            }
                        }));
                break;

        }
    }

   /* private void setClickListener(){
        header_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                promptDialog.showAlertSheet("", true, cancle,
                        new PromptButton("打开相册", new PromptButtonListener(){
                            @Override
                            public void onClick(PromptButton promptButton) {
                                Intent intent1 = new Intent(HomePageActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                            }
                        }), new PromptButton("打开相机", new PromptButtonListener(){
                            @Override
                            public void onClick(PromptButton promptButton) {
                                Intent intent = new Intent(HomePageActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                            }
                        }));
            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if(data != null && requestCode == REQUEST_CODE_SELECT){
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                LogUtils.d(Uri.fromFile(new File(images.get(0).path))+"\n"+images.get(0).path);
                Glide.with(HomePageActivity.this)
                        .load(Uri.fromFile(new File(images.get(0).path)))
                        .into(header_img);
                Glide.with(HomePageActivity.this)
                        .load(Uri.fromFile(new File(images.get(0).path)))
                        .apply(bitmapTransform(new BlurTransformation( 14, 2)))
                        .into(bg_img);
                JMessageClient.updateUserAvatar(new File(images.get(0).path), new BasicCallback(){
                    @Override
                    public void gotResult(int i, String s) {
                        ToastUtils.showShort("上传成功");
                    }
                });
            }
        }
    }

    @SuppressLint("ParcelCreator")
    class PicassoImageLoader implements ImageLoader , Serializable, Parcelable {

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

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar()  //透明状态栏，不写默认透明色
                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
                .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
                .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .flymeOSStatusBarFontColor(R.color.black)  //修改flyme OS状态栏字体颜色
                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
                .fitsSystemWindows(true)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色
                .supportActionBar(true) //支持ActionBar使用
                .removeSupportAllView() //移除全部view支持
                .addTag("tag")  //给以上设置的参数打标记
                .getTag("tag")  //根据tag获得沉浸式参数
                .reset()  //重置所以沉浸式参数
                .keyboardEnable(false)  //解决软键盘与底部输入框冲突问题，默认为false
                .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
//                        LogUtils.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
                    }
                })
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
                .init();  //必须调用方可沉浸式
    }

    private void initListener() {
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                bg_img.setTranslationY(mOffset);
            }
            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;
                bg_img.setTranslationY(mOffset);
            }
        });

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                bg_img.setTranslationY(verticalOffset);
                //200是appbar的高度
                if (Math.abs(verticalOffset) == DensityUtil.dp2px(200) - toolbar.getHeight()) {//关闭
                    if (iswhite) {//变黑
                        if (ImmersionBar.isSupportStatusBarDarkFont()) {
                            mImmersionBar.statusBarDarkFont(true).init();
                            isblack = true;
                            iswhite = false;
                        }
                    }
                    buttonBarLayout.setVisibility(View.VISIBLE);
                    collapsing_toolbar.setContentScrimResource(R.color.white);
                    back_img.setBackgroundResource(R.drawable.back);
                    //iv_date.setBackgroundResource(R.drawable.back);
//                    toolbar.setVisibility(View.VISIBLE);
                } else {  //展开
                    if (isblack) { //变白
                        mImmersionBar.statusBarDarkFont(false).init();
                        isblack = false;
                        iswhite = true;
                    }
                    buttonBarLayout.setVisibility(View.INVISIBLE);
                    collapsing_toolbar.setContentScrimResource(R.color.transparent);
                    back_img.setBackgroundResource(R.drawable.back_title);
                    //iv_date.setBackgroundResource(R.drawable.back);
//                    toolbar.setVisibility(View.INVISIBLE);
                }
            }
        });

       // setClickListener();
    }

}
