package com.example.administrator.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.app.R;
import com.example.administrator.app.bean.TestImage;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class ShowImageAdapter extends BaseQuickAdapter<TestImage, BaseViewHolder> {
    private Context mContext;
    public ShowImageAdapter(int layoutResId, List data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }
    @Override
    protected void convert(BaseViewHolder helper, TestImage item) {
        helper.setText(R.id.message_name,item.getName());
        helper.setText(R.id.message_time,item.getTime());
        helper.setText(R.id.message_text,item.getMessage());
        helper.setText(R.id.message_location,item.getLocation());
        LinearLayout linearLayout1 = helper.getView(R.id.linearLayout1);
        LinearLayout linearLayout2 = helper.getView(R.id.linearLayout2);
        LinearLayout linearLayout3 = helper.getView(R.id.linearLayout3);

        JMessageClient.getUserInfo(item.getName(), "c08a6fa3ea3bb53fccdcd60d", new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (userInfo.getAvatarFile() == null){
                    Glide.with(mContext).load(R.drawable.header_default).into((ImageView) helper.getView(R.id.message_img));
                }else{
                    Glide.with(mContext).load(userInfo.getAvatarFile()).into((ImageView) helper.getView(R.id.message_img));
                }
            }
        });


        if(item.getImage1() != null)
        {
            Glide.with(mContext).load(item.getImage1().getFileUrl()).into((ImageView) helper.getView(R.id.image_1));
        }
        if(item.getImage2() != null)
        {
            Glide.with(mContext).load(item.getImage2().getFileUrl()).into((ImageView) helper.getView(R.id.image_2));
        }
        if(item.getImage3() != null)
        {
            Glide.with(mContext).load(item.getImage3().getFileUrl()).into((ImageView) helper.getView(R.id.image_3));
        }
        if(item.getImage4() != null)
        {
            Glide.with(mContext).load(item.getImage4().getFileUrl()).into((ImageView) helper.getView(R.id.image_4));

            if(item.getImage5() != null)
            {
                Glide.with(mContext).load(item.getImage5().getFileUrl()).into((ImageView) helper.getView(R.id.image_5));
            }
            if(item.getImage6() != null)
            {
                Glide.with(mContext).load(item.getImage6().getFileUrl()).into((ImageView) helper.getView(R.id.image_6));
            }
            if(item.getImage7() != null)
            {
                Glide.with(mContext).load(item.getImage7().getFileUrl()).into((ImageView) helper.getView(R.id.image_7));
                if(item.getImage8() != null)
                {
                    Glide.with(mContext).load(item.getImage8().getFileUrl()).into((ImageView) helper.getView(R.id.image_8));
                }
                if(item.getImage9() != null)
                {
                    Glide.with(mContext).load(item.getImage9().getFileUrl()).into((ImageView) helper.getView(R.id.image_9));
                }
            }else{
                linearLayout3.setVisibility(View.GONE);
            }
        }else {
            linearLayout2.setVisibility(View.GONE);
            linearLayout3.setVisibility(View.GONE);
        }

    }
}
