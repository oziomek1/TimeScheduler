/**
 * Created by wojtek on 2017-08-17.
 */

package me.ozimek.timescheduler;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.*;


public class SettingsActivity extends AppCompatActivity {

    EditText firstCategory;
    EditText secondCategory;
    EditText firstDescription;
    EditText secondDescription;

    Button setFirst;
    Button setSecond;

    Switch setSwitch;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firstCategory = (EditText) findViewById(R.id.set_edit_category);
        secondCategory = (EditText) findViewById(R.id.set_edit_category2);
        firstDescription = (EditText) findViewById(R.id.set_edit_description);
        secondDescription = (EditText) findViewById(R.id.set_edit_description2);
        setFirst = (Button) findViewById(R.id.set_button_button);
        setSecond = (Button) findViewById(R.id.set_button_button2);

        setSwitch = (Switch) findViewById(R.id.set_switch);

        setSwitch.setChecked(true);
    }
}
