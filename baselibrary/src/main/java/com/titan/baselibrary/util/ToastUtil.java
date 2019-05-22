package com.titan.baselibrary.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import com.titan.baselibrary.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 信息提示工具
*/
public class ToastUtil {


	@SuppressLint("ShowToast")
	private static Toast makeText(Context context, CharSequence text,int duration) {
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context, R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		textView.setTextSize(18);
		result.setView(textView);
		result.setGravity(Gravity.CENTER, 0, 120);
		return result;
	}

	@SuppressLint("ShowToast")
	private static Toast makeTextBootem(Context context, CharSequence text,int duration) {
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context,R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		textView.setTextSize(18);
		result.setView(textView);
		result.setGravity(Gravity.BOTTOM, 0, 120);
		return result;
	}

	@SuppressLint("ShowToast")
	private static Toast makeTextLeft(Context context, CharSequence text,int duration){
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context,R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		result.setView(textView);
		result.setGravity(Gravity.LEFT, 100, 120);
		return result;
	}

	/**
	 * 提示
	 * @param context activity
	 * @param text 提示内容
	 */
	public static void setToast(final Context context, final String text) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 此处执行UI操作
                ToastUtil.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
	}

	/**
	 * 提示
	 * @param context activity
	 * @param text 提示内容
	 */
	public static void setToastLow(final Context context, final String text) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				// 此处执行UI操作
				Toast toast = ToastUtil.makeText(context, text, Toast.LENGTH_SHORT);

				showMyToast(toast,1000);
			}
		});
	}


	/**
	 * 提示
	 * @param context activity
	 * @param txt 提示内容
	 */
	public static void setToast(final Context context,final int txt) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				String text = context.getResources().getString(txt);
				ToastUtil.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
	/**
	 * 左侧提示
	 * @param context activity
	 * @param text 提示内容
	 */
	public static void setToatLeft(final Context context, final String text){
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				// 此处执行UI操作
				ToastUtil.makeTextLeft(context, text,Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 底部提示
	 * @param context activity
	 * @param text 提示内容
	 */
	public static void setToatBootem(final Context context, final String text){
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				// 此处执行UI操作
				ToastUtil.makeTextBootem(context, text,Toast.LENGTH_SHORT).show();
			}
		});
	}

	private static void showMyToast(final Toast toast, final int cnt) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				toast.show();
			}
		}, 0, 3500);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				toast.cancel();
				timer.cancel();
			}
		}, cnt );
	}
}
