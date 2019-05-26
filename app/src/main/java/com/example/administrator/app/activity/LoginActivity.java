package com.example.administrator.app.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.administrator.app.MainActivity;
import com.example.administrator.app.R;
import com.example.administrator.app.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

import static cn.jpush.im.android.api.JMessageClient.deleteGroupConversation;
import static com.example.administrator.app.utils.UtilSetting.conversationlist;
import static com.example.administrator.app.utils.UtilSetting.offMessagelist;

public class LoginActivity extends AppCompatActivity {

    EditText edit_login_username, edit_login_password, edit_sign_username, edit_sign_password, edit_sign_confirmPass;
    RelativeLayout relativeLayout, relativeLayout2;
    LinearLayout mainLinear,img;
    TextView signUp,login,forgetPass;
    ImageView logo,back;
    LinearLayout.LayoutParams params, params2;
    FrameLayout.LayoutParams params3;
    FrameLayout mainFrame;
    ObjectAnimator animator2, animator1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //JMessageClient.init(this);

        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params3 = new FrameLayout.LayoutParams(inDp(50), inDp(50));

        signUp = (TextView) findViewById(R.id.signUp);
        login = (TextView) findViewById(R.id.login);
        edit_login_username = (EditText) findViewById(R.id.email);
        edit_login_password = (EditText) findViewById(R.id.pass);
        img = (LinearLayout) findViewById(R.id.img);
        edit_sign_username = (EditText) findViewById(R.id.email2);

        forgetPass = (TextView) findViewById(R.id.forget);
        edit_sign_password = (EditText) findViewById(R.id.pass2);
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        edit_sign_confirmPass = (EditText) findViewById(R.id.pass3);
        back = (ImageView) findViewById(R.id.backImg);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relative2);
        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);

        logo = new ImageView(this);
        logo.setImageResource(R.drawable.logo);
        logo.setLayoutParams(params3);

        relativeLayout.post(new Runnable() {
            @Override
            public void run() {

                logo.setX((relativeLayout2.getRight() / 2));
                logo.setY(inDp(50));
                mainFrame.addView(logo);
            }
        });

        params.weight = (float) 0.75;
        params2.weight = (float) 4.25;

        mainLinear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mainLinear.getWindowVisibleDisplayFrame(r);
                int screenHeight = mainFrame.getRootView().getHeight();


                int keypadHeight = screenHeight - r.bottom;


                if (keypadHeight > screenHeight * 0.15) {
                    // keyboard is opened
                    if (params.weight == 4.25) {

                        animator1 = ObjectAnimator.ofFloat(back, "scaleX", (float) 1.95);
                        animator2 = ObjectAnimator.ofFloat(back, "scaleY", (float) 1.95);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2);
                        set.setDuration(1000);
                        set.start();

                    } else {

                        animator1 = ObjectAnimator.ofFloat(back, "scaleX", (float) 1.75);
                        animator2 = ObjectAnimator.ofFloat(back, "scaleY", (float) 1.75);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2);
                        set.setDuration(500);
                        set.start();
                    }
                } else {
                    // keyboard is closed
                    animator1 = ObjectAnimator.ofFloat(back, "scaleX", 3);
                    animator2 = ObjectAnimator.ofFloat(back, "scaleY", 3);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(animator1, animator2);
                    set.setDuration(500);
                    set.start();
                }
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (params.weight == 4.25) {

                    signUp(edit_sign_username.getText().toString(),edit_sign_password.getText().toString(),edit_sign_confirmPass.getText().toString());

                    return;
                }
                edit_sign_username.setVisibility(View.VISIBLE);
                edit_sign_username.setText("");
                edit_sign_password.setVisibility(View.VISIBLE);
                edit_sign_password.setText("");
                edit_sign_confirmPass.setVisibility(View.VISIBLE);
                edit_sign_confirmPass.setText("");

                final ChangeBounds bounds = new ChangeBounds();
                bounds.setDuration(1500);
                bounds.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {


                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(signUp, "translationX", mainLinear.getWidth() / 2 - relativeLayout2.getWidth() / 2 - signUp.getWidth() / 2);
                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "translationX", -relativeLayout2.getX());
                        ObjectAnimator animator3 = ObjectAnimator.ofFloat(signUp, "rotation", 0);

                        ObjectAnimator animator4 = ObjectAnimator.ofFloat(edit_login_username, "alpha", 1, 0);
                        ObjectAnimator animator5 = ObjectAnimator.ofFloat(edit_login_password, "alpha", 1, 0);
                        ObjectAnimator animator6 = ObjectAnimator.ofFloat(forgetPass, "alpha", 1, 0);

                        ObjectAnimator animator7 = ObjectAnimator.ofFloat(login, "rotation", 90);
                        ObjectAnimator animator8 = ObjectAnimator.ofFloat(login, "y", relativeLayout2.getHeight() / 2);
                        ObjectAnimator animator9 = ObjectAnimator.ofFloat(edit_sign_username, "alpha", 0, 1);

                        ObjectAnimator animator10 = ObjectAnimator.ofFloat(edit_sign_confirmPass, "alpha", 0, 1);
                        ObjectAnimator animator11 = ObjectAnimator.ofFloat(edit_sign_password, "alpha", 0, 1);
                        ObjectAnimator animator12 = ObjectAnimator.ofFloat(signUp, "y", login.getY());

                        ObjectAnimator animator13 = ObjectAnimator.ofFloat(back, "translationX", img.getX());
                        ObjectAnimator animator14 = ObjectAnimator.ofFloat(signUp, "scaleX", 2);
                        ObjectAnimator animator15 = ObjectAnimator.ofFloat(signUp, "scaleY", 2);

                        ObjectAnimator animator16 = ObjectAnimator.ofFloat(login, "scaleX", 1);
                        ObjectAnimator animator17 = ObjectAnimator.ofFloat(login, "scaleY", 1);
                        ObjectAnimator animator18 = ObjectAnimator.ofFloat(logo, "x", relativeLayout2.getRight() / 2 - relativeLayout.getRight());

                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2, animator3, animator4, animator5, animator6, animator7,
                                animator8, animator9, animator10, animator11, animator12, animator13, animator14, animator15, animator16, animator17, animator18);
                        set.setDuration(1500).start();


                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {


                        edit_login_username.setVisibility(View.INVISIBLE);
                        edit_login_password.setVisibility(View.INVISIBLE);
                        forgetPass.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {


                    }
                });

                TransitionManager.beginDelayedTransition(mainLinear, bounds);

                params.weight = (float) 4.25;
                params2.weight = (float) 0.75;


                relativeLayout.setLayoutParams(params);
                relativeLayout2.setLayoutParams(params2);

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (params2.weight == 4.25) {
                        login(edit_login_username.getText().toString(),edit_login_password.getText().toString());
                    return;
                }

                edit_login_username.setVisibility(View.VISIBLE);
                edit_login_username.setText("");
                edit_login_password.setVisibility(View.VISIBLE);
                edit_login_password.setText("");
                forgetPass.setVisibility(View.VISIBLE);


                final ChangeBounds bounds = new ChangeBounds();
                bounds.setDuration(1500);
                bounds.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {


                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(login, "translationX", mainLinear.getWidth() / 2 - relativeLayout.getWidth() / 2 - login.getWidth() / 2);
                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "translationX", (relativeLayout.getX()));
                        ObjectAnimator animator3 = ObjectAnimator.ofFloat(login, "rotation", 0);

                        ObjectAnimator animator4 = ObjectAnimator.ofFloat(edit_login_username, "alpha", 0, 1);
                        ObjectAnimator animator5 = ObjectAnimator.ofFloat(edit_login_password, "alpha", 0, 1);
                        ObjectAnimator animator6 = ObjectAnimator.ofFloat(forgetPass, "alpha", 0, 1);

                        ObjectAnimator animator7 = ObjectAnimator.ofFloat(signUp, "rotation", 90);
                        ObjectAnimator animator8 = ObjectAnimator.ofFloat(signUp, "y", relativeLayout.getHeight() / 2);
                        ObjectAnimator animator9 = ObjectAnimator.ofFloat(edit_sign_username, "alpha", 1, 0);

                        ObjectAnimator animator10 = ObjectAnimator.ofFloat(edit_sign_confirmPass, "alpha", 1, 0);
                        ObjectAnimator animator11 = ObjectAnimator.ofFloat(edit_sign_password, "alpha", 1, 0);
                        ObjectAnimator animator12 = ObjectAnimator.ofFloat(login, "y", signUp.getY());

                        ObjectAnimator animator13 = ObjectAnimator.ofFloat(back, "translationX", -img.getX());
                        ObjectAnimator animator14 = ObjectAnimator.ofFloat(login, "scaleX", 2);
                        ObjectAnimator animator15 = ObjectAnimator.ofFloat(login, "scaleY", 2);

                        ObjectAnimator animator16 = ObjectAnimator.ofFloat(signUp, "scaleX", 1);
                        ObjectAnimator animator17 = ObjectAnimator.ofFloat(signUp, "scaleY", 1);
                        ObjectAnimator animator18 = ObjectAnimator.ofFloat(logo, "x", logo.getX()+relativeLayout2.getWidth());


                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2, animator3, animator4, animator5, animator6, animator7,
                                animator8, animator9, animator10, animator11, animator12, animator13, animator14, animator15, animator16, animator17,animator18);
                        set.setDuration(1500).start();

                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {

                        edit_sign_username.setVisibility(View.INVISIBLE);
                        edit_sign_password.setVisibility(View.INVISIBLE);
                        edit_sign_confirmPass.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });

                TransitionManager.beginDelayedTransition(mainLinear, bounds);

                params.weight = (float) 0.75;
                params2.weight = (float) 4.25;

                relativeLayout.setLayoutParams(params);
                relativeLayout2.setLayoutParams(params2);


            }
        });
    }

    private void signUp(String username, String password, String confirmPass){

        if (!confirmPass.equals(password)){
            edit_sign_password.setError("两次密码不一样");
        }
        if (password.equals("") || confirmPass.equals("")){
            edit_sign_password.setError("密码不能为空");
        }
        if (username.equals("")){
            edit_sign_username.setError("用户名不能为空");
        }else {

            JMessageClient.register(username, password, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    LogUtils.d(s+"  "+i);
                    if (s.equals("user exist")){
                        edit_sign_username.setError("用户名已存在");
                    }else if(s.equals("Invalid username.")){
                        edit_sign_username.setError("用户名不合法");
                    }else if (s.equals("Success")){
                        ToastUtils.showShort("注册成功");
                        User user = new User();
                        user.setUsername(username);
                        user.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId,BmobException e) {
                                if(e==null){
                                    ToastUtils.showShort("添加成功");
                                }else{

                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void login(String username, String password){
        if (username.equals("")){
            edit_login_username.setError("用户名不能为空");
        }
        if (password.equals("")){
            edit_login_password.setError("密码不能为空");
        }
        JMessageClient.login( username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                //LogUtils.d(s+"  "+i);
                if (s.equals("Success")){
                    conversationlist=JMessageClient.getConversationList();
                    for(Conversation c : conversationlist){
                        Message t;
                        if(c.getType().equals(ConversationType.group)){
                            t=c.getLatestMessage();
                            if(t!=null)
                                offMessagelist.add(c.getLatestMessage());
                        }
                    }
                    JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
                        @Override
                        public void gotResult(int i, String s, List<Long> list) {
                            if(i==0){
                                List<Conversation> del=new ArrayList<Conversation>();
                                for(Conversation c:conversationlist){
                                    if(c.getType()==(ConversationType.group)){
                                        //判断人是否还在群里
                                        long k=Long.parseLong(c.getTargetId());
                                        if(!list.contains(k)){
                                            del.add(c);
                                            deleteGroupConversation(k);
                                        }
                                    }
                                }
                                conversationlist.removeAll(del);
                            }else{

                            }

                        }
                    });

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else if (s.equals("user not exist")){
                    edit_login_username.setError("用户名不存在");
                }else if(s.equals("Invalid username.")){
                    edit_login_username.setError("用户名不合法");
                }
            }
        });
    }


    private int inDp(int dp) {

        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
