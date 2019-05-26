package com.example.administrator.app.activity.fragment_home_page;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.app.R;
import com.example.administrator.app.adapter.ShowImageAdapter;
import com.example.administrator.app.bean.TestImage;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FragmentDynamic extends Fragment {
    private RecyclerView friendRecyclerView;
    private Context mContext = null;
    private List<TestImage> relist= new ArrayList<>();
    private BmobQuery<TestImage> query = new BmobQuery<TestImage>();
    private ShowImageAdapter showImageAdapter ;
    private String username = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LogUtils.d("这是动态界面");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dynamic, container, false);
        initView(rootView);

        query = new BmobQuery<TestImage>();
        //按照时间降序
        query.addWhereEqualTo("Name", username);

        query.order("-createdAt");
        query.findObjects(new FindListener<TestImage>()
        {
            @Override
            public void done(List<TestImage> list, BmobException e) {
                if(e == null)
                {
                    for(TestImage testImage:list)
                    {
                        relist.clear();
                        relist.addAll(list);
                        showImageAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    // Toast.makeText(mContext,"获取数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        // LogUtils.d("onresume");
    }
    public void initView(View view)
    {
        friendRecyclerView = (RecyclerView) view.findViewById(R.id.friends_recyclerView);
        showImageAdapter = new ShowImageAdapter(R.layout.message_item,relist ,getContext());
        showImageAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        friendRecyclerView.setAdapter(showImageAdapter);
        friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    public void setUsername(String name){
        username = name;
    }

}
