package me.ozimek.timescheduler;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Created by wojtek on 2017-09-02.
 */

public class CategoryActivity extends AppCompatActivity {

    DBHelper db;
    //DBCategory dbCat;
    //DBSubcategory dbSubCat;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    //ListView expListView;
    //ArrayAdapter<String> expListAdapter;
    List<String> categories = new ArrayList<String>();
    HashMap<String, List<String>> subcategories = new HashMap<>();
    String newCategory;
    String newSubCategory;

    FloatingActionButton fabMain, subFab1, subFab2, subFab3;
    Animation FabOpen, FabClose, FabRorateClockwise, FabRotateCounterClockwise;
    boolean isFabOpen = false;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        fabMain = (FloatingActionButton) findViewById(R.id.cat_fab);
        subFab1 = (FloatingActionButton) findViewById(R.id.cat_subfab1);
        subFab2 = (FloatingActionButton) findViewById(R.id.cat_subfab2);
        subFab3 = (FloatingActionButton) findViewById(R.id.cat_subfab3);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRorateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRotateCounterClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_counter_clockwise);

        db = new DBHelper(this);
    //    dbCat = new DBCategory(getBaseContext());
    //    dbSubCat = new DBSubcategory(getApplicationContext());
        expandableListView = (ExpandableListView) findViewById(R.id.cat_expandable_category);
    //    expListView = (ListView) findViewById(R.id.exp_category);
        new GetFromDBTask().execute();

    //    expListAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, categories);
    //    expListView.setAdapter(expListAdapter);
        expandableListAdapter = new ExpandableListAdapter(getBaseContext(), categories, subcategories);
        expandableListView.setAdapter(expandableListAdapter);

        fabMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isFabOpen) {
                    subFab1.startAnimation(FabClose);
                    subFab2.startAnimation(FabClose);
                    subFab3.startAnimation(FabClose);
                    fabMain.startAnimation(FabRotateCounterClockwise);
                    subFab1.setClickable(false);
                    subFab2.setClickable(false);
                    subFab3.setClickable(false);
                    isFabOpen = false;
                } else {
                    subFab1.startAnimation(FabOpen);
                    subFab2.startAnimation(FabOpen);
                    subFab3.startAnimation(FabOpen);
                    fabMain.startAnimation(FabRorateClockwise);
                    subFab1.setClickable(true);
                    subFab2.setClickable(true);
                    subFab3.setClickable(true);
                    isFabOpen = true;
                }
            }
        });
        subFab1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                subFab1.startAnimation(FabClose);
                subFab2.startAnimation(FabClose);
                subFab3.startAnimation(FabClose);
                fabMain.startAnimation(FabRotateCounterClockwise);
                subFab1.setClickable(false);
                subFab2.setClickable(false);
                subFab3.setClickable(false);
                isFabOpen = false;

                LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                View promptView = layoutInflater.inflate(R.layout.cat_category_prompt, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final EditText categoryInput = (EditText) promptView.findViewById(R.id.cat_category_add_prompt);
                builder.setView(promptView);
                builder.setCancelable(false)
                        .setTitle(R.string.cat_prompt_Category)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //EditText editText = (EditText) v.findViewById(R.id.cat_category_add_prompt);
                                newCategory = categoryInput.getText().toString();
                                new AddNewCategoryTask().execute();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        subFab2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                subFab1.startAnimation(FabClose);
                subFab2.startAnimation(FabClose);
                subFab3.startAnimation(FabClose);
                fabMain.startAnimation(FabRotateCounterClockwise);
                subFab1.setClickable(false);
                subFab2.setClickable(false);
                subFab3.setClickable(false);
                isFabOpen = false;

                LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                View promptView = layoutInflater.inflate(R.layout.cat_subcategory_prompt, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                EditText categoryInput = (EditText) findViewById(R.id.cat_subcategory_add_prompt);
                //ListView categoryInput = (ListView) v.findViewById(R.id.cat_category_list_prompt);
                //ArrayAdapter categoryInputAdapter = new ArrayAdapter(v.getContext(), android.R.layout.simple_list_item_1, categories);
                //categoryInput.setAdapter(categoryInputAdapter);
                final EditText subcategoryInput = (EditText) promptView.findViewById(R.id.cat_subsubcategory_add_prompt);
                builder.setView(promptView);
                builder.setCancelable(false)
                        .setTitle(R.string.cat_prompt_SubCategory)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newSubCategory = subcategoryInput.getText().toString();
                                new AddNewSubcategoryTask().execute();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        subFab3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                subFab1.startAnimation(FabClose);
                subFab2.startAnimation(FabClose);
                subFab3.startAnimation(FabClose);
                fabMain.startAnimation(FabRotateCounterClockwise);
                subFab1.setClickable(false);
                subFab2.setClickable(false);
                subFab3.setClickable(false);
                isFabOpen = false;
                deleteAll();
            }
        });
    }
    void deleteAll() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + DBHelper.CAT_TABLE_NAME);
        db.execSQL("delete from " + DBHelper.SUBCAT_TABLE_NAME);
    }
    class GetFromDBTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            categories.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {

            DBHelper dbHelper = new DBHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(" SELECT * FROM " + DBHelper.CAT_TABLE_NAME + "", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                categories.add(cursor.getString(cursor.getColumnIndex(DBHelper.CAT_CATEGORY)));
                cursor.moveToNext();
            }
            cursor.close();


            List<String> sub = new ArrayList<String>();
            Cursor cursorSub = db.rawQuery(" SELECT * FROM " + DBHelper.SUBCAT_TABLE_NAME + "", null);
            cursorSub.moveToFirst();


            String category = cursorSub.getString(cursorSub.getColumnIndexOrThrow(DBHelper.SUBCAT_CATEGORY));
            while (!cursorSub.isAfterLast()) {
                String tempC = cursorSub.getString(cursorSub.getColumnIndexOrThrow(DBHelper.SUBCAT_CATEGORY));
                String tempSC = cursorSub.getString(cursorSub.getColumnIndexOrThrow(DBHelper.SUBCAT_SUBCATEGORY));

                if(category.equals(tempC)) {
                    sub.add(tempSC);
                } else {
                    subcategories.put(category, sub);
                    category = tempC;
                    sub = new ArrayList<String>();
                    sub.add(tempSC);
                }
                cursorSub.moveToNext();
                //    categories.add(cursorSub.getString(cursorSub.getColumnIndex(DBHelper.SUBCAT_CATEGORY)));
                //    cursorSub.moveToNext();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
    class AddNewCategoryTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {
            DBHelper dbHelper = new DBHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cV = new ContentValues();
            cV.put(DBHelper.CAT_CATEGORY, newCategory);

            db.insert(DBHelper.CAT_TABLE_NAME, null, cV);
            return null;
        }
    }
    class AddNewSubcategoryTask extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {
            DBHelper dbHelper = new DBHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cV = new ContentValues();
            cV.put(DBHelper.SUBCAT_CATEGORY, newCategory);
            cV.put(DBHelper.SUBCAT_SUBCATEGORY, newSubCategory);

            db.insert(DBHelper.SUBCAT_TABLE_NAME, null, cV);
            return null;
        }
    }
}