package com.example.shoebox;
import android.net.Uri;

import java.io.Serializable;

public class Item implements Serializable {
    private int right;
    int id;
    String text=null;
    String audioFile=null;
    String background =null;

    public Item(String text) {
        this.text =text;
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

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
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

    public String getAudioFileString() {
        return this.audioFile;
    }
   public int getCorrect(){
        return right;
    }

}
