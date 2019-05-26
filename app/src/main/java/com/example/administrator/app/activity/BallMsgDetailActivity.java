package com.example.administrator.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.app.R;
import com.example.administrator.app.adapter.BallMsgDetailAdapter;
import com.example.administrator.app.bean.BallMsg;
import com.example.administrator.app.bean.UserInfo;
import com.example.administrator.app.view.CtrlLinearLayoutManager;
import com.example.administrator.app.view.MarginConfig;
import com.example.administrator.app.view.ZoomHeaderView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.model.GroupMemberInfo;
import cn.jpush.im.api.BasicCallback;

public class BallMsgDetailActivity extends AppCompatActivity {

  private RecyclerView mRecyclerView;
  private ViewPager mViewPager;
  private ZoomHeaderView mZoomHeader;
  private boolean isFirst = true;
  private ArrayList<BallMsg> ballMsgArrayList = new ArrayList<>();
  private ArrayList<UserInfo> userInfoArrayList = new ArrayList<>();
  private ArrayList<UserInfo> memberInfoArrayList = new ArrayList<>();
  private Adapter pagerAdapter;
  private BallMsgDetailAdapter ballMsgDetailAdapter;
  private cn.jpush.im.android.api.model.UserInfo myInfo = JMessageClient.getMyInfo();
  private String court_name = "";
  private String court_addr = "";
  private int currentItemPosition = 0;
  private String group_master = "";
  private String group_id = "";
  private String username = "";
  private ArrayList<View> views;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ball_msg_detail);
    initViews();

    getBallMemberData();

    initListener();

  }

  private void initViews(){
    username = myInfo.getUserName();
    List<BallMsg> list = (List<BallMsg>) getIntent().getSerializableExtra("list");
    court_name = getIntent().getStringExtra("court_name");
    court_addr = getIntent().getStringExtra("court_addr");
    ballMsgArrayList.addAll(list);

    pagerAdapter = new Adapter(ballMsgArrayList);

    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    mViewPager = (ViewPager) findViewById(R.id.viewpager);
    mZoomHeader = (ZoomHeaderView) findViewById(R.id.zoomHeader);
    mViewPager.setAdapter(pagerAdapter);
    mViewPager.setOffscreenPageLimit(4);
    CtrlLinearLayoutManager layoutManager = new CtrlLinearLayoutManager(this);

    currentItemPosition = mViewPager.getCurrentItem();
    UserInfo groupMaster = new UserInfo();
    group_master = ballMsgArrayList.get(currentItemPosition).getUsername();
    //LogUtils.d("group_master: "+group_master);
    groupMaster.setUsername(group_master);
    userInfoArrayList.add(groupMaster);

    //未展开禁止滑动
    layoutManager.setScrollEnabled(false);
    ballMsgDetailAdapter = new BallMsgDetailAdapter(R.layout.item_comment,userInfoArrayList,court_addr,this);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(ballMsgDetailAdapter);
    mRecyclerView.setAlpha(0);
  }

  private void initListener(){
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int i, float v, int i1) {

      }

      @Override
      public void onPageSelected(int i) {
        currentItemPosition = i;
        UserInfo groupMaster = new UserInfo();
        group_master = ballMsgArrayList.get(currentItemPosition).getUsername();
        //LogUtils.d("group_master: "+group_master);
        //ToastUtils.showShort("page"+i+" group_master: "+group_master+" username: "+username+" size: "+userInfoArrayList.size());
        groupMaster.setUsername(group_master);
        //userInfoArrayList.add2(new UserInfo());
        userInfoArrayList.set(0,groupMaster);
        pagerAdapter.notifyDataSetChanged();
        /*if (isMember(username)){
          ToastUtils.showShort("已在该球局");
        }else{
          ToastUtils.showShort("不在");
        }*/

        getBallMemberData();
      }

      @Override
      public void onPageScrollStateChanged(int i) {

      }
    });

    ballMsgDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
          case R.id.join_group_button:
            String group_id = userInfoArrayList.get(position).getGroup_id();
            long gid=Long.parseLong(group_id);
            checkIsInGroup(gid);
            break;
        }
      }
    });
  }

  private void checkIsInGroup(long group_id){
    JMessageClient.getGroupMembers(group_id, new RequestCallback<List<GroupMemberInfo>> (){
      @Override
      public void gotResult(int i, String s, List<GroupMemberInfo> groupMemberInfos) {
        for (int n=0;n<groupMemberInfos.size();n++){
          if (groupMemberInfos.get(n).getUserInfo().getUserName().equals(myInfo.getUserName())){
            //已加入群
            ToastUtils.showShort("已加入群聊");
            Intent intent = new Intent(BallMsgDetailActivity.this, GroupActivity.class);

            intent.putExtra("group_id",group_id);
            startActivity(intent);
            return;
          }

        }

        //未加入群
        ToastUtils.showShort("正在审核...");
        /*Intent intent = new Intent(BallMsgDetailActivity.this, GroupActivity.class);
        intent.putExtra("group_id",group_id);
        List<String> userNameList = new ArrayList<>();
        userNameList.add2(myInfo.getUserName());
        JMessageClient.addGroupMembers(group_id, "c08a6fa3ea3bb53fccdcd60d", userNameList, new BasicCallback() {
          @Override
          public void gotResult(int i, String s) {
            LogUtils.d(i+" ; "+s);
            if ()
            //startActivity(intent);
          }
        });*/
      }
    });
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (isFirst) {
      for (int i = 0; i < mViewPager.getChildCount(); i++) {
        View v = mViewPager.getChildAt(i).findViewById(R.id.ll_bottom);
        v.setY(mViewPager.getChildAt(i).findViewById(R.id.imageView).getHeight());
        v.setX(MarginConfig.MARGIN_LEFT_RIGHT);
        //触发一次depen
        // dency变化，让按钮归位
        mZoomHeader.setY(mZoomHeader.getY() - 1);
        isFirst = false;
      }
    }
  }

  private void getBallMemberData(){
      BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
      bmobQuery.addWhereEqualTo("group_name", group_master+court_name);
   // bmobQuery.addWhereNotEqualTo("username",group_master);
      bmobQuery.findObjects(new FindListener<UserInfo>(){

      @Override
      public void done(List<UserInfo> list, BmobException e) {
        if(e==null){
            userInfoArrayList.clear();
            userInfoArrayList.addAll(list);
            ballMsgDetailAdapter.notifyDataSetChanged();
            pagerAdapter.notifyDataSetChanged();

            TextView join_btn = (TextView) views.get(currentItemPosition).findViewById(R.id.join_btn);

            if (ballMsgArrayList.get(currentItemPosition).getUsername().equals(username)){
                join_btn.setText("解散球局");
                join_btn.setBackgroundColor(getResources().getColor(R.color.gray));
            }else if (!isMember(username)){
                join_btn.setText("加入球局");
                join_btn.setBackground(getResources().getDrawable(R.drawable.button_bg));
            }else{
                join_btn.setText("退出球局");
                join_btn.setBackgroundColor(getResources().getColor(R.color.gray));
            }

        }else{
          ToastUtils.showShort("失败");
          //LogUtils.d("testing "+e.getMessage());
        }
      }
    });
  }

  private void exit(){
    BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
    bmobQuery.addWhereEqualTo("group_name", group_master+court_name);
    bmobQuery.addWhereEqualTo("username",username);
    bmobQuery.findObjects(new FindListener<UserInfo>(){

      @Override
      public void done(List<UserInfo> list, BmobException e) {
        if(e==null){
          String objectid = list.get(0).getObjectId();
          UserInfo p2 = new UserInfo();
          p2.setObjectId(objectid);
          p2.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
              if(e==null){
                int position = 0;
                for (int i=0;i<userInfoArrayList.size();i++){
                  if (userInfoArrayList.get(i).getUsername().equals(username)){
                    position = i;
                  }
                }
                userInfoArrayList.remove(position);
                ballMsgDetailAdapter.notifyDataSetChanged();
                pagerAdapter.notifyDataSetChanged();
                ToastUtils.showShort("你已退出该球局");
              }else{
                ToastUtils.showShort("退出失败");
              }
            }

          });

        }else{
          ToastUtils.showShort("失败");
          //LogUtils.d("testing "+e.getMessage());
        }
      }
    });
  }

  private boolean isMember(String name){
    int size = userInfoArrayList.size();
    for (int i=0;i<size;i++){
      if (userInfoArrayList.get(i).getUsername().equals(name)){
        return true;
      }
    }

    return false;
  }

  private void join_Ball(String court_name,String group_master, long groupID){
    JMessageClient.applyJoinGroup(groupID, "", new BasicCallback() {
      @Override
      public void gotResult(int i, String s) {

      }
    });
    BmobQuery<UserInfo> categoryBmobQuery = new BmobQuery<>();
    categoryBmobQuery.addWhereEqualTo("group_name", group_master+court_name);
    categoryBmobQuery.findObjects(new FindListener<UserInfo>() {
      @Override
      public void done(List<UserInfo> object, BmobException e) {
        if (e == null) {
          group_id = object.get(0).getGroup_id();
          UserInfo userinfo = new UserInfo();
          userinfo.setUsername(myInfo.getUserName());
          userinfo.setCourt_name(court_name);
          userinfo.setGroup_name(group_master+court_name);
          userinfo.setGroup_id(group_id);
          userinfo.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
              if(e==null){
                ToastUtils.showShort("成功加入球局");
                userInfoArrayList.add(userinfo);
                ballMsgDetailAdapter.notifyDataSetChanged();
                //LogUtils.d("testing "+"成功创建群聊");
              }else{
                ToastUtils.showShort("加入失败");
                LogUtils.d(e.getMessage());
              }
            }
          });
        } else {

        }
      }
    });
  }


  class Adapter extends PagerAdapter {
    //private ArrayList<View> views;
    private int[] imgs = { R.drawable.court, R.drawable.court, R.drawable.court,R.drawable.court, R.drawable.court, R.drawable.court };
    public Adapter(List<BallMsg> list) {
      views = new ArrayList<>();

      for (int i=0; i<list.size();i++){
        View view = View.inflate(BallMsgDetailActivity.this, R.layout.item_img, null);
        TextView courtname = (TextView) view.findViewById(R.id.tv_name);
        TextView username = (TextView) view.findViewById(R.id.tv_cost);
        TextView join_btn = (TextView) view.findViewById(R.id.join_btn);
        courtname.setText(list.get(i).getCourt());
        username.setText("发起人："+list.get(i).getUsername());

        views.add(view);
      }
    }

    @Override
    public int getCount() {
      return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      views.get(position).findViewById(R.id.imageView).setBackgroundResource(imgs[position]);
        TextView join_btn = (TextView) views.get(position).findViewById(R.id.join_btn);
      join_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (join_btn.getText().equals("加入球局")){
            String group_id = userInfoArrayList.get(position).getGroup_id();
            long gid=Long.parseLong(group_id);
            join_Ball(court_name,group_master,gid);
          }else if (join_btn.getText().equals("退出球局")){
            exit();
          }else{
            ToastUtils.showShort("解散球局");
          }
        }
      });
      /*if (ballMsgArrayList.get(position).getUsername().equals(username)){
        join_btn.setText("解散球局");
        join_btn.setBackgroundColor(getResources().getColor(R.color.gray));
      }else if (!isMember(username)){
        join_btn.setText("加入球局");
        //join_btn.setBackground(getResources().getDrawable(R.drawable.button_bg));
      }else{
        join_btn.setText("退出球局");
        join_btn.setBackgroundColor(getResources().getColor(R.color.gray));
      }*/

      container.addView(views.get(position));

      return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView(views.get(position));
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  public void onBackPressed() {

    if (mZoomHeader.isExpand()) {
      mZoomHeader.restore(mZoomHeader.getY());
    } else {
      finish();
    }
  }
}

