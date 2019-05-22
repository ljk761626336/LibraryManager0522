package com.titan.baselibrary.mview;

import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by Jelly on 2016/9/3.
 *
 * 图片放大、滑动页面 UI接口
 */
public interface ImageBrowseView {

    Intent getDataIntent();

    void setImageBrowse(List<String> images, int position);

    Context getMyContext();
}
