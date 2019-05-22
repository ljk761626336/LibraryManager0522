package com.titan.drawtool;

import android.view.MotionEvent;
import android.view.View;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.MapView;


public class SketchDrawTouchEvent extends DefaultMapViewOnTouchListener {

    private View.OnTouchListener _listener;
    private DefaultMapViewOnTouchListener _maplistener;
    private ValueCallback callBack;


    public SketchDrawTouchEvent(MapView mapView,View.OnTouchListener lo, ValueCallback callBack) {
        super(mapView.getContext(), mapView);
        this.callBack = callBack;
        _listener = lo;
        _maplistener = (DefaultMapViewOnTouchListener) lo;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        //这里实现监听选择性调用
        if (e.getAction() == MotionEvent.ACTION_UP) {
            boolean flag = mMapView.getSketchEditor().isSketchValid();
            if(flag){
                Geometry geometry = mMapView.getSketchEditor().getGeometry();
                callBack.onGeometry(geometry);
            }
        }

        if (_maplistener != null) {
            return _maplistener.onTouch(v, e);
        }
        if (_listener != null)
            return _listener.onTouch(v, e);

        return false;
    }
}
