package com.eclat.firebreathers.epos.Modeclasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.eclat.firebreathers.epos.Activities.LoginActivity;

public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "EPOS";
    public static final String IS_LOGIN = "islogin";
    public static final String KEY_NAME = "key_name";
    public static final String KEY_USERID = "key_userid";
    public static final String KEY_MOBILE = "key_mobile";
    public static final String KEY_EMAIL = "key_email";
    public static final String KEY_USERIMG = "key_userimg";
    public static final String KEY_DOB = "key_dob";
    public static final String KEY_GENDER = "key_gender";
    public static final String KEY_ADDRESS = "key_address";
    public static final String KEY_OTPSTATUS = "key_otpstatus";
    public static final String KEY_CARTCOUNT = "key_cartcount";


    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    public void createLoginSession(String name, String mobile, String email, String userid, String userimg, String loginstatus, String dob, String gender, String address, String otpstatus, String cartcount) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERIMG, userimg);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_OTPSTATUS, otpstatus);
        editor.putString(KEY_CARTCOUNT, cartcount);
        editor.putString(IS_LOGIN, loginstatus);
        editor.commit();
        Log.d("Login Session :", "Successfully Created");
    }

    public void checkLogin() {
        Intent i = new Intent(_context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
        Log.d("Login Checking:", "Done");
    }

    public void logoutUser() {

        editor.clear();
        editor.commit();
        Log.d("Login Session Status:", "Logout");
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(i);

    }
}
