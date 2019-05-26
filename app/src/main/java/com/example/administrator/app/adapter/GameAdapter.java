package com.example.administrator.app.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.app.bean.Game;
import com.example.administrator.app.R;

import java.util.List;

public class GameAdapter extends BaseQuickAdapter<Game, BaseViewHolder> {
    private Context mContext;
    public GameAdapter(int layoutResId, List data , Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Game item) {

        helper.setText(R.id.player1_name,item.getPlayer1());
        Glide.with(mContext).load(item.getPlayer1LogoUrl()).into((ImageView) helper.getView(R.id.player1_logo));
        helper.setText(R.id.player2_name,item.getPlayer2());
        Glide.with(mContext).load(item.getPlayer2LogoUrl()).into((ImageView) helper.getView(R.id.player2_logo));

        String[] score = item.getScore().split(":");
        helper.setText(R.id.player1_score, score[0]);
        helper.setText(R.id.player2_score, score[1]);

        String gameStatus = "";
        if (item.getStatus() == 0){
            gameStatus += "未开始";
        }else if (item.getStatus() == 1){
            gameStatus += "进行中";
        }else if (item.getStatus() == 2){
            gameStatus += "结束";
        }
        helper.setText(R.id.game_status, gameStatus);
        helper.addOnClickListener(R.id.game_layout);
        helper.addOnClickListener(R.id.skill_text);
        helper.addOnClickListener(R.id.live_text);

        if (!item.isShowMenu()){
            helper.getView(R.id.button_layout).setVisibility(View.GONE);
        }else{
            helper.getView(R.id.button_layout).setVisibility(View.VISIBLE);

        }

    }
}