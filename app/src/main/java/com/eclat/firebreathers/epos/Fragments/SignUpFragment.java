package com.eclat.firebreathers.epos.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eclat.firebreathers.epos.Activities.LoginActivity;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;
import com.eclat.firebreathers.epos.Util.CheckInternetConnection;
import com.eclat.firebreathers.epos.Util.Validation;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Eclat-PC on 9/21/2016.
 */
public class SignUpFragment extends Fragment {
    Button register, verifyOtp;
    LinearLayout registerLayout;
    Config config = new Config();
    public static RelativeLayout otpLyout;
    EditText name, email, mobile, password;
    static EditText otpEnter;
    String Name, Email, Mobile, Password, Otp;
    TextView sendOtpAgain, alreadylogin;
    Validation val;
    TextView time;
    CheckInternetConnection netCheck;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Config.backstack_state = config.signupfrag;
        Config.optlayoutstate_onsignup = 0;
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        register = (Button) view.findViewById(R.id.register_button);
        registerLayout = (LinearLayout) view.findViewById(R.id.register_layout);
        otpLyout = (RelativeLayout) view.findViewById(R.id.otp_layout);
        otpEnter = (EditText) view.findViewById(R.id.otpsignup);
        verifyOtp = (Button) view.findViewById(R.id.verify_otp_btn);
        sendOtpAgain = (TextView) view.findViewById(R.id.send_otp_again);
        name = (EditText) view.findViewById(R.id.user_name);
        email = (EditText) view.findViewById(R.id.email_id);
        mobile = (EditText) view.findViewById(R.id.mobile_no);
        password = (EditText) view.findViewById(R.id.pwd);
        time = (TextView) view.findViewById(R.id.timer);
        val = new Validation();
        netCheck = new CheckInternetConnection(getActivity());
        alreadylogin = (TextView) view.findViewById(R.id.alreadylogin);
        alreadylogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getContext()).goTo(LoginActivity.loginfrag);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = name.getText().toString();
                Email = email.getText().toString();
                Mobile = mobile.getText().toString();
                Password = password.getText().toString();

                if (Validation.isValidName(Name) && Validation.isEmailValid(Email) && Validation.isPhoneNumberValid(Mobile)
                        && Validation.isValidPassword(Password)) {
                    if (netCheck.isConnectingToInternet()) {
                        submitForm();
                    } else {
                        err();
                    }
                } else if (Name.length()>3)
                {
                    if (!Validation.isValidName(Name)) {
                    name.setError("Special Characters are not allowed");
                }
                }
                else name.setError("Atleast 3 characters need");

                if (!Validation.isEmailValid(Email)) {
                    email.setError("Invalid Email");
                }
                if (!Validation.isPhoneNumberValid(Mobile)) {
                    mobile.setError("Enter 10 digits");
                }
                if (!Validation.isValidPassword(Password)) {
                    password.setError("Enter atleast 7 characters");
                }
            }
        });
        return view;
    }

    private void successotp() {
        ((LoginActivity) getContext()).goTo(LoginActivity.loginfrag);
    }

    private void submitForm() {
        SignUpAsync async = new SignUpAsync();
        async.execute(Name, Email, Mobile, Password);
    }

    public void err() {
        TastyToast.makeText(getActivity(), "Please Enable Internet Connection for complete Registration", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
    }

    class SignUpAsync extends AsyncTask<String, String, String> {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String status;
        private String url = config.HOSTURL + config.SIGNUP_URL;
        byte[] byteArray;
        String encodedImage;
        ProgressDialog progress;
        OkHttpClient client;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
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
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.userplaceholder);
            icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            RequestBody body = new FormBody.Builder()
                    .add(config.NAME, Name)
                    .add(config.EMAIL, Email)
                    .add(config.MOBILE, Mobile)
                    .add(config.PASSWORD, Password)
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
            if (s != null) {
                try {
                    JSONObject object = new JSONObject(s);
                    status = object.getString(config.RESPONSE_STATUS);
                    if (status.equals(config.response_success)) {
                        TastyToast.makeText(getActivity(), config.response_success, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        registerLayout.setVisibility(View.GONE);
                        otpLyout.setVisibility(View.VISIBLE);
                        Config.optlayoutstate_onsignup = 1;
                        otpEnter.setFocusable(true);
                        new CountDownTimer(25000, 1000) {
                            @SuppressLint("SetTextI18n")
                            public void onTick(long millisUntilFinished) {
                                time.setText("Time: " + millisUntilFinished / 1000);

                                if (Config.msg != null) {
                                    otpEnter.setText(Config.msg);
                                    Otp = otpEnter.getText().toString();
                                    VerifyOtpAsync otpAsync = new VerifyOtpAsync();
                                    otpAsync.execute(Otp, Mobile);
                                    cancel();
                                }
                            }
                            public void onFinish() {
                                otpEnter.setFocusable(true);
                            }
                        }.start();
                        verifyOtp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Otp = otpEnter.getText().toString();
                                VerifyOtpAsync otpAsync = new VerifyOtpAsync();
                                otpAsync.execute(Otp, Mobile);
                            }
                        });

                        sendOtpAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GenerateOtpAsync genOtp = new GenerateOtpAsync();
                                genOtp.execute(Mobile);
                                new CountDownTimer(25000, 1000) {
                                    @SuppressLint("SetTextI18n")
                                    public void onTick(long millisUntilFinished) {
                                        time.setText("Time: " + millisUntilFinished / 1000);

                                        if (Config.msg != null) {
                                            otpEnter.setText(Config.msg);
                                            Otp = otpEnter.getText().toString();
                                            VerifyOtpAsync otpAsync = new VerifyOtpAsync();
                                            otpAsync.execute(Otp, Mobile);
                                            cancel();
                                        }
                                    }
                                    public void onFinish() {
                                        otpEnter.setFocusable(true);
                                        otpEnter.setClickable(true);
                                    }
                                }.start();
                            }
                        });
                    } else if (status.equals(config.response_msgfailed)) {
                        TastyToast.makeText(getActivity(), config.response_msgfailed, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (status.equals(config.response_alreadyreg)) {
                        TastyToast.makeText(getActivity(), config.response_alreadyreg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        TastyToast.makeText(getActivity(), config.response_serverfailed, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class VerifyOtpAsync extends AsyncTask<String, String, String> {
        String status;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.OTP_URL;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
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
                    .add(config.MOBILE, Mobile)
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

            try {
                JSONObject object = new JSONObject(c);
                status = object.getString(config.RESPONSE_STATUS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (status.equals(config.response_success)) {
                TastyToast.makeText(getActivity(), c, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                successotp();
            } else if (status.equals(config.response_serverfailed)) {
                TastyToast.makeText(getActivity(), config.response_serverfailed, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            } else if (status.equals(config.response_otpmismatch)) {
                TastyToast.makeText(getActivity(), config.response_otpmismatch, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            } else {
                TastyToast.makeText(getActivity(), config.response_field_required, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                    .add(config.LOGIN_MOBILE, Mobile)
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
                Log.d("result", c);
                TastyToast.makeText(getActivity(), c, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

            } else
                Log.d("result", "no result recieved");

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Config.msg = null;
    }
}



