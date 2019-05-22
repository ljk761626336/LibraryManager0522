package com.titan.baselibrary.util;

import android.content.Context;

import java.io.File;

/**
 * Created by li on 2017/5/31.
 * 地址工具类
 */

public class StorageUtil {

    private Context mContext;
    private static StorageUtil instance;

    private static synchronized StorageUtil getInstance(Context context){
        if(instance == null){
            instance = new StorageUtil(context);
        }
        return instance;
    }

    private StorageUtil(Context context){
        this.mContext = context;
    }

    /**获取文件夹地址
     * name 文件夹名字
     * */
    private String getFolderPath(String name){
        String[] storages = ResourcesManager.getInstance(mContext).getStoragePath();
        for(String path : storages){
            File file = new File(path+"/"+name);
            if(file.exists()){
                return file.getPath();
            }
        }
        return "";
    }

    /**获取文件
     * name 文件名字 存在返回文件 不存在返回null
     * */
    private File getFilePath(String filename){
        String[] storages = ResourcesManager.getInstance(mContext).getStoragePath();
        for(String path : storages){
            File file = new File(path+"/"+filename);
            if(file.exists()){
                return file;
            }
        }
        return null;
    }



}
