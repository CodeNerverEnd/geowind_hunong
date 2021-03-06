package com.geowind.hunong.utils;

/**
 * Created by zhangwen on 16-7-15.
 */
public interface MyConstants {
    String CONFIGFILE="configfile";//sharePreferences的文件名
    String ISSETUP="issetup";//是否设置过向导
    String ENCODING="utf-8";//编码方式
    String USERNAME="username";//用户名id

    // String LOGINURL="http://115.159.125.122:8080/HuNong/UserServlet";

    String LOGINURL="http://115.159.125.122:8080/MutualAgriculture/UserServlet";//登录

    String LOCATION="location";//当前的位置信息
    String ISLOGIN="islogin";//当前登录状态
    String LibraryURL= "http://115.159.125.122:8080/MutualAgriculture/LibraryServlet";//文库
    String TASKURL="http://115.159.125.122:8080/MutualAgriculture/TaskServlet";//任务

    String HISTORY_TASK_URL="http://115.159.125.122:8080/MutualAgriculture/taskServlet";//历史任务
    String LIBRARY_JSON="libraryJson";//文库的json数据
    String HOMEURL="";//主页数据的URL
    String CACHED_APP_KEY="cachedAppkey";//缓存的appkey
    //病虫害反馈及专家咨询处URL，服务器地址
    String PEST_OR_CONSULT_UPLOAD_URL="http://115.159.125.122:8080/MutualAgriculture/pestOrConsultInfoUploadServlet";
    String REGISTER="http://115.159.125.122:8080/MutualAgriculture/UserServlet";//注册的
    String GETCENTER="http://115.159.125.122:8080/MutualAgriculture/UserServlet";//获取服务中心
    String LIBRARYSEARCH="http://115.159.125.122:8080/MutualAgriculture/LibraryServlet";//文库搜索
    String DATABASE="honong";//数据库名
    String TABLE_TASK="task";//任务表名
    String TABLE_SYSTEM_MSG="system_msg";//系统消息表
    String TABLE_EXPERT_REPLY="expert_reply";//专家回复
    String IS_LIGHT="isLight";//是否闪烁呼吸灯
    String IS_VIBRATE="isVibrate";//是否震动
    String USER_TYPE="userType";//用户类型
    String HISTORY_TASK="historyTask";//历史任务列表
    String VERSION="V1.0.0";
}
