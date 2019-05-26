package com.example.administrator.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.administrator.app.R;
import com.example.administrator.app.bean.CollectNews;
import com.ldoublem.loadingviewlib.view.LVBlazeWood;
import com.lilei.springactionmenu.ActionMenu;
import com.lilei.springactionmenu.OnActionItemClickListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.sendtion.xrichtext.RichTextView;

import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import okhttp3.Call;
import okhttp3.Response;

public class NewsDetailActivity extends AppCompatActivity {

    private final static String NewsDetailUrl = "http://10.25.208.232/TP5_Splider-master/public/index.php/api/News/new_detail?postid=";
    private Intent intent;
    private ImageView newsDetailImg;
    private HtmlTextView htmlTextView;
    private TextView newsTitleText;
    private TextView newsPtimeText;
    private TextView newsSourceText;
    private ActionMenu actionMenu;
    private LVBlazeWood lvBlazeWood;
    public int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        intent = getIntent();
        initViews();
        getNewsDetailData();
    }

    private void initViews(){
        newsDetailImg = (ImageView) findViewById(R.id.news_detail_img);
        //newsDetailText = (RichTextView) findViewById(R.id.news_detail_text);
        htmlTextView = (HtmlTextView) findViewById(R.id.news_detail_text);
        newsTitleText = (TextView) findViewById(R.id.news_title_text);
        newsPtimeText = (TextView) findViewById(R.id.news_ptime_text);
        newsSourceText = (TextView) findViewById(R.id.news_source_text);

        actionMenu = (ActionMenu) findViewById(R.id.actionMenu);

// add2 menu items
        actionMenu.addView(R.drawable.share, getResources().getColor(R.color.orange), getResources().getColor(R.color.orange));
        actionMenu.addView(R.drawable.like, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorAccent));
        actionMenu.addView(R.drawable.write);

        lvBlazeWood = findViewById(R.id.lvBlaze);

        setClickListener();
    }

    private void setClickListener(){
        actionMenu.setItemClickListener(new OnActionItemClickListener() {
            @Override
            public void onItemClick(int index) {
                switch (index){
                    case 2:
                        //ToastUtils.showShort("click 2");
                        switch (flag) {
                            case 0:
                                //collectbutton.setActivated(true);
                                flag = 1;
                                insert();
                                break;
                            case 1:
                                //collectbutton.setActivated(false);
                                flag = 0;
                                delete();
                                break;
                        }
                        break;
                }
            }

            @Override
            public void onAnimationEnd(boolean isOpen) {

            }
        });
    }

    private void insert(){
        UserInfo myInfo = JMessageClient.getMyInfo();
        CollectNews collectNews=new CollectNews();
        collectNews.setUsername(myInfo.getUserName());
        collectNews.setPostid(intent.getStringExtra("postid"));
        collectNews.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    //添加成功
                    ToastUtils.showShort("收藏成功");
                    //Toast.makeText(NewsDetailActivity.this,collectNews.getTitle(),Toast.LENGTH_SHORT).show();
                }else{
                    //添加失败
                    ToastUtils.showShort("收藏失败");
                    //Toast.makeText(NewsDetailActivity.this,"添加數據失敗",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void delete(){
        UserInfo myInfo = JMessageClient.getMyInfo();
        CollectNews collectNews=new CollectNews();
        BmobQuery<CollectNews> collectNewsBmobQuery=new BmobQuery<>();
        collectNewsBmobQuery.findObjects(new FindListener<CollectNews>() {
            @Override
            public void done(List<CollectNews> list, BmobException e) {
                if (e == null) {
                    // Toast.makeText(NewsDetailActivity.this,list.get(2).getObjectId(),Toast.LENGTH_SHORT).show();
                    int n=list.size();
                    for (int i=0;i<n;i++) {
                        //Toast.makeText(NewsDetailActivity.this,"数量"+ i,Toast.LENGTH_SHORT).show();
                        if (list.get(i).getPostid().equals(intent.getStringExtra("postid"))&&list.get(i).getUsername().equals(myInfo.getUserName())){
                            collectNews.setObjectId(list.get(i).getObjectId());
                            collectNews.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        //Toast.makeText(NewsDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                        ToastUtils.showShort("取消收藏");
                                    }
                                }
                            });
                        }
                    }
                }
                else {
                    System.out.println(e.getErrorCode());
                }
            }
        });

    }

    private void getNewsDetailData(){
        lvBlazeWood.setVisibility(View.VISIBLE);
        lvBlazeWood.startAnim();
        OkGo.get(NewsDetailUrl+intent.getStringExtra("postid")).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                // s 即为所需要的结果
                LogUtils.d(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    String imgSrc = jsonData.getJSONArray("img").getJSONObject(0).getString("src");
                    String content = jsonData.getString("body");
                    String ptime = jsonData.getString("ptime");
                    String source = jsonData.getString("source");
                    String title = jsonData.getString("title");
                    Glide.with(NewsDetailActivity.this).load(imgSrc).into(newsDetailImg);

                    htmlTextView.setHtml(content, new HtmlResImageGetter(htmlTextView));
                    newsTitleText.setText(title);
                    newsPtimeText.setText(ptime);
                    newsSourceText.setText(source);
                    LogUtils.d(imgSrc+"\n"+content);

                }catch (Exception e){
                    e.printStackTrace();
                }

                lvBlazeWood.stopAnim();
                lvBlazeWood.setVisibility(View.GONE);


            }
        });
    }

}
