package com.example.administrator.app.activity.fragment_home_page;

import android.content.Context;
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
import com.example.administrator.app.adapter.NewsAdapter;
import com.example.administrator.app.bean.CollectNews;
import com.example.administrator.app.bean.News;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import okhttp3.Call;

public class FragmentLike extends Fragment {

    private final static String NewsUrl="http://10.25.208.232/TP5_Splider-master/public/index.php/api/News/new_list?type=3&page=30";
    //private SliderLayout sliderLayout;
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private News news;
    public int n;
    private ArrayList arrayListcl;
    private String stringcl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_like, container, false);
        initViews(view);
        getNewsData();
        setClickListener();
        return view;
    }

    private void initViews(View view){

        newsRecyclerView = (RecyclerView) view.findViewById(R.id.rec);
        newsAdapter = new NewsAdapter(R.layout.news_item,newsArrayList ,getContext());
        newsAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        newsAdapter.isFirstOnly(false);
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    private void setClickListener(){
        newsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.news_layout:
                        /*Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                        intent.putExtra("postid",newsArrayList.get(position).getPostid());
                        startActivity(intent);*/
                        break;
                }

            }
        });

    }


    private void getNewsData(){
        OkGo.get(NewsUrl).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, okhttp3.Response response) {
                // s 即为所需要的结果
                UserInfo myInfo = JMessageClient.getMyInfo();
                CollectNews collectNews=new CollectNews();
                BmobQuery<CollectNews> collectNewsBmobQuery=new BmobQuery<>();
                collectNewsBmobQuery.findObjects(new FindListener<CollectNews>() {
                    @Override
                    public void done(List<CollectNews> list, BmobException ex) {
                        if (ex == null) {
                            ArrayList<News> newsList = new ArrayList<>();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                int n=list.size();
                                int length = jsonArray.length();
                                for (int i=0;i<length;i++){
                                    JSONObject newsObject = jsonArray.getJSONObject(i);
                                    for(int j=0;j<n;j++) {
                                        if (newsObject.getString("postid").equals(list.get(j).getPostid())&&myInfo.getUserName().equals(list.get(j).getUsername())) {
                                            newsList.add(new News(newsObject.getString("postid"), newsObject.getString("title"), newsObject.getString("digest"),
                                                    newsObject.getString("ptime"), newsObject.getString("imgsrc"), newsObject.getString("source")));
                                        }
                                    }
                                }

                                newsAdapter.addData(newsList);
                                newsAdapter.notifyDataSetChanged();
                                //LogUtils.d(newsList.size()+newsList.get(0).getImgsrc());

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                        else {
                            System.out.println(ex.getErrorCode());
                        }
                    }
                });


            }
        });
    }

}
