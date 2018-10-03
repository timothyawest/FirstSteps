package com.example.timothy.shootthemdown;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.example.shoebox.Item;

public class Missile extends GameObject {
    private int id = getNextId();
    private final Bitmap[] missiles;
    private int colUsing;
    private long lastDrawNanoTime = -1;
    private int movingVectorX = 10;
    private int movingVectorY = 5;
    private GameSurface gameSurface;
    private long missileTurnLast = 0;
    private SpriteList spriteList;
    private int pressedTime = -1;
    private Item shoeBoxItem;
    private Target target = null;
    private int degrees = -70;
    private int TIME = 20;         //millies to reach bottom

    public Missile(Item item, SpriteList spriteList, GameSurface gameSurface, int x, int y) {
        super(BitmapFactory.decodeResource(gameSurface.getResources(), R.drawable.missile), 1, 3, x, y);
        this.gameSurface = gameSurface;
        this.spriteList = spriteList;
        missiles = new Bitmap[3];
        for (int i = 0; i < 3; i++) {
            missiles[i] = this.createSubImageAt(0, i);
        }
        this.shoeBoxItem=item;

    }

    public void update() {
        long now = System.nanoTime();
        //if we come back to the app after going somewhere else pick up as though we are at se
        if (lastDrawNanoTime == -1 || now - lastDrawNanoTime > 500000000) {
            lastDrawNanoTime = now;
        }
        int deltaTime = (int) ((now - lastDrawNanoTime) / 1000000);  //time in milli
        int screenHeight = this.gameSurface.getHeight();
        int screenWidth = this.gameSurface.getWidth();
        if (this.y > screenHeight - spriteList.getLetterBoxes().get(0).getHeight()*2 - this.image.getHeight()) {
            //Explosion explosion = new Explosion(spriteList, gameSurface, this.x, this.y + image.getHeight() / 4);
            Explosion explosion = new Explosion(spriteList, this.gameSurface, this.getX(), this.getY() + missiles[0].getHeight() / 4);
            spriteList.getGameObjectsToRemove().add(this);
            gameSurface.playSound("explosion", .1f);
            int leastDistance = 99999999;
            GameObject gameObjectClosest = null;
            int chibicnt = 0;
            for (GameObject gameObject : spriteList.getGameObjects()) {
                if (!gameObject.getClass().getSimpleName().equals("LittleTanks"))
                    continue;
                int x = (this.getX() - gameObject.getX()) * (this.getX() - gameObject.getX());
                int y = (this.getY() - gameObject.getY()) * (this.getY() - gameObject.getY());
                int dis = x + y;
                if (dis < leastDistance) {
                    gameObjectClosest = gameObject;
                    leastDistance = dis;
                }
                chibicnt++;
            }
            if (gameObjectClosest != null) {
                spriteList.getGameObjectsToRemove().add(gameObjectClosest);
                if (chibicnt == 1)
                    gameSurface.gameOver = true;
            }
            if(!gameSurface.gameOver)
                spriteList.getGameObjectsToAdd().add(explosion);

            return;
        }

        missiles[0].getHeight();
        float height = (float) (screenHeight - this.image.getHeight());
        float width = (float) (screenWidth - this.image.getWidth());
        float speed = (screenHeight / (float) TIME * (float) deltaTime / 1000);
        float rad = ((float) this.degrees) / 180f * (float) Math.PI;
        Log.i("rad", String.valueOf((Math.cos(rad) * speed)));
        this.y += speed;
        //   this.x +=  (int)(Math.cos(rad)*speed)*degrees/Math.abs(degrees)*-1;  //negative degrees should be neg cos this is a hack

        //  if(this.x >= width)
        //    this.degrees =45;
        //else if(this.x <= 0)
        //  this.degrees = -45;
        if ((int) ((now - missileTurnLast) / 1000000) > 200) {
            missileTurnLast = now;
            this.colUsing++;
            if (colUsing >= this.colCount) {
                this.colUsing = 0;
            }

        }

    }

    public int getPressedTime() {
        return this.pressedTime;
    }

    private Bitmap[] getMoveBitmaps() {
        return missiles;
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = this.getCurrentMoveBitmap();
        // Matrix matrix = new Matrix();

        //matrix.postRotate(degrees);
        // bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(bitmap, this.x, this.y, null);
        // Last draw time.
        this.lastDrawNanoTime = System.nanoTime();
    }

    public Bitmap getCurrentMoveBitmap() {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.colUsing];
    }

    public void unPress() {
        pressedTime = -1;
    }

    public Item getShoeBoxItem() {
        return shoeBoxItem;
    }

    public Missile pressed(int x, int y) {
        if (x < this.getWidth() + this.x && x > this.x && this.getHeight() + this.y > y && this.y < y) {
            for (GameObject gameObject : spriteList.getGameObjects()) {
                if (gameObject.getClass().getSimpleName().equals("Missile"))
                    ((Missile) gameObject).unPress();
            }
            pressedTime = (int) ((System.nanoTime()) / 1000000);  //time in milli
            Target target = new Target(spriteList, gameSurface, this);

            if (this.target != null)
                spriteList.getGameObjectsToRemove().add(this.target);
            this.target = target;
            spriteList.getGameObjectsToAdd().add(target);
            //int indx= spriteList.getMissiles().indexOf(this);
            //spriteList.getMissiles().remove(indx);
            //Explosion explosion = new Explosion(spriteList,gameSurface,this.x,this.y+this.getHeight()/4);
            //spriteList.getExplosions().add(explosion);
            return this;
        }
        return null;
    }

    public void setMovingVector(int movingVectorX, int movingVectorY) {
        this.movingVectorX = movingVectorX;
        this.movingVectorY = movingVectorY;
    }
}
