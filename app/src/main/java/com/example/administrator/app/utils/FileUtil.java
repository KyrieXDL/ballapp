package com.example.administrator.app.utils;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.Message;

public class FileUtil {
    //保存对象到缓存文件
    public static boolean saveToJson(Context context, String fileName, Object object) {
        if (context == null || fileName == null || object == null) {
            return false;
        }
        //gson解析对象，转化为字符串
        String json = new Gson().toJson(object);
        //获取sd卡应用包名下的文件路径，Android/data/应用包名/files/fileName
        String path = context.getExternalCacheDir() + "/" + fileName;
        File file = new File(path);
        FileOutputStream os = null;
        try {
            if (!file.exists() && !file.createNewFile())
                return false;
            os = new FileOutputStream(file);
            os.write(json.getBytes("utf-8"));
            os.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //从缓存里读取出保存的缓存对象，直接gson解析为Object
    public static List<String>  readmsglist(Context context, String fileName) {
        if (context == null) return null;
        String path = context.getExternalCacheDir() + "/" + fileName;
        File file = new File(path);
        if (!file.exists())
            return null;
        List<String> stringList=new ArrayList<String>();
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            BufferedReader bufferedReader=new BufferedReader(reader);
            String temp;
            while((temp=bufferedReader.readLine())!=null)
                stringList.add(temp);
            return stringList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean saveMsglist(Context context, String fileName,  List<Message> object)
    {
        if (context == null || fileName == null || object == null) {
            return false;
        }
        List<String> uidlist=new ArrayList<String>();
        for(Message message:object){
            uidlist.add(message.getFromUser().getUserName());
        }
        //获取sd卡应用包名下的文件路径，Android/data/应用包名/files/fileName
        String path = context.getExternalCacheDir() + "/" + fileName;
        File file = new File(path);
        FileWriter os = null;
        try {

            if (!file.exists() && !file.createNewFile())
                return false;
            os = new FileWriter(file);
            BufferedWriter writer=new BufferedWriter(os);
            for(String s:uidlist){
                writer.write(s);
                writer.newLine();
            }
            writer.close();
            os.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
