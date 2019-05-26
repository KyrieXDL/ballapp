package com.example.administrator.app.activity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.app.R;
import com.example.administrator.app.adapter.ChatwindowAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

import static com.example.administrator.app.utils.UtilSetting.conversationlist;


public class ChatActivity extends AppCompatActivity {

    //控件
    Button sendbtn,graphbtn;
    RecyclerView chattext;
    EditText sendtext;
    List<Message> msglist;
    ImageView srimgView;
    //ImageView imageView;
    Toolbar toolbar;
    //data
    String chatusername="";
    Bitmap graph;File imageFile;
    String username,password;
    //control
    boolean isImage=false;
    Conversation conversation;
    ChatwindowAdapter adapter;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存销毁之前的数据
        outState.putString("chatusername",chatusername);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(!chatusername.equals(""))
            chatusername= savedInstanceState.getString("chatusername");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

    }

    private void init(){
        EventBus.getDefault().register(this);
        JMessageClient.registerEventReceiver(this);
       // username="lhhhh";password="123456";
//        JMessageClient.login(username, password, new BasicCallback() {
//            @Override
//            public void gotResult(int i, String s) {
//                if(i!=0){Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG);}
//            }
//        });//此处应该使用存储的本地应用数据，暂时没有。
        //相册的初始化
        Intent intent2=getIntent();

        srimgView=(ImageView)findViewById(R.id.srimgView);
        //imageView=(ImageView)findViewById(R.id.imageView);
        sendtext=(EditText)findViewById(R.id.editText);
        chattext=(RecyclerView)findViewById(R.id.textView);
        sendbtn=(Button)findViewById(R.id.button);
        graphbtn=(Button)findViewById(R.id.buttonGraph);
        //imageView.setVisibility(View.INVISIBLE);
        srimgView.setVisibility(View.INVISIBLE);
        srimgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srimgView.setVisibility(View.INVISIBLE);
                sendbtn.setVisibility(View.VISIBLE);
                graphbtn.setVisibility(View.VISIBLE);
            }

        });

        msglist=new ArrayList<Message>();
        adapter=new ChatwindowAdapter(R.layout.chat_message,msglist);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(msglist.get(position).getContentType().equals(ContentType.image)){
                    ((ImageContent)msglist.get(position).getContent()).downloadOriginImage(msglist.get(position), new DownloadCompletionCallback() {
                        @Override
                        public void onComplete(int i, String s, File file) {
                            if(i==0){
                                Glide.with(getApplicationContext()).load(file).into(srimgView);
                                srimgView.setVisibility(View.VISIBLE);
                                sendbtn.setVisibility(View.INVISIBLE);
                                graphbtn.setVisibility(View.INVISIBLE);
                                srimgView.bringToFront();
                                //下载原图成功，直接在整个界面上显示
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "获取图片出现错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        chattext.setAdapter(adapter);
        RecyclerView.LayoutManager kx=new LinearLayoutManager(this);
        ((LinearLayoutManager) kx).setOrientation(LinearLayoutManager.VERTICAL);
        chattext.setLayoutManager(kx);


        chatusername = intent2.getStringExtra("chatusername");

        conversation= Conversation.createSingleConversation(chatusername);
        if(!conversationlist.contains(conversation))conversationlist.add(conversation);
        setTitle(conversation.getTitle());
            //将消息标记为已读
            int num=conversation.getUnReadMsgCnt();
            conversation.setUnReadMessageCnt(0);
            List<Message> k = conversation.getMessagesFromNewest(0,num);
            for(Message m:k){
                m.setHaveRead(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {

                    }
                });
            }
            //获取历史消息
            k = conversation.getMessagesFromNewest(0,5);
            List<Message> k2=new ArrayList<Message>();
            //将获得的信息反过来
            for(Message temp:k){
                k2.add(0,temp);
            }
            msglist.addAll(k2);
            adapter.notifyDataSetChanged();



        graphbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //从相册中选择图片
                String fileName="";
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date(System.currentTimeMillis());
                fileName = "easyassetFromAlbum"+format.format(date);
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File outputImage = new File(path,fileName+".jpg");
                Uri imageUri = Uri.fromFile(outputImage);
                Intent intent1=new Intent("android.intent.action.GET_CONTENT");
                intent1.putExtra("scale",true);
                intent1.putExtra("crop",true);
                intent1.setType("image/*");
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent1, 3);//开始选择
            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            Message message;
            @Override
            public void onClick(View v) {
                //发送消息
                //imageView.setVisibility(View.INVISIBLE);
                sendtext.setVisibility(View.VISIBLE);
                if(!sendtext.getText().toString().equals("[图片]"))isImage=false;

                if(!isImage) {
                    if(sendtext.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    message = JMessageClient.createSingleTextMessage(chatusername, sendtext.getText().toString());

                    JMessageClient.sendMessage(message);
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {

                                adapter.addData(message);
                                adapter.notifyDataSetChanged();
                                sendtext.setText("");//发送成功则清空文字
                            } else {
                                Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    try {
                        message = JMessageClient.createSingleImageMessage(chatusername, imageFile);

                        JMessageClient.sendMessage(message);

                        message.setOnSendCompleteCallback(new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    adapter.addData(message);
                                    adapter.notifyDataSetChanged();
                                  //  imageView.setVisibility(View.INVISIBLE);
                                    sendtext.setVisibility(View.VISIBLE);
                                    sendtext.setText("");//发送成功则清空文字
                                } else {
                                    Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }catch(FileNotFoundException e){
                        Toast.makeText(getApplicationContext(), "找不到源文件", Toast.LENGTH_SHORT).show();
                    }

                }
                isImage=false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
//        JMessageClient.logout();
        //JMessageClient.exitConversation();

    }
    @Override
    protected void onResume(){
        super.onResume();
        //JMessageClient.

    }
    //监听到新信息时
    @Subscribe
    public void onEventMainThread  (MessageEvent event){
        //do your own business

        Message message=event.getMessage();
        message.setHaveRead(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {

            }
        });
        adapter.addData(message);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        List<Message> msgoff=event.getOfflineMessageList();
        adapter.addData(msgoff);
        adapter.notifyDataSetChanged();
    }
    //图片调用的回调方法
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode!=RESULT_OK){
            Toast.makeText(this, "获取图片出现错误", Toast.LENGTH_SHORT).show();

            return;
        }
        try {
            isImage=true;
            //imageView.setVisibility(View.VISIBLE);
            //sendtext.setVisibility(View.INVISIBLE);
            imageFile = new File(handleImageOnKitKat(data));
            sendtext.setText("[图片]");
//            Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(data.getData()));
//            graph=bitmap;
            //imageView.setImageBitmap(graph);
            Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(this, "获取图片出现错误", Toast.LENGTH_SHORT).show();
        }
    }

    private String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //Log.d(TAG, "content: " + uri.toString());
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onBackPressed() {
        if (srimgView.getVisibility() == View.VISIBLE){
            srimgView.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }
}
