package com.example.timothy.shootthemdown;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class SpriteList implements Serializable{
    private final List<LetterBox> letterBoxes = new CopyOnWriteArrayList<>();
//    private final List<Target> targets = new ArrayList<>();
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameObject> gameObjectsToAdd = new ArrayList<>();
    // private final List<Missile> missilesToRemove = new ArrayList<>();
    private final List<GameObject> gameObjectsToRemove = new ArrayList<>();
    private final HashMap<String,List<GameObject>> gameObjectsHashMap = new HashMap<>();
    public SpriteList(){
    }

    public List<GameObject> getGameObjectsToAdd() {
        return gameObjectsToAdd;
    }

    /*called to add game objects and remove game object game obects
      game objects shouldn't be acted on directly and should only be done once in an
      update cycle*/

    public void UpdateGameObjects(){
        Iterator<GameObject> gameObjectIter = getGameObjectsToRemove().iterator();
        while (gameObjectIter.hasNext()) {
            GameObject gameObject = gameObjectIter.next();
            Log.i("UpdateGameObjects","remove");
            gameObjectsHashMap.get(gameObject.getClass().getSimpleName()).remove(gameObject);
            getGameObjects().remove(gameObject);
            gameObjectIter.remove();
        }
        Iterator<GameObject> gameObjectIterator = getGameObjectsToAdd().iterator();
        while (gameObjectIterator.hasNext()) {
            Log.i("UpdateGameObjects","add");

            GameObject gameObject = gameObjectIterator.next();
            getGameObjects().add(gameObject);
            if(!gameObjectsHashMap.containsKey(gameObject.getClass().getSimpleName())) {
                gameObjectsHashMap.put(gameObject.getClass().getSimpleName(), new ArrayList<GameObject>());
            }
                gameObjectsHashMap.get(gameObject.getClass().getSimpleName()).add(gameObject);


            gameObjectIterator.remove();
        }

    }
    public HashMap<String,List<GameObject>> getGameObjectsHashMap(){
        return gameObjectsHashMap;
    }
    public List<GameObject> getGameObjectsToRemove(){
        return gameObjectsToRemove;
    }
    public List<GameObject> getGameObjects(){
        return gameObjects;
    }




    public List<LetterBox> getLetterBoxes() {
        return letterBoxes;
    }

    public GameObject pressed(int x, int y) {
                for (GameObject gameObject : gameObjects) {
                    if (gameObject.pressed(x, y) != null)
                        return gameObject;
                }
                for (LetterBox letterBox : letterBoxes) {
                    if (letterBox.pressed(x, y) != null)
                        return letterBox;
            }
        return null;
    }

}
