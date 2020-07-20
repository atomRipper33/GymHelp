package com.example.android.gymhelp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelperStat extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelperStat";
    private static final String TABLE_NAME = "StatisticsTableGym";
    private static final String COL1 = "ID";
    private static final String COL2 = "Date";
    private static final String COL4 = "Earnings";


    public DatabaseHelperStat(Context context) {

        super(context, TABLE_NAME, null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL2 + " DATE," +
                 COL4 + " INTEGER DEFAULT 0" + ")";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addPriceToDb(String date, Integer Earnings) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(CheckIfDateIsInDb(date)){
            int sum=Earnings + getEarningsForGivenDate(date);
            contentValues.put(COL4,sum);
            db.update(TABLE_NAME,contentValues,COL2+"='"+date+"'",null);
        }else{
            contentValues.put(COL2, date);
            contentValues.put(COL4, Earnings);
            db.insert(TABLE_NAME, null, contentValues);
        }
    }


    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        db.setTransactionSuccessful();
        db.endTransaction();
        return data;
    }
    public int getEarningsForGivenDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String query = "SELECT * FROM " + TABLE_NAME +" WHERE "+COL2+" = "+"'" + date + "'";
        Cursor data = db.rawQuery(query, null);
        data.moveToNext();
        int earnings =data.getInt(2);
        data.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return earnings;
    }


//    public Cursor getDataByYear(String year) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String queryN = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + " = " + "'" + year + "'" + " ORDER BY " + COL3;
//        String query = new StringBuilder(80).append("SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + " = " + "'").append(year).append("'").toString();
//        Cursor data = db.rawQuery(queryN, null);
//        return data;
//    }

    public boolean CheckIfDateIsInDb(String fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + COL2 + " = '" + fieldValue+"'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public Map<Integer,Integer> getEarningsForThirtyDays(String month, Integer year){
        Log.i("here","i was here");
        String firstDay =year+"-"+month+"-01";
        String lastDay= year+"-"+month+"-31";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query ="SELECT * FROM "+ TABLE_NAME + " WHERE "+ COL2 +" >= " +"'"+firstDay+"'"+ " AND "+COL2+" <= "+"'"+lastDay+"'"+ " ORDER BY "+COL2 ;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        Map<Integer,Integer> dic = new HashMap<Integer, Integer>();
        while(cursor.moveToNext()){
            String [] justDay = cursor.getString(1).split("-");
            dic.put(Integer.parseInt(justDay[2]),cursor.getInt(2));
            Log.i("day","testing day: "+Integer.parseInt(justDay[2]));
            Log.i("date:",cursor.getString(1)+":"+cursor.getInt(2));
        }
        cursor.close();
        return dic;
    }
    public String addPrefix(Integer number){
        return  "0"+number;
    }
    public Map<Integer,Integer> getEarningsForYear(Integer year){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Map<Integer,Integer> dic = new HashMap<Integer, Integer>();
        Integer sum=0;
        String firstDay="";
        String lastDay="";
        for (int i=1;i<=12;i++){
            sum=0;
            if (i<10){
                firstDay =year+"-"+addPrefix(i)+"-01";
                lastDay= year+"-"+addPrefix(i)+"-31";
            }else{
                firstDay =year+"-"+i+"-01";
                lastDay= year+"-"+i+"-31";
            }
            String query ="SELECT * FROM "+ TABLE_NAME + " WHERE "+ COL2 +" >= " +"'"+firstDay+"'"+ " AND "+COL2+" <= "+"'"+lastDay+"'"+ " ORDER BY "+COL2 ;
            Cursor cursor = sqLiteDatabase.rawQuery(query,null);
            while(cursor.moveToNext()){
                sum=sum+ cursor.getInt(2);
            }
            dic.put(i,sum);
        }
        return dic;
    }
    public int getEarningsForDay(int year, String month,String day){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String Day=year+"-"+month+"-"+day;
        String query ="SELECT * FROM "+ TABLE_NAME + " WHERE "+ COL2 +" = " +"'"+Day+"'";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        if(cursor.moveToNext()){
            int sum=  cursor.getInt(2);
            cursor.close();
            return sum;
        }else {
            return 0;
        }
    }

//    private void fillGraph(Dictionary){
//        for(int i=0;i<=31;i++){
//            if(dic.get){
//                value=dic.get;
//            }else{
//                value=0;
//            }
//        }
//    }
//    public boolean CheckIsDataAlreadyInDBorNotMonth(Integer fieldValueM) {
//        Log.i("test","checking month");
//        SQLiteDatabase sqldb = this.getWritableDatabase();
//        String Query = "Select * from " + TABLE_NAME + " where " + COL3 + " = "+fieldValueM;
//        Cursor cursor = sqldb.rawQuery(Query, null);
//        if (cursor.getCount() <= 0) {
//            Log.i("test","checking count for month:"+cursor.getCount());
//            cursor.close();
//            return false;
//        }
//        cursor.close();
//        return true;
//    }


//    public void getEarnings(String year,Integer month, Integer valueToAdd){
//        Integer valueOut = 0;
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + " = " + "'" + year + "'" + " AND "+COL3+" = "+month;
//        Cursor cursor = db.rawQuery(query,null);
//        if (cursor.moveToFirst()){
//            Log.i("test","cheking cursor size"+cursor.getCount());
//            Log.i("test","cheking cursor number "+cursor.getInt(3));
//            Log.i("test","checking valueto add "+valueToAdd);
//            valueOut= cursor.getInt(3)+valueToAdd;
//            cursor.close();
//            String queryN = "UPDATE " + TABLE_NAME + " SET " + COL4 +
//                    " = "+ valueOut+ " WHERE " + COL2 + " = '" + year + "'" +
//                    " AND " + COL3 + " = " + month;
//
//            db.execSQL(queryN);
//        }
//        cursor.close();
//    }

//    public Integer getMinMonth(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT min("+COL3+") FROM "+TABLE_NAME;
//        Cursor cursor = db.rawQuery(query,null);
//        cursor.moveToFirst();
//        int minMonth = cursor.getInt(0);
//        return minMonth;
//
//    }

}


