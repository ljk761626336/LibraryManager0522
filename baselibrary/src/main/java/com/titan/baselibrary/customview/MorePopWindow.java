package com.titan.baselibrary.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.PopupWindowCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.titan.baselibrary.R;
import com.titan.baselibrary.util.PadUtil;


@SuppressLint("ViewConstructor")
public class MorePopWindow extends PopupWindow {
	public static View conentView;
	private Activity mContext;

	public static View getConentView() {
		return conentView;
	}

	public static void setConentView(View conentView) {
		MorePopWindow.conentView = conentView;
	}

	public MorePopWindow(final Activity context, int resouce) {
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(resouce, null);
		@SuppressWarnings("deprecation")
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(conentView);
		if(PadUtil.isPad(context)){
			this.setWidth((int) (w*0.15));
		}else{
			this.setWidth((int) (w*0.25));
		}
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		//this.setAnimationStyle(R.style.AnimationPreview_down);

		this.setAnimationStyle(R.style.AnimationPreview_up);

	}

	public void showAtLocation(View parent) {
		if (!this.isShowing()) {
			int[] location = new int[2];
			parent.getLocationOnScreen(location);
			this.showAtLocation(parent, Gravity.NO_GRAVITY, (location[0] + parent.getWidth() / 2) - this.getWidth() / 2, -location[1] + this.getHeight());
		} else {
			this.dismiss();
		}
	}

	/* 弹出位置在父控件的方*/
	public void showPopupWindow(MorePopWindow mWindow,View parent) {
		if (!this.isShowing()) {
			if(PadUtil.isPad(mContext)){
				this.showAsDropDown(parent, parent.getLayoutParams().width / 2 - this.getWidth()/2, 0);
			}else{
				View contentView = mWindow.getContentView();
				//需要先测量，PopupWindow还未弹出时，宽高为0
				contentView.measure(makeDropDownMeasureSpec(mWindow.getWidth()), makeDropDownMeasureSpec(mWindow.getHeight()));
				int offsetX = Math.abs(mWindow.getContentView().getMeasuredWidth() - parent.getWidth()) / 2;
				int offsetY = -(mWindow.getContentView().getMeasuredHeight() + parent.getHeight());
				PopupWindowCompat.showAsDropDown(mWindow, parent, offsetX, offsetY, Gravity.START);
			}
		} else {
			this.dismiss();
		}
	}

	private static int makeDropDownMeasureSpec(int measureSpec) {

		int mode;
		if (measureSpec == LayoutParams.WRAP_CONTENT) {
			mode = View.MeasureSpec.UNSPECIFIED;
		} else {
			mode = View.MeasureSpec.EXACTLY;
		}
		return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
	}

}
