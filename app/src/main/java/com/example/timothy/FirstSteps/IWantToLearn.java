package com.example.timothy.FirstSteps;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.RadioButton;

import com.example.timothy.FirstSteps.R;



public class IWantToLearn extends Activity {
    private int difficulty=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iwant_to_learn);
    }

    public void launchDynoQuiz(View view) {
        Intent dynQuizIntent = new Intent(this,  com.example.timothy.alphabetquiz.MainActivity.class);
        dynQuizIntent.putExtra("DIFFICULTY",difficulty);
        startActivity(dynQuizIntent);
    }
    public void launchAlphabetGame(View view){
        Intent alphaGameIntent = new Intent(this, com.example.timothy.shootthemdown.MainActivity.class);
        alphaGameIntent.putExtra("DIFFICULTY",difficulty);
        startActivity(alphaGameIntent);
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();
        switch(view.getId()){
            case R.id.radioButtonEasy:
                difficulty =2;
                break;
            case R.id.radioButtonMedium:
                difficulty=5;
                break;
            case R.id.radioButtonHard:
                difficulty=8;
                break;
        }
    }
}
