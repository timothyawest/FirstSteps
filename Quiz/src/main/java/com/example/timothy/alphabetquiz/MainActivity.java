package com.example.timothy.alphabetquiz;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.flowlayout.FlowLayout;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.UUID;
import java.util.Vector;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    MediaPlayer mp;
    com.example.timothy.alphabetquiz.ShoeBox shoeBox = new com.example.timothy.alphabetquiz.ShoeBox();

  
    protected void playSound(final Uri file) {
        if (mp == null) {
            mp = MediaPlayer.create(this, file);
            mp.start();

        } else if (!mp.isPlaying()) {
            try {
                mp.reset();
                mp.setDataSource(this, file);
                mp.prepare();
                mp.start();

            } catch (Exception e) {
            }
        } else {
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                Uri thisfile = file;

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (thisfile != null) {
                        mp.reset();
                        try {
                            mp.setDataSource(getApplicationContext(), file);
                            mp.prepare();
                        } catch (Exception e) {

                        }
                        thisfile = null;
                        mp.start();
                        /*FlowLayout buttonLayout = findViewById(R.id.Button_layout);
                        for(int i=0;i<buttonLayout.getChildCount();i++){
                            buttonLayout.getChildAt(i).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.egg));
                        }*/



                    }
                }
            });
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("shoeBox", shoeBox);
     }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("shoeBox"))
                shoeBox= (ShoeBox)savedInstanceState.get("shoeBox");

        }

       if (savedInstanceState == null) {
           Log.i("INstance", "Instance  null");
           for (int i = 0; i < 26; i++) {
               String let = Character.toString((char) (97 + i));
               String filename = "android.resource://" + this.getPackageName() + "/raw/" + let;
               Uri myUri = Uri.parse(filename);
               Item item = new Item();
               item.setAudioFile(myUri);
               item.setText(let);
               shoeBox.addCard(item);
           }
       }
        shoeBox.numInActiveList =5;
        int difficulty=getIntent().getIntExtra("DIFFICULTY",0);
        shoeBox.numInActiveList =difficulty;

        FlowLayout buttonLayout = findViewById(R.id.Button_layout);

           for(int i=0;i<shoeBox.numInActiveList;i++){
                Button button = new Button(this);
                button.setId(View.generateViewId());
                button.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.egg));
                button.setTextSize(30f);
                button.setTextColor(getResources().getColor(R.color.colorAccent));
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                       final Button b = (Button) v;
                        Log.i("IDS",String.valueOf(b.getId()) +" "+ String.valueOf(shoeBox.getCurrentItem().getId()) );
                        if (b.getId() == shoeBox.getCurrentItem().getId()) {
                            shoeBox.getCurrentItem().setCorrect(shoeBox.getCurrentItem().getCorrect() + 1);
                            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/nicework");
                            b.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.egghatch));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    b.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.egg));

                                }
                            }, 600);
                            playSound(uri);

                            loadButtons();
                        } else {
                            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/failbuzzer");
                            b.startAnimation(AnimationUtils.loadAnimation(b.getContext(),R.anim.rotate));
                            playSound(uri);
                            shoeBox.getCurrentItem().setCorrect(0);
                        }

                    }

                });
                buttonLayout.addView(button);
            }


        loadButtons();
    }


    public void loadButtons() {
        final Random rnd = new Random(System.currentTimeMillis());
        int j = 0;
        FlowLayout buttonLayout = findViewById(R.id.Button_layout);
        shoeBox.getActiveList();
        shoeBox.getRandActiveList();

        for (int i = 0; i < shoeBox.getActiveList().size(); i++) {
            Log.i("items",String.valueOf(i));
            Button button = (Button)buttonLayout.getChildAt(i);
            shoeBox.getActiveList().get(i).setId(button.getId());
            button.setText(shoeBox.getActiveList().get(i).getText());
           // button.setAnimation(AnimationUtils.loadAnimation(this,R.anim.move));
        }
        shoeBox.setCurrentItem( shoeBox.getChoice());
        if (shoeBox.getCurrentItem() == null) {
            //  if(null ==null){
            Intent newIntent = new Intent(this, YouWon.class);
            startActivity(newIntent);
            System.exit(0);

        }

        // shoeBox.setCurrentItem( shoeBox.getChoice();
        playSound(shoeBox.getCurrentItem().getAudioFile());
        /*Random rnd = new Random(System.currentTimeMillis());
        int btoChoose =  rnd.nextInt(buttonlist.size());
        for(int i=0;i<buttonlist.size();i++){
            TextView button = buttonlist.get(i);
            int rnum=0;
            String let="a";
            boolean haveit;
            boolean good=false;
            for(Integer val:timesRight){
                if(val.intValue() <2) {
                    break;
                }
            }
            if(!good){
                Toast.makeText(this, "Congratulations!!", Toast.LENGTH_SHORT).show();
            }
            do {
                haveit=false;
                do {
                    rnum = rnd.nextInt(26);
                }
                while(timesRight.get(rnum).intValue() >=2);
                let = Character.toString((char)(97+rnum));
                for(int j=0;j<i;j++){
                    TextView check = buttonlist.get(j);
                    String blet=check.getText().toString();
                    if(blet.compareTo(let)==0) {
                        haveit = true;
                        break;
                    }

                }
            }while(haveit);
            button.setText(let);
            if(btoChoose == i) {
                letter = let;
            }
            button.setText(let);
        }*/
        //playSound(letter);
    }

    public void playSound(View view) {
        playSound(shoeBox.getCurrentItem().getAudioFile());
    }
}
