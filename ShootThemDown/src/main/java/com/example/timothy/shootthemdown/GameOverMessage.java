package com.example.timothy.shootthemdown;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class GameOverMessage extends GameObject {
    private GameSurface gameSurface=null;
    int my=0;
    private int inc=3;
    static Bitmap createGameOverScreen(GameSurface gameSurface,int y){
        int heightM=gameSurface.getHeight()/3;
        int widthM = gameSurface.getWidth();
        Bitmap bmp = Bitmap.createBitmap(widthM,heightM, Bitmap.Config.ARGB_8888);

        Canvas canvasM = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL);
        canvasM.drawPaint(paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(widthM/8);
        paint.setTextAlign(Paint.Align.CENTER);
        canvasM.drawText("Tap to Play",widthM/2,heightM/2+y,paint);
    /*    canvasM.drawRect((float)(widthM*1/3-yesWidth),(float)(heightM/2+y+10),(float)(widthM*1/3+yesWidth),(float)(heightM/2+y+widthM/8+10),paint);
        paint.setColor(Color.BLACK);
        canvasM.drawText("Yes",widthM*1/3,heightM/2+y+widthM/8+10,paint);
        canvasM.drawText("No",widthM*2/3,heightM/2+y+widthM/8+10,paint);*/

        //   canvasM.drawLine(1,0,1,canvasL.getHeight(),paint);
       // canvasM.drawLine(canvasL.getWidth()-1,0,canvasL.getWidth()-1,canvasL.getHeight(),paint);
        return bmp;
    }
    GameOverMessage(GameSurface gameSurface){
        super(createGameOverScreen(gameSurface,0),1,1,0,gameSurface.getHeight()/3);
        this.gameSurface =gameSurface;
        this.x=0;
        this.y=gameSurface.getHeight()/3;

    }
    public void draw(Canvas canvas){
        //   Bitmap bitmap = this.get;
        //  this.letterBox=b;
        if(this.my+20>this.getHeight()/3)
            inc =-3;
        if(my<10-(this.getHeight()/3))
            inc = 3;
        my+=inc;
        canvas.drawBitmap(createGameOverScreen(gameSurface,my),x, y, null);
        // Last draw time.

    }
    public GameOverMessage pressed(int x,int y){
        Log.i("gameover","pressed");
        if(x>this.getX() && y > this.getY() && x < this.getX()+this.getWidth() && this.getY() < this.getY()+this.getHeight()){
            return this;
        }
        return null;
    }
}
