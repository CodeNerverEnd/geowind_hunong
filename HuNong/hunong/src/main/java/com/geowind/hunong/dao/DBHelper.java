package com.geowind.hunong.dao;import android.content.Context;import android.database.DatabaseErrorHandler;import android.database.sqlite.SQLiteDatabase;import android.database.sqlite.SQLiteOpenHelper;import cn.smssdk.gui.layout.TitleLayout;/** * 数据库辅助类 * Created by zhangwen on 2016/11/18. */public class DBHelper extends SQLiteOpenHelper {    public static final String DB_NAME = "hunong.db";//数据库名    public static final int DB_VERSION = 1;//历史数据库版本    public static final int CURRENT_VERSION = 2;//当前数据库版本    public static final String TASK_TABLE_NAME ="task" ;//表名    public static final String UNMAE = "muser";//农机手    public static final String FNO = "bid";//块id    public static  final  String BNAME="bname";//块名    public  static final  String ID="tid";//任务id    public static final String WORKLOAD = "workLoad";//工作量    public static final String MNO = "mid";//农机编号    public static final String TYPE = "type";//作业类型    public static final String DATE = "date";//日期    public static final String STATE ="state" ;//状态    public static final String FZNO = "zonename";//农田分区名    public static final String FAREA = "barea";//块面积    public static final String FADDR = "address";//块地址    public static final String LONGITUDE = "longitude";//经度    public static final String LATITUDE = "latitude";//纬度    public static final String FPIC = "pic";//农田照片    public static final String CROPTYPE = "croptype";//作物类型    public static final String MSTYLE = "mstyle";//农机类型    public static final String NOTE = "note";//备注    public static final String LIBSEARCH_TABLE_NAME = "libSearch";    public static final String LIBSEARCH_ID = "_id";    public static final String ARTICLE_TITLE = "title";    public static final String EXPERT_REPLY = "expert_reply";    public static final String EXPERT_ID = "_id";    public static final String CCONTENT = "ccontent";    public static final String CTIME = "ctime";    public static final String ACONTENT = "acontent";    public static final String ATIME = "atime";    public static final String REPLY_STATUS = "status";    public static final String SYSTEM_MSG = "system_msg";    public static final String USER_TABLE = "user";    public static final String SYSTEM_MSG_ID = "_id";    public static final String SYSTEM_CONTENT = "content";    public static final String SYSTEM_TITLE = "title";    public static final String USERNAME = "username";    public static final String CENTERNAME = "centerName";    public static final String SEX = "sex";    public static final String BIRTHDAY = "birthday";    public static final String PHONE = "phone";    public static final String USER_TYPE = "type";    public static final String ADDRESS = "address";    public static final String CREDIT = "credit";    public static final String PICTURE = "picture";    public static final String VALID = "valid";    public String sql;    public DBHelper(Context context) {        super(context, DB_NAME, null, DB_VERSION);    }    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {        super(context, name, factory, version, errorHandler);    }    @Override    public void onCreate(SQLiteDatabase db) {        //创建表        sql = "CREATE TABLE "+TASK_TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +                UNMAE+" VARCHAR(10), "+FNO+" INT," +BNAME+" VARCHAR(30),"+                WORKLOAD+" VARCHAR(10), "+MNO+" VARCHAR(20), "+TYPE+" VARCHAR(10), "+DATE+" VARCHAR(20), "+STATE+" VARCHAR(5), "+FZNO+" VARCHAR(20)," +                FAREA+" DOUBLE,"+FADDR+" VARCHAR(30),"+LONGITUDE+" DOUBLE,"+LATITUDE+" DOUBLE,"+FPIC+                " VARCHAR(50),"+CROPTYPE+" VARCHAR(10) ,"+MSTYLE+" VARCHAR(10),"+NOTE+" VARCHAR(200))";        db.execSQL(sql);        onUpgrade(db,DB_VERSION,CURRENT_VERSION);    }    @Override    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {       //文库搜索表        sql="CREATE TABLE "+LIBSEARCH_TABLE_NAME+"("+LIBSEARCH_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+                ARTICLE_TITLE+" VARCHAR(30))";        db.execSQL(sql);       //专家回复        sql="create table "+EXPERT_REPLY+"("+EXPERT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +                CCONTENT +" varchar(300),"+CTIME+" varchar(15),"+ACONTENT+" varchar(300),"+ATIME+" varchar(15)," +                REPLY_STATUS+" integer)";        db.execSQL(sql);        //系统消息        sql="create table "+SYSTEM_MSG+"("+SYSTEM_MSG_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +SYSTEM_TITLE+" varchar(15),"+                SYSTEM_CONTENT +" varchar(300))";        db.execSQL(sql);        //用户表        sql="create table "+USER_TABLE+"("+USERNAME+" varchar(30) PRIMARY KEY," +CENTERNAME+" varchar(30),"+                SEX +" varchar(10),"+BIRTHDAY+" datetime,"+PHONE+" varchar(20),"+USER_TYPE+" INTEGER," +                ADDRESS+" varchar(50),"+CREDIT+" varchar(10),"+PICTURE+" varchar(50),"+VALID+" INTEGER)";        db.execSQL(sql);    }}