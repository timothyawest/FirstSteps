package com.example.timothy.FirstSteps;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;

public class WriteReadVariablesToJson {
    private HashMap<String, String> keyvalues = new HashMap<>();

    public void addKeyValue(String key, String value) {
        keyvalues.put(key, value);
    }

    public void addKeyValue(String key, int value) {
        keyvalues.put(key, String.valueOf(value));
    }

    public String get(String key) {
        return keyvalues.get(key);
    }

    public HashMap<String, String> getHash() {
        return this.keyvalues;
    }

    public void writeJsonStream(Context context) {
        try {

            OutputStream out = context.openFileOutput("state.json", Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.setIndent("  ");
            writeMessagesArray(writer, keyvalues);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeMessagesArray(JsonWriter writer, HashMap<String, String> keyvaluePairs) throws IOException {
        writer.beginArray();
        Iterator<String> iterator = keyvaluePairs.keySet().iterator();
        while (iterator.hasNext()) {

            String key = iterator.next();
            writeMessage(writer, key, keyvaluePairs.get(key));
        }
        writer.endArray();
    }

    private void writeMessage(JsonWriter writer, String key, String value) throws IOException {

        writer.beginObject();
        writer.name(key).value(value);
        writer.endObject();
    }

    //--------------------------------------------------
    public HashMap<String, String> readJsonStream(Context context) {
        HashMap<String, String> myHash = new HashMap<>();
        JsonReader reader;
        try {
            InputStream in = context.openFileInput("state.json");
            reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String key = reader.nextName();
                    String value = reader.nextString();
                    myHash.put(key, value);
                }
                reader.endObject();
            }
            reader.endArray();
            reader.close();
        } catch (IOException IOE) {
            IOE.printStackTrace();
        } finally {
        }
        this.keyvalues = myHash;
        return myHash;
    }

    public HashMap<String, String> readMessagesArray(JsonReader reader) {
        HashMap<String, String> messages = new HashMap<>();

        return messages;
    }
}
