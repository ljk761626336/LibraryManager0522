package com.titan.baselibrary.util;

import android.content.Context;
import android.os.storage.StorageManager;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * Created by li on 2016/5/31.
 * 获取设备内存地址工具类
 */
public class ResourcesManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private static Context mContext;

	private static class LazyHolder {
		private static final ResourcesManager INSTANCE = new ResourcesManager();
	}

	public static final ResourcesManager getInstance(Context context) {
		mContext = context;
		return ResourcesManager.LazyHolder.INSTANCE;
	}


	/** 获取手机内部存储地址和外部SD卡存储地址 */
	public String[] getStoragePath() {

		StorageManager sm = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
		String[] paths = null;
		try {
			//paths = (String[]) sm.getClass().getMethod("getVolumePaths", new Class[0]).invoke(sm,new Object[]{});
			paths = (String[])sm.getClass().getMethod("getVolumePaths").invoke(sm);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return paths;
	}

}
