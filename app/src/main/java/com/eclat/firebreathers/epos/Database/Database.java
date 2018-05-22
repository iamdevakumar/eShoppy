package com.eclat.firebreathers.epos.Database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class Database extends SQLiteOpenHelper {


    public static final String reg_table = "registration";
    public static final String reg_username = "Username";
    public static final String reg_pass = "Password";
    public static final String reg_mobile = "Mobile";
    public static final String reg_emailid = "Email";
    public static final String reg_place="Address";
    public static final String reg_gender="Gender";
    static final String DB_NAME = "Login&Signup.db";

    static final int DB_VERSION = 1;

    private static final String CREATE_LoginSignup_Table = "create table " + reg_table+ "("  + reg_username + ","+reg_pass+","+reg_mobile+" INTEGER PRIMARY KEY, "
            + reg_emailid + "," +reg_place+ ","+ reg_gender + ");";

    public Database(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db)
    {
        db.execSQL(CREATE_LoginSignup_Table);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + reg_table);
        onCreate(db);
    }
}