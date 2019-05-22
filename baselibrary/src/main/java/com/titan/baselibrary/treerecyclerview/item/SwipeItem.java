package com.titan.baselibrary.treerecyclerview.item;

import android.support.annotation.NonNull;

import com.titan.baselibrary.treerecyclerview.base.ViewHolder;
import com.titan.baselibrary.treerecyclerview.widget.swipe.SwipeItemMangerInterface;
import com.titan.baselibrary.treerecyclerview.widget.swipe.SwipeLayout;


/**
 * Created by Administrator on 2017/8/18 0018.
 * 实现该接口,侧滑删除
 */

public interface SwipeItem {

    int getSwipeLayoutId();

    SwipeLayout.DragEdge getDragEdge();

    void onBindSwipeView(@NonNull ViewHolder viewHolder, int position, SwipeItemMangerInterface swipeManger);

    void openCallback();
}
