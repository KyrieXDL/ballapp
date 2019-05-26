package com.example.administrator.app.fragment.fragment_game;

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
import com.example.administrator.app.adapter.RankAdapter;
import com.example.administrator.app.bean.Team;
import com.ldoublem.loadingviewlib.view.LVBlock;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

public class FragmentRankWest extends Fragment {

    private RecyclerView recyclerView;
    private RankAdapter rankAdapter;
    private final static String LogoUrl="http://120.79.7.33/teamlogo/";
    private ArrayList<Team> teamArrayList = new ArrayList<>();
    private ArrayList<Team> westTeamArrayList = new ArrayList<>();
    private LVBlock lvBlock;

    private final static String RankUrl="https://route.showapi.com/1677-5?eventId=41&eventName=&season=2017&showapi_appid=88113&showapi_sign=dbc427a9819866dd449a06684e963485";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rank_west, container, false);
        initViews(view);
        //getRankData();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        getRankData();
    }

    private void initViews(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.rank_recyclerView);
        rankAdapter = new RankAdapter(R.layout.rank_item,null ,getContext());
        rankAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rankAdapter.isFirstOnly(false);
        recyclerView.setAdapter(rankAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lvBlock = view.findViewById(R.id.lvblock);

    }

    private void getRankData(){
        lvBlock.setVisibility(View.VISIBLE);
        lvBlock.setViewColor(getResources().getColor(R.color.blue));
        lvBlock.startAnim();
        OkGo.get(RankUrl).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                // s 即为所需要的结果
                ArrayList<Team> newsList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONObject("showapi_res_body").getJSONArray("teamRank");
                    int length = jsonArray.length();
                    for (int i=0;i<length;i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        teamArrayList.add(new Team(LogoUrl+object.get("team_name")+".png",object.getString("team_name"),
                                object.getString("win"),object.getString("lose"), object.getString("team_rank"),
                                object.getJSONObject("basketball").getString("win_percentage"),object.getJSONObject("basketball").getString("table_area")));
                    }

                    sortData();

                    rankAdapter.addData(westTeamArrayList);
                    rankAdapter.notifyDataSetChanged();


                }catch (Exception e){
                    e.printStackTrace();
                }

                lvBlock.stopAnim();
                lvBlock.setVisibility(View.GONE);


            }
        });
    }

    private void sortData(){
        int size = teamArrayList.size();
        for (int i=0;i<size;i++){
            Team team = teamArrayList.get(i);
            if (team.getTable_area().equals("west")){
                westTeamArrayList.add(team);
            }
        }
    }


}
