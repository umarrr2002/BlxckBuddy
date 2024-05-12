package com.example.blxckbuddy;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefUtil {

    private static final String SHARED_APP_PREFERENCE_NAME = "SharedPref";

    //TODO: Shared Preferences Required for Keeping apps locked and not resetting on BlockBuddy reopen
    //Context cxt;

    private SharedPreferences preferences;
    private SharedPreferences.Editor mEditor;

    public SharedPrefUtil(Context context) {
        this.preferences = context.getSharedPreferences(SHARED_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefUtil getInstance(Context context) {
        return new SharedPrefUtil(context);
    }

    //
    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void putInteger(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    //
    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public int getInteger(String key) {
        return preferences.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    //TODO: Shared Preferences Required for Keeping apps locked and not resetting on BlockBuddy reopen
    public void setListString(List<String> list){

        for(int i = 0; i < list.size(); i++){
            putString("app_" +i, list.get(i));
        }

        putInteger("listSize",list.size());
    }

    //
    public List<String> getListString() {
        int size = getInteger("listSize");

        List<String> temp = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            temp.add(getString("app_" + i));
        }

        return temp;
    }
}
