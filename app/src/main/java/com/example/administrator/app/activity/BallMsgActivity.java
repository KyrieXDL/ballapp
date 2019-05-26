package com.example.administrator.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.app.R;
import com.example.administrator.app.adapter.BallMsgAdapter;
import com.example.administrator.app.bean.BallCourt;
import com.example.administrator.app.bean.BallMsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class BallMsgActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BallMsgAdapter adapter;
    private ArrayList<BallCourt> ballCourtArrayList = new ArrayList<>();
    private ArrayList<BallMsg> ballMsgArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball_msg);
        initViews();
        getCourtData();
        setClickListener();
    }

    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.ball_msg_recyclerview);
        adapter = new BallMsgAdapter(R.layout.ball_msg_item,ballCourtArrayList ,this);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getCourtData(){
        BmobQuery<BallCourt> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<BallCourt>(){

            @Override
            public void done(List<BallCourt> list, BmobException e) {
                if(e==null){
                    ToastUtils.showShort("成功");
                    ballCourtArrayList.clear();
                    ballCourtArrayList.addAll(list);
                    adapter.notifyDataSetChanged();
                    LogUtils.d("court_img"+list.get(0).getCourt_img());
                }else{
                    ToastUtils.showShort("失败");
                    LogUtils.d(e.getMessage());
                }
            }
        });
    }

    private void setClickListener(){
        adapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.cardview:
                        getBallMsgData(ballCourtArrayList.get(position).getCourt_name(), ballCourtArrayList.get(position).getCourt_addr());
                        //ToastUtils.showShort("clicked"+position);
                        /*Intent intent = new Intent(BallMsgActivity.this, BallMsgDetailActivity.class);
                        //TextView court_name_text = (TextView) adapter.getViewByPosition(position,R.id.court_name);
                        intent.putExtra("court_name",ballCourtArrayList.get(position).getCourt_name());
                        startActivity(intent);*/
                        break;
                }
                return false;
            }
        });
    }

    private void getBallMsgData(String court_name, String court_addr){
        BmobQuery<BallMsg> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("court", court_name);
        categoryBmobQuery.findObjects(new FindListener<BallMsg>() {
            @Override
            public void done(List<BallMsg> object, BmobException e) {
                if (e == null) {
                    if (object.size()>0) {
                        ballMsgArrayList.clear();
                        ballMsgArrayList.addAll(object);
                        Intent intent = new Intent(BallMsgActivity.this, BallMsgDetailActivity.class);
                        intent.putExtra("list", (Serializable) ballMsgArrayList);
                        intent.putExtra("court_name",court_name);
                        intent.putExtra("court_addr",court_addr);
                        startActivity(intent);
                    }else{
                        ToastUtils.showShort("该球场暂无可加入的球局");
                    }
                } else {
                    ToastUtils.showShort("获取数据失败");

                }
            }
        });
    }

}
