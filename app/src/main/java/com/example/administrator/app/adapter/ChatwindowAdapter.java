package com.example.administrator.app.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daasuu.bl.BubbleLayout;
import com.example.administrator.app.R;

import java.util.List;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;


public class ChatwindowAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {

    public ChatwindowAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {

        TextView chattext_left=helper.getView(R.id.chattext_left);
        TextView chattext_right=helper.getView(R.id.chattext_right);
        BubbleLayout bubbleLayout_left = helper.getView(R.id.bubble_layout_left);
        BubbleLayout bubbleLayout_right = helper.getView(R.id.bubble_layout_right);
        ImageView chatimage_left=helper.getView(R.id.chatimage_left);
        helper.addOnClickListener(R.id.chatimage_left);
        ImageView chatimage_right=helper.getView(R.id.chatimage_right);
        helper.addOnClickListener(R.id.chatimage_right);
        UserInfo u=item.getFromUser();

        final CircleImageView headicon_left=helper.getView(R.id.headicon_left);
        final CircleImageView headicon_right=helper.getView(R.id.headicon_right);
        if(item.getDirect()== MessageDirect.send)
        {
            chattext_left.setVisibility(View.GONE);
            headicon_left.setVisibility(View.GONE);
            bubbleLayout_left.setVisibility(View.GONE);
            chattext_right.setVisibility(View.VISIBLE);
            headicon_right.setVisibility(View.VISIBLE);
            bubbleLayout_right.setVisibility(View.VISIBLE);

            initSideView(chattext_right,headicon_right,item,u,chatimage_right);
        }
        else{
            chattext_left.setVisibility(View.VISIBLE);
            headicon_left.setVisibility(View.VISIBLE);
            bubbleLayout_left.setVisibility(View.VISIBLE);
            chattext_right.setVisibility(View.GONE);
            headicon_right.setVisibility(View.GONE);
            bubbleLayout_right.setVisibility(View.GONE);

            initSideView(chattext_left,headicon_left,item,u,chatimage_left);
        }


    }

    private void initSideView(TextView chattext, CircleImageView headicon, Message item, UserInfo u, ImageView chatimage){
        switch (item.getContentType())
        {
            case text: {
                String msgtext="";
                msgtext+=((TextContent) item.getContent()).getText();

                chattext.setText(msgtext);
                chatimage.setVisibility(View.GONE);

            } break;
            case image:{
                chattext.setVisibility(View.INVISIBLE);
                String path=((ImageContent)item.getContent()).getLocalThumbnailPath();
                chatimage.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(path).into(chatimage);
            }break;
        }
        u.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if(i==0)
                    headicon.setImageBitmap(bitmap);//设置头像
                else
                {
                    Bitmap  bit = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.header_default);
                    headicon.setImageBitmap(bit);
                    //当没有头像的时候设置默认头像，此处暂缺资源
                }
            }
        });
    }
}

