package com.example.projet_session.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "userinfo.db";
    public static final int DBVERSION = 1;
    
    // Table name
    private static final String TABLE_RESERVATIONS = "reservations";
    
    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_MONTANT = "montant";
    private static final String COLUMN_STATUT = "statut";
    private static final String COLUMN_NB_PLACES = "nb_places";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";

    // Create table SQL query
    private static final String CREATE_TABLE_RESERVATIONS = 
        "CREATE TABLE " + TABLE_RESERVATIONS + "("
        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_DESTINATION + " TEXT,"
        + COLUMN_DATE + " TEXT,"
        + COLUMN_MONTANT + " FLOAT,"
        + COLUMN_STATUT + " TEXT,"
        + COLUMN_NB_PLACES + " INTEGER,"
        + COLUMN_FULL_NAME + " TEXT,"
        + COLUMN_EMAIL + " TEXT,"
        + COLUMN_PHONE + " TEXT"
        + ")";

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_RESERVATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addReservation(String destination, String date, float montant, String statut, 
                             int nbPlaces, String fullName, String email, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_DESTINATION, destination);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_MONTANT, montant);
        values.put(COLUMN_STATUT, statut);
        values.put(COLUMN_NB_PLACES, nbPlaces);
        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);

        db.insert(TABLE_RESERVATIONS, null, values);
        db.close();
    }

    public Cursor getReservations() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_RESERVATIONS, null, null, null, null, null, 
                       COLUMN_DATE + " DESC");
    }

    public void deleteReservation(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_RESERVATIONS, COLUMN_ID + " = ?", new String[]{id});
        db.close();
    }
}
