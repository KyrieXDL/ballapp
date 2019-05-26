package com.example.administrator.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.app.R;
import com.example.administrator.app.activity.ChatActivity;
import com.example.administrator.app.activity.GroupActivity;
import com.example.administrator.app.adapter.ChatFragAdapter;
import com.example.administrator.app.utils.UtilSetting;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

import static com.example.administrator.app.utils.UtilSetting.conversationlist;


public class FragmentMsg extends Fragment {
    //控件
    RecyclerView usermsglist;
    ChatFragAdapter adapter;

    //数据
    List<Message> msglist;
    List<Conversation> conversationList;

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //保存销毁之前的数据
//        outState.putString("msglist",new Gson().toJson(msglist));
//
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {

        LogUtils.d("这是消息界面");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);

        org.greenrobot.eventbus.EventBus.getDefault().register(this);
        JMessageClient.registerEventReceiver(this);

        usermsglist = (RecyclerView) view.findViewById(R.id.msglist);

        msglist = UtilSetting.offMessagelist;//用通用消息列表
        this.conversationList = conversationlist;
        adapter = new ChatFragAdapter(R.layout.message_list, conversationlist);
        usermsglist.setAdapter(adapter);

        RecyclerView.LayoutManager kx = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) kx).setOrientation(LinearLayoutManager.VERTICAL);
        usermsglist.setLayoutManager(kx);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击对应信息时跳到特定人的对话界面。
                if (conversationlist.get(position).getType().equals(ConversationType.group)) {
                    Intent intent = new Intent(getContext(), GroupActivity.class);
                    long t = ((GroupInfo) (conversationlist.get(position).getTargetInfo())).getGroupID();
                    intent.putExtra("intype", "group");
                    intent.putExtra("group_id", t);
//  intent.putExtra("conversation",conversationList.get(msglist.get(position)));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    String t = ((UserInfo) conversationlist.get(position).getTargetInfo()).getUserName();
                    intent.putExtra("intype", "hanashi");
                    intent.putExtra("chatusername", t);
//  intent.putExtra("conversation",conversationList.get(msglist.get(position)));
                    startActivity(intent);
                }
            }
        });
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        this.conversationList = conversationlist;
        adapter.notifyDataSetChanged();
        usermsglist.setAdapter(adapter);
    }

    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        adapter.notifyDataSetChanged();
    }

    private int getMsgUnread(int position) {
        String from = msglist.get(position).getFromUser().getUserName();
        Conversation co = Conversation.createSingleConversation(from);
        return co.getUnReadMsgCnt();
    }
}
