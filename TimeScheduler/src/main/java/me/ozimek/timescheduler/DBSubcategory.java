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

public class DBSubcategory extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ActivitiesDB";
    public static final String SUBCAT_TABLE_NAME = "subcategories";
    public static final String SUBCAT_ID = "id";
    public static final String SUBCAT_CATEGORY = "category";
    public static final String SUBCAT_SUBCATEGORY = "subcategory";
    Context context;
    public DBSubcategory(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + SUBCAT_TABLE_NAME + " ("
                + SUBCAT_ID + " integer primary key autoincrement, "
                + SUBCAT_CATEGORY + " text, "
                + SUBCAT_SUBCATEGORY + "text"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SUBCAT_TABLE_NAME + "");
        onCreate(db);
    }

    public boolean insert(String category, String subcategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
//        cV.put(CAT_ID, autoIncrementer);
        cV.put(SUBCAT_CATEGORY, category);
        cV.put(SUBCAT_SUBCATEGORY, subcategory);

        db.insert(SUBCAT_TABLE_NAME, null, cV);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + SUBCAT_TABLE_NAME + " WHERE " + SUBCAT_ID + "=" + id + "", null);
        return cursor;
    }

    public int number() {
        SQLiteDatabase db = this.getReadableDatabase();
        int row = (int) DatabaseUtils.queryNumEntries(db, SUBCAT_TABLE_NAME);
        return row;
    }

    public boolean update(Integer id, String category, String subcategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(SUBCAT_ID, id);
        cV.put(SUBCAT_CATEGORY, category);
        cV.put(SUBCAT_SUBCATEGORY, subcategory);

        db.update(SUBCAT_TABLE_NAME, cV, SUBCAT_ID + " = ? ", new String[] {Integer.toString(id)});
        return true;
    }

    public String getCategory(String subcategory) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + SUBCAT_TABLE_NAME + "", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(subcategory.compareTo(cursor.getString(cursor.getColumnIndex(SUBCAT_SUBCATEGORY)))==0) {
                return cursor.getString(cursor.getColumnIndex(SUBCAT_CATEGORY));
            }
        }
        return "";
    }

    public ArrayList<String> Subcategories() {
        ArrayList<String> arrayList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + SUBCAT_TABLE_NAME + "", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(SUBCAT_SUBCATEGORY)));
            cursor.moveToNext();
        }
        return arrayList;
    }
    public void createEssentialSubcategories() {
        String[] Category = context.getResources().getStringArray(R.array.categories);
        String[] Meal = context.getResources().getStringArray(R.array.Meal);
        String[] Leisure = context.getResources().getStringArray(R.array.Leisure);
        String[] Sleep = context.getResources().getStringArray(R.array.Sleep);
        String[] Work = context.getResources().getStringArray(R.array.Work);
        String[] Learn = context.getResources().getStringArray(R.array.Learn);
        String[] Sport = context.getResources().getStringArray(R.array.Sport);
        String[] Social = context.getResources().getStringArray(R.array.Social);
        String[] Duty = context.getResources().getStringArray(R.array.Duty);


        new AddToDatabaseTask().execute(addFirst(Meal,Category[0]));
        new AddToDatabaseTask().execute(addFirst(Leisure,Category[0]));
        new AddToDatabaseTask().execute(addFirst(Sleep,Category[0]));
        new AddToDatabaseTask().execute(addFirst(Work,Category[0]));
        new AddToDatabaseTask().execute(addFirst(Learn,Category[0]));
        new AddToDatabaseTask().execute(addFirst(Sport,Category[0]));
        new AddToDatabaseTask().execute(addFirst(Social,Category[0]));
        new AddToDatabaseTask().execute(addFirst(Duty,Category[0]));
    }
    public String[] addFirst(String [] arr, String s) {
        String[] temp = new String[arr.length + 1];
        temp[0] = s;
        System.arraycopy(arr, 0, temp, 1, arr.length);
        return temp;
    }

    class AddToDatabaseTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String category = params[0];
            for(int i = 1; i < params.length; i++)
                insert(category, params[i]);
            return null;
        }

    }
}
