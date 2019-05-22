package com.titan.drawtool;

import android.graphics.Color;

import com.esri.arcgisruntime.symbology.FillSymbol;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.MarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

/**
 * 要素编辑状态符号化信息
 * Created by gis-luq on 15/5/21.
 */
public class DrawSymbol {

    private static int SIZE=12;//节点大小

    public static MarkerSymbol markerSymbol  = new SimpleMarkerSymbol( SimpleMarkerSymbol.Style.CIRCLE,Color.RED, SIZE);
    public static SimpleMarkerSymbol mRedMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,Color.RED, SIZE);
    public static SimpleMarkerSymbol mBlackMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,Color.BLACK, SIZE);
    public static SimpleMarkerSymbol mGreenMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,Color.GREEN, SIZE);
    public static LineSymbol mLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,Color.RED, 3);
    public static FillSymbol mFillSymbol =  new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,Color.TRANSPARENT,mLineSymbol);

}
