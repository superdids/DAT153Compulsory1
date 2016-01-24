package com.example.didrik.compulsory1v3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by didrik on 24.01.16.
 */
public class ApplicationDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "persons.db";
    private static final String TABLE_PERSONS = "persons";
    private static final String COLUMN_ID = "name";
    private static final String COLUMN_URI_STRING = "uriString";

    public ApplicationDatabase(Context context, String name,
                               SQLiteDatabase.CursorFactory factory,
                               int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PERSONS + "(" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_URI_STRING + " TEXT " + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONS);
        onCreate(db);
    }

    public SQLiteDatabase getDB() {
        return getWritableDatabase();
    }

    public boolean exists(String name) {
        return find(name) != null;
    }

    public Person find(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PERSONS + " WHERE " +
                COLUMN_ID + "=\"" + name +"\";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Person ret = null;
        if (!cursor.isAfterLast()) {
            String n = cursor.getString(cursor.getColumnIndex("name"));
            String p = cursor.getString(cursor.getColumnIndex("uriString"));
            if(n != null && p != null)
                ret = new Person(n, p);
        }
        db.close();
        return ret;
    }

    public void addPerson(Person person) {
        if(exists(person.getName()))
            return;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, person.getName());
        values.put(COLUMN_URI_STRING, person.getUriString());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PERSONS, null, values);
        db.close();
    }

    public void deletePerson(String name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERSONS + " WHERE " +
                COLUMN_ID + "=\"" + name + "\";");
        db.close();
    }

    public void clearDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERSONS + " WHERE 1;");
        db.close();
    }

    public ArrayList<Person> fetchAll() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Person> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_PERSONS + " WHERE 1";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String name, uriString;
        while(!cursor.isAfterLast()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
            uriString = cursor.getString(cursor.getColumnIndex("uriString"));
            if(name != null && uriString != null)
                result.add(new Person(name, uriString));
            cursor.moveToNext();
        }
        db.close();
        return result;
    }
}
