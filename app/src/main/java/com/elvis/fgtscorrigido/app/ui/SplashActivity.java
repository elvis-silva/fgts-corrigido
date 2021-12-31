package com.elvis.fgtscorrigido.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.elvis.fgtscorrigido.app.FGTSCorrigido;
import com.elvis.fgtscorrigido.app.MainActivity;
import com.elvis.fgtscorrigido.app.R;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread logoTimer = new Thread() {
            public void run(){
                try {
                    int logoTimer = 0;
                    while(logoTimer < 3000){
                        sleep(100);
                        logoTimer = logoTimer + 100;
                    }
                    startActivity(new Intent("com.fgtscorrigido.app.CLEARSCREEN"));
                } catch (InterruptedException e) {
                    //TODO Auto-generated catch block
                    e.printStackTrace();
                }

                finally {
                    finish();
                }
            }
        };
        logoTimer.start();
    }
}