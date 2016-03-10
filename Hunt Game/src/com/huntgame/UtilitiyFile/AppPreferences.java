package com.huntgame.UtilitiyFile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
   //  private static final String APP_SHARED_PREFS = "com.aydabtu.BroadcastSMS_preferences"; //  Name of the file -.xml
     private SharedPreferences appSharedPrefs;
     private Editor prefsEditor;

     public AppPreferences(Context context,String Preferncename)
     {
         this.appSharedPrefs = context.getSharedPreferences(Preferncename, Activity.MODE_PRIVATE);
         this.prefsEditor = appSharedPrefs.edit();
     }
/****
 * 
 * getdata() get the value from the preference
 * 
 * */
     public String getData(String key) {
         return appSharedPrefs.getString(key, "");
     }
     /****
      * 
      * SaveData() save  the value to the preference
      * 
      * */
     public void SaveData(String text,String Tag) {
         prefsEditor.putString(Tag, text);
         prefsEditor.commit();
     }
}