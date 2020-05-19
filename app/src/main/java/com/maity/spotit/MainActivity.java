package com.maity.spotit;

import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT =2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent goToHomeIntent=new Intent(MainActivity.this, home.class);
                startActivity(goToHomeIntent);
                finish();

            }
        },SPLASH_TIME_OUT);

    }
}
