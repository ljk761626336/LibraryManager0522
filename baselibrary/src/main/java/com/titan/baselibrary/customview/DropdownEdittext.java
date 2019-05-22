package com.titan.baselibrary.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.titan.baselibrary.R;
/**
 * Created by li on 2016/5/31.
 * 自定义下拉列表
 */
public class DropdownEdittext extends LinearLayout {

	public AutoCompleteTextView tv;
	private TextView btn;
	Context mcontext;
	long dismissTime;//
	View DropdownEditText;
	String[] droplist;

	public DropdownEdittext(Context context, AttributeSet attrs) {
		super(context, attrs);

		DropdownEditText = LayoutInflater.from(context).inflate(R.layout.dropdown_edittextview, this);
		this.mcontext = context;
		DropdownEditText.setBackgroundResource(R.drawable.background_edittxt);
		//
		tv = (AutoCompleteTextView) findViewById(R.id.autotextview);
		btn = (TextView) findViewById(R.id.btn_dropdown);
		tv.setThreshold(100);
		tv.setDropDownAnchor(this.getId());

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!tv.isPopupShowing()
						&& (System.currentTimeMillis() - dismissTime > 100)
						&& droplist != null && droplist.length > 0) {
					btn.setBackgroundResource(R.drawable.droparrow2);
					tv.showDropDown();
				}
			}
		});

//		tv.setOnDismissListener(new OnDismissListener() {
//
//			@SuppressLint("NewApi")
//			@Override
//			public void onDismiss() {
//				btn.setBackgroundResource(R.drawable.droparrow1);
//				dismissTime = System.currentTimeMillis();
//			}
//		});
		//
		tv.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					DropdownEditText
							.setBackgroundResource(R.drawable.background_edittxt);
				} else {
					DropdownEditText
							.setBackgroundResource(R.drawable.background_edittxt);
				}
			}
		});

	}

	public void setAdapter(Context context, String[] droplist) {
		this.droplist = droplist;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext,
				android.R.layout.simple_dropdown_item_1line, droplist);

		tv.setAdapter(adapter);
	}

	@SuppressLint("MissingSuperCall")
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		if (gainFocus) {
			setBackgroundResource(R.drawable.background_edittxt);
		} else {
			setBackgroundResource(R.drawable.background_edittxt);
		}
	}

	// 获取text输入值
	public String getText() {
		String tv_text = tv.getText().toString();
		if (tv_text == null) {
			tv_text = "";
		}
		return tv_text;
	}
}
