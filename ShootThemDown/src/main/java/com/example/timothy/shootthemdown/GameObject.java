
package com.example.timothy.shootthemdown;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class GameObject {
    private static int genId=1;
    final Bitmap image;
    protected boolean remove=false;
    final int rowCount;
    final int colCount;

    private final int WIDTH;
    private final int HEIGHT;
    final int width;
    private int id = getNextId();
    final int height;
    int x;
    int y;


    GameObject(Bitmap image, int rowCount, int colCount, int x, int y)  {

        this.image = image;
        this.rowCount= rowCount;
        this.colCount= colCount;

        this.x= x;
        this.y= y;

        this.WIDTH = image.getWidth();
        this.HEIGHT = image.getHeight();
        this.width = this.WIDTH/ colCount;
        this.height= this.HEIGHT/ rowCount;
    }
    public int getId(){
        return id;
    }
    static int getNextId(){
        return genId++;
    }

    Bitmap createSubImageAt(int row, int col)  {
        // createBitmap(bitmap, x, y, width, height).
        return Bitmap.createBitmap(image, col* width, row* height ,width,height);
    }
    public void update(){

    }
    public void draw(Canvas canvas){

    }
    public GameObject pressed(int x,int y){
        return null;
    }

    public int getX()  {
        return this.x;
    }
    public int getY()  {
        return this.y;
    }


    public int getHeight() {
        return height;
    }

    int getWidth() {
        return width;
    }

}

