package com.vpapps.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Boolean getIsNotification() {
        return sharedPreferences.getBoolean("noti", true);
    }

    public void setIsNotification(Boolean isNotification) {
        editor.putBoolean("noti", isNotification);
        editor.apply();
    }

    public Boolean getIsSelectCatShown() {
        return sharedPreferences.getBoolean("sel_cat", false);
    }

    public void setIsSelectCatShown(Boolean isshown) {
        editor.putBoolean("sel_cat", isshown);
        editor.apply();
    }

    public String getCat() {
        return sharedPreferences.getString("cat", "");
    }

    public void setCat(String cat) {
        editor.putString("cat", cat);
        editor.apply();
    }
}
