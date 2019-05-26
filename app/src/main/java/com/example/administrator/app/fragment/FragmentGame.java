package com.example.administrator.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.app.R;
import com.example.administrator.app.fragment.fragment_game.FragmentGames;
import com.example.administrator.app.fragment.fragment_game.FragmentNews;
import com.example.administrator.app.fragment.fragment_game.FragmentRank;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

import java.util.ArrayList;


public class FragmentGame extends Fragment {
    private NavigationTabStrip navigationTabStrip;
    ArrayList<Fragment> listfragment=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LogUtils.d("这是比赛资讯界面");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        intiViews(view);
        replacedByFragment(new FragmentGames());
        setClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //navigationTabStrip.setTabIndex(0, true);
        //LogUtils.d("onresume");
    }

    private void intiViews(View view){

        navigationTabStrip = (NavigationTabStrip) view.findViewById(R.id.navigationTabStrip);
        navigationTabStrip.setTabIndex(0, true);

    }

    private void setClickListener(){
        navigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener(){
            @Override
            public void onStartTabSelected(String title, int index) {
                switch (index){
                    case 0:
                        replacedByFragment(new FragmentGames());
                        break;
                    case 1:
                        replacedByFragment(new FragmentNews());
                        break;
                    case 2:
                        replacedByFragment(new FragmentRank());
                        break;
                }
            }

            @Override
            public void onEndTabSelected(String title, int index) {

            }
        });
    }

    private void initFragments(){
        Fragment f1 = new FragmentGames();
        Fragment f2 = new FragmentNews();
        Fragment f3 = new FragmentRank();
        listfragment.add(f1);
        listfragment.add(f2);
        listfragment.add(f3);
    }

    private void replacedByFragment(Fragment fragment){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.game_framelayout,fragment);
        transaction.commit();
    }
}
