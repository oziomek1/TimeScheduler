package me.ozimek.timescheduler.pager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import me.ozimek.timescheduler.DBHelper;
import me.ozimek.timescheduler.R;

/**
 * Created by wojtek on 2017-08-17.
 */

public class FragmentHome extends Fragment {

    int i = 0;
    DBHelper db;
    private ListView listView;
    protected ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        db = new DBHelper(getContext());

        listView = (ListView) v.findViewById(R.id.listView1);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFragment();
    }
    public void updateFragment() {
        new TextLoaderTask().execute();
    }

    void deleteAll() {
        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + DBHelper.ACTIV_TABLE_NAME);
    }

    class TextLoaderTask extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            DBHelper dbHelper = new DBHelper(getContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(" SELECT * FROM " + DBHelper.ACTIV_TABLE_NAME + "", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                arrayList.add(createDBString(cursor));

                cursor.moveToNext();
            }
            return null;
        }

        protected String createDBString(Cursor cursor) {
            String d = cursor.getString(cursor.getColumnIndex(DBHelper.ACTIV_DATE));
            String t = cursor.getString(cursor.getColumnIndex(DBHelper.ACTIV_TIME));
            String c = cursor.getString(cursor.getColumnIndex(DBHelper.ACTIV_CATEGORY));
            String s = cursor.getString(cursor.getColumnIndex(DBHelper.ACTIV_SUBCATEGORY));
            String dS = cursor.getString(cursor.getColumnIndex(DBHelper.ACTIV_DESCRIPTION));

            return d + "  |  " + t + "  |  " + c + "  |  " + s + "  |  " + dS;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);
        }
    }
}
