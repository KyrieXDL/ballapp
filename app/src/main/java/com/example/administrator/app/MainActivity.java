package com.example.administrator.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.administrator.app.fragment.FragmentBall;
import com.example.administrator.app.fragment.FragmentFriend;
import com.example.administrator.app.fragment.FragmentGame;
import com.example.administrator.app.fragment.FragmentMe;
import com.example.administrator.app.fragment.FragmentMsg;
import com.example.administrator.app.utils.LogoUtils;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.listener.SimpleTabItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    private PageNavigationView tab;
    private NavigationController navigationController;

    final RxPermissions rxPermissions = new RxPermissions(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initFragment();
        initViews();
        setClickListener();
        requestPermissions();
    }
        List<Fragment> fragmentList =new ArrayList<>();
    private void initFragment() {
        FragmentGame fragmentGame = new FragmentGame();
        FragmentFriend fragmentFriend = new FragmentFriend();
        FragmentMsg fragmentMsg = new FragmentMsg();
        FragmentBall fragmentBall = new FragmentBall();
        FragmentMe fragmentMe = new FragmentMe();
        fragmentList.add(fragmentGame);
        fragmentList.add(fragmentFriend);
        fragmentList.add(fragmentMsg);
        fragmentList.add(fragmentBall);
        fragmentList.add(fragmentMe);
    }

    private void requestPermissions(){
        rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(permission -> { // will emit 1 Permission object
                    if (permission.granted) {
                        //ToastUtils.showShort("success");
                    }else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                    } else {
                        ToastUtils.showShort("你拒绝了权限");
                    }} );
    }

    private void initViews(){
        tab = (PageNavigationView) findViewById(R.id.tab);

        navigationController = tab.material()
                .addItem(getResources().getDrawable(R.drawable.game), getResources().getString(R.string.game))
                .addItem(getResources().getDrawable(R.drawable.friends), getResources().getString(R.string.friends))
                .addItem(getResources().getDrawable(R.drawable.msg), getResources().getString(R.string.msg))
                .addItem(getResources().getDrawable(R.drawable.ball), getResources().getString(R.string.ball))
                .addItem(getResources().getDrawable(R.drawable.me),getResources().getString(R.string.me))
                .build();

//        replacedByFragment(fragmentList.get(0));
        replacedByFragment(new FragmentGame());

    }

    private void setClickListener(){
        navigationController.addSimpleTabItemSelectedListener(new SimpleTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                // 选中时触发
                //viewpager.setCurrentItem(index,true);
                switch (index){
                    case 0:
                        replacedByFragment(new FragmentGame());
//                        replacedByFragment(fragmentList.get(0));
                        break;
                    case 1:
                        replacedByFragment(new FragmentFriend());
//                        replacedByFragment(fragmentList.get(1));
                        break;
                    case 2:
                        replacedByFragment(new FragmentMsg());
//                        replacedByFragment(fragmentList.get(2));
                        break;
                    case 3:
                        replacedByFragment(new FragmentBall());
//                        replacedByFragment(fragmentList.get(3));
                        break;
                    case 4:
                        replacedByFragment(new FragmentMe());
//                        replacedByFragment(fragmentList.get(4));
                        break;

                }
            }
        });
    }

    private void replacedByFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout,fragment);
        transaction.commit();
    }
}
