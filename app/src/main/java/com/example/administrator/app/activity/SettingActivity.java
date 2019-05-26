package com.example.administrator.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.app.R;
import com.example.administrator.app.view.ExitDialog;

import cn.jpush.im.android.api.JMessageClient;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView logout_text;
    private ExitDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();

    }

    private void initViews(){
        logout_text = (TextView) findViewById(R.id.logout_text);
        logout_text.setOnClickListener(this);
        exitDialog = new ExitDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout_text:
                showDialog();
                break;
        }
    }

    private void showDialog(){

        exitDialog.setYesOnclickListener("确定", new ExitDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                exitDialog.dismiss();
                JMessageClient.logout();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        exitDialog.setNoOnclickListener("取消", new ExitDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                //ToastUtils.showShort("取消了约球");
                exitDialog.dismiss();
            }
        });
        exitDialog.show();
    }
}
