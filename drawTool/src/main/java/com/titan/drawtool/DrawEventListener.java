package com.titan.drawtool;


import java.util.EventListener;


/**
 * update by gis-luq
 *	定义画图事件监听接口
 */
public interface DrawEventListener extends EventListener {

	void handleDrawEvent(DrawEvent event);
}
