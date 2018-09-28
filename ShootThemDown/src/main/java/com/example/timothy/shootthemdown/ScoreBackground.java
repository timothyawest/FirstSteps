package com.example.timothy.shootthemdown;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class ScoreBackground {
    private GameSurface gameSurface=null;
    private int score=0;
    int my=0;
    private Bitmap createScore(){
        int heightM= gameSurface.getHeight()/3;
        int widthM = gameSurface.getWidth();
        Bitmap bmp = Bitmap.createBitmap(widthM,heightM, Bitmap.Config.ARGB_8888);

        Canvas canvasM = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL);
        canvasM.drawPaint(paint);
        paint.setColor(Color.GRAY);
        paint.setTextSize(widthM/4);
        paint.setTextAlign(Paint.Align.CENTER);
        canvasM.drawText(String.valueOf(score),widthM/2,heightM/2,paint);
        return bmp;
    }
    ScoreBackground(GameSurface gameSurface){
        this.gameSurface = gameSurface;
    }
    public void setScore(int num){
        score = num;
    }
    public void changeScoreBy(int num){
        score += num;
    }
    public void update(){

    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(createScore(),0, canvas.getHeight()/3, null);

    }

}
