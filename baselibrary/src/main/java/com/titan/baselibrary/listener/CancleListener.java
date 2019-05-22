package com.titan.baselibrary.listener;

import android.app.Dialog;
import android.view.View;

/**
 * Created by li on 2017/5/31.
 * dialog里取消按钮事件类
 */

public class CancleListener implements View.OnClickListener {

    Dialog dialog;
    public CancleListener(Dialog dialog){
        this.dialog = dialog;
    }

    @Override
    public void onClick(View v) {
        dialog.dismiss();
    }
}
