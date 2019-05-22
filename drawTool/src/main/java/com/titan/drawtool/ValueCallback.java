package com.titan.drawtool;

import com.esri.arcgisruntime.geometry.Geometry;

public interface ValueCallback {

    void onSuccess(Object t);

    void onFail(String value);

    void onGeometry(Geometry geometry);

}
