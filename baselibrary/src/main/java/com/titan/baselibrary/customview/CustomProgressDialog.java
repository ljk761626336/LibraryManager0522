package com.titan.baselibrary.customview;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.titan.baselibrary.R;
/**
 * Created by li on 2016/5/31.
 * 自定义progressDialog，请求数据耗时展示
 */
public class CustomProgressDialog extends Dialog {
	private static CustomProgressDialog customProgressDialog = null;

	public CustomProgressDialog(Context context) {
		super(context);
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static CustomProgressDialog createDialog(Context context) {
		customProgressDialog = new CustomProgressDialog(context, R.style.progress_dialog);
		customProgressDialog.setContentView(R.layout.customprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setCanceledOnTouchOutside(false);
		customProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText("数据加载中...");//"数据加载中..."
		}
		//customProgressDialog.setCancelable(false);// 不可以用“返回键”取消
//		customProgressDialog.setOnKeyListener(new OnKeyListener() {
//			
//			@Override
//			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
//				switch (keyCode) {
//				case KeyEvent.KEYCODE_BACK:
//					return true;
//				}
//				return false;
//			}
//		});
		return customProgressDialog;
	}

	/**设置标题*/
	public CustomProgressDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**设置显示文字*/
	public CustomProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return customProgressDialog;
	}
}
