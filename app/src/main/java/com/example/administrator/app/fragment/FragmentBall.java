package com.example.administrator.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.administrator.app.R;
import com.example.administrator.app.activity.BallMsgActivity;
import com.example.administrator.app.adapter.InfoWinAdapter;
import com.example.administrator.app.bean.BasketallCourt;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;


public class FragmentBall extends Fragment implements PoiSearch.OnPoiSearchListener, LocationSource,AMapLocationListener,View.OnClickListener{
    private MapView mMapView;
    public AMapLocationClient mLocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption ;
    public LocationSource.OnLocationChangedListener mListener ;
    private MyLocationStyle myLocationStyle;
    private AMap aMap;
    private List<BasketallCourt> basketallCourtList = new ArrayList<>();
    PoiSearch.Query query ;
    private String cityCode = "";
    private InfoWinAdapter adapter ;
    private Marker currentMarker;
    private PoiSearch poiSearch;
    private FloatingActionButton call_btn, join_btn;
    private FloatingActionMenu menu;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ball, container, false);
        initViews(view,savedInstanceState);

        //setMapClickListener();
        return view;
    }

    private void initViews(View view,Bundle savedInstanceState){

        call_btn = (FloatingActionButton) view.findViewById(R.id.call_btn);
        call_btn.setOnClickListener(this);
        join_btn = (FloatingActionButton) view.findViewById(R.id.join_btn);
        join_btn.setOnClickListener(this);


        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.interval(5000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);

        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        adapter = new InfoWinAdapter(getContext(), aMap);

        query = new PoiSearch.Query("篮球场", "", "");
        query.setPageSize(100);
        query.setPageNum(0);
        poiSearch = new PoiSearch(getContext(), query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call_btn:
                ToastUtils.showShort("快速约球");
                break;
            case R.id.join_btn:
                Intent intent = new Intent(getActivity(), BallMsgActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getContext());
            mLocationOption = new AMapLocationClientOption();

            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(5000);

            mLocationClient.setLocationListener(this);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
            if (mLocationClient != null) {
                mLocationClient.stopLocation();
                mLocationClient.onDestroy();
            }
            mLocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (mListener != null&&aMapLocation != null) {
            if ( aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                //LogUtils.d("changed "+aMapLocation.getCityCode());
                if (!cityCode .equals(aMapLocation.getCityCode())) {
                    cityCode = aMapLocation.getCityCode();

                    String latitude = aMapLocation.getLatitude()+"";
                    String longitude = aMapLocation.getLongitude()+"";
                    SPUtils.getInstance("startPoint").put("latitude",latitude);
                    SPUtils.getInstance("startPoint").put("longitude",longitude);
                    //LogUtils.d("cityCode: " + aMapLocation.getCityCode());
                    query = new PoiSearch.Query("篮球场", "", cityCode);
                    query.setPageSize(100);
                    query.setPageNum(0);
                    poiSearch.setQuery(query);
                    poiSearch.searchPOIAsyn();
                }
            } else {
                ToastUtils.showShort("定位失败");
            }
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            ArrayList<BasketallCourt> data = new ArrayList<>();//自己创建的数据集合
            if (poiResult != null && poiResult.getQuery() != null) {
                if (poiResult.getQuery().equals(query)) {  // 是否是同一条
                    ToastUtils.showShort("查询成功");
                    List<PoiItem> poiItems = poiResult.getPois();

                    for(PoiItem item : poiItems){
                        LatLonPoint llp = item.getLatLonPoint();
                        double lon = llp.getLongitude();
                        double lat = llp.getLatitude();
                        String title = item.getTitle();
                        String text = item.getSnippet();
                        data.add(new BasketallCourt(lat,lon,title,text));
                    }

                    basketallCourtList.clear();
                    basketallCourtList.addAll(data);
                    addMoreMarket();
                }
            } else {
                ToastUtils.showShort("无结果");
            }
        } else {
            ToastUtils.showShort("查询失败");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private void addMoreMarket() {

        for (int i = 0; i < basketallCourtList.size(); i++) {
            aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(basketallCourtList.get(i).getLatitude(),//设置纬度
                            basketallCourtList.get(i).getLongitude()))//设置经度
                    .title(basketallCourtList.get(i).getTitle())//设置标题
                    .snippet(basketallCourtList.get(i).getContent())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.basketball)));
        }
        //设置自定义弹窗
        aMap.setInfoWindowAdapter(adapter);
        setMapClickListener();

    }

    private void setMapClickListener(){
        //绑定信息窗点击事件
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener(){
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                /*if(currentMarker!=null){
                    currentMarker.hideInfoWindow();
                }*/
            }
        });
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //currentMarker = marker;
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy");
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if(null != mLocationClient){
            mLocationClient.onDestroy();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("onResume");
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("onPause");
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.d("onSaveInstanceState");
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
