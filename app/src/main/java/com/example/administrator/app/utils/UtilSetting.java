package com.example.administrator.app.utils;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

public class UtilSetting {
    public final static int MAX_MESSAGE=20;
    public static List<Message> offMessagelist=new ArrayList<Message>();
    public static List<Conversation> conversationlist=new ArrayList<Conversation>();
}
