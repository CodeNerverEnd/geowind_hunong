package com.geowind.hunong.weather.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by logaxy on 2016/9/27.
 */

public class MyUtils {

    /**
     * 方法名：pm2_5Rank(String pm2_5)
     * 功能：判断pm2.5等级
     *
     * @param pm2_5
     * @return
     */
    public static int pm2_5Rank(String pm2_5) {
        if (pm2_5 != null) {
            int pm = Integer.parseInt(pm2_5);
            if (pm <= 50)
                return 1;//优
            if (pm <= 100)
                return 2;//良
            if (pm <= 200)
                return 3;//中
            if (pm > 200)
                return 4;//差
        }
        return 0;//异常、无数据
    }

    /**
     * 方法名 ：isNetworkAvailable(Context context)
     * 功能：判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo == null)
            return false;
        if (netinfo.isConnected())
            return true;
        return false;
    }

    /**
     * 功能：将为先高温再低温的温度范围格式数据转化为先低温再高温的格式
     * 例：15~8℃转化为8~15℃
     * （因百度天气接口返回的温度范围格式15~8℃这种，而期望的显示格式
     * 为8~15℃这种）
     *
     * @param string 原始数据
     * @return 转换后的数据
     */
    public static String changeTempFormat(String string) {
        String returnString;
        int indexOfTilde = -1;
        indexOfTilde = string.indexOf("~");

        if (indexOfTilde == -1) {
            returnString = string;
        } else {
            String s, s1, s2;
            int indexOfTempUnit = -1;
            indexOfTempUnit = string.indexOf("℃");
            s = string.substring(0, indexOfTempUnit);//先去掉单位
            s1 = s.substring(0, indexOfTilde);
            s2 = s.substring(indexOfTilde + 1, s.length());
            returnString = s2 + "~" + s1 + "℃";
        }
        return returnString;
    }
}
