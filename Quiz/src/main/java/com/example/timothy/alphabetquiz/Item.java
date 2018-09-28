package com.example.timothy.alphabetquiz;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.widget.Button;

import java.io.Serializable;
import java.net.URI;
import java.util.Vector;

public class Item implements Serializable {
    int right;
    int id;
    String text=null;
    String audioFile=null;
    String background =null;
    Item() {
    }
    public void setCorrect(int correct){
        this.right = correct;
    }
    public void setId(int id){
        this.id =id;
    }
    public int getId(){
        return id;
    }
    public void setAudioFile(Uri audioFile){
        this.audioFile = audioFile.toString();
    }
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text =text;
    }
    public Uri getAudioFile(){
        return (Uri.parse(this.audioFile));
    }
   public int getCorrect(){
        return right;
    }

}
