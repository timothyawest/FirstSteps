package com.example.timothy.shootthemdown;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

public class LittleTanks extends GameObject {
    private static final int ROW_TOP_TO_BOTTOM = 0;
    private static final int ROW_RIGHT_TO_LEFT = 1;
    private static final int ROW_LEFT_TO_RIGHT = 2;
    private static final int ROW_BOTTOM_TO_TOP = 3;

    // Row index of Image are being used.
    private int rowUsing = ROW_LEFT_TO_RIGHT;

    private int colUsing;

    private final Bitmap[] leftToRights;
    private Bitmap[] rightToLefts;
    private Bitmap[] topToBottoms;
    private Bitmap[] bottomToTops;

    // Velocity of game character (pixel/millisecond)
    private static final float VELOCITY = 0.01f;
    public static boolean yes=false;
    private int movingVectorX = 10;
    private int movingVectorY = 5;
    private Random rnd=null;

    private long lastDrawNanoTime =-1;

    private GameSurface gameSurface;
    public LittleTanks(GameSurface gameSurface, int x, int y) {
        super(BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.tank), 4, 3, x, y);

        this.gameSurface= gameSurface;
        this.rnd = new Random();
        this.topToBottoms = new Bitmap[colCount]; // 3
        this.rightToLefts = new Bitmap[colCount]; // 3
        this.leftToRights = new Bitmap[colCount]; // 3
        this.bottomToTops = new Bitmap[colCount]; // 3
        this.y -= this.getHeight();
        for(int col = 0; col< this.colCount; col++ ) {
            this.topToBottoms[col] = this.createSubImageAt(ROW_TOP_TO_BOTTOM, col);
            this.rightToLefts[col]  = this.createSubImageAt(ROW_RIGHT_TO_LEFT, col);
            this.leftToRights[col] = this.createSubImageAt(ROW_LEFT_TO_RIGHT, col);
            this.bottomToTops[col]  = this.createSubImageAt(ROW_BOTTOM_TO_TOP, col);
        }
    }

    public Bitmap[] getMoveBitmaps()  {
        switch (rowUsing)  {
            case ROW_BOTTOM_TO_TOP:
                return  this.bottomToTops;
            case ROW_LEFT_TO_RIGHT:
                return this.leftToRights;
            case ROW_RIGHT_TO_LEFT:
                return this.rightToLefts;
            case ROW_TOP_TO_BOTTOM:
                return this.topToBottoms;
            default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap()  {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.colUsing];
    }


    public void update()  {
        long now = System.nanoTime();

        if(lastDrawNanoTime==-1 || now - lastDrawNanoTime >500000000) {
            lastDrawNanoTime= now;
        }
        this.colUsing++;
        if(colUsing >= this.colCount)  {
            this.colUsing =0;
        }
        // Current time in nanoseconds

        // Never once did draw.
        if(lastDrawNanoTime==-1) {
            lastDrawNanoTime= now;
        }
        // Change nanoseconds to milliseconds (1 nanosecond = 1000000 milliseconds).
        int deltaTime = (int) ((now - lastDrawNanoTime)/ 1000000 );

        // Distance moves
        float distance = VELOCITY * deltaTime;

        double movingVectorLength = Math.sqrt(movingVectorX* movingVectorX + movingVectorY*movingVectorY);

        // Calculate the new position of the game character.
        this.x = x +  (int)(distance* movingVectorX );

        int num =rnd.nextInt(200);

        // When the game's character touches the edge of the screen, then change direction

        if(this.x < 0)  {
            this.x = 0;
            this.movingVectorX = - this.movingVectorX;
            this.rowUsing = ROW_LEFT_TO_RIGHT;

        } else if(this.x > this.gameSurface.getWidth() -width )  {
            this.x= this.gameSurface.getWidth()-width;
            this.movingVectorX = - this.movingVectorX;
            this.rowUsing = ROW_RIGHT_TO_LEFT;

        } else if(num ==50){
            if(this.rowUsing ==ROW_RIGHT_TO_LEFT) {
                this.rowUsing = ROW_LEFT_TO_RIGHT;
                this.movingVectorX = -this.movingVectorX;
            }
            else if (this.rowUsing ==ROW_LEFT_TO_RIGHT){
                this.rowUsing = ROW_RIGHT_TO_LEFT;
                this.movingVectorX = -this.movingVectorX;
            }
        }



    }

    public void draw(Canvas canvas)  {

        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap,x, y, null);
        // Last draw time.
        this.lastDrawNanoTime= System.nanoTime();
    }

    public void setMovingVector(int movingVectorX, int movingVectorY)  {
        this.movingVectorX= movingVectorX;
        this.movingVectorY = movingVectorY;
    }
}
