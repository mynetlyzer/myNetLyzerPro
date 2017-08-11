package com.keepics.mynetlyzer;

/**
 * Created by jeffery.liu on 2017-03-11.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.preference.PreferenceManager;

public class SplashActivity extends Activity{
    private static int TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i;
                i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, TIME_OUT);
    }
}
