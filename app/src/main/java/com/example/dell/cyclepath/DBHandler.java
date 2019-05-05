package com.example.dell.cyclepath;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    SQLiteDatabase sqldb;
    public static final String Table_Name = "User_Details";
    public static final String DataBase_Name = "User_db.db";
    public static final int DataBase_ver = 1;

    String qry = "CREATE TABLE IF NOT EXISTS " + Table_Name + "(user_id integer PRIMARY KEY autoincrement,user_name varchar,user_mob varchar,wal_money varchar);";


    public DBHandler(Context context) {
        super(context, DataBase_Name, null, DataBase_ver);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(qry);
        //sqldb=this.getWritableDatabase(); if you are taking this access in oncreate then error will occur so bcoz of this create new function as below...
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldver, int newver) {


    }

    public void opendb() {
        sqldb = this.getWritableDatabase();
    }

    public void readdb() {
        sqldb = this.getReadableDatabase();
    }

    public long insertdta(GetterSetter get) {

        ContentValues cv = new ContentValues();
       /* cv.put("user_name", get.getName());
        cv.put("user_mob", get.getMobno());
        cv.put("wal_money", get.getWalmoney());
*/
        cv.put("user_name", get.getName());
        cv.put("user_mob", get.getMobno());
        cv.put("wal_money", get.getWalmoney());
        long l = sqldb.insert(Table_Name, null, cv);
        return l;
    }

    public int updatewal(String newbal, String phoneno) {
        ContentValues newValues = new ContentValues();
        newValues.put("wal_money", newbal);
        sqldb.update(Table_Name, newValues, "user_mob="+phoneno, null);
        return 1;
    }

    public int updateuser(String newbal, String name, String no) {
        ContentValues newValues = new ContentValues();
        newValues.put("wal_money", newbal);
        newValues.put("user_name", name);
        newValues.put("user_mob", no);
        sqldb.update(Table_Name, newValues, no, null);
        return 1;
    }

    public Cursor Cursor() {

        Cursor c = sqldb.rawQuery("select * from " + Table_Name, null);
        //  Cursor c = sqldb.rawQuery("select * from " + Table_Name+"WHERE user_mob =mob "++, null);
        return c;
    }

    public Cursor balanceCursor(String mob) {
        Cursor c = sqldb.rawQuery("select * from " + Table_Name + " WHERE user_mob =" + mob + "", null);
        return c;
    }
}
