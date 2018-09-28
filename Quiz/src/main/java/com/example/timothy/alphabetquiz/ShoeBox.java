package com.example.timothy.alphabetquiz;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.Vector;

import com.example.timothy.alphabetquiz.Item;

public class ShoeBox implements Serializable {
    public int numInActiveList = 10;
    private int numTimesCorrect = 2;
     Item currentItem = null;
     Vector <Item> activeList = new Vector<>();
     Vector<Item> toDoList = new Vector<>();
     Vector<Item> doneList = new Vector<>();

    ShoeBox () {

    }
    public Item getCurrentItem(){
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }

    public void addCard(Item item) {
        toDoList.add(item);
    }

    public void addACard(Item item) {
        toDoList.add(item);
    }
    private Vector<Item> randomizeVector(Vector<Item> list){
        Random rnd = new Random(System.currentTimeMillis());
        Vector<Item> myrndlist= (Vector<Item>)list.clone();
        for (int i = 0; i < myrndlist.size(); i++) {
            int choice = rnd.nextInt(myrndlist.size());
            Item swap = myrndlist.get(choice);
            myrndlist.remove(choice);
            myrndlist.add(swap);
        }
        return myrndlist;
    }
    public Vector<Item> getRandActiveList() {
        activeList= randomizeVector(getActiveList());
        return activeList;
    }
    public Item getChoice(){
        Vector<Item> myrndlist= randomizeVector(getActiveList());
        for(Item item:myrndlist){
            if(item.getCorrect() <numTimesCorrect)
                return item;
        }
        return null;

        //return null;
    }
    public Vector<Item> getActiveList() {
        if (activeList.size() == 0)
            for (int i = 0; i < numInActiveList && i < toDoList.size(); i++) {
                activeList.add(toDoList.get(0));
                toDoList.remove(0);
            }
        //clean out ones that are correct enough
        try {
                for (Item item : activeList) {
                    if (item.getCorrect() >= numTimesCorrect) {
                        doneList.add(item);
                        if (toDoList.size() > 0) {
                            activeList.remove(item);
                            activeList.add(toDoList.get(0));
                            toDoList.remove(0);
                        }
                    }
                }
        } catch (ConcurrentModificationException e) {    // call again and hopefully somebody else isn't trying to access it
            return getActiveList();
        }
        return activeList;
    }

}