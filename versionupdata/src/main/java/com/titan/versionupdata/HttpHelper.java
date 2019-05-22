package com.titan.versionupdata;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by li on 2017/5/26.
 * 远程访问xml地址http类
 */
public class HttpHelper {
    private static final int REQUEST_TIMEOUT = 15 * 1000;
    private static final int SO_TIMEOUT = 15 * 1000;

    /**构造*/
    public static HashMap<String, String> sendHttpRequest(Context context,String updateurl) {
        HashMap<String, String> result = getNetVersion(context,updateurl);
        return getNetVersion(context,updateurl);
    }

    /** 获取网络版本号 */
    public static HashMap<String, String> getNetVersion(Context context,String updateurl) {
        HashMap<String, String> mHashMap = new HashMap<>();
        // 把version.xml放到网络上，然后获取文件信息
		/* 获取xml */
        URL url;
        try {
            url = new URL(updateurl);
            URLConnection connection = url.openConnection();

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = 1;
            responseCode = httpConnection.getResponseCode();
            InputStream inStream = null;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
            // InputStream inStream = ParseXmlService.class.getClassLoader()
            // .getResourceAsStream("version.xml");
            // 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
            mHashMap = ParseXmlService.parseXml(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHashMap;
    }

    /*public static String doGet(String url) {
        String result = null;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {

            }
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            //Log.i("GET", "Bad Request!");
        }
        return result;
    }*/

    public static String GetJsonListValue(String str) {
        String strError = "";
        try {
            JSONObject json1 = new JSONObject(str);
            JSONObject tmp = json1.getJSONObject("d");
            return tmp.getString("a");
        } catch (Exception e) {
            strError = e.toString();
        }
        return strError;

    }

    public static JSONArray GetJsonValue(String str) {

        try {
            JSONArray json1 = new JSONArray(str);
            return json1;
        } catch (Exception e) {
            //strError=e.toString();
        }
        return null;
    }
}