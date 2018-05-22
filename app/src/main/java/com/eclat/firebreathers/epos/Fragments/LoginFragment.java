package com.eclat.firebreathers.epos.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eclat.firebreathers.epos.Activities.Home;
import com.eclat.firebreathers.epos.Activities.LoginActivity;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.eclat.firebreathers.epos.Modeclasses.UserData;
import com.eclat.firebreathers.epos.R;
import com.eclat.firebreathers.epos.Util.CheckInternetConnection;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Eclat-PC on 9/20/2016.
 */
public class LoginFragment extends Fragment {
    Button signup, login;
    TextView forgetPass, reglater;
    EditText mobile, pwd;
    String Mobile, Password;
    SessionManager session;
    CheckInternetConnection netCheck;
    Config config = new Config();
    android.support.v4.app.FragmentTransaction fragmentTransaction;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Config.backstack_state = config.loginfrag;
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        netCheck = new CheckInternetConnection(getActivity());
        session = new SessionManager(getActivity().getApplicationContext());
        mobile = (EditText) view.findViewById(R.id.user_mobile);
        pwd = (EditText) view.findViewById(R.id.user_pwd);
        login = (Button) view.findViewById(R.id.login_button);
        forgetPass = (TextView) view.findViewById(R.id.forget_pwd);
        reglater = (TextView) view.findViewById(R.id.skip_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mobile = mobile.getText().toString();
                Password = pwd.getText().toString();
                if (netCheck.isConnectingToInternet()) {
                    if (!Mobile.equals("")  && (!Password.equals(""))) {
                        if (Mobile.length() == 10) {
                            LoginAsync logincheck = new LoginAsync();
                            logincheck.execute(Mobile, Password);
                        }
                        else  TastyToast.makeText(getContext(), "Please check your mobileno", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }else
                        TastyToast.makeText(getContext(), "Please fill both fields", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else
                    TastyToast.makeText(getContext(), "Please check your internet connection", TastyToast.LENGTH_SHORT, TastyToast.INFO);
            }
        });
        forgetPass = (TextView) view.findViewById(R.id.forget_pwd);
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (netCheck.isConnectingToInternet()) {
                    ((LoginActivity) getContext()).goTo(LoginActivity.forgetpassfrag);
                } else
                    TastyToast.makeText(getContext(), "Please check your internet connection", TastyToast.LENGTH_SHORT, TastyToast.INFO);

            }
        });
        signup = (Button) view.findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getContext()).goTo(LoginActivity.signupfrag);
            }
        });
        reglater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (netCheck.isConnectingToInternet()) {
                    Intent home = new Intent(getContext(), Home.class);
                    startActivity(home);
                    getActivity().finish();
                }
            }
        });
        return view;
    }

    class LoginAsync extends AsyncTask<String, String, String> {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.LOGIN_URL;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Login validation running... Please wait...");
            progress.setTitle("Login");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            okhttp3.Response response;
            OkHttpClient client = new OkHttpClient();
            //  RequestBody body = RequestBody.create(JSON,"json");
            RequestBody body = new FormBody.Builder()
                    .add(config.MOBILE, Mobile)
                    .add(config.PASSWORD, Password)
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
        protected void onPostExecute(@Nullable String jsonData) {
            super.onPostExecute(jsonData);
            String required = "Please fill all the fields";
            String failed = "Enter correct password";
            String not_reg = "Please register your mobile number";
            progress.dismiss();
            if (jsonData != null) {
                try {
                    JSONObject object = new JSONObject(jsonData);
                    String status = object.getString(config.RESPONSE_STATUS);
                    if (status.equals(config.response_field_required)) {
                        TastyToast.makeText(getContext(), required, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (status.equals(config.response_failed)) {
                        TastyToast.makeText(getContext(), failed, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (status.equals(config.response_not_registered)) {
                        TastyToast.makeText(getContext(), not_reg, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                    if (status.equals(config.response_success)) {
                        Config.LOGIN_STATUS = "true";
                        try {
                            String data = object.getString(config.RESPONSE_DATA);
                            Log.d("Login Success Data:", data);
                            JSONObject userInfo = new JSONObject(data);
                            UserData info = new UserData();
                            info.setUserName(userInfo.getString(config.LOGIN_CUST_NAME));
                            info.setUserMobile(userInfo.getString(config.LOGIN_MOBILE));
                            info.setUserEmail(userInfo.getString(config.EMAIL));
                            info.setUserID(userInfo.getString(config.LOGIN_USERID));
                            info.setUserIMG(userInfo.getString(config.LOGIN_CUST_IMG));
                            info.setUserDob(userInfo.getString(config.DOB));
                            info.setUserGender(userInfo.getString(config.GENDER));
                            info.setUserAddress(userInfo.getString(config.ADDRESS_ID));
                            info.setUserotpstatus(userInfo.getString(config.LOGIN_OTP_STATUS));
                            info.setUsercartcount(userInfo.getString(config.LOGIN_CART_COUNT));
                            Config.name = info.getUserName();
                            Config.mobile = info.getUserMobile();
                            Config.email = info.getUserEmail();
                            Config.userid = info.getUserID();
                            Log.d("USERID",Config.userid);
                            Config.userimg = info.getUserIMG();
                            Config.dob = info.getUserDob();
                            Config.gender = info.getUserGender();
                            Config.address = info.getUserAddress();
                            Config.otpstatus = info.getUserotpstatus();
                            Config.cartcount = info.getUsercartcount();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        session.createLoginSession(Config.name, Config.mobile, Config.email, Config.userid, Config.userimg, Config.LOGIN_STATUS, Config.dob, Config.gender, Config.address, Config.otpstatus, Config.cartcount);
                        Intent home = new Intent(getContext(), Home.class);
                        startActivity(home);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
            TastyToast.makeText(getActivity(),"Server busy try again after sometimes",TastyToast.LENGTH_SHORT,TastyToast.INFO);
                Log.d("result", "no result recieved");
        }
    }
}
