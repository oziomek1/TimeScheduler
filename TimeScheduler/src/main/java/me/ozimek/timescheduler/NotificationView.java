package me.ozimek.timescheduler;

import android.app.Activity;
import android.os.Bundle;
/**
 * Created by wojtek on 2017-09-05.
 */

public class NotificationView extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
    }
}
