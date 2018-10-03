package com.example.timothy.alphabetquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class YouWon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_won);
    }

    public void exitApp(View view) {
        finish();
    }

}
