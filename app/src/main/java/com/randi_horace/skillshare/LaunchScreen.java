package com.randi_horace.skillshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class LaunchScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE); //no toolbar & task bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen

        setContentView(R.layout.activity_launch_screen);    //fullscreen b4 set content

        Launcher launcher = new Launcher ();
        launcher.start();

    }

    private class Launcher extends Thread{

        public void run(){

            try{
                sleep(3000);    //display for 3s

            } catch (InterruptedException e){ //avoid thread interrupt error
                e.printStackTrace();
            }

            Intent intent = new Intent(LaunchScreen.this, MainActivity.class);  //intent
            startActivity(intent);
            LaunchScreen.this.finish();     //destroy launch screen after displayed
        }


    }
}
