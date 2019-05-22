package com.titan.baselibrary.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.titan.baselibrary.R;
import com.titan.baselibrary.customview.CustomProgressDialog;


/**
 * Created by li on 2016/5/31.
 * 自定义progressDialog工具类
 */
public class ProgressDialogUtil {

	@SuppressLint("HandlerLeak")
	static Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0){
				if(progressDialog != null){
					progressDialog.show();
				}
			}else if(msg.what==1){
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
			}
		}
	};
	private static CustomProgressDialog progressDialog;

	/**显示数据加载框*/
	public static void startProgressDialog(final Context context,String mesage) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context);
			progressDialog.setMessage(mesage);
		}
		Message message = new Message();
		message.what = 0;
		handler.sendMessage(message);
	}

	/**显示数据加载框*/
	public static void startProgressDialog(final Context context) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context);
			progressDialog.setMessage("加载中...");
		}
		Message message = new Message();
		message.what = 0;
		handler.sendMessage(message);
	}
	/**关闭数据加载框*/
	public static void stopProgressDialog(Context context) {
		Message message = new Message();
		message.what = 1;
		handler.sendMessage(message);
	}

}
