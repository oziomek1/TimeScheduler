package me.ozimek.timescheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by wojtek on 2017-09-02.
 */

public class DBCategory extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ActivitiesDB";
    public static final String CAT_TABLE_NAME = "categories";
    public static final String CAT_ID = "id";
    public static final String CAT_CATEGORY = "category";

    Context context;

    public DBCategory(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + CAT_TABLE_NAME + " ("
                + CAT_ID + " integer primary key autoincrement, "
                + CAT_CATEGORY + " text"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CAT_TABLE_NAME + "");
        onCreate(db);
    }

    public boolean insert(String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(CAT_CATEGORY, category);

        db.insert(CAT_TABLE_NAME, null, cV);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CAT_TABLE_NAME + " WHERE " + CAT_ID + "=" + id + "", null);
        return cursor;
    }

    public int number() {
        SQLiteDatabase db = this.getReadableDatabase();
        int row = (int) DatabaseUtils.queryNumEntries(db, CAT_TABLE_NAME);
        return row;
    }

    public boolean update(Integer id, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(CAT_ID, id);
        cV.put(CAT_CATEGORY, category);

        db.update(CAT_TABLE_NAME, cV, CAT_ID + " = ? ", new String[] {Integer.toString(id)});
        return true;
    }

    public ArrayList<String> Categories() {
        ArrayList<String> arrayList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + CAT_TABLE_NAME + "", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(CAT_CATEGORY)));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public void createEssentialCategories() {
        String[] Category = context.getResources().getStringArray(R.array.categories);
        new AddToDatabaseTask().execute(Category);
    }

    class AddToDatabaseTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {
            for (String category : params) {
                insert(category);
            }
            return null;
        }
    }
}
