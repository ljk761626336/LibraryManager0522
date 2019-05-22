package com.titan.baselibrary.util;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by li on 2017/5/31.
 * Dialog高度宽度设置类
 */

public class DialogParamsUtil {


    /** 根据不同的设备dialog 宽度设置
     *pwidth 大屏幕设备的宽度权重
     *mwidth 小屏幕设备的宽度权重
     * */
    public static void setDialogParamsTop(Context context, Dialog dialog,double pwidth, double mwidth) {
        dialog.show();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        if (PadUtil.isPad(context)) {
            params.width = (int) (dm.widthPixels * pwidth);
        } else {
            params.width = (int) (dm.widthPixels * mwidth);
        }
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP;
        params.y = 150;
        dialog.getWindow().setAttributes(params);
    }

    /** 根据不同的设备dialog 宽度设置
     *pwidth 大屏幕设备的宽度权重
     *mwidth 小屏幕设备的宽度权重
     * */
    public static void setDialogParamsCenter(Context context, Dialog dialog,double pwidth, double mwidth) {
        dialog.show();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        if (PadUtil.isPad(context)) {
            params.width = (int) (dm.widthPixels * pwidth);
        } else {
            params.width = (int) (dm.widthPixels * mwidth);
        }
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
    }

    /** 根据不同的设备dialog 宽度设置
     *pwidth 大屏幕设备的宽度权重
     *mwidth 小屏幕设备的宽度权重
     * */
    public static void setDialogParamsTop(Context context, Dialog dialog) {
        dialog.show();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().getDecorView().setPadding(0,0,0,0);
    }


}
