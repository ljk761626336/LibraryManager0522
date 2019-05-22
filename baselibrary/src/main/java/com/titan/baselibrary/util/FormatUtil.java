package com.titan.baselibrary.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by otitan_li on 2018/6/10.
 * FormatUtil
 *
 */

public class FormatUtil {

    public static DecimalFormat getFormat(){
        DecimalFormat format = new DecimalFormat(".000000");
        return format;
    }

    public static SimpleDateFormat getTimeFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat;
    }

    public static SimpleDateFormat getDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat;
    }


}
