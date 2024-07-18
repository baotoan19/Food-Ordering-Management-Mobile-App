package com.example.quanlycuahangbandoanvat.Config;

import android.content.Context;
import android.util.Log;

import com.example.quanlycuahangbandoanvat.R;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class InitDatabase {
    private FirebaseFirestore firestore;
    private Context context;
    private static final String TAG = "InitDatabase";

    public InitDatabase(Context context) {
        this.context = context;
        this.firestore = FirebaseFirestore.getInstance();
    }

    // Đã push all data
    public void initData() {
        int[] jsonFiles = {
               // R.raw.role,
               // R.raw.bill,
               R.raw.bill_detail,
                //R.raw.cart,
                 // R.raw.cart_detail,
               // R.raw.category,
               // R.raw.customer,
               // R.raw.food,
               // R.raw.promotion,
               // R.raw.rating,
               // R.raw.employee
        };

        for (int resourceId : jsonFiles) {
            importJsonToFirestore(resourceId, getCollectionNameFromResourceId(resourceId));
        }
    }

    private String readRawResource(int resourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getResources().openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void importJsonToFirestore(int resourceId, String collectionName) {
        String jsonString = readRawResource(resourceId);
        if (jsonString == null || jsonString.isEmpty()) {
            Log.e(TAG, "JSON content is empty or null for resource: " + resourceId);
            return;
        }

        JsonElement jsonElement = JsonParser.parseString(jsonString);

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            processJsonObjectData(collectionName, jsonObject);
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            processJsonArrayData(collectionName, jsonArray);
        }
    }

    private void processJsonObjectData(String collectionName, JsonObject jsonObject) {
        firestore.collection(collectionName).add(convertJsonObjectToMap(jsonObject))
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    private void processJsonArrayData(String collectionName, JsonArray jsonArray) {
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            firestore.collection(collectionName).add(convertJsonObjectToMap(jsonObject))
                    .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        }
    }

    private Map<String, Object> convertJsonObjectToMap(JsonObject jsonObject) {
        return new Gson().fromJson(jsonObject, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    private String getCollectionNameFromResourceId(int resourceId) {
        String fileName = context.getResources().getResourceEntryName(resourceId);
        return fileName;
    }
}
