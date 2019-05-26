package com.example.administrator.app.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.app.R;
import com.example.administrator.app.bean.BallCourt;
import com.example.administrator.app.bean.BallMsg;

import java.util.List;

public class BallMsgAdapter extends BaseQuickAdapter<BallCourt, BaseViewHolder> {
    private Context mContext;

    public BallMsgAdapter(int layoutResId, List data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BallCourt item) {
        helper.setText(R.id.court_name,item.getCourt_name());
        helper.setText(R.id.court_addr,item.getCourt_addr());
        helper.addOnLongClickListener(R.id.cardview);
        Glide.with(mContext).load(item.getCourt_img()).into((ImageView) helper.getView(R.id.court_img));

        String latitude = SPUtils.getInstance("startPoint").getString("latitude");
        String longitude = SPUtils.getInstance("startPoint").getString("longitude");
        double la = Double.parseDouble(latitude);
        double lo = Double.parseDouble(longitude);
        LatLng start = new LatLng(la,lo);
        LatLng end = new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude()));
        float distance = AMapUtils.calculateLineDistance(start,end);
        helper.setText(R.id.court_distance,"距离："+(distance/1000)+"公里");

    }
}
