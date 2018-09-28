package com.example.timothy.shootthemdown;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Target extends GameObject {
    private final SpriteList spriteList;
    Missile missile;
    Bitmap target;
    public Target(SpriteList spriteList,GameSurface gameSurface,Missile missile){
        super(BitmapFactory.decodeResource(gameSurface.getResources(),R.drawable.target),1,1,missile.getX(),missile.getY());
        this.spriteList =  spriteList;
        this.missile =missile;
        target = this.image;
        update();
    }
    public void draw(Canvas canvas){
        //   Bitmap bitmap = this.get;
        //  this.letterBox=b;
        canvas.drawBitmap(this.target,x, y, null);
        // Last draw time.

    }
    public void update(){
            if(spriteList.getGameObjects().contains(missile) && missile.getPressedTime() >0) {
                int difX = (missile.getWidth()-target.getWidth())/2;
                this.x = missile.getX()+difX;
                this.y = missile.getY();
            }else {
                spriteList.getGameObjectsToRemove().add(this);
            }
    }


}
