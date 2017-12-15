package me.ozimek.timescheduler;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import me.ozimek.timescheduler.pager.FragmentHome;


/**
 * Created by wojtek on 2017-08-20.
 */

public class AddActivity extends AppCompatActivity {

    TextView hourTextView;
    TextView buttonNOW;
    EditText subcategory;
    EditText description;
    //AutoCompleteTextView description;
    Button add;

    String categoryBundle = "";
    String subCategoryBundle = "";
    String descriptionBundle = "";
    String timeBundle = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
    String dateBundle = MainActivity.dateTextView.getText().toString(); //new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

    DBHelper db;
    Calendar calendar;
    List<String> categories;
    HashMap<String, List<String>> subcategories;
    String [] descriptionArray = new String[0];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        db = new DBHelper(this);

        hourTextView = (TextView) findViewById(R.id.add_hourChooser);
        calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        hourTextView.setText(convertTime(hour) + ":" + convertTime(minute));

        buttonNOW = (TextView) findViewById(R.id.add_nowButton);
        buttonNOW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                hourTextView.setText(convertTime(hour) + ":" + convertTime(minute));
            }
        });
        hourTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker1, int selectedHour, int selectedMinute) {
                        String tTime = convertTime(selectedHour) + ":" + convertTime(selectedMinute);
                        hourTextView.setText(tTime);
                        timeBundle = tTime;
                    }
                }, hour, minute, true);
                timePicker.setTitle("Select time");
                timePicker.show();
            }
        });

        subcategory = (EditText) findViewById(R.id.add_editCategory);
        subcategory.setKeyListener(null);
        subcategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            //    final List<String> listHeader = new ArrayList<String>();
            //    final HashMap<String, List<String>> listChild = new HashMap<String, List<String>>();

                categories = new ArrayList<String>();
                subcategories = new HashMap<>();

            /*    String[] headers = getResources().getStringArray(R.array.categories);
                for(int i = 0; i < headers.length; i++) {
                    listHeader.add(headers[i]);
                }

                List<String> Meal = new ArrayList<String>();
                String[] subCategories = getResources().getStringArray(R.array.Meal);
                for(int i = 0; i < subCategories.length; i++) {
                    Meal.add(subCategories[i]);
                }
                List<String> Leisure = new ArrayList<String>();
                subCategories = getResources().getStringArray(R.array.Leisure);
                for(int i = 0; i < subCategories.length; i++) {
                    Leisure.add(subCategories[i]);
                }
                List<String> Sleep = new ArrayList<String>();
                subCategories = getResources().getStringArray(R.array.Sleep);
                for(int i = 0; i < subCategories.length; i++) {
                    Sleep.add(subCategories[i]);
                }
                List<String> Work = new ArrayList<String>();
                subCategories = getResources().getStringArray(R.array.Work);
                for(int i = 0; i < subCategories.length; i++) {
                   Work.add(subCategories[i]);
                }
                List<String> Learn = new ArrayList<String>();
                subCategories = getResources().getStringArray(R.array.Learn);
                for(int i = 0; i < subCategories.length; i++) {
                    Learn.add(subCategories[i]);
                }
                List<String> Sport = new ArrayList<String>();
                subCategories = getResources().getStringArray(R.array.Sport);
                for(int i = 0; i < subCategories.length; i++) {
                    Sport.add(subCategories[i]);
                }
                List<String> Social = new ArrayList<String>();
                subCategories = getResources().getStringArray(R.array.Social);
                for(int i = 0; i < subCategories.length; i++) {
                    Social.add(subCategories[i]);
                }
                List<String> Duty = new ArrayList<String>();
                subCategories = getResources().getStringArray(R.array.Duty);
                for(int i = 0; i < subCategories.length; i++) {
                    Duty.add(subCategories[i]);
                }

                listChild.put(listHeader.get(0), Meal);
                listChild.put(listHeader.get(1), Leisure);
                listChild.put(listHeader.get(2), Sleep);
                listChild.put(listHeader.get(3), Work);
                listChild.put(listHeader.get(4), Learn);
                listChild.put(listHeader.get(5), Sport);
                listChild.put(listHeader.get(6), Social);
                listChild.put(listHeader.get(7), Duty); */

                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final ExpandableListView listView = new ExpandableListView(v.getContext());

                new GetFromDBTask().execute();

                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        String subCategoryString = subcategories.get(categories.get(groupPosition)).get(childPosition).toString();
                        String categoryString = categories.get(groupPosition);
                        subcategory.setText(subCategoryString);

                        categoryBundle = categoryString;
                        subCategoryBundle = subCategoryString;
                        return false;
                    }
                });
                final ExpandableListAdapter adapter = new ExpandableListAdapter(v.getContext(), categories, subcategories);
                listView.setAdapter(adapter);
                builder.setView(listView);
                builder.setTitle(R.string.add_prompt_CategoryName)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        description = (EditText) findViewById(R.id.add_editDescription);
        description.setKeyListener(null);
    /*    description = (AutoCompleteTextView) findViewById(R.id.add_editDescription);
        new DescriptionTask().execute();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.select_dialog_item, descriptionArray);
        description.setThreshold(1);
        description.setAdapter(adapter);
    */

        description.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                View promptView = layoutInflater.inflate(R.layout.add_description_prompt, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                final EditText input = (EditText) promptView.findViewById(R.id.add_prompt_description);
            //    final AutoCompleteTextView input = (AutoCompleteTextView) promptView.findViewById(R.id.add_prompt_description);
                input.setText(description.getText());
            //    new DescriptionTask().execute();
            //    ArrayAdapter<String> adapter = new ArrayAdapter<String>(promptView.getContext(), android.R.layout.select_dialog_item, descriptionArray);
            //    input.setAdapter(adapter);
                builder.setView(promptView);
                builder.setTitle(R.string.add_prompt_TypeDescription)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                description.setText(input.getText());
                                descriptionBundle = input.getText().toString();
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
        add = (Button) findViewById(R.id.add_addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subCategoryBundle == "") {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Empty category")
                            .setMessage("Choose category before adding")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).show();

                } else {
                    new AddToDatabaseTask().execute();
                    finish();
                }
            }
        });
    }

    private String convertTime(int time) {
        if(time >= 10) {
            return String.valueOf(time);
        } else {
            return "0" + String.valueOf(time);
        }
    }

    class AddToDatabaseTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            descriptionBundle = description.getText().toString();
        }
        @Override
        protected Void doInBackground(Void... params) {
            DBHelper dbHelper = new DBHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues cV = new ContentValues();
            cV.put(DBHelper.ACTIV_DATE, dateBundle);
            cV.put(DBHelper.ACTIV_TIME, timeBundle);
            cV.put(DBHelper.ACTIV_CATEGORY, categoryBundle);
            cV.put(DBHelper.ACTIV_SUBCATEGORY, subCategoryBundle);
            cV.put(DBHelper.ACTIV_DESCRIPTION, descriptionBundle);

            db.insert(DBHelper.ACTIV_TABLE_NAME, null, cV);
            DBHelper.autoIncrementer++;

           // dbHelper = new DBHelper(getBaseContext());
           // db = dbHelper.getWritableDatabase();
            cV = new ContentValues();
            cV.put(DBHelper.DESC_DESCRIPTION, descriptionBundle);

            db.insert(DBHelper.DESC_TABLE_NAME, null, cV);

            return null;
        }
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
                categories.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CAT_CATEGORY)));
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
            cursorSub.close();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
/*    class DescriptionTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            DBHelper dbHelper = new DBHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(" SELECT * FROM " + DBHelper.DESC_TABLE_NAME + "", null);
            cursor.moveToFirst();
            List<String> desc = new ArrayList<String>();
            while(!cursor.isAfterLast()) {
                desc.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DESC_DESCRIPTION)));
                cursor.moveToNext();
            }
            cursor.close();
            descriptionArray = new String[desc.size()];
            for(int i = 0; i < desc.size(); i++)
                descriptionArray[i] = desc.get(i);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    } */
}