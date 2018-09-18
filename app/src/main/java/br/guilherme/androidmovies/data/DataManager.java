package br.guilherme.androidmovies.data;

import android.content.SharedPreferences;

public class DataManager {

    private SharedPreferences sharedPreferences;

    public DataManager(SharedPreferences sharedPrefsHelper)
    {
        sharedPreferences = sharedPrefsHelper;
    }

    public void setValue(String key, int value)
    {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public void setValue(String key, String value)
    {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void setValue(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void remove(String key)
    {
        sharedPreferences.edit().remove(key).apply();
    }

    public String getValue(String key) {
        return sharedPreferences.getString(key, null);
    }

    public boolean getBoolValue(String key) {
        return sharedPreferences.getBoolean(key, false);
    }


}
