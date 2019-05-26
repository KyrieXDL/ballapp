package com.example.administrator.app.fragment.fragment_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.administrator.app.R;
import com.example.administrator.app.activity.NewsDetailActivity;
import com.example.administrator.app.adapter.NewsAdapter;
import com.example.administrator.app.bean.News;
import com.ldoublem.loadingviewlib.view.LVGhost;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.Call;
import okhttp3.Response;


public class FragmentNews extends Fragment {
    private SliderLayout sliderLayout;
    private BGABanner bgaBanner;
    private final static String NewsUrl="http://10.25.208.232/TP5_Splider-master/public/index.php/api/News/new_list?type=3&page=30";
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private ArrayList<News> bannerNewsList = new ArrayList<>();
    private LVGhost lvGhost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initViews(view);
        getNewaData();
        setClickListener();
        return view;
    }

    private void initViews(View view){

        newsRecyclerView = (RecyclerView) view.findViewById(R.id.news_recyclerView);
        newsAdapter = new NewsAdapter(R.layout.news_item,newsArrayList ,getContext());
        newsAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        newsAdapter.isFirstOnly(false);
        newsRecyclerView.setAdapter(newsAdapter);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bgaBanner = (BGABanner) view.findViewById(R.id.banner_guide_content);
        bgaBanner.setAutoPlayAble(true);
        bgaBanner.setAutoPlayInterval(5000);

        bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(getActivity())
                        .load(model)
                        .into(itemView);
            }
        });

        lvGhost = (LVGhost) view.findViewById(R.id.lvghost);
        lvGhost.setViewColor(getResources().getColor(R.color.gray));

    }

    private void setClickListener(){
        newsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.news_layout:
                        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                        intent.putExtra("postid",newsArrayList.get(position).getPostid());
                        startActivity(intent);
                        break;
                }

            }
        });

        bgaBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("postid",bannerNewsList.get(position).getPostid());
                startActivity(intent);
            }
        });


    }


    private void getNewaData(){
        lvGhost.setVisibility(View.VISIBLE);
        lvGhost.startAnim();
        OkGo.get(NewsUrl).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                // s 即为所需要的结果
                ArrayList<News> newsList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    int length = jsonArray.length();
                    ArrayList<String> titleList = new ArrayList<>();
                    ArrayList<String> imgList = new ArrayList<>();
                    for (int i=0;i<length;i++){
                        JSONObject newsObject = jsonArray.getJSONObject(i);
                        if (newsObject.get("postid") != null){
                            if (i >= 5){
                            newsList.add(new News(newsObject.getString("postid"),newsObject.getString("title"),newsObject.getString("digest"),
                                    newsObject.getString("ptime"), newsObject.getString("imgsrc"),newsObject.getString("source")));
                            }else{
                                bannerNewsList.add(new News(newsObject.getString("postid"),newsObject.getString("title"),newsObject.getString("digest"),
                                        newsObject.getString("ptime"), newsObject.getString("imgsrc"),newsObject.getString("source")));

                                imgList.add(newsObject.getString("imgsrc"));
                                titleList.add(newsObject.getString("title"));

                            }
                        }
                    }
                    bgaBanner.setData(imgList,titleList);

                    newsArrayList.addAll(newsList);
                    newsAdapter.notifyDataSetChanged();
                    //LogUtils.d(newsList.size()+newsList.get(0).getImgsrc());

                }catch (Exception e){
                    e.printStackTrace();
                }
                lvGhost.stopAnim();
                lvGhost.setVisibility(View.GONE);


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
