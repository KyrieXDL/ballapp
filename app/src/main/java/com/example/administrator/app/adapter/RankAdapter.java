package com.example.administrator.app.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.app.R;
import com.example.administrator.app.bean.Team;

import java.util.List;

public class RankAdapter extends BaseQuickAdapter<Team, BaseViewHolder> {
    private Context mContext;

    public RankAdapter(int layoutResId, List data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Team item) {
        helper.setText(R.id.teamName,item.getTeamName());
        helper.setText(R.id.rank,item.getRank());
        helper.setText(R.id.win,item.getWin_num());
        helper.setText(R.id.lose,item.getLose_num());
        helper.setText(R.id.win_percentage,item.getWin_percentage());
        Glide.with(mContext).load(item.getLogoUrl()).into((ImageView) helper.getView(R.id.teamLogo));

    }
}
