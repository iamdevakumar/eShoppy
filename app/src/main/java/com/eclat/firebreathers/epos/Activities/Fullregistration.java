package com.eclat.firebreathers.epos.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.eclat.firebreathers.epos.Application.EPOSApp;
import com.eclat.firebreathers.epos.Util.CheckInternetConnection;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.eclat.firebreathers.epos.Util.Validation;
import com.eclat.firebreathers.epos.R;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Fullregistration extends AppCompatActivity {

    ImageView navigationimage;
    byte[] byteArray;
    String encodedImage, logstate, otpstatus;
    EditText name, email, mobile, address, dob, password;
    EditText oldpass, newpass, confirmpass, otpEnter;
    @Nullable
    String uname = null, uemail = null, umobile = null, uaddress = null, udob = null, uimg = null, ugender = null, Otp = null;
    RadioGroup Gender;
    RadioButton selectedGender;
    int chosendId;
    Bitmap USER_IMAGE, originBitmap = null;
    SessionManager session;
    private Calendar calendar;
    private int year, month, day;
    LinearLayout reg, resetpass;
    SharedPreferences shared;
    CheckInternetConnection netCheck;
    TextView otpstate, sendOtpAgain;
    Button verifyOtp;
    RelativeLayout otpLayout;
    Config config = new Config();
    public static Fullregistration fullregistration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullregistration = this;
        Config.exit_state_fullreg = config.fullregactivity;
      //  Config.backstack_state = config.fullregactivity;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullregistration);
        netCheck = new CheckInternetConnection(getApplicationContext());
        name = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.useremail);
        email.setEnabled(false);
        mobile = (EditText) findViewById(R.id.mobileno);
        mobile.setEnabled(false);
        dob = (EditText) findViewById(R.id.userdob);
        password = (EditText) findViewById(R.id.pwd);
        Gender = (RadioGroup) findViewById(R.id.radiogrp);
        address = (EditText) findViewById(R.id.add);
        oldpass = (EditText) findViewById(R.id.oldpass);
        newpass = (EditText) findViewById(R.id.newpwd);
        confirmpass = (EditText) findViewById(R.id.confirmpwd);
        ImageButton calbtn = (ImageButton) findViewById(R.id.calendarbtn);
        otpstate = (TextView) findViewById(R.id.verifyotp);
        otpEnter = (EditText) findViewById(R.id.otp);
        otpLayout = (RelativeLayout) findViewById(R.id.otp_layout);
        verifyOtp = (Button) findViewById(R.id.verify_otp_btn);
        reg = (LinearLayout) findViewById(R.id.reg_layout);
        sendOtpAgain = (TextView) findViewById(R.id.send_otp_again);
        resetpass = (LinearLayout) findViewById(R.id.resetpass_layout);
        calbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);
            }
        });
        calendar = Calendar.getInstance();
        year = 1990;
        month = 1;
        day = 1;
        navigationimage = (ImageView) findViewById(R.id.userimageupload);
        navigationimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage();
            }
        });
        session = new SessionManager(getApplicationContext());
        shared = getSharedPreferences("EPOS", 0);
        logstate = (shared.getString("islogin", ""));
        otpstatus = (shared.getString("key_otpstatus", ""));
        if (logstate.equals("true")) {
            name.setText(shared.getString("key_name", ""));
            uname = name.getText().toString();
            mobile.setText(shared.getString("key_mobile", ""));
            umobile = mobile.getText().toString();
            email.setText(shared.getString("key_email", ""));
            uemail = email.getText().toString();
            dob.setText(shared.getString("key_dob", ""));
            udob = dob.getText().toString();

            String chosengender = shared.getString("key_gender", "");
            if (chosengender.equals("Male"))
                Gender.check(R.id.male);
            else
                Gender.check(R.id.female);
            address.setText(shared.getString("key_address", ""));
            uaddress = address.getText().toString();
            uimg = (shared.getString("key_userimg", ""));
            USER_IMAGE = ConvertToImage(uimg);
            navigationimage.setImageBitmap(USER_IMAGE);
            Home.cartobj.drawerImage.setImageBitmap(USER_IMAGE);
            if (otpstatus.equals("1")) {
                otpstate.setText("Verified");
            } else {
                otpstate.setText("Verify Now");
                otpstate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GenerateOtpAsync genOtp = new GenerateOtpAsync();
                        genOtp.execute(umobile);
                        otp();
                    }
                });
            }
        }
        Button save = (Button) findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = name.getText().toString();
                uemail = email.getText().toString();
                umobile = mobile.getText().toString();
                udob = dob.getText().toString();
                uaddress = address.getText().toString();
                chosendId = Gender.getCheckedRadioButtonId();
                selectedGender = (RadioButton) findViewById(chosendId);
                ugender = selectedGender.getText().toString();
                if (Validation.isValidName(uname) && Validation.isEmailValid(uemail) && Validation.isPhoneNumberValid(umobile)
                        && Validation.isValidDob(udob) && Validation.isValidAddress(uaddress)) {
                    if (netCheck.isConnectingToInternet()) {
                        new SignUpAsync().execute(uname, uemail, umobile, udob, uaddress, ugender, encodedImage);

                    } else {
                        TastyToast.makeText(getApplication(), "Please Enable Internet Connection for complete Registration", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } else if (!Validation.isValidName(uname)) {
                    name.setError("Please enter the username");
                }
                if (!Validation.isEmailValid(uemail)) {
                    email.setError("Invalid Email");
                }
                if (!Validation.isPhoneNumberValid(umobile)) {
                    mobile.setError("Enter 10 digits");
                }
                if (!Validation.isValidDob(udob)) {
                    dob.setError("Please enter valid date");
                }
                if (!Validation.isValidAddress(uaddress)) {
                    address.setError("Do not leave empty");
                }
            }
        });
        Button changepwd = (Button) findViewById(R.id.edit_btn);
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg.setVisibility(View.GONE);
                resetpass.setVisibility(View.VISIBLE);
            }
        });
        Button savebtn_onchangepass = (Button) findViewById(R.id.savebutton_changepass);
        savebtn_onchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newpass.getText().toString().equals(confirmpass.getText().toString()) && newpass.length() > 6 && confirmpass.length() > 6) {
                    new Async_Changepass(oldpass.getText().toString(), newpass.getText().toString()).execute();
                    if (Config.changepasswordresponse.equals("changed")) {
                        reg.setVisibility(View.VISIBLE);
                        resetpass.setVisibility(View.GONE);
                        oldpass.setText("");
                        newpass.setText("");
                        confirmpass.setText("");
                    }

                } else
                    TastyToast.makeText(getApplicationContext(), "Entered password is mismatched or password length is too short (Min 7 Char) ", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplication().
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                //originBitmap = BitmapFactory.decodeStream(imageStream);
                originBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(imageStream), 300, 300);
                if (originBitmap != null) {
                    navigationimage.setImageBitmap(originBitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    originBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            TastyToast.makeText(getApplication(), "ERROR ", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }
    }

    public void ChooseImage() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);

        } else {
            TastyToast.makeText(getApplication(), "No activity found to perform this task", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

        }
    }

    @Override
    public void onBackPressed() {
        if (resetpass.getVisibility() == View.VISIBLE) {
            resetpass.setVisibility(View.INVISIBLE);
            reg.setVisibility(View.VISIBLE);
        } else if (Config.exit_state_fullreg.equals(config.fullregactivity)) {
                finish();
                Config.exit_state_fullreg="";
        } else {
            Intent home = new Intent(Fullregistration.this, Home.class);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    private void showDate(int year, int month, int day) {
        dob.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Nullable
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month - 1, day);
        }
        return null;
    }

    @NonNull
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2, arg3);
        }
    };

    class SignUpAsync extends AsyncTask<String, String, String> {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.FULLSIGNUP_URL;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(Fullregistration.this);
            progress.setMessage("Updating Acount Details ....");
            progress.setTitle("Loading");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            okhttp3.Response response;
            //  RequestBody body = RequestBody.create(JSON,"json");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(config.NAME, uname)
                    .add(config.EMAIL, uemail)
                    .add(config.MOBILE, umobile)
                    .add(config.DOB, udob)
                    .add(config.GENDER, ugender)
                    .add(config.ADDRESS, uaddress)
                    .add(config.IMAGE, encodedImage)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            try {
                response = call.execute();
                if (response.isSuccessful()) {
                    jsonData = response.body().string();
                } else {
                    jsonData = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
            JSONObject object = null;
            try {
                object = new JSONObject(s);
                String status = object.getString(config.RESPONSE_STATUS);
                if (status.equals(config.response_success)) {
                    TastyToast.makeText(getApplication(), "Succesfully Updated", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    Config.drawerimg = originBitmap;
                    SharedPreferences.Editor datasubmit = shared.edit();
                    datasubmit.putString("key_dob", udob);
                    datasubmit.putString("key_gender", ugender);
                    datasubmit.putString("key_address", uaddress);
                    datasubmit.putString("key_userimg",encodedImage);
                    datasubmit.apply();
                    datasubmit.commit();
                    Intent intent = new Intent(Fullregistration.this, Home.class);
                    startActivity(intent);
                } else
                    Log.d("result", s);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    //async
    public class Async_Changepass extends AsyncTask<String, Integer, String> {

        String oldpass, newpass;
        SharedPreferences shared;
        ProgressDialog progress;
        SessionManager session;
        private String url = config.HOSTURL + config.CHANGE_PASSWORD_URL;

        public Async_Changepass(String oldpass, String newpass) {
            this.oldpass = oldpass;
            this.newpass = newpass;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(Fullregistration.this);
            progress.setMessage("Please wait ....");
            progress.setTitle("Changing Password");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response = null;
            session = new SessionManager(Fullregistration.this);
            shared = getApplication().getSharedPreferences("EPOS", 0);
            String mobile = (shared.getString("key_mobile", "1"));
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(config.MOBILE, mobile)
                    .add(config.OLD_PASS, oldpass)
                    .add(config.NEW_PASS, newpass)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            try {
                response = call.execute();
                if (response.isSuccessful()) {
                    jsonData = response.body().string();
                } else {
                    jsonData = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }


        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            String status = null;
            try {
                JSONObject object = new JSONObject(jsonData);
                status = object.getString(config.RESPONSE_STATUS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (status != null) {
                if (status.equals(config.response_success)) {
                    Config.changepasswordresponse = "changed";
                    progress.dismiss();
                    TastyToast.makeText(getApplication(), "Password Changed Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);


                } else {
                    progress.dismiss();
                    TastyToast.makeText(getApplication(), jsonData, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        }
    }

    @Nullable
    public Bitmap ConvertToImage(@NonNull String uimg) {
        try {
            InputStream stream = new ByteArrayInputStream(Base64.decode(uimg.getBytes(), Base64.DEFAULT));
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(stream), 300, 300);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public void otp() {
        reg.setVisibility(View.GONE);
        navigationimage.setVisibility(View.GONE);
        otpLayout.setVisibility(View.VISIBLE);
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Otp = otpEnter.getText().toString();
                VerifyOtpAsync otpAsync = new VerifyOtpAsync();
                otpAsync.execute(Otp, umobile);

            }
        });
        sendOtpAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenerateOtpAsync genOtp = new GenerateOtpAsync();
                genOtp.execute(umobile);
            }
        });

    }

    class VerifyOtpAsync extends AsyncTask<String, String, String> {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.OTP_URL;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(Fullregistration.this);
            progress.setMessage("Submitting Form ....");
            progress.setTitle("Loading");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            okhttp3.Response response;

            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(config.OTP, Otp)
                    .add(config.LOGIN_MOBILE, umobile)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);

            try {
                response = call.execute();

                if (response.isSuccessful()) {
                    jsonData = response.body().string();
                } else {
                    jsonData = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String c) {
            super.onPostExecute(c);
            progress.dismiss();
            String status = null;
            try {
                JSONObject object = new JSONObject(c);
                status = object.getString(config.RESPONSE_STATUS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (status.equals(config.response_success)) {
                TastyToast.makeText(Fullregistration.this, c, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                otpstate.setText("Verified");
                SharedPreferences.Editor otpedit = shared.edit();
                otpedit.putString("key_otpstatus", "1");
                otpedit.apply();
                otpedit.commit();
                reg.setVisibility(View.VISIBLE);
                otpLayout.setVisibility(View.GONE);

            } else if (status.equals(config.response_serverfailed)) {
                TastyToast.makeText(Fullregistration.this, config.response_serverfailed, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            } else if (status.equals(config.response_otpmismatch)) {
                TastyToast.makeText(Fullregistration.this, config.response_otpmismatch, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            } else {
                TastyToast.makeText(Fullregistration.this, config.response_field_required, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }
    }

    class GenerateOtpAsync extends AsyncTask<String, String, String> {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.GENERATE_OTP_URL;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            okhttp3.Response response;

            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(config.LOGIN_MOBILE, umobile)
                    .build();


            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);

            try {
                response = call.execute();

                if (response.isSuccessful()) {
                    jsonData = response.body().string();
                } else {
                    jsonData = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(@Nullable String c) {
            super.onPostExecute(c);
            if (c != null) {
                TastyToast.makeText(Fullregistration.this, c, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            } else
                TastyToast.makeText(Fullregistration.this, c, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        EPOSApp.freeMemory();
    }
}