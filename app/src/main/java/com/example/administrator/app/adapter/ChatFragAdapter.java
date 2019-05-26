package com.example.administrator.app.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;


public class ChatFragAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> {
    Badge badge;
    public ChatFragAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
        //TextView chattext=helper.getView(R.id.chattext);
        TextView chat_time = helper.getView(R.id.chat_time);
        TextView chat_username = helper.getView(R.id.chat_username);
        TextView chat_msg = helper.getView(R.id.latest_msg);
        TextView chat_badge = helper.getView(R.id.badge);
        final CircleImageView headicon=helper.getView(R.id.headicon);
        if(item==null){this.getData().remove(item);return;}
        badge = new QBadgeView(mContext).bindTarget(chat_badge);
        Message mes=item.getLatestMessage();

        if(item.getUnReadMsgCnt()>0) {
            badge.setBadgeGravity(Gravity.CENTER | Gravity.END);
            badge.setBadgeTextSize(14, true);
            badge.setBadgePadding(6, true);
            badge.setBadgeNumber(item.getUnReadMsgCnt());
        }else{
            badge.setBadgeGravity(Gravity.CENTER | Gravity.END);
            badge.setBadgeTextSize(0, true);
            badge.setBadgePadding(0, true);
            badge.setBadgeNumber(item.getUnReadMsgCnt());
            //为0则不显示
        }
        //若为群聊的系统消息


        String title="";
        if(item.getType()==ConversationType.group){
            title=((GroupInfo)item.getTargetInfo()).getGroupName();

        }else{
            title=((UserInfo)item.getTargetInfo()).getUserName();
        }
        long t1 = mes.getCreateTime();
        if(mes!=null) {
            //有消息的情况下
            //若为普通消息
            switch (mes.getContentType()) {

                case text: {

                    item.getTitle();
                    chat_username.setText(title);
                    chat_msg.setText(((TextContent) mes.getContent()).getText());
                    chat_time.setText(getDateToString(t1, "yyyy-MM-dd HH:mm"));

                    //chatimage.setVisibility(View.INVISIBLE);
                    //chattext.setText(msgtext);

                }
                break;
                case image: {
                    chat_msg.setText("[图片]");
                }
                break;
            }
        }
            if(item.getType().equals(ConversationType.single)) {

                UserInfo userInfo;
                if (JMessageClient.getMyInfo().equals(item.getTargetInfo()))
                {
                    userInfo=mes.getFromUser();
                }
                else userInfo=(UserInfo) item.getTargetInfo();
                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int i, String s, Bitmap bitmap) {
                        if (i == 0)
                            headicon.setImageBitmap(bitmap);//设置头像
                        else {
                            Bitmap bit = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.header_default);
                            headicon.setImageBitmap(bit);
                            //当没有头像的时候设置默认头像，此处暂缺资源
                        }
                    }
                });


//                ((UserInfo) item.getTargetInfo()).getAvatarBitmap(new GetAvatarBitmapCallback() {
//                    @Override
//                    public void gotResult(int i, String s, Bitmap bitmap) {
//                        if (i == 0) {
//                            headicon.setImageBitmap(bitmap);//设置头像
//                        }
//                        else {
//                            Bitmap bit = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.welcome);
//                            headicon.setImageBitmap(bit);
//                            //当没有头像的时候设置默认头像，此处暂缺资源
//                        }
//                    }
//                });
            }else{
                //群聊无头像，直接设置为默认头像
                Bitmap bit = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.group);
                headicon.setImageBitmap(bit);
            }
    }
    private static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

}

