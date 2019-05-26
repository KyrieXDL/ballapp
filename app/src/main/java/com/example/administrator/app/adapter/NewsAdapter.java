package com.example.administrator.app.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.app.R;
import com.example.administrator.app.bean.News;

import java.util.List;

public class NewsAdapter extends BaseQuickAdapter<News, BaseViewHolder> {
    private Context mContext;

    public NewsAdapter(int layoutResId, List data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, News item) {
        helper.setText(R.id.news_title,item.getTitle());
        helper.setText(R.id.news_ptime,item.getPtime());
        Glide.with(mContext).load(item.getImgsrc()).into((ImageView) helper.getView(R.id.news_img));
        helper.addOnClickListener(R.id.news_layout);
    }
}
