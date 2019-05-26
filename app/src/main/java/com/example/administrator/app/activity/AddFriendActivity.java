package com.example.administrator.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.app.R;
import com.example.administrator.app.adapter.SearchAdapter;
import com.example.administrator.app.bean.Relation;
import com.example.administrator.app.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

public class AddFriendActivity extends AppCompatActivity {

    private FloatingSearchView mSearchView;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<User> userList = new ArrayList<>();
    private List<Relation> relationList = new ArrayList<>();
    private UserInfo userInfo = JMessageClient.getMyInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initViews();
        initListener();

    }

    private void initViews(){
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        searchAdapter = new SearchAdapter(R.layout.search_item,userList,relationList ,this);
        searchAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        searchAdapter.isFirstOnly(false);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initListener(){
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //get suggestions based on newQuery

               // ToastUtils.showShort(oldQuery+"; "+newQuery+"; "+mSearchView.getQuery());

                getSearchedUserData(newQuery);
                //pass them on to the search view
                //mSearchView.swapSuggestions(newSuggestions);
            }
        });

       /* mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener(){

        });*/

       searchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
           @Override
           public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
               Button likeBtn = view.findViewById(R.id.like_button);
               switch (view.getId()){
                   case R.id.like_button:
                       if (likeBtn.getText().toString().equals("+关注")){
                           likeBtn.setText("已关注");
                           addRelation(userList.get(position).getUsername(),userInfo.getUserName());
                       }else{
                           //likeBtn.setText("+关注");
                       }
                       break;
                   case R.id.header_img:
                       Intent intent = new Intent(AddFriendActivity.this, HomePageActivity.class);
                       intent.putExtra("name",userList.get(position).getUsername());
                       startActivity(intent);
                       break;
               }
           }
       });
    }

    private void getSearchedUserData(String key){
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        //bmobQuery.addWhereContains("username","test");
        bmobQuery.findObjects(new FindListener<User>(){

            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    //ToastUtils.showShort("成功"+list.size());
                    userList.clear();
                    for (int i=0;i<list.size();i++){
                        if (list.get(i).getUsername().contains(key)){
                            userList.add(list.get(i));
                        }
                    }
                    searchAdapter.notifyDataSetChanged();
                    getRelationData();

                }else{
                    ToastUtils.showShort("失败");

                }
            }
        });
    }

    private void getRelationData(){
        BmobQuery<Relation> bmobQuery = new BmobQuery<>();
        //bmobQuery.addWhereContains("username","test");
        bmobQuery.findObjects(new FindListener<Relation>(){

            @Override
            public void done(List<Relation> list, BmobException e) {
                if(e==null){
                    relationList.clear();
                    relationList.addAll(list);
                    searchAdapter.notifyDataSetChanged();

                }else{
                    ToastUtils.showShort("失败");

                }
            }
        });
    }

    private void addRelation(String like, String fan){
        Relation relation = new Relation();
        relation.setLike(like);
        relation.setFan(fan);
        relation.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    ToastUtils.showShort("关注成功");
                }else{
                    ToastUtils.showShort("关注失败");
                }
            }
        });
    }
}
