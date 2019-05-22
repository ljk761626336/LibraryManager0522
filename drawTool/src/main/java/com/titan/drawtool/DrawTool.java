package com.titan.drawtool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.esri.arcgisruntime.geometry.EnvelopeBuilder;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SketchCreationMode;
import com.esri.arcgisruntime.mapping.view.SketchEditor;
import com.esri.arcgisruntime.mapping.view.SketchStyle;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

import java.util.ArrayList;

/**
 * 画图实现类，支持画点、矩形、线、多边形、圆、手画线、手画多边形，可设置各种图形的symbol。
 */
public class DrawTool extends Subject {

    private static String TAG = "DrawTool";

    private MapView mapView;
    private GraphicsOverlay mGraphicsLayerEditing;//绘制要素图层
    private GraphicsOverlay tempLayer;//零时图层，用于存储要素各个节点信息
    private int drawType;//当前要素绘制类型
    private boolean active;
    private boolean isAllowDoubleTouchToEnd = true;//是否允许双击结束绘制
    private EnvelopeBuilder envelopeBuilder;
    private DrawTouchListener drawListener;//绘图事件
    private DefaultMapViewOnTouchListener defaultListener;//默认点击事件
    private Graphic drawGraphic;//当前绘制要素
    private Point startPoint;//当前绘制要素起点信息

    private boolean isCompleteDraw = true;//是否完成要素绘制

    //=======================================================================================================
    ArrayList<Point> mPoints = new ArrayList<Point>(); //节点
    ArrayList<Point> mMidPoints = new ArrayList<Point>(); //中间点
    boolean mMidPointSelected = false; //线段中间点
    boolean mVertexSelected = false;//顶点是否选择
    int mInsertingIndex;//插入位置信息
    ArrayList<EditingStates> mEditingStates = new ArrayList<EditingStates>();//编辑状态信息
    private PointCollection pointCollection;

    public SketchEditor sketchEditor;

    public ValueCallback getCallBack() {
        return callBack;
    }

    public void setCallBack(ValueCallback callBack) {
        this.callBack = callBack;
    }

    private ValueCallback callBack;
    private SpatialReference defaultSpatial;

    public DrawTool(MapView mapView, SpatialReference defaultSpatial) {
        this.mapView = mapView;
        this.defaultSpatial = defaultSpatial;
        this.mGraphicsLayerEditing = new GraphicsOverlay();
        this.mapView.getGraphicsOverlays().add(this.mGraphicsLayerEditing);
        this.tempLayer = new GraphicsOverlay();
        this.mapView.getGraphicsOverlays().add(this.tempLayer);

        this.drawListener = new DrawTouchListener(this.mapView.getContext(), this.mapView);
        this.defaultListener = new MyDefaultMapViewOnTouchListener(this.mapView.getContext(), this.mapView);
        this.sketchEditor = mapView.getSketchEditor();
        mapView.setOnTouchListener(this.defaultListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void activate(int drawType) {
        if (this.mapView == null)
            return;

        this.deactivate();

        this.mapView.setOnTouchListener(drawListener);
        this.drawType = drawType;
        this.active = true;
        if (sketchEditor == null) {
            sketchEditor = new SketchEditor();

            SimpleMarkerSymbol mPointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, 0xFFFF0000, 20);

            SketchStyle sketchStyle = new SketchStyle();
            sketchStyle.setFeedbackVertexSymbol(mPointSymbol);
            sketchStyle.setLineSymbol(DrawSymbol.mLineSymbol);
            sketchStyle.setFillSymbol(DrawSymbol.mFillSymbol);
            sketchEditor.setSketchStyle(sketchStyle);

            sketchEditor.addGeometryChangedListener(new GeometryChangedListener(mapView, callBack));
            mapView.setSketchEditor(sketchEditor);
        }
        switch (this.drawType) {
            case DrawType.POINT:
                sketchEditor.start(SketchCreationMode.POINT);
                break;
            case DrawType.POLYGON:
                sketchEditor.start(SketchCreationMode.POLYGON);
                break;
            case DrawType.ENVELOPE:
                envelopeBuilder = new EnvelopeBuilder(this.defaultSpatial);
                drawGraphic = new Graphic();
                drawGraphic.setGeometry(envelopeBuilder.toGeometry());
                drawGraphic.setSymbol(DrawSymbol.mFillSymbol);
                mGraphicsLayerEditing.getGraphics().add(drawGraphic);
                drawGraphic.getGraphicsOverlay().setOpacity(0.7f);
                break;
            case DrawType.FREEHAND_POLYGON:
                sketchEditor.start(SketchCreationMode.FREEHAND_POLYGON);
                break;
            case DrawType.POLYLINE:
                sketchEditor.start(SketchCreationMode.POLYLINE);
                break;
            case DrawType.FREEHAND_POLYLINE:
                sketchEditor.start(SketchCreationMode.FREEHAND_LINE);
                break;
            case DrawType.CIRCLE:
                pointCollection = new PointCollection(mapView.getSpatialReference());
                break;
        }

        isCompleteDraw = false;//开始绘制
    }

    @SuppressLint("ClickableViewAccessibility")
    public void deactivate() {
        this.mapView.setOnTouchListener(defaultListener);
        this.active = false;
        this.isCompleteDraw = true;//完成绘制
        this.drawType = -1;
        this.drawGraphic = null;
        this.startPoint = null;
    }


    public void sendDrawEndEvent() {
        if (drawType != DrawType.ENVELOPE) {
            drawGraphic.setGeometry(sketchEditor.getGeometry());
        }
        DrawEvent e = new DrawEvent(this, DrawEvent.DRAW_END, DrawTool.this.drawGraphic);
        DrawTool.this.notifyEvent(e);
        int type = this.drawType;
        this.deactivate();
        if (type == DrawType.POINT) {
            this.activate(type);
        }
    }


    /**
     * 刷新要素信息
     */
    private void refresh() {
        if (tempLayer != null) {
            tempLayer.getGraphics().clear();
        }
        drawMidPoints(); //绘制要素中心点
        drawVertices(); //绘制要素节点
    }

    /**
     * 绘制节点的中心点
     */
    private void drawMidPoints() {
        int index;
        Graphic graphic;

        mMidPoints.clear();
        if (mPoints.size() > 1) {

            // Build new list of mid-points
            for (int i = 1; i < mPoints.size(); i++) {
                Point p1 = mPoints.get(i - 1);
                Point p2 = mPoints.get(i);
                mMidPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }
            if (drawType == DrawType.POLYGON && mPoints.size() > 2) {
                // Complete the circle
                Point p1 = mPoints.get(0);
                Point p2 = mPoints.get(mPoints.size() - 1);
                mMidPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }

            // Draw the mid-points
            index = 0;
            for (Point pt : mMidPoints) {
                if (mMidPointSelected && mInsertingIndex == index) {
                    graphic = new Graphic(pt, DrawSymbol.mRedMarkerSymbol);
                } else {
                    graphic = new Graphic(pt, DrawSymbol.mGreenMarkerSymbol);
                }
                this.tempLayer.getGraphics().add(graphic);
                index++;
            }
        }
    }

    /**
     * 绘制要素的节点信息在mPoints中.
     */
    private void drawVertices() {
        int index = 0;
        SimpleMarkerSymbol symbol;

        for (Point pt : mPoints) {
            if (mVertexSelected && index == mInsertingIndex) {
                // This vertex is currently selected so make it red
                symbol = DrawSymbol.mRedMarkerSymbol;
            } else if (index == mPoints.size() - 1 && !mMidPointSelected && !mVertexSelected) {
                // Last vertex and none currently selected so make it red
                symbol = DrawSymbol.mRedMarkerSymbol;
            } else {
                // Otherwise make it black
                symbol = DrawSymbol.mBlackMarkerSymbol;
            }
            Graphic graphic = new Graphic(pt, symbol);
            tempLayer.getGraphics().add(graphic);//添加节点信息到零时图层
            index++;
        }
    }

    /**
     * 清除节点和终点信息
     */
    private void clear() {
        // 清除要素编辑状态数据
        mPoints.clear();
        mMidPoints.clear();
        mEditingStates.clear();

        mMidPointSelected = false;
        mVertexSelected = false;
        mInsertingIndex = 0;

        if (tempLayer != null) {
            tempLayer.getGraphics().clear();
        }
    }


    /**
     * 节点回退操作.
     */
    public boolean actionUndo() {
        if (active && (drawType == DrawType.POLYGON || drawType == DrawType.POLYLINE)) {
            if (mPoints.size() >= 1) {//删除时至少保留一个节点
                if (mEditingStates.size() >= 1) {
                    if (mEditingStates.size() == 0) {
                        mMidPointSelected = false;
                        mVertexSelected = false;
                        mInsertingIndex = 0;
                    } else {
                        mPoints.clear();//清空节点要素
                        EditingStates state = mEditingStates.get(mEditingStates.size() - 1);
                        mPoints.addAll(state.points);
                        //Log.d(TAG, "# of points = " + mPoints.size());
                        mMidPointSelected = state.midPointSelected;
                        mVertexSelected = state.vertexSelected;
                        mInsertingIndex = state.insertingIndex;
                        refresh();
                        mEditingStates.remove(mEditingStates.size() - 1);
                        Toast.makeText(mapView.getContext(), "撤销操作", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                } else {
                    Toast.makeText(mapView.getContext(), "当前状态无法撤销", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }


    /**
     * 当一个新的点被添加/移动/删除时该实例被创建
     * 用于记录编辑的状态信息，用于允许编辑的回退操作
     */
    private class EditingStates {
        ArrayList<Point> points = new ArrayList<Point>();

        boolean midPointSelected = false;
        boolean vertexSelected = false;
        int insertingIndex;

        public EditingStates(ArrayList<Point> points, boolean midpointselected, boolean vertexselected, int insertingindex) {
            this.points.addAll(points);
            this.midPointSelected = midpointselected;
            this.vertexSelected = vertexselected;
            this.insertingIndex = insertingindex;
        }
    }

    /**
     * 扩展MapOnTouchListener，实现画图功能
     */
    class DrawTouchListener extends DefaultMapViewOnTouchListener {

        public DrawTouchListener(Context context, MapView view) {
            super(context, view);
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            Point point = toMapPoint(event);
            if (active && drawType == DrawType.ENVELOPE && event.getAction() == MotionEvent.ACTION_DOWN) {
                switch (drawType) {
                    case DrawType.ENVELOPE:
                        startPoint = point;
                        envelopeBuilder.setXY(point.getX(), point.getY(), point.getX(), point.getY());
                        break;
                    case DrawType.CIRCLE:
                        pointCollection.add(point);
                        double radius = 0;
                        if (pointCollection.size() == 2) {
                            double x = (pointCollection.get(1).getX() - pointCollection.get(0).getX());
                            double y = (pointCollection.get(1).getY() - pointCollection.get(0).getY());
                            radius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                        }

                        getCircle(pointCollection.get(0), radius);
                }
            }
            return super.onTouch(view, event);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onUp(MotionEvent e) {
            //sendDrawEndEvent();
            if (drawGraphic != null) {
                callBack.onGeometry(drawGraphic.getGeometry());
            }
            return true;
        }

        @Override
        public boolean onRotate(MotionEvent event, double rotationAngle) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent from, MotionEvent to, float velocityX, float velocityY) {
            Point point = toMapPoint(to);
            if (active && drawType == DrawType.ENVELOPE) {
                switch (drawType) {
                    case DrawType.ENVELOPE:

                        envelopeBuilder.setXMin(startPoint.getX() > point.getX() ? point
                                .getX() : startPoint.getX());
                        envelopeBuilder.setYMin(startPoint.getY() > point.getY() ? point
                                .getY() : startPoint.getY());
                        envelopeBuilder.setXMax(startPoint.getX() < point.getX() ? point
                                .getX() : startPoint.getX());
                        envelopeBuilder.setYMax(startPoint.getY() < point.getY() ? point
                                .getY() : startPoint.getY());

                        boolean flag = envelopeBuilder.isSketchValid();
                        if (flag) {
                            drawGraphic.setGeometry(envelopeBuilder.toGeometry());
                        }

                        break;
                }
                return true;
            }
            return super.onFling(from, to, velocityX, velocityY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            if (active && (drawType == DrawType.POLYGON || drawType == DrawType.POLYLINE)) {
//                Point point = mapView.toMapPoint(event.getX(), event.getY());
//                //双击最后一个点不算在内
//                switch (drawType) {
//                    case DrawTool.POLYGON:
//                        polygon.lineTo(point);
//                        break;
//                    case DrawTool.POLYLINE:
//                        polyline.lineTo(point);
//                        break;
//                }
                int ptNum = mPoints.size();
                if (drawType == DrawType.POLYLINE) {
                    if (ptNum < 2) {
                        Toast.makeText(mapView.getContext(), "线段至少两个节点", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                } else if (drawType == DrawType.POLYGON) {
                    if (ptNum < 3) {
                        Toast.makeText(mapView.getContext(), "面至少三个节点", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                isCompleteDraw = true;//开始绘制
                if (isAllowDoubleTouchToEnd) {
                    sendDrawEndEvent();
                    startPoint = null;
                }
                return true;
            }
            return super.onDoubleTap(event);
        }

        /***
         * Handle a tap on the map (or the end of a magnifier long-press event).
         * @param event The point that was tapped.
         */
        private boolean handleTap(MotionEvent event) {
            Point point = toMapPoint(event);
            if (active && (drawType == DrawType.POLYGON || drawType == DrawType.POLYLINE)) {
                // If a point is currently selected, move that point to tap point
                if (mMidPointSelected || mVertexSelected) {
                    movePoint(point);
                } else {
                    // If tap coincides with a mid-point, select that mid-point
                    int idx1 = getSelectedIndex(event.getX(), event.getY(), mMidPoints, mapView);
                    if (idx1 != -1) {
                        mMidPointSelected = true;
                        mInsertingIndex = idx1;
                    } else {
                        // If tap coincides with a vertex, select that vertex
                        int idx2 = getSelectedIndex(event.getX(), event.getY(), mPoints, mapView);
                        if (idx2 != -1) {
                            mVertexSelected = true;
                            mInsertingIndex = idx2;
                        } else {
                            // No matching point above, add new vertex at tap point
                            mEditingStates.add(new EditingStates(mPoints, mMidPointSelected, mVertexSelected, mInsertingIndex));
                            mPoints.add(point);
                        }
                    }
                }
                refresh();
                return true;
            } else if (drawType == DrawType.POINT) {
                //DrawTool.this.point.setXY(point.getX(), point.getY());
                sendDrawEndEvent();
                isCompleteDraw = true;//开始绘制
                return true;
            }
            return false;//默认为false
        }


        /**
         * Checks if a given location coincides (within a tolerance) with a point in a given array.
         *
         * @param x      Screen coordinate of location to check.
         * @param y      Screen coordinate of location to check.
         * @param points Array of points to check.
         * @param map    MapView containing the points.
         * @return Index within points of matching point, or -1 if none.
         */
        private int getSelectedIndex(double x, double y, ArrayList<Point> points, MapView map) {
            final int TOLERANCE = 40; // Tolerance in pixels

            if (points == null || points.size() == 0) {
                return -1;
            }

            // Find closest point
            int index = -1;
            double distSQ_Small = Double.MAX_VALUE;
            for (int i = 0; i < points.size(); i++) {
                Point p = points.get(i);
                double diffx = p.getX() - x;
                double diffy = p.getY() - y;
                double distSQ = diffx * diffx + diffy * diffy;
                if (distSQ < distSQ_Small) {
                    index = i;
                    distSQ_Small = distSQ;
                }
            }

            // Check if it's close enough
            if (distSQ_Small < (TOLERANCE * TOLERANCE)) {
                return index;
            }
            return -1;
        }

        /**
         * 移动当前选择点
         *
         * @param point Location to move the point to.
         */
        private void movePoint(Point point) {
            if (mMidPointSelected) {
                // Move mid-point to the new location and make it a vertex
                mPoints.add(mInsertingIndex + 1, point);
            } else {
                // Must be a vertex: move it to the new location
                ArrayList<Point> temp = new ArrayList<Point>();
                for (int i = 0; i < mPoints.size(); i++) {
                    if (i == mInsertingIndex) {
                        temp.add(point);
                    } else {
                        temp.add(mPoints.get(i));
                    }
                }
                mPoints.clear();
                mPoints.addAll(temp);
            }
            // Go back to the normal drawing mode and save the new editing state
            mMidPointSelected = false;
            mVertexSelected = false;
            mEditingStates.add(new EditingStates(mPoints, mMidPointSelected, mVertexSelected, mInsertingIndex));
        }

    }

    /**
     * 绘制圆
     */
    /*private void getCircle(Point center, double radius, Polygon circle) {
        circle.setEmpty();
        Point[] points = getPoints(center, radius);
        circle.startPath(points[0]);
        for (int i = 1; i < points.length; i++)
            circle.lineTo(points[i]);
    }

    private Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }*/

    private Point toMapPoint(MotionEvent event) {
        Point point = mapView.screenToLocation(new android.graphics.Point((int) event.getX(), (int) event.getY()));
        return point;
    }

    class MyDefaultMapViewOnTouchListener extends DefaultMapViewOnTouchListener {

        public MyDefaultMapViewOnTouchListener(Context context, MapView mapView) {
            super(context, mapView);
        }

        @Override
        public boolean onRotate(MotionEvent event, double rotationAngle) {
            return true;
        }
    }

    private void getCircle(Point point, double radius) {
        //        polygon.setEmpty();
        Point[] points = getPoints(point, radius);
        pointCollection.clear();
        for (Point p : points) {
            pointCollection.add(p);
        }
        Polygon polygon = new Polygon(pointCollection);

        SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 10);
        Graphic pointGraphic = new Graphic(point, simpleMarkerSymbol);
        mGraphicsLayerEditing.getGraphics().add(pointGraphic);

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.parseColor("#FC8145"), 3.0f);
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.parseColor("#33e97676"), lineSymbol);

        Graphic graphic = new Graphic(polygon, simpleFillSymbol);

        mGraphicsLayerEditing.getGraphics().add(graphic);
    }

    /**
     * 通过中心点和半径计算得出圆形的边线点集合
     */
    private Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }

}
