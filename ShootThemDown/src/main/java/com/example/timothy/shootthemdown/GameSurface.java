package com.example.timothy.shootthemdown;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import android.view.MotionEvent;
import android.widget.Toast;


import com.example.shoebox.Item;
import com.example.shoebox.ShoeBox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    private ShoeBox shoeBox;
    private GameThread gameThread = null;
    private SpriteList spriteList;
    private SoundPool soundPool;
    public int lastMissileTime;
    private boolean soundPoolLoaded = false;
    private float[] stars;
    private Bitmap background;
    private String directory;
    private HashMap<String, Integer> soundHash = new HashMap<>();
    private Random rnd;
    private LetterBox letterBoxPressed = null;
    public boolean gameOver = true;
    private boolean gameWon = false;
    private GameOverMessage gameOverMessage;
    private ScoreBackground scoreBackground;
    private MainActivity mainActivity;
    public GameSurface(MainActivity context) {
        super(context);
        this.getHolder().addCallback(this);
        this.setFocusable(true);
        mainActivity = context;
        directory = mainActivity.getDirectory();
        initSoundPool();

        lastMissileTime = 0;
    }

    public void pauseGameThread(boolean pause) {
        if (gameThread != null)
            gameThread.setRunning(!pause);
    }

    private void initVariables() {
        shoeBox = null;
        spriteList = null;
        lastMissileTime = 0;
        stars = new float[100];
        background = null;
    }

    private void initObjects() {
        scoreBackground = new ScoreBackground(this);
        rnd = new Random(System.currentTimeMillis());
        this.shoeBox = new ShoeBox();
        Log.i("difficulty",String.valueOf(mainActivity.getDifficulty()));
        if(mainActivity.getDifficulty()<3){
            shoeBox.setNumInActiveList(2);
        }else if(mainActivity.getDifficulty()>=3 && mainActivity.getDifficulty()<7 ){
            shoeBox.setNumInActiveList(3);
        }else {
            shoeBox.setNumInActiveList(5);
        }
        AssetManager assetManager = getContext().getAssets();
        try {
            Log.i("initObjects", directory);
            String[] files = assetManager.list(directory);
            if(files==null)
                throw new NullPointerException();
            for (String file:files) {
                String text=file.substring(0, file.indexOf('.'));
                Log.i("files",text );
                shoeBox.addACard(new Item(text));

            }
        }catch(Exception e){
            Log.e("Error:" ,e.toString());
            System.exit(1);
        }
        this.spriteList = new SpriteList();
        // Make Game Surface focusable so it can handle events.
        // Sét callback.
        for (int i = 0; i < 100; i += 2) {
            float x = (float) rnd.nextInt(getWidth());
            float y = (float) rnd.nextInt(getHeight());
            stars[i] = x;
            stars[i + 1] = y;
        }
        background = BitmapFactory.decodeResource(this.getResources(), R.drawable.background);
        float ratio = ((float) background.getHeight() / (float) background.getWidth());
        int myheight = (int) (ratio * (float) getWidth());
        background = Bitmap.createScaledBitmap(background, getWidth(), myheight, false);


    }


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

                // Playing background sound.
                //playSoundBackground();
            }
        });

        AssetManager assetManager = getContext().getAssets();
        String[] files;
        try {
            Log.i("LoadHash", directory);
            files = assetManager.list(directory);
            if(files==null)
                throw new NullPointerException();
            for (String file:files) {
                if(file.contains("mp3")) {
                    String text=file.substring(0, file.indexOf('.'));
                    if (!soundHash.containsKey(text)) {
                        try {
                            int id = this.soundPool.load(assetManager.openFd(directory + "/" + file), 1);
                            soundHash.put(text, id);
                        } catch (Exception e) {
                            Log.e("PlaySound", e.toString());
                        }
                    }
                }
            }
        }catch(Exception ex){
            Log.e("exception",ex.toString());
            System.exit(1);

        }


        soundHash.put("explosion", this.soundPool.load(this.getContext(), R.raw.explosion, 4));
        soundHash.put("failbuzzer", this.soundPool.load(this.getContext(), R.raw.failbuzzer, 4));


    }


    public void playSound(String text, float volumn) {
        if (this.soundPoolLoaded) {


            Log.i("playsound", text);
            soundPool.play(soundHash.get(text), volumn, volumn, 1, 0, 1f);
        }
    }

    private void buttonPressed() {
        for (GameObject gameObject : spriteList.getGameObjects()) {
            if (gameObject.getClass().getSimpleName().equals("Missile")) {
                Missile missile = (Missile) gameObject;
                Item itemLetter = null;
                if (missile.getPressedTime() > 0) {
                    for (Item item : shoeBox.getActiveList()) {
                        if (item == missile.getShoeBoxItem()) {
                            itemLetter = item;
                            break;
                        }
                    }
                    if (missile.getShoeBoxItem() == letterBoxPressed.getShoeBoxItem()) {
                        playSound("explosion", .1f);
                        Explosion explosion = new Explosion(spriteList, this, missile.getX(), missile.getY() + missile.getHeight() / 4);
                        spriteList.getGameObjectsToRemove().add(missile);
                        spriteList.getGameObjectsToAdd().add(explosion);
                        this.scoreBackground.changeScoreBy(50);
                        if (itemLetter != null) {
                            itemLetter.setCorrect(itemLetter.getCorrect() + 1);
                        }

                    } else {
                        if (itemLetter != null) {
                            itemLetter.setCorrect(-1);
                            this.scoreBackground.changeScoreBy(-50);
                            for (LetterBox letterBox : spriteList.getLetterBoxes()) {
                                if (letterBox.getShoeBoxItem() == missile.getShoeBoxItem()) {
                                    letterBox.indicator = true;
                                }
                            }
                            playSound("failbuzzer", .5f);
                        }
                    }
                }

            }
        }

    }

    int messageTimes = 0;
    public void update() {
        if (shoeBox == null)
            initializeGame();

        int x = rnd.nextInt(this.getWidth() - 100);
        int now = (int) (System.nanoTime() / 1000000);
        HashSet<Item> boxHash = new HashSet<>();
        HashSet<Item> missileHash = new HashSet<>();
        if (gameOver)
            return;

        spriteList.UpdateGameObjects();

        for (GameObject gameObject : spriteList.getGameObjects()) {
            if (gameObject.getClass().getSimpleName().equals(Missile.class.getSimpleName())) {
                missileHash.add(((Missile) gameObject).getShoeBoxItem());
            }else if(gameObject.getClass().getSimpleName().equals("LetterBox")){
                boxHash.add(((LetterBox) gameObject).getShoeBoxItem());
            }
            gameObject.update();
        }
        if (gameWon)
            return;

        if (spriteList.getLetterBoxes().size() == 0) {
            for (Item item : shoeBox.getActiveList()) {
                LetterBox myletter = new LetterBox(spriteList, item, getWidth() / shoeBox.getNumInActiveList(), 0, 0);
                spriteList.getLetterBoxes().add(myletter);

            }
            int numLives = 5;
            for (int i = 0; i < numLives; i++) {
                LittleTanks chibi = new LittleTanks(this, i * this.getWidth() / 5, this.getHeight()-spriteList.getLetterBoxes().get(0).getHeight());
                spriteList.getGameObjectsToAdd().add(chibi);
            }
        }

        int howOftenNewMissiles = 3000;
        if (now - lastMissileTime > howOftenNewMissiles) {
            if (missileHash.size() < shoeBox.getNumInActiveList()) { // if we have a new letter but havent cleared old yet
                Item item = shoeBox.getChoice();
                Missile missile = new Missile(item, spriteList, this, x, 0);
                spriteList.getGameObjectsToAdd().add(missile);
                this.lastMissileTime = now;
            }
        }

        if (letterBoxPressed != null) {
            buttonPressed();
            letterBoxPressed = null;
        }


        for (LetterBox letterBox : spriteList.getLetterBoxes()) {
            boxHash.add(letterBox.getShoeBoxItem());
        }
        HashSet<Item> activeHash = new HashSet<>(shoeBox.getActiveList());


        Iterator<Item> listICanAdd = activeHash.iterator();
        for (LetterBox letterBox:spriteList.getLetterBoxes()) {
            if (!activeHash.contains(letterBox.getShoeBoxItem()) && !missileHash.contains(letterBox.getShoeBoxItem())) {
                while (listICanAdd.hasNext()) {
                    Item item = listICanAdd.next();
                    if (!boxHash.contains(item)) {
                        spriteList.getLetterBoxes().set(spriteList.getLetterBoxes().indexOf(letterBox), new LetterBox(spriteList, item, getWidth() / shoeBox.getNumInActiveList(), 0, 0));
                        break;
                    }
                }

            }
        }
        if (shoeBox.masteredTheList()) {
            spriteList.getGameObjectsToRemove().addAll(spriteList.getGameObjects());
            spriteList.getGameObjectsToAdd().add(new GameWonMessage(this));
            gameWon = true;
        }


    }

    @Override

    public void draw(Canvas canvas) {
        super.draw(canvas);
        int start;
        int end;
        start = rnd.nextInt(49);
        end = rnd.nextInt(2);
        if (start + end > 50)
            end = 50 - start;
        float stars2[] = Arrays.copyOfRange(stars, 0, start * 2);
        float stars3[] = Arrays.copyOfRange(stars, (start + end) * 2, 100);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
        canvas.drawPoints(stars2, paint);
        canvas.drawPoints(stars3, paint);

        Log.i("chibi", String.valueOf(4));
        scoreBackground.draw(canvas);

        canvas.drawBitmap(background, 0, getHeight() - 100 - background.getHeight(), null);

        Iterator<GameObject> gameObjectIterator = spriteList.getGameObjects().iterator();
        Log.i("draw", "objects start");
        while (gameObjectIterator.hasNext()) {
            gameObjectIterator.next().draw(canvas);
            Log.i("draw", "objects");
        }
        if (gameOver) {
            if (gameOverMessage == null)
                this.gameOverMessage = new GameOverMessage(this);
            gameOverMessage.draw(canvas);
        }
        if (this.gameWon)
            return;
        // synchronized (spriteList.getLetterBoxes()) {
        for (int i = 0; i < spriteList.getLetterBoxes().size(); i++) {
            LetterBox letterBox = spriteList.getLetterBoxes().get(i);
            letterBox.draw(getWidth() * i / spriteList.getLetterBoxes().size(), getHeight() - 100, canvas);
            //   }
        }
    }

    int missileMessageTimes = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            GameObject myObject = spriteList.pressed(x, y);
            if (gameOver)

                myObject = gameOverMessage.pressed(x, y);
            if (myObject != null) {
                if(myObject.getClass().getSimpleName().equals(GameOverMessage.class.getSimpleName())) {
                    this.gameOver = false;
                    this.initializeGame();
                }else if(myObject.getClass().getSimpleName().equals(Missile.class.getSimpleName())) {

                        Missile missile = (Missile) myObject;
                    Log.i("OnTouchEvent", missile.getShoeBoxItem().getText());
                        playSound(missile.getShoeBoxItem().getText(), 1f);
                }else if(myObject.getClass().getSimpleName().equals(LetterBox.class.getSimpleName())) {
                        this.letterBoxPressed = (LetterBox) myObject;

                }else if(myObject.getClass().getSimpleName().equals(GameWonMessage.class.getSimpleName())) {
                    mainActivity.finish();
                }
            }

            return true;
        }
        return false;
    }

    private void initializeGame() {
        //gameThread.setRunning(false);
        initVariables();
        initObjects();
        Log.i("gameover", "true");
        //    this.gameOver = false;
        //gameThread.setRunning(true);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (this.gameThread == null) {
            this.gameThread = new GameThread(this, holder);
            this.gameThread.setRunning(true);
            this.gameThread.start();
        }
        if (!this.gameThread.isAlive()) {
            gameThread = new GameThread(this, holder);
            this.gameThread.setRunning(true);
            this.gameThread.start();
        }

    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("shoeBox", "surface Changed");


    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("shoeBox", "destroy Surface");

        boolean retry = true;
        this.gameThread.setRunning(false);
        while (retry) {
            try {

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}