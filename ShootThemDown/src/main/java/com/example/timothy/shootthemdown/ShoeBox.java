package com.example.timothy.shootthemdown;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ShoeBox implements Serializable {
    private int numInActiveList = 2;
    private final int numTimesCorrect = 2;
    private boolean endOfList=false;
    private Random rnd = new Random(System.currentTimeMillis());
    Item currentItem = null;
    List<Item> activeList = new ArrayList<>();
    List<Item> toDoList = new ArrayList<>();
    List<Item> doneList = new ArrayList<>();

    ShoeBox() {

    }

    public int getNumInActiveList() {
        return numInActiveList;
    }
    public void setNumInActiveList(int numInActiveList){
        this.numInActiveList = numInActiveList;
    }

    public Item getCurrentItem() {
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
    public boolean masteredTheList(){
        return endOfList;
    }
    private List<Item> randomizeList(List<Item> list) {
        synchronized (activeList) {
            for (int i = 0; i < activeList.size(); i++) {
                int choice = rnd.nextInt(activeList.size());
                int choice2 = rnd.nextInt(activeList.size());

                Item swap = activeList.get(choice);
                activeList.set(choice,activeList.get(choice2));
                activeList.set(choice2,swap);
            }
        }
        return getActiveList();
    }

    public List<Item> getRandActiveList() {
        activeList = randomizeList(getActiveList());
        return activeList;
    }

    public Item getChoice() {
        List<Item>  myrndlist = getActiveList();
        Item item = myrndlist.get(rnd.nextInt(myrndlist.size()));
        if(item.getCorrect()<numTimesCorrect) //first attempt to get a good one...
            return item;

        for(int i=0; i<myrndlist.size();i++){          //but if I can't find it return the one that needs the most practice
            Item newItem = myrndlist.get(i);
            if(newItem.getCorrect()< item.getCorrect())
                item = newItem;
        }

        return item;
        //return null;

        //return null;
    }

    public List<Item> getActiveList() {
        synchronized (activeList) {

            Iterator<Item> activeIter = activeList.iterator();
            int cntFinishedItems=0;
            while (activeIter.hasNext()) {
                Item item = activeIter.next();
                if (item.getCorrect() >= numTimesCorrect) {
                    doneList.add(item);
                    if (toDoList.size() + activeList.size() > numInActiveList) {
                        activeIter.remove();
                    }else{
                        cntFinishedItems++;
                    }
                }
            }
            if(cntFinishedItems == numInActiveList)
                this.endOfList = true;
            while (activeList.size() < numInActiveList && toDoList.size() > 0) {
                Log.i("shoebox", "in 2nd while");
                activeList.add(toDoList.remove(0));

            }
        }
        return activeList;
    }

}