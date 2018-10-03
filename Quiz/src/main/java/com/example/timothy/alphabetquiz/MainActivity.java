package com.example.timothy.alphabetquiz;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shoebox.Item;
import com.example.shoebox.ShoeBox;
import com.nex3z.flowlayout.FlowLayout;

import java.util.HashMap;
import java.util.Random;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mp;
    ShoeBox shoeBox = new ShoeBox();
    private SoundPool soundPool;
    private boolean soundPoolLoaded = false;
    private int difficulty = 2;
    private HashMap<String, Integer> soundHash = new HashMap<>();
    private long last_time = 0;
    private String directory = "alphabet";
    private FlowLayout buttonLayout;

    private void initSoundPool() {

        // With Android API >= 21.
        if (Build.VERSION.SDK_INT >= 21) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(100);

            this.soundPool = builder.build();
        }
        // With Android API < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        }

        // When SoundPool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;
                // playSound(shoeBox.getCurrentItem().getAudioFileString(), 1f, 1f);
                // Playing background sound.
                //playSoundBackground();
            }
        });

        AssetManager assetManager = this.getAssets();
        String[] files;
        try {
            files = assetManager.list(directory);
            if (files == null)
                throw new NullPointerException();
            for (String file : files) {
                if (file.contains("mp3")) {
                    String text = file.substring(0, file.indexOf('.'));
                    if (!soundHash.containsKey(text)) {
                        try {
                            int id = this.soundPool.load(assetManager.openFd(directory + "/" + file), 1);
                            soundHash.put(text, id);
                        } catch (Exception e) {
                            Log.e(getClass().getSimpleName(), e.toString());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("exception", ex.toString());
            System.exit(1);

        }


        soundHash.put("failbuzzer", this.soundPool.load(this, R.raw.failbuzzer, 4));
        soundHash.put("nicework", this.soundPool.load(this, R.raw.nicework, 4));


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("shoeBox", shoeBox);
        outState.putString("DIRECTORY", directory);
        outState.putSerializable("shoeBox", shoeBox);
        outState.putInt("DIFFICULTY", difficulty);

        soundPool.release();
        //soundPool =null;
        //soundPoolLoaded=false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        difficulty = getIntent().getIntExtra("DIFFICULTY", 2);
        if (getIntent().getStringExtra("DIRECTORY") != null)
            directory = getIntent().getStringExtra("DIRECTORY");

        if (savedInstanceState != null) {
            Log.i("test", "savedInstanceNotNull");
            if (savedInstanceState.containsKey("shoeBox"))
                shoeBox = (ShoeBox) savedInstanceState.get("shoeBox");
            if (savedInstanceState.containsKey("DIRECTORY"))
                directory = savedInstanceState.getString("DIRECTORY");
            if (savedInstanceState.containsKey("DIFFICULTY"))
                difficulty = savedInstanceState.getInt("DIFFICULTY");

        }
        Log.i("INstance", "Instance  null");
        AssetManager assetManager = this.getAssets();

        if (savedInstanceState == null) {
            try {
                String[] files = assetManager.list(directory);
                if (files == null)
                    throw new NullPointerException();
                for (String file : files) {
                    String text = file.substring(0, file.indexOf('.'));
                    Log.i("files", text);
                    Item item = new Item(text);
                    item.setAudioFile(text);
                    shoeBox.addACard(item);

                }

            } catch (Exception e) {
                Log.e("Error:", e.toString());
                System.exit(1);
            }


        }
        initSoundPool();

        shoeBox.setNumInActiveList(difficulty);

        buttonLayout = new FlowLayout(this);
        buttonLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        // buttonLayout.setGravity(Gravity.BOTTOM);
        LinearLayout linearLayout = findViewById(R.id.LinearLayout);
        //buttonLayout.setMinimumWidth(constraintLayout.getWidth());
        // linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(buttonLayout);
        Log.i("test", "1");
        for (int i = 0; i < shoeBox.getNumInActiveList(); i++) {
            Log.i("test", "2");
            Button button = new Button(this);
            button.setId(View.generateViewId());
            button.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.egg));
            button.setTextSize(30f);
            button.setTextColor(getResources().getColor(R.color.colorAccent));

            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    final Button b = (Button) v;
                    Log.i("IDS", String.valueOf(b.getId()) + " " + String.valueOf(shoeBox.getCurrentItem().getId()));
                    if (b.getId() == shoeBox.getCurrentItem().getId()) {
                        shoeBox.getCurrentItem().setCorrect(shoeBox.getCurrentItem().getCorrect() + 1);
                        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/nicework");
                        b.startAnimation(AnimationUtils.loadAnimation(b.getContext(), R.anim.move));

                        playSound("nicework", 0, 1f);

                        loadButtons();
                    } else {
                        b.startAnimation(AnimationUtils.loadAnimation(b.getContext(), R.anim.rotate));
                        playSound("failbuzzer", 0f, 1f);
                        shoeBox.getCurrentItem().setCorrect(0);
                    }

                }

            });
            Log.i("test", "3");
            buttonLayout.addView(button);
        }

        Context context = getApplicationContext();
        CharSequence text = "Touch anywhere! But not an Egg";

        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 10, 10);
        toast.show();
        loadButtons();
    }

    public void playSound(final String text, final float volumnL, final float volumnR) {
        //nice work needs to be played before playing next sound

        Log.i("playsound1", text);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                Log.i("playsound2", text);

                while (now - last_time < 500) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Log.e("PlaySound Error:", ex.toString());
                    }
                    now = System.currentTimeMillis();
                }
                Log.i("playsound3", text);

                if (soundPoolLoaded) {
                    Log.i("playsound", text);
                    soundPool.play(soundHash.get(text), volumnL, volumnR, 1, 0, 1f);
                }
                last_time = now;


            }
        };
        runnable.run();
    }


    public void loadButtons() {
        final Random rnd = new Random(System.currentTimeMillis());
        int j = 0;
        shoeBox.getActiveList();
        shoeBox.getRandActiveList();
        if (shoeBox.masteredTheList()) {
            //  if(null ==null){
            Intent newIntent = new Intent(this, YouWon.class);
            startActivity(newIntent);
            finish();

        }
        for (int i = 0; i < shoeBox.getActiveList().size(); i++) {
            Log.i("items", String.valueOf(i));
            Button button = (Button) buttonLayout.getChildAt(i);
            button.setAllCaps(false);
            shoeBox.getActiveList().get(i).setId(button.getId());
            button.setText(shoeBox.getActiveList().get(i).getText());
            //button.setAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_up));
            // button.setAnimation(AnimationUtils.loadAnimation(this,R.anim.move));
        }
        shoeBox.setCurrentItem(shoeBox.getChoice());

        playSound(shoeBox.getCurrentItem().getAudioFileString(), 1f, 1f);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void playSound(View view) {
        playSound(shoeBox.getCurrentItem().getAudioFileString(), 1f, 0f);
    }

    public void playSoundToPick(View view) {
        Context context = getApplicationContext();
        CharSequence textToast = "Touch the egg that matches the sound";

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, textToast, duration);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 10, 10);
        toast.show();
        playSound(shoeBox.getCurrentItem().getAudioFileString(), 1f, 1f);
    }
}
