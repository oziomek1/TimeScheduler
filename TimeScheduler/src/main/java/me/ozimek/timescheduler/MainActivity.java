/**
 * Created by wojtek on 2017-08-06.
 */

package me.ozimek.timescheduler;


import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.widget.DatePicker;
import android.content.Context;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.ozimek.timescheduler.tabs.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private SlidingTabLayout mTabs;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private NavigationView navigationView;
    private FloatingActionButton fab;

    private TextView todayButton;
    public static TextView dateTextView;

    private static int navItemIndex = 0;

    private static int NUM_ITEMS = 3;
    String[] tabText = {"HOME", "CATEGORIES", "STATS"};
    private Context context;
    int[] icons = {R.drawable.home, R.drawable.category, R.drawable.stats};
    int numMessage = 0;

    private ActionBarDrawerToggle drawerToggle;

//    private DBCategory dbCategory;
//    private DBSubcategory dbSubcategory;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = new DBHelper(this);
    //    dbCategory = new DBCategory(this);
    //    dbCategory.createEssentialCategories();
        createEssentialCategories();

        //dbSubcategory = new DBSubcategory(this);
        //dbSubcategory.createEssentialSubcategories();
        createEssentialSubcategories();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_View);
        setupDrawerContent(navigationView);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        dateTextView = (TextView) findViewById(R.id.dateView);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        dateTextView.setText(date);

        todayButton = (TextView) findViewById(R.id.todayButton);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String date = df.format(Calendar.getInstance().getTime());
                dateTextView.setText(date);
            }
        });
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateTextView.setText(convertTime(dayOfMonth) + "/" + convertTime(month) + "/" + year);
                    }
                }, year, month, day);
                datePicker.setTitle("Select date");
                datePicker.show();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), tabText, context));

        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
    //    mTabs.setCustomTabView(R.layout.sliding_tab, R.id.tabText);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        mTabs.setViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                Intent intent = null;
                switch(position) {
                    case 1:
                        intent = new Intent(MainActivity.this, CategoryActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, StatsActivity.class);
                        break;
                    default:
                        intent = new Intent(MainActivity.this, AddActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        addNotificationBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //    drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.syncState();
    }

    public void setupDrawerContent(NavigationView nV) {
        nV.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    void deleteAll() {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + DBHelper.ACTIV_TABLE_NAME);
        db.execSQL("delete from " + DBHelper.DESC_TABLE_NAME);
    }

    private String convertTime(int date) {
        if(date >= 10) {
            return String.valueOf(date);
        } else {
            return "0" + String.valueOf(date);
        }
    }

    private void selectDrawerItem(MenuItem item) {
    //    Fragment fragment = null;
    //    Class fragmentClass;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navDrawer_home:
                navItemIndex = 0;
                break;
            case R.id.navDrawer_categories:
                navItemIndex = 1;
                intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.navDrawer_history:
                navItemIndex = 3;
                break;
            case R.id.navDrawer_stats:
                navItemIndex = 4;
                break;
            case R.id.navDrawer_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                navItemIndex = 5;
                break;
            case R.id.navDrawer_export_import:
                navItemIndex = 6;
                break;
            case R.id.navDrawer_markUs:
                navItemIndex = 8;
                break;
            case R.id.navDrawer_help:
                navItemIndex = 10;
                break;
            case R.id.navDrawer_about:
                navItemIndex = 11;
                break;
            case R.id.navDrawer_trash:
                navItemIndex = 12;
                new AlertDialog.Builder(this)
                        .setTitle("Deleting activities")
                        .setMessage("Are you sure to delete all activities?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAll();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            default:
                navItemIndex = 0;
        }
        drawerLayout.closeDrawers();
    }

    private void addNotificationBar() {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        android.support.v4.app.NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle("Time Scheduler")
                .setContentText("Add your activities!")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(ContextCompat.getColor(this, R.color.colorFab))
                .addAction(R.drawable.notif_first, "First", contentIntent)
                .addAction(R.drawable.notif_second, "Second", contentIntent)
                .addAction(R.drawable.notif_last, "Last", contentIntent);
               // .setNumber(++numMessage);

   //     NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        nBuilder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, nBuilder.build());
    }

    void createEssentialCategories() {
        String[] Category = getResources().getStringArray(R.array.categories);
        new AddToCatTask().execute(Category);
    }

    class AddToCatTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {
            DBHelper dbHelper = new DBHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("delete from " + DBHelper.CAT_TABLE_NAME);
            ContentValues cV;
            for(String p : params) {
                cV = new ContentValues();
                cV.put(DBHelper.CAT_CATEGORY, p);

                db.insert(DBHelper.CAT_TABLE_NAME, null, cV);
            }
            return null;
        }
    }

    public void createEssentialSubcategories() {
        String[] Category = getResources().getStringArray(R.array.categories);
        String[] Meal = getResources().getStringArray(R.array.Meal);
        String[] Leisure = getResources().getStringArray(R.array.Leisure);
        String[] Sleep = getResources().getStringArray(R.array.Sleep);
        String[] Work = getResources().getStringArray(R.array.Work);
        String[] Learn = getResources().getStringArray(R.array.Learn);
        String[] Sport = getResources().getStringArray(R.array.Sport);
        String[] Social = getResources().getStringArray(R.array.Social);
        String[] Duty = getResources().getStringArray(R.array.Duty);


        new AddToSubCatTask().execute(addFirst(Meal,Category[0]));
        new AddToSubCatTask().execute(addFirst(Leisure,Category[1]));
        new AddToSubCatTask().execute(addFirst(Sleep,Category[2]));
        new AddToSubCatTask().execute(addFirst(Work,Category[3]));
        new AddToSubCatTask().execute(addFirst(Learn,Category[4]));
        new AddToSubCatTask().execute(addFirst(Sport,Category[5]));
        new AddToSubCatTask().execute(addFirst(Social,Category[6]));
        new AddToSubCatTask().execute(addFirst(Duty,Category[7]));
    }

    public String[] addFirst(String [] arr, String s) {
        String[] temp = new String[arr.length + 1];
        temp[0] = s;
        System.arraycopy(arr, 0, temp, 1, arr.length);
        return temp;
    }

    class AddToSubCatTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String category = params[0];
            DBHelper dbHelper = new DBHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues cV;
            for(int i = 1; i < params.length; i++) {
                cV = new ContentValues();
                cV.put(DBHelper.SUBCAT_CATEGORY, category);
                cV.put(DBHelper.SUBCAT_SUBCATEGORY, params[i]);

                db.insert(DBHelper.SUBCAT_TABLE_NAME, null, cV);
            }
            return null;
        }

    }

}
