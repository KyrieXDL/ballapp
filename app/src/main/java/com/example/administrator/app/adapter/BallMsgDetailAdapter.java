package com.example.administrator.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.app.R;
import com.example.administrator.app.bean.UserInfo;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wing on 12/4/16.
 */
public class BallMsgDetailAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {
  private Context mContext;
  private String court_addr;

  public BallMsgDetailAdapter(int layoutResId, List data, String courtaddr, Context context) {
    super(layoutResId, data);
    mContext = context;
    court_addr = courtaddr;
  }

  @Override
  protected void convert(BaseViewHolder helper, UserInfo item) {
    helper.setText(R.id.member_name,item.getUsername());
    LinearLayout linearLayout = helper.getView(R.id.linearLayout);
    if (helper.getPosition() == 0){
      linearLayout.setVisibility(View.VISIBLE);
      helper.setText(R.id.addr,court_addr);
    }

    JMessageClient.getUserInfo(item.getUsername(), "c08a6fa3ea3bb53fccdcd60d", new GetUserInfoCallback() {
      @Override
      public void gotResult(int i, String s, cn.jpush.im.android.api.model.UserInfo userInfo) {
        if (userInfo.getAvatarFile() == null){
          Glide.with(mContext).load(R.drawable.header_default).into((CircleImageView) helper.getView(R.id.header_img));
        }else{
          Glide.with(mContext).load(userInfo.getAvatarFile()).into((CircleImageView) helper.getView(R.id.header_img));
        }
      }
    });

    helper.setText(R.id.join_time,item.getCreatedAt());
    helper.addOnClickListener(R.id.join_group_button);
  }
}
