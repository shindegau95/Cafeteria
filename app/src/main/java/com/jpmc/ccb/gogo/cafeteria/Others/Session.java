package com.jpmc.ccb.gogo.cafeteria.Others;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
/**
 * Created by DELL on 04-02-2018.
 */

public class Session {

    private static final String TAG = "GSession";
    public static final String SESSIONPREF = "SessionPref";
    public static final String USERID = "useridkey";



    public static int getUserId(final Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(SESSIONPREF, Context.MODE_MULTI_PROCESS);
        int id = sharedpreferences.getInt(USERID,-1);
        return id;
    }

    public static void removePreviousUserId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SESSIONPREF, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(USERID);
        editor.commit();
    }
}
