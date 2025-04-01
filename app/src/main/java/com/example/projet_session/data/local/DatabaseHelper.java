package com.example.projet_session.data.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "userinfo.db";
    public static final int DBVERSION = 1;
    public DatabaseHelper(Context context){
        super(context, DBNAME, null, DBVERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + DBNAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, destination TEXT, date TEXT, montant FLOAT, statut TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + DBNAME);
        onCreate(sqLiteDatabase);
    }

    public void addData(String destination, String date, float montant, String statut){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("insert into " + DBNAME + " (destination, date, montant, statut) values ('" + destination + "', '" + date + "', '" + montant + "', '" + statut + "')");
        db.close();
    }

    public Cursor getData(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DBNAME, null);
        return cursor;
    }

    public void deleteData(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + DBNAME + " where ID = '" + id + "'");
        db.close();
    }

}
