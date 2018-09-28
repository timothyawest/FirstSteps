package com.example.timothy.shootthemdown;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private GameSurface gameSurface= null;
    private long timeOnExit=0;
    private int difficulty;
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.i("Shoebox", "save instance");

    }
    @Override
    protected void onResume(){
        super.onResume();
        //make the last missile to come update to current time like the gap never happened

    }
    @Override
    protected void onPause(){
        super.onPause();
        gameSurface.pauseGameThread(true);
        this.timeOnExit =System.nanoTime();

    }
    @Override
    protected void onStart(){
        super.onStart();
       // if(gameSurface !=null) {
            int now = (int)(System.nanoTime()/1000000);
            gameSurface.lastMissile = gameSurface.lastMissile-(int)(timeOnExit/1000000)+now ;
            gameSurface.pauseGameThread(false);

        //}

        Log.i("Shoebox","start");

    }
    @Override
    protected void onStop(){
        Log.i("Shoebox","stop");
        super.onStop();

    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i("Shoebox","restart");

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("Shoebox","restored");

      /*  if (savedInstanceState != null) {
            Log.i("Shoebox","savedInstanceState");
            if (savedInstanceState.containsKey("shoeBox"))
                shoeBox= (ShoeBox)savedInstanceState.get("shoeBox");

        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        this.gameSurface = new GameSurface(this,shoeBox);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(gameSurface);*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Shoebox","create");
        difficulty=getIntent().getIntExtra("DIFFICULTY",0);

            // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // Set No Title
       this.gameSurface = new GameSurface(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(this.gameSurface);
    }

    public int getDifficulty(){
        return this.difficulty;
    }

}

