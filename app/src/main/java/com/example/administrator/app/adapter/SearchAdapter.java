package com.example.administrator.app.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.app.R;
import com.example.administrator.app.bean.Relation;
import com.example.administrator.app.bean.User;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    private Context mContext;
    private List<Relation> relationList;
    private UserInfo userInfo = JMessageClient.getMyInfo();

    public SearchAdapter(int layoutResId, List data, List<Relation> relationList, Context context) {
        super(layoutResId, data);
        mContext = context;
        this.relationList = relationList;
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.username,item.getUsername());

        JMessageClient.getUserInfo(item.getUsername(), "c08a6fa3ea3bb53fccdcd60d", new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (userInfo.getAvatarFile() == null){
                    Glide.with(mContext).load(R.drawable.header_default).into((CircleImageView) helper.getView(R.id.header_img));
                }else{
                    Glide.with(mContext).load(userInfo.getAvatarFile()).into((CircleImageView) helper.getView(R.id.header_img));
                }
            }
        });

        if (checkRelation(item.getUsername())){
            helper.setText(R.id.like_button,"已关注");
        }else{
            helper.setText(R.id.like_button,"+关注");
        }
        helper.addOnClickListener(R.id.like_button);
        helper.addOnClickListener(R.id.header_img);
        //Glide.with(mContext).load(item.getImgsrc()).into((ImageView) helper.getView(R.id.news_img));
    }

    private boolean checkRelation(String name){
        int size = relationList.size();
        String username = userInfo.getUserName();
        for (int i=0;i<size;i++){
            if (relationList.get(i).getLike().equals(name) && relationList.get(i).getFan().equals(username)){
                return true;
            }
        }



        return false;
    }


}
