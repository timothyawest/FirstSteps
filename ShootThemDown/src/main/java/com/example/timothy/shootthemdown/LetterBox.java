package com.example.timothy.shootthemdown;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class LetterBox extends GameObject {
        private SpriteList spriteList;
        private final Bitmap letterBox;
        private GameSurface gameSurface;
        private String text=null;
        private Item shoeBoxItem;
        public boolean indicator=false;
        private Bitmap drawButton(String text, int width, int height){
                Bitmap bmp = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                Paint paint = new Paint();
                if(indicator) {
                        paint.setColor(Color.RED);
                        indicator = false;
                }else
                      paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPaint(paint);
                paint.setColor(Color.BLACK);
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(text,width/2,80,paint);
                canvas.drawLine(1,0,1,height,paint);
                canvas.drawLine(width-1,0,width-1,height,paint);

            return bmp;
        }
        LetterBox(SpriteList spriteList, Item item, int width, int x, int y){
                super(Bitmap.createBitmap(width, 100, Bitmap.Config.ARGB_8888),1,1,x,y);
                letterBox = this.createSubImageAt(0,0);
                this.spriteList = spriteList;
                this.shoeBoxItem=item;
                this.text = item.getText();
        }
        public void update(){

        }
        //public String getText(){
        //    return text;
       // }
        public Item getShoeBoxItem(){
                return this.shoeBoxItem;
        }
        public void draw(int x, int y, Canvas canvas){
            this.x=x;
            this.y=y;
            //   Bitmap bitmap = this.get;
                //  this.letterBox=b;
            canvas.drawBitmap(drawButton(text,width,height),x, y, null);
            // Last draw time.

        }
        public void draw( Canvas canvas){
                //   Bitmap bitmap = this.get;
                //  this.lettierBox=b;
                canvas.drawBitmap(drawButton(text,width,height),x, y, null);
                // Last draw time.

        }
        public LetterBox pressed(int x,int y){
                Log.i("Letter Pressed","maybe");
                int now =  (int)(System.currentTimeMillis()/1000000);
                if(x<this.letterBox.getWidth()+this.x && x>this.x && this.letterBox.getHeight()+this.y> y && this.y < y){
                        Log.i("Letter Pressed","yes");


                        return this;
                }
                return null;
        }
}

