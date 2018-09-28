package com.example.timothy.shootthemdown;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Explosion extends GameObject {
    private int rowIndex = 0 ;
    private int colIndex = -1 ;
    private final SpriteList spriteList;
    private boolean finish= false;

    public Explosion(SpriteList spriteList, GameSurface GameSurface,  int x, int y) {
        super( BitmapFactory.decodeResource(GameSurface.getResources(),R.drawable.explosion), 5, 5, x, y);
        com.example.timothy.shootthemdown.GameSurface gameSurface = GameSurface;
        this.spriteList = spriteList;
    }

    public void update()  {
        this.colIndex++;
        // Play sound explosion.wav.
       // if(this.colIndex==0 && this.rowIndex==0) {
       //     this.gameSurface.playSoundExplosion();
       // }
        if(this.colIndex >= this.colCount)  {
            this.colIndex =0;
            this.rowIndex++;

            if(this.rowIndex>= this.rowCount)  {
                this.finish= true;
                this.spriteList.getGameObjectsToRemove().add(this);

            }
        }

    }

    public void draw(Canvas canvas)  {
        if(!finish)  {
            Bitmap bitmap= this.createSubImageAt(rowIndex,colIndex);
            canvas.drawBitmap(bitmap, this.x, this.y,null);
        }
    }

    public boolean isFinish() {
        return finish;
    }
}
