package com.example.administrator.app.fragment.fragment_game;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.app.R;
import com.example.administrator.app.adapter.RankFragmentAdapter;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class FragmentRank extends Fragment {
    private ViewPager viewpager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        final NavigationTabBar navigationTabBar = (NavigationTabBar) view.findViewById(R.id.ntb);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.eastern_logo),
                        Color.parseColor("#F0AB58")
                ).title("东部联盟").build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.western_logo),
                        Color.parseColor("#58B6F0")
                ).title("西部联盟").build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);

        viewpager=(ViewPager) view.findViewById(R.id.rank_viewpager); //获取ViewPager
        ArrayList<Fragment> listfragment=new ArrayList<Fragment>(); //new一个List<Fragment>
        Fragment f1 = new FragmentRankEast();
        Fragment f2 = new FragmentRankWest();

        listfragment.add(f1);
        listfragment.add(f2);

        FragmentManager fm=getActivity().getSupportFragmentManager();
        RankFragmentAdapter mfpa=new RankFragmentAdapter(fm, listfragment); //new myFragmentPagerAdater记得带上两个参数

        viewpager.setAdapter(mfpa);
        viewpager.setCurrentItem(0); //设置当前页是第一页
        navigationTabBar.setViewPager(viewpager);

    }

    @Override
    public void onResume() {
        super.onResume();
       // LogUtils.d("onresume");
        viewpager.setCurrentItem(0);
    }

}
