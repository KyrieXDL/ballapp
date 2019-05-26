package com.example.administrator.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.nostra13.dcloudimageloader.core.ImageLoader;
import com.nostra13.dcloudimageloader.core.ImageLoaderConfiguration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.GroupApprovalEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import static com.example.administrator.app.utils.UtilSetting.conversationlist;

public class MyApp extends Application {
    private GroupApprovalEvent eve;
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        OkGo.init(this);
        Bmob.initialize(this, "7f0a78fe07b1b9dbb4927fb9aa99af8e");
        MultiDex.install(this);
        JMessageClient.init(getApplicationContext(), true);

        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);

        ImageLoader.getInstance().init(config);     //UniversalImageLoader初始化

        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });

        org.greenrobot.eventbus.EventBus.getDefault().register(this);
        JMessageClient.registerEventReceiver(this);
    }

    @Subscribe
    public void onEvent(GroupApprovalEvent event){
        eve=event;
        event.getApprovalUserInfoList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {
                for(UserInfo u:list){
                    eve.acceptGroupApproval(u.getUserName(), "c08a6fa3ea3bb53fccdcd60d", new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {

                        }
                    });
                }

            }
        });

    }

    @Subscribe
    public void onEvent(OfflineMessageEvent event) {
        Conversation co=event.getConversation();
        for(Conversation c:conversationlist){
            if(c.equals(co)){
                return;//已有对话则直接跳过处理，界面上有自动刷新机制
            }
        }
        conversationlist.add(co);//否则增加对话
    }

    @Subscribe
    public void onEvent(MessageEvent event) {

        Message t=event.getMessage();

        Conversation co;
        if(t.getTargetType().equals(ConversationType.group))
        {
            co=Conversation.createGroupConversation(((GroupInfo)t.getTargetInfo()).getGroupID());
        }
        else{
            co=Conversation.createSingleConversation(((UserInfo)t.getTargetInfo()).getUserName());
        }
        for(Conversation c:conversationlist){
            if(c.equals(co)){
                return;//已有对话则直接跳过处理，界面上有自动刷新机制
            }
        }
        conversationlist.add(co);//否则增加对话

    }


}
