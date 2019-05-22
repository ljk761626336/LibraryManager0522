package com.titan.baselibrary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by li on 2017/5/31.
 * 网络帮助类
 */

public class NetUtil {

    public static final int NETWORN_NONE = 0;// 无网络
    public static final int NETWORN_HAVE = 1;// 无网络
    public static final int NETWORN_WIFI = 2;// wifi网络
    public static final int NETWORN_MOBILE = 3;// 3G网络
    private Context mContext;
    public NetUtil(Context context) {
        this.mContext = context;
    }

    /**获取网络状态*/
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        int NETWORN_STATE = NETWORN_NONE;
        NetworkInfo mNetworkInfo = connManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
            // Wifi
            NetworkInfo.State state = connManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState();
            if (state != null) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORN_STATE = NETWORN_WIFI;
                }
            }
            // 3G
            NetworkInfo.State state3G = connManager.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE).getState();
            if (state3G != null) {
                if (state3G == NetworkInfo.State.CONNECTED || state3G == NetworkInfo.State.CONNECTING) {
                    return NETWORN_STATE = NETWORN_MOBILE;
                }
            }

            if (NETWORN_STATE == NETWORN_NONE) {
                return NETWORN_STATE = NETWORN_HAVE;
            }

        } else {
            return NETWORN_STATE = NETWORN_NONE;
        }
        return NETWORN_STATE;
    }

    /**检查网络是否有效*/
    public static boolean checkNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return true;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /** 关闭或打开网络 */
    // 通过反射实现开启或关闭移动数据
    public static void setMobileDataStatus(Context mContext, boolean enabled) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class<?> conMgrClass = Class.forName(mConnectivityManager
                    .getClass().getName());
            // 得到ConnectivityManager类的成员变量mService（ConnectivityService类型）
            Field iConMgrField = conMgrClass.getDeclaredField("mService");
            iConMgrField.setAccessible(true);
            // mService成员初始化
            Object iConMgr = iConMgrField.get(mConnectivityManager);
            // 得到mService对应的Class对象
            Class<?> iConMgrClass = Class.forName(iConMgr.getClass().getName());
			/*
			 * 得到mService的setMobileDataEnabled(该方法在android源码的ConnectivityService类中实现
			 * )， 该方法的参数为布尔型，所以第二个参数为Boolean.TYPE
			 */
            Method setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
			/*
			 * 调用ConnectivityManager的setMobileDataEnabled方法（方法是隐藏的），
			 * 实际上该方法的实现是在ConnectivityService(系统服务实现类)中的
			 */
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**获取手机数据*/
    public static void toggleMobileData(Context context, boolean enabled) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Method setMobileDataEnabl;
        try {
            setMobileDataEnabl = connectivityManager.getClass()
                    .getDeclaredMethod("setMobileDataEnabled", boolean.class);
            setMobileDataEnabl.invoke(connectivityManager, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 关闭wifi网络连接 */
    public static boolean openOrcloseWifi(Context mContext, boolean flag) {
        WifiManager wifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.isWifiEnabled()) {
            // 如果wifi打开
            wifiMgr.setWifiEnabled(false);
        }
        return wifiMgr.setWifiEnabled(false);
    }

}
