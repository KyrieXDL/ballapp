package com.example.administrator.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.administrator.app.R;
import com.example.administrator.app.activity.AddFriendActivity;
import com.example.administrator.app.activity.HomePageActivity;
import com.example.administrator.app.activity.SettingActivity;
import com.example.administrator.app.bean.Relation;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class FragmentMe extends Fragment implements View.OnClickListener{

    private ImageView bg_img;
    private CircleImageView header_img;
    private ImageView setting_img, add_friend_img;
    private RelativeLayout user_info_layout;
    private TextView username_text, like_sum_text, fans_sum_text, signature_text;
    private List<Relation> relationList = new ArrayList<>();
    private UserInfo userInfo = JMessageClient.getMyInfo();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // LogUtils.d("这是个人信息界面");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initViews(view);
        getLikeData();
        getFanData();
        return view;
    }


    private void initData(){
        UserInfo myInfo = JMessageClient.getMyInfo();
        if (myInfo.getAvatarFile() != null) {
            Glide.with(getActivity())
                    .load(myInfo.getAvatarFile())
                    .apply(bitmapTransform(new BlurTransformation(14, 2)))
                    .into(bg_img);

            Glide.with(getActivity())
                    .load(myInfo.getAvatarFile())
                    .into(header_img);
        }else{
            Glide.with(getActivity())
                    .load(R.drawable.header_default)
                    .apply(bitmapTransform(new BlurTransformation(14, 2)))
                    .into(bg_img);

            Glide.with(getActivity())
                    .load(R.drawable.header_default)
                    .into(header_img);
        }

        if (!myInfo.getSignature().equals("")){
            signature_text.setText(myInfo.getSignature());
        }
        username_text.setText(myInfo.getUserName());
    }

    private void getLikeData(){
        BmobQuery<Relation> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("fan",userInfo.getUserName());
        bmobQuery.findObjects(new FindListener<Relation>(){

            @Override
            public void done(List<Relation> list, BmobException e) {
                if(e==null){
                    like_sum_text.setText(list.size()+"");

                }else{
                    ToastUtils.showShort("失败");

                }
            }
        });
    }

    private void getFanData(){
        BmobQuery<Relation> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("like",userInfo.getUserName());
        bmobQuery.findObjects(new FindListener<Relation>(){

            @Override
            public void done(List<Relation> list, BmobException e) {
                if(e==null){
                    fans_sum_text.setText(list.size()+"");

                }else{
                    ToastUtils.showShort("失败");

                }
            }
        });
    }


    private void initViews(View view){

        bg_img = (ImageView) view.findViewById(R.id.bg_img);
        header_img = (CircleImageView) view.findViewById(R.id.header_img);

        setting_img = (ImageView) view.findViewById(R.id.setting_img);
        setting_img.setOnClickListener(this);


        user_info_layout = (RelativeLayout) view.findViewById(R.id.user_info_layout);
        user_info_layout.setOnClickListener(this);

        username_text = (TextView) view.findViewById(R.id.username_text);

        add_friend_img = (ImageView) view.findViewById(R.id.add_friend_img);
        add_friend_img.setOnClickListener(this);

        like_sum_text = (TextView) view.findViewById(R.id.like_sum_text);
        fans_sum_text = (TextView) view.findViewById(R.id.fans_sum_text);
        signature_text = (TextView) view.findViewById(R.id.signature_text);

        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_img:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.user_info_layout:
                Intent intent1 = new Intent(getActivity(), HomePageActivity.class);
                intent1.putExtra("name",userInfo.getUserName());
                startActivity(intent1);
                break;

            case R.id.add_friend_img:
                Intent intent2 = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(intent2);
                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        //LogUtils.d("onresume");
    }


}
