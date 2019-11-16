package com.example.kiit.remindmeplease;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DBVRS = 1;
    private static final String DBNAME = "RemindMe1117.db";
    public static final String TABLENAME = "Events";
    public static final String EVENTNAME = "Event";
    public static final String DATE = "Date";
    public static final String TIME = "Time";


    Context context;
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBNAME, factory, DBVRS);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLENAME + "("
                + DATE + " VARCHAR(12),"
                + TIME + " VARCHAR(8),"
                + EVENTNAME + " VARCHAR(40)"+"," +
                "PRIMARY KEY(DATE,TIME));";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addEvents(String Event,String Date,String Time){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENTNAME,Event);
        values.put(DATE,Date);
        values.put(TIME,Time);
        db.insert(TABLENAME,null,values);
        db.close();
    }
    public void delEvents(String Time){
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLENAME + " WHERE " + TIME + "=\"" + Time + "\";";
        db.execSQL(query);
        db.close();
    }
    public String getData(){
        SQLiteDatabase db = getWritableDatabase();
        String eventlist = "";
        String query = "SELECT * FROM " + TABLENAME + " WHERE 1;";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(TIME))!=null){
                eventlist +=  c.getString(c.getColumnIndex(DATE)) + " " + c.getString(c.getColumnIndex(TIME)) + "@" + c.getString(c.getColumnIndex(EVENTNAME)) + ".";
                eventlist += "\n" ;
                c.moveToNext();
            }
        }
        db.close();
        return eventlist;
    }
}
