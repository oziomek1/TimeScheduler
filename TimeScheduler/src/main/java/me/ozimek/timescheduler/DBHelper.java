package me.ozimek.timescheduler;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.StaticLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wojtek on 2017-08-23.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ActivitiesDB.db";
    public static final String ACTIV_TABLE_NAME = "activities";
    public static final String ACTIV_ID = "id";
    public static final String ACTIV_DATE = "date";
    public static final String ACTIV_TIME = "time";
    public static final String ACTIV_CATEGORY = "category";
    public static final String ACTIV_SUBCATEGORY = "sub_category";
    public static final String ACTIV_DESCRIPTION = "description";
    public static final String CAT_TABLE_NAME = "categories";
    public static final String CAT_ID = "id";
    public static final String CAT_CATEGORY = "category";
    public static final String SUBCAT_TABLE_NAME = "subcategories";
    public static final String SUBCAT_ID = "id";
    public static final String SUBCAT_CATEGORY = "category";
    public static final String SUBCAT_SUBCATEGORY = "subcategory";
    public static final String DESC_TABLE_NAME = "desriptions";
    public static final String DESC_ID = "id";
    public static final String DESC_DESCRIPTION = "description";


    public static Integer autoIncrementer = 0;
    private HashMap hashMap;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ACTIV_TABLE_NAME + " ("
                + ACTIV_ID + " integer primary key autoincrement, "
                + ACTIV_DATE + " text, "
                + ACTIV_TIME + " text, "
                + ACTIV_CATEGORY + " text, "
                + ACTIV_SUBCATEGORY + " text, "
                + ACTIV_DESCRIPTION + " text"
                + ")");
        db.execSQL("create table " + CAT_TABLE_NAME + " ("
                + CAT_ID + " integer primary key autoincrement, "
                + CAT_CATEGORY + " text"
                + ")");
        db.execSQL("create table " + SUBCAT_TABLE_NAME + " ("
                + SUBCAT_ID + " integer primary key autoincrement, "
                + SUBCAT_CATEGORY + " text, "
                + SUBCAT_SUBCATEGORY + " text"
                + ")");
        db.execSQL("create table " + DESC_TABLE_NAME + " ("
                + DESC_ID + " integer primary key autoincrement, "
                + DESC_DESCRIPTION + "text"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACTIV_TABLE_NAME + "");
        onCreate(db);
    }

    public boolean insert(String date, String time, String category, String subCategory, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
//        cV.put(CAT_ID, autoIncrementer);
        cV.put(ACTIV_DATE, date);
        cV.put(ACTIV_TIME, time);
        cV.put(ACTIV_CATEGORY, category);
        cV.put(ACTIV_SUBCATEGORY, subCategory);
        cV.put(ACTIV_DESCRIPTION, description);

        db.insert(ACTIV_TABLE_NAME, null, cV);
        autoIncrementer++;
        return true;
    }

    public boolean insert(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(ACTIV_DATE, date);

        db.insert(ACTIV_TABLE_NAME, null, cV);
        autoIncrementer++;
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ACTIV_TABLE_NAME + " WHERE " + ACTIV_ID + "=" + id + "", null);
        return cursor;
    }

    public int number() {
        SQLiteDatabase db = this.getReadableDatabase();
        int row = (int) DatabaseUtils.queryNumEntries(db, ACTIV_TABLE_NAME);
        return row;
    }

    public boolean update(Integer id, String date, String time, String category, String subCategory, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(ACTIV_ID, id);
        cV.put(ACTIV_DATE, date);
        cV.put(ACTIV_TIME, time);
        cV.put(ACTIV_CATEGORY, category);
        cV.put(ACTIV_SUBCATEGORY, subCategory);
        cV.put(ACTIV_DESCRIPTION, description);

        db.update(ACTIV_TABLE_NAME, cV, ACTIV_ID + " = ? ", new String[] {Integer.toString(id)});
        return true;
    }

    public ArrayList<String> getSubCategories() {
        ArrayList<String> arrayList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + ACTIV_TABLE_NAME + "", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(ACTIV_SUBCATEGORY)));
            cursor.moveToNext();
        }
        return arrayList;
    }
}
