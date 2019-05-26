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

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.app.activity.LiveActivity;
import com.example.administrator.app.activity.SkillDetailActivity;
import com.example.administrator.app.bean.Game;
import com.example.administrator.app.bean.GameDay;
import com.example.administrator.app.R;
import com.example.administrator.app.adapter.GameAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ldoublem.loadingviewlib.view.LVCircularRing;
import com.ldoublem.loadingviewlib.view.LVGhost;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class FragmentGames extends Fragment implements View.OnClickListener{

    private RecyclerView gameRecyclerView ;
    private GameAdapter gameAdapter;
    private ArrayList<GameDay> allGameList = new ArrayList<>();
    private final static String GamesUrl = "http://op.juhe.cn/onebox/basketball/nba?key=b5ca2acdab01c88c99d532cdfb5c3aa2";
    private TextView dateText;
    private ImageView next_img;
    private ImageView last_img;
    private int currentIndex = 0;
    private ArrayList<Game> curentGameList = new ArrayList<>();
    private int totalDay = 0;
    private LVCircularRing lvCircularRing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_games, container, false);
        initViews(view);
        getGameData();
        setClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
       // LogUtils.d("onresume");
        getGameData();
    }

    private void initViews(View view){
        gameRecyclerView = (RecyclerView) view.findViewById(R.id.games_recyclerview);
        gameAdapter = new GameAdapter(R.layout.game_item,curentGameList ,getContext());
        //gameAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        gameAdapter.isFirstOnly(false);
        gameRecyclerView.setAdapter(gameAdapter);
        gameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dateText = view.findViewById(R.id.date_text);
        next_img = view.findViewById(R.id.next);
        next_img.setOnClickListener(this);
        last_img = view.findViewById(R.id.last);
        last_img.setOnClickListener(this);

        lvCircularRing = (LVCircularRing) view.findViewById(R.id.lvCircular);
        lvCircularRing.setViewColor(getResources().getColor(R.color.blue));
        lvCircularRing.setBarColor(getResources().getColor(R.color.orange));

    }

    private void setClickListener(){
        gameAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.skill_text:
                        Intent intent = new Intent(getActivity(), SkillDetailActivity.class);
                        intent.putExtra("link2url",curentGameList.get(position).getLink2url());
                        startActivity(intent);
                        ToastUtils.showShort("暂无技术统计");
                        break;

                    case R.id.live_text:
                        Intent intent2 = new Intent(getActivity(), LiveActivity.class);
                        intent2.putExtra("link1url",curentGameList.get(position).getLink1url());
                        startActivity(intent2);
                        ToastUtils.showShort("暂无直播信息");
                        break;

                    case R.id.game_layout:

                        RelativeLayout buttonLayout = (RelativeLayout) adapter.getViewByPosition(gameRecyclerView,position,R.id.button_layout);
                        if (curentGameList.get(position).isShowMenu()){
                            //ToastUtils.showShort("clicked true");
                            //buttonLayout.setVisibility(View.GONE);
                            curentGameList.get(position).setShowMenu(false);
                        }else {
                            //ToastUtils.showShort("clicked false");
                            //buttonLayout.setVisibility(View.VISIBLE);
                            curentGameList.get(position).setShowMenu(true);
                        }

                        for (int i=0;i<curentGameList.size();i++){
                            if (curentGameList.get(position).isShowMenu() && i!=position) {
                                /*RelativeLayout relativeLayout = (RelativeLayout) gameAdapter.getViewByPosition(gameRecyclerView, i, R.id.button_layout);
                                relativeLayout.setVisibility(View.GONE);*/
                                curentGameList.get(i).setShowMenu(false);

                            }
                        }
                        gameAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next:
                if (currentIndex < totalDay-1) {
                    currentIndex++;
                    refreshGameData(currentIndex);
                }
                break;
            case R.id.last:
                if (currentIndex > 0) {
                    currentIndex--;
                    refreshGameData(currentIndex);
                }
                break;

        }
    }

    private void refreshGameData(int index){
        GameDay dayGame = allGameList.get(currentIndex);
        ArrayList<Game> dayGameList = dayGame.getGameArrayList();
        curentGameList.clear();
        curentGameList.addAll(dayGameList);
        gameAdapter.notifyDataSetChanged();
        dateText.setText(dayGame.getTitle()+" ("+dayGameList.size()+"场比赛)");
    }

    private void getGameData(){
        lvCircularRing.setVisibility(View.VISIBLE);
        lvCircularRing.startAnim();
        OkGo.get(GamesUrl).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                // s 即为所需要的结果
                //LogUtils.d(s);
                ArrayList<Game> gameList = new ArrayList<>();
                int todayIndex = 0;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = (JSONObject) jsonObject.get("result");
                    JSONArray jsonArray = result.getJSONArray("list");
                    totalDay = jsonArray.length();
                    for (int i=0;i<totalDay;i++){
                        JSONArray gameDay = jsonArray.getJSONObject(i).getJSONArray("tr");
                        Gson gson = new Gson();
                        gameList = gson.fromJson(gameDay.toString(),new TypeToken<List<Game>>() {}.getType());

                        String title = jsonArray.getJSONObject(i).get("title").toString();
                        if (title.contains("今日")){
                            currentIndex = todayIndex = i;
                        }
                        allGameList.add(new GameDay(title,gameList));
                    }
                    GameDay todayGame = allGameList.get(todayIndex);
                    ArrayList<Game> todayGameList = todayGame.getGameArrayList();
                    curentGameList.clear();
                    curentGameList.addAll(todayGameList);
                    //gameAdapter.addData(todayGameList);
                    dateText.setText(todayGame.getTitle()+" ("+todayGameList.size()+"场比赛)");
                    gameAdapter.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }

                lvCircularRing.stopAnim();
                lvCircularRing.setVisibility(View.GONE);


            }
        });
    }



}
