package com.example.android.gymhelp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "MembersGym";
    private static final String COL1 = "Name";
    private static final String COL2 = "Price";
    private static final String COL3 = "Date";
    private static final String COL4 = "Email";


    public DatabaseHelper(Context context) {

        super(context, TABLE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COL1 + " VARCHAR," + COL2 + " INTEGER," +
                COL3 + " VARCHAR," + COL4 +" VARCHAR" + ")";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String Name, Integer Price, String Date,String Email) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, Name);
        contentValues.put(COL2, Price);
        contentValues.put(COL3, Date);
        contentValues.put(COL4, Email);
        Log.i("addingMember",Name);
        Log.i("addingMember",Price.toString());
        Log.i("addingMember",Date);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1

        if (result == -1) {

            return false;
        } else {
            return true;
        }

    }



    /**
     * Returns all the data from database
     *
     * @return
     */

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("was","i was here");
        db.beginTransaction();
        String query = "SELECT * FROM " + TABLE_NAME +" ORDER BY "+COL1;
        Cursor data = db.rawQuery(query, null);
        db.setTransactionSuccessful();
        db.endTransaction();
        return data;
    }



    public Cursor getDataByDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = new StringBuilder(80).append("SELECT * FROM "+TABLE_NAME+" WHERE "+COL3+" = "+"'").append(date).append("'").toString();
        Log.i("query",query);
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public String getEmailsByDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String listOfEmails;
        StringBuilder sb = new StringBuilder();
        String query = new StringBuilder(80).append("SELECT * FROM "+TABLE_NAME+" WHERE "+COL3+" = "+"'").append(date).append("'").toString();
        Cursor data = db.rawQuery(query,null);
        while (data.moveToNext()) {
            if (!data.getString(3).equals("")) {
                sb.append(data.getString(3)).append(",");
            }
        }
        listOfEmails = sb.toString();
        if (listOfEmails !=null && !listOfEmails.trim().isEmpty()) {
            listOfEmails = listOfEmails.substring(0, listOfEmails.length() - 1);
        }
        data.close();
        Log.i("list",listOfEmails);
        return listOfEmails;
    }


    public void updateName(String newName, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";

        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);

    }

    public void updateMember(String newName, String oldName,String newDate,Integer newPrice,String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updatedMember = new ContentValues();
        updatedMember.put(COL1,newName);
        updatedMember.put(COL2,newPrice);
        updatedMember.put(COL3,newDate);
        updatedMember.put(COL4,newEmail);
        db.update(TABLE_NAME,updatedMember,COL1+"='"+oldName+"'",null);
        Log.d(TAG, "updateName: Setting name to " + newName);

    }

    public void renewMembership(String Name,Integer Price,String Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues renewedMember = new ContentValues();
        renewedMember.put(COL2,Price);
        renewedMember.put(COL3,Date);
        db.update(TABLE_NAME,renewedMember,COL1+"='"+Name+"'",null);

    }

    public  boolean CheckIsDataAlreadyInDBorNot(String fieldValue) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + COL1 + " = '" + fieldValue+"'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Integer getTheNumberOfExMemships(){
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String todayDate = sdf.format(date);
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + COL3 + " = '" + todayDate+"'";
        Cursor cursor = db.rawQuery(Query, null);
        Integer numOfMemships = cursor.getCount();
        cursor.close();
        return numOfMemships;
    }





    public void deletebyName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }

    public void getReadableDatabase(String file){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        String csvFile ="/storage/emulated/0/"+file;
        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                ContentValues cv = new ContentValues();
                String httpMaker = new StringBuilder(80).append("https://www.instagram.com/web/search/topsearch/?query={").append(line).append("}").toString();
                cv.put(COL1, line);
                cv.put(COL2, httpMaker);
                db.insert(TABLE_NAME,null,cv);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void deleteDatabase(Context mContext){
        mContext.deleteDatabase(TABLE_NAME);
    }

}
