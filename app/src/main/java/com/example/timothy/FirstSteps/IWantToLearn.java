package com.example.timothy.FirstSteps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;



public class IWantToLearn extends Activity {
    private int difficulty = 2;
    private String directory = "alphabet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iwant_to_learn);

        WriteReadVariablesToJson writeRead = new WriteReadVariablesToJson();
        writeRead.readJsonStream(this);
        String dif = writeRead.get("DIFFICULTY");
        String dir = writeRead.get("DIRECTORY");
        if (dif != null) {
            difficulty = Integer.parseInt(dif);
        }
        if (dir != null) {
            directory = dir;
        }
        Log.i("difficulty", String.valueOf(difficulty));
        if (difficulty < 4) {
            ((RadioButton) findViewById(R.id.radioButtonEasy)).setChecked(true);
            ((RadioButton) findViewById(R.id.radioButtonMedium)).setChecked(false);
            ((RadioButton) findViewById(R.id.radioButtonHard)).setChecked(false);

        } else if (difficulty < 7) {
            ((RadioButton) findViewById(R.id.radioButtonMedium)).setChecked(true);
            ((RadioButton) findViewById(R.id.radioButtonEasy)).setChecked(false);
            ((RadioButton) findViewById(R.id.radioButtonHard)).setChecked(false);

        } else {
            ((RadioButton) findViewById(R.id.radioButtonHard)).setChecked(true);
            ((RadioButton) findViewById(R.id.radioButtonEasy)).setChecked(false);
            ((RadioButton) findViewById(R.id.radioButtonMedium)).setChecked(false);

        }
        ((CheckBox) findViewById(R.id.abc)).setChecked(false);
        ((CheckBox) findViewById(R.id.ABC)).setChecked(false);
        ((CheckBox) findViewById(R.id.bWords)).setChecked(false);
        ((CheckBox) findViewById(R.id.cWords)).setChecked(false);

        switch (directory) {
            case "ALPHABET":
                ((CheckBox) findViewById(R.id.ABC)).setChecked(true);
                break;
            case "alphabet":
                ((CheckBox) findViewById(R.id.abc)).setChecked(true);
                break;
            case "3letterB":
                ((CheckBox) findViewById(R.id.bWords)).setChecked(true);
                break;
            case "3letterC":
                ((CheckBox) findViewById(R.id.cWords)).setChecked(true);
                break;
        }
    }

    public void launchDynoQuiz(View view) {
        Intent dynQuizIntent = new Intent(this, com.example.timothy.alphabetquiz.MainActivity.class);
        dynQuizIntent.putExtra("DIFFICULTY", difficulty);
        dynQuizIntent.putExtra("DIRECTORY", directory);
        startActivity(dynQuizIntent);
    }

    public void launchAlphabetGame(View view) {
        Intent alphaGameIntent = new Intent(this, com.example.timothy.shootthemdown.MainActivity.class);
        alphaGameIntent.putExtra("DIFFICULTY", difficulty);
        alphaGameIntent.putExtra("DIRECTORY", directory);

        startActivity(alphaGameIntent);
    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.radioButtonEasy:
                difficulty = 2;
                break;
            case R.id.radioButtonMedium:
                difficulty = 5;
                break;
            case R.id.radioButtonHard:
                difficulty = 8;
                break;
        }
    }

    public void onCheckBoxClick(View view) {
        ((CheckBox) findViewById(R.id.abc)).setChecked(false);
        ((CheckBox) findViewById(R.id.ABC)).setChecked(false);
        ((CheckBox) findViewById(R.id.bWords)).setChecked(false);
        ((CheckBox) findViewById(R.id.cWords)).setChecked(false);

        switch (view.getId()) {
            case R.id.ABC:
                ((CheckBox) findViewById(R.id.ABC)).setChecked(true);
                directory = "ALPHABET";
                break;
            case R.id.abc:
                ((CheckBox) findViewById(R.id.abc)).setChecked(true);
                directory = "alphabet";
                break;
            case R.id.bWords:
                ((CheckBox) findViewById(R.id.bWords)).setChecked(true);
                directory = "3letterB";
                break;
            case R.id.cWords:
                ((CheckBox) findViewById(R.id.cWords)).setChecked(true);
                directory = "3letterC";
                break;
        }

    }

    protected void onStop() {
        super.onStop();
        WriteReadVariablesToJson writeRead = new WriteReadVariablesToJson();
        writeRead.addKeyValue("DIFFICULTY", difficulty);
        writeRead.addKeyValue("DIRECTORY", directory);
        writeRead.writeJsonStream(this);
    }
}
