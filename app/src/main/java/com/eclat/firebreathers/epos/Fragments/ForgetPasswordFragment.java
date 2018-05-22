package com.eclat.firebreathers.epos.Fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Acer on 9/21/2016.
 */
public class ForgetPasswordFragment extends Fragment {

    android.support.v4.app.FragmentTransaction fragmentTransaction;
    RelativeLayout forget, newPass;
    LinearLayout otp;
    Button sendOtp, verify, submit;
    EditText forget_pwd_mobile, otpEntered, new_pass, confirm_pass;
    String Mobile, Otp, NewPassword, ConfirmPassword;
    TextView sendOtpAgain, time;
    Config config = new Config();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Config.backstack_state = config.forgetpassfrag;
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        forget = (RelativeLayout) view.findViewById(R.id.forget_layout);
        otp = (LinearLayout) view.findViewById(R.id.otp_layout);
        newPass = (RelativeLayout) view.findViewById(R.id.change_pwd_layout);
        sendOtp = (Button) view.findViewById(R.id.send_otp_btn);
        verify = (Button) view.findViewById(R.id.verify_otp_btn);
        submit = (Button) view.findViewById(R.id.submit_btn);
        forget_pwd_mobile = (EditText) view.findViewById(R.id.forget_pwd_mobile);
        otpEntered = (EditText) view.findViewById(R.id.otp_entered);
        sendOtpAgain = (TextView) view.findViewById(R.id.send_otp_again);
        time = (TextView) view.findViewById(R.id.otptimer);
        new_pass = (EditText) view.findViewById(R.id.new_pwd);
        confirm_pass = (EditText) view.findViewById(R.id.confirm_pwd);

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mobile = forget_pwd_mobile.getText().toString();
                int lenght = Mobile.length();
                if (Mobile != null && lenght == 10) {
                    GenerateOtpAsync genOtp = new GenerateOtpAsync();
                    genOtp.execute(Mobile);
                    forget.setVisibility(View.GONE);
                    otp.setVisibility(View.VISIBLE);
                    otpEntered.setFocusable(true);
                    otpEntered.setClickable(false);
                    verify.setClickable(false);
                    new CountDownTimer(25000, 1000) {
                        @SuppressLint("SetTextI18n")
                        public void onTick(long millisUntilFinished) {
                            time.setText("Time: " + millisUntilFinished / 1000);
                            if (Config.msg != null) {
                                otpEntered.setText(Config.msg);
                                Otp = otpEntered.getText().toString();
                                VerifyOtpAsync otpAsync = new VerifyOtpAsync();
                                otpAsync.execute(Otp, Mobile);
                                cancel();
                            }
                        }

                        public void onFinish() {
                            otpEntered.setFocusable(true);
                            otpEntered.setClickable(true);
                            verify.setClickable(true);
                        }
                    }.start();
                    otpEntered.setFocusable(true);
                    otpEntered.setClickable(true);
                } else
                    TastyToast.makeText(getContext(), "Please enter valid Mobile No", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        });

        sendOtpAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mobile = forget_pwd_mobile.getText().toString();
                GenerateOtpAsync genOtp = new GenerateOtpAsync();
                genOtp.execute(Mobile);
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Otp = otpEntered.getText().toString();
                int lenght = Otp.length();
                if (Otp != null && lenght == 6) {
                    VerifyOtpAsync verOtp = new VerifyOtpAsync();
                    verOtp.execute(Otp, Mobile);
                    forget.setVisibility(View.GONE);
                    otp.setVisibility(View.VISIBLE);
                } else
                    TastyToast.makeText(getContext(), "Please enter received OTP", TastyToast.LENGTH_SHORT, TastyToast.ERROR);


            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPassword = new_pass.getText().toString();
                ConfirmPassword = confirm_pass.getText().toString();
                if (!NewPassword.isEmpty() || !ConfirmPassword.isEmpty()) {
                    if (NewPassword.equals(ConfirmPassword)) {
                        NewPasswordAsync newpass = new NewPasswordAsync();
                        newpass.execute(Mobile, NewPassword);
                    } else {
                        TastyToast.makeText(getActivity(), "Please enter the same password in both fields", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

                    }
                } else {
                    TastyToast.makeText(getActivity(), "Please enter the Password", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }
            }
        });


        return view;
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
        protected void onPostExecute(@Nullable String c) {
            super.onPostExecute(c);
            if (c != null) {
                TastyToast.makeText(getActivity(), c, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            } else
                Log.d("result", "No result recieved");
        }
    }

    class VerifyOtpAsync extends AsyncTask<String, String, String> {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.OTP_URL;

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
                    .add(config.OTP, Otp)
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

                TastyToast.makeText(getActivity(), c, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                otp.setVisibility(View.GONE);
                newPass.setVisibility(View.VISIBLE);

            } else
                Log.d("result", "No result recieved");

        }
    }

    class NewPasswordAsync extends AsyncTask<String, String, String> {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.FORGET_PASSWORD_URL;

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
                    .add(config.MOBILE, Mobile)
                    .add(config.PASSWORD, NewPassword)
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
                TastyToast.makeText(getActivity(), c, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                LoginFragment login = new LoginFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragContainer, login);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } else
                Log.d("result", "No result recieved");

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Config.msg = null;
    }

}