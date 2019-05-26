package com.example.administrator.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.jmessage.support.google.gson.reflect.TypeToken;
import cn.jpush.im.android.api.model.Message;


/**
 * Created by lh on 2018/6/5.
 */

public class ConfigUtil {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String userJson;
    public static final String USER_KEY = "user_key";
    public static final String TEST_KEY="test_temp";
    public String getUserJson(){
        return sp.getString(USER_KEY,"");
    }

//    public List<Message> getMessageList(){
//        List<Message> e;
//        String s = this.getUserJson();

//        return e;
//    }
    public List<Message> getMessageList(String key){
        String s=sp.getString(key,"");
        if(s=="")return null;
        List<Message> e=new ArrayList<Message>();
        new Gson().fromJson(s,new TypeToken<List<Message>>(){}.getType());
        return e;
    }


    public void setJson(String testJson,String key){
        editor.putString(key,testJson);
        editor.commit();
    }

    public ConfigUtil(Context ctx){
        sp=ctx.getSharedPreferences("my_sp",Context.MODE_PRIVATE);
        editor = sp.edit();
    }
}
