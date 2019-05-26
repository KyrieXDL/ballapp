package com.example.administrator.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.administrator.app.R;
import com.example.administrator.app.bean.BallMsg;
import com.example.administrator.app.bean.UserInfo;
import com.example.administrator.app.view.MyDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;

public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener ,RouteSearch.OnRouteSearchListener{
    private Context context ;
    private LatLng latLng;
    private LatLonPoint endPoint;
    private LatLng end;
    private LinearLayout call;
    private LinearLayout navigation;
    private TextView nameTV;
    private String agentName;
    private TextView addrTV, distance_text;
    private String snippet;
    private MyDialog myDialog;
    private AMap aMap;
    private cn.jpush.im.android.api.model.UserInfo myInfo = JMessageClient.getMyInfo();
    private List<LatLng> latLngList = new ArrayList<LatLng>();

    public InfoWinAdapter(Context mContext, AMap map){
        context = mContext;
        aMap = map;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        return view;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        snippet = marker.getSnippet();
        agentName = marker.getTitle();
        endPoint = new LatLonPoint(latLng.latitude,latLng.longitude);
        end = new LatLng(latLng.latitude,latLng.longitude);
    }

    @NonNull
    private View initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_infowindow, null);
        navigation = (LinearLayout) view.findViewById(R.id.navigation_LL);
        call = (LinearLayout) view.findViewById(R.id.call_LL);
        nameTV = (TextView) view.findViewById(R.id.agent_name);
        addrTV = (TextView) view.findViewById(R.id.agent_addr);

        distance_text = (TextView) view.findViewById(R.id.distance);

        String latitude = SPUtils.getInstance("startPoint").getString("latitude");
        String longitude = SPUtils.getInstance("startPoint").getString("longitude");
        double la = Double.parseDouble(latitude);
        double lo = Double.parseDouble(longitude);
        //LatLonPoint startPoint = new LatLonPoint(la,lo);
        LatLng start = new LatLng(la,lo);
        float distance = AMapUtils.calculateLineDistance(start,end);

        distance_text.setText("距离："+(distance/1000)+"公里");


        nameTV.setText(agentName);
        addrTV.setText(snippet);

        navigation.setOnClickListener(this);
        call.setOnClickListener(this);

        myDialog = new MyDialog(context);

        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.navigation_LL:  //点击导航
               // NavigationUtils.Navigation(latLng);
                String latitude = SPUtils.getInstance("startPoint").getString("latitude");
                String longitude = SPUtils.getInstance("startPoint").getString("longitude");
                LogUtils.d("开始导航<"+latitude+", "+longitude+">");
                double la = Double.parseDouble(latitude);
                double lo = Double.parseDouble(longitude);
                navigation(la,lo);
                break;

            case R.id.call_LL:  //点击打电话
                LogUtils.d("约球，我在("+latLng.latitude+", "+latLng.longitude+")");
                myDialog.setName("发起人："+myInfo.getUserName());
                myDialog.setCourt("球场："+agentName);
                myDialog.setAddr("地址："+snippet);


                myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                       isBallMsgCreated();
                        myDialog.dismiss();
                    }
                });
                myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        //ToastUtils.showShort("取消了约球");
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                break;
        }
    }

    private void navigation(double latitude, double longitude){
        LatLonPoint startPoint = new LatLonPoint(latitude,longitude);
        RouteSearch routeSearch = new RouteSearch(context);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        routeSearch.setRouteSearchListener(this);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WALK_DEFAULT);
        routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    WalkPath walkPath = result.getPaths().get(0);
                    List<WalkStep> steps = walkPath.getSteps();
                    for (WalkStep step : steps) {
                        List<LatLonPoint> polyline = step.getPolyline();
                        for (LatLonPoint point : polyline){
                            latLngList.add(new LatLng(point.getLatitude(),point.getLongitude()));
                        }
                    }

                    aMap.addPolyline(new PolylineOptions().
                            addAll(latLngList).width(10).color(Color.argb(255, 1, 1, 1)));

                } else if (result != null && result.getPaths() == null) {
                    //ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                //ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            //ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private void createGroup(String groupName, String groupDesc){
        JMessageClient.createPublicGroup( groupName,  groupDesc, new CreateGroupCallback(){
            @Override
            public void gotResult(int i, String s, long l) {
                UserInfo userinfo = new UserInfo();
                userinfo.setUsername(myInfo.getUserName());
                userinfo.setCourt_name(agentName);
                userinfo.setGroup_name(groupName);
                userinfo.setGroup_id(l+"");
                userinfo.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if(e==null){
                            ToastUtils.showShort("成功创建群聊");
                            //LogUtils.d("testing "+"成功创建群聊");
                        }else{
                            ToastUtils.showShort("发送失败");
                            LogUtils.d(e.getMessage());
                        }
                    }
                });
            }

        });
    }

    private void isBallMsgCreated(){

        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        LogUtils.d("testing "+myInfo.getUserName()+" "+agentName);
        bmobQuery.addWhereEqualTo("username", myInfo.getUserName());
        bmobQuery.addWhereEqualTo("court_name",agentName);
        bmobQuery.findObjects(new FindListener<UserInfo>(){

            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if(e==null){
                    //ToastUtils.showShort("testing "+list.size());
                    //LogUtils.d(list.size());
                    if (list.size()!=0){
                        ToastUtils.showShort("你已经在该球场发起过约球");
                    }else{
                        sendBallMsg();

                    }
                }else{
                    ToastUtils.showShort("失败");
                    LogUtils.d("testing "+e.getMessage());
                }
            }
        });

    }

    private void sendBallMsg(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());

        BallMsg ballMsg = new BallMsg();
        ballMsg.setUsername(myInfo.getUserName());
        ballMsg.setCourt(agentName);
        ballMsg.setAddr(snippet);
        ballMsg.setContent(myDialog.getEditContent());
        //LogUtils.d(edit_content.getText().toString());
        ballMsg.setTime(simpleDateFormat.format(date));
        ballMsg.setLatitude(latLng.latitude+"");
        ballMsg.setLongitude(latLng.longitude+"");
        ballMsg.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    ToastUtils.showShort("成功发起约球");
                    //LogUtils.d("testing "+"成功发起约球");
                    createGroup(myInfo.getUserName()+agentName,"");
                }else{
                    ToastUtils.showShort("发送失败");
                    LogUtils.d(e.getMessage());
                }
            }
        });
    }
}
