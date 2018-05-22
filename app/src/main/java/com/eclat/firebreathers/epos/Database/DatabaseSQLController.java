package com.eclat.firebreathers.epos.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

public class DatabaseSQLController {
    private Database dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseSQLController(Context c) {
        context = c;
    }

    @NonNull
    public DatabaseSQLController open() throws SQLException {
        dbHelper = new Database(context);
        database = dbHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbHelper.close();
            }

    public long signupdatainsertion(String name,String email,String mobile,String address,String gender,String pwd) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(Database.reg_username,name );
        contentValue.put(Database.reg_pass, pwd);
        contentValue.put(Database.reg_mobile, mobile);
        contentValue.put(Database.reg_emailid, email);
        contentValue.put(Database.reg_place, address);
        contentValue.put(Database.reg_gender, gender);
        database.insert(Database.reg_table, null, contentValue);
        return 0;
    }

    /*public Cursor fetchEnquiry() {
        String[] columns = new String[]{Database.COLUMN_ROWID, Database.COLUMN_NAME, Database.COLUMN_MOBILE,
                Database.COLUMN_EMAIL, Database.COLUMN_ADDRESS, Database.COLUMN_PRODUCT };
        Cursor cursor = database.query(Database.ENQUIRY_TABLE, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;*//*
    }*/


   /* public List<Signupmodel> getAllProductSpecLists() {

        List<Signupmodel> regList = new ArrayList<Signupmodel>();

       // String SELECT_SQL = "SELECT * FROM " + dbHelper.SPECIFICATION_TABLE +";";

        String SELECT_SQL = "";
        SQLiteDatabase readAccess = dbHelper.getReadableDatabase();
        Cursor c = readAccess.rawQuery(SELECT_SQL, null);

        if(c.moveToFirst()) {
            do{
                Signupmodel credentials = new Signupmodel();
                credentials.setName(c.getString(c.getColumnIndex(dbHelper.reg_username)));
                credentials.setPwd(c.getString(c.getColumnIndex(dbHelper.reg_pass)));
                credentials.setMobieno(c.getString(c.getColumnIndex(dbHelper.reg_mobile)));
                credentials.setEmailid(c.getString(c.getColumnIndex(dbHelper.reg_emailid)));
                credentials.setPlace(c.getString(c.getColumnIndex(dbHelper.reg_place)));
                credentials.setGender(c.getString(c.getColumnIndex(dbHelper.reg_gender)));

                regList.add(credentials);
            }while(c.moveToNext());
        }

        return regList;
    }

  *//*  public ProductSpec getProductSpecByProductName(String productName) {

        ProductSpec spec = new ProductSpec();
        try {
            String SELECT_WSQL = "SELECT * FROM " + dbHelper.SPECIFICATION_TABLE + " where PRODUCT = " + productName + ";";
            SQLiteDatabase readAccess = dbHelper.getReadableDatabase();
            Cursor c = readAccess.rawQuery(SELECT_WSQL, null);

            if (c != null) {
                c.moveToFirst();
            }

            spec.setProductId(c.getString(c.getColumnIndex(dbHelper.PRODUCTID)));
            spec.setProductName(c.getString(c.getColumnIndex(dbHelper.PRODUCTNAME)));
            spec.setEngineType(c.getString(c.getColumnIndex(dbHelper.ENGINETYPE)));
            spec.setDisplacementType(c.getString(c.getColumnIndex(dbHelper.DISPLACEMENTTYPE)));
            spec.setMileage(c.getString(c.getColumnIndex(dbHelper.MILEAGE)));
            spec.setFuelCapacity(c.getString(c.getColumnIndex(dbHelper.FUELCAPACITY)));
            spec.setFuelReserved(c.getString(c.getColumnIndex(dbHelper.FUELRESERVED)));
            spec.setIgnitionType(c.getString(c.getColumnIndex(dbHelper.IGNITIONTYPE)));
            spec.setTransmissionType(c.getString(c.getColumnIndex(dbHelper.TRANSMISSIONTYPE)));
            spec.setWheelType(c.getString(c.getColumnIndex(dbHelper.WHEELTYPE)));
            spec.setTyreType(c.getString(c.getColumnIndex(dbHelper.TYRETYPE)));
            spec.setBreakType(c.getString(c.getColumnIndex(dbHelper.BRAKETYPE)));
        } catch (SQLException e) {
            Log.d("Exception",e.getMessage());
            return null;
        }
*//**//*
        return spec;
    }*//*
  *//*  public Cursor fetch() {
        String[] columns = new String[]{Database.COLUMN_REGISTRATION_NO, Database.COLUMN_CUSTOMER_NAME,
              Database.COLUMN_EMAIL_ID, Database.COLUMN_MOBILE_NO, Database.COLUMN_CITY, Database.COLUMN_FRAME_NO, Database.COLUMN_MODEL};
        Cursor cursor = database.query(Database.REGISTRATION_TABLE, columns, null,null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(String register, String name, String Email,String mobile,String city,String frame,String model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.COLUMN_REGISTRATION_NO, register);
        contentValues.put(Database.COLUMN_CUSTOMER_NAME,name);
        contentValues.put(Database.COLUMN_EMAIL_ID, Email);
        contentValues.put(Database.COLUMN_MOBILE_NO,mobile);
        contentValues.put(Database.COLUMN_CITY, city);
        contentValues.put(Database.COLUMN_FRAME_NO, frame);
        contentValues.put(Database.COLUMN_MODEL, model);
        int i = database.update(Database.REGISTRATION_TABLE, contentValues,
                Database.COLUMN_CUSTOMER_NAME + " = " + name, null);
        return i;
    }

    public void delete(String name) {
        database.delete(Database.REGISTRATION_TABLE, Database.COLUMN_CUSTOMER_NAME+ "=" + name, null);
    }*//*
*/
}