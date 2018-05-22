package com.eclat.firebreathers.epos.AsyncTasks;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.eclat.firebreathers.epos.Interface.Relaventinterface;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Relaventitemasync extends AsyncTask<String, Integer, String> {

    private Context context;
    private Relaventinterface listener;
    SessionManager session;
    SharedPreferences shared;
    @Nullable
    String user_id;
    Config config = new Config();
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String url = config.HOSTURL + config.RELAVENT_URL;

    public Relaventitemasync(Context ctx, Relaventinterface listener) {
        context = ctx;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Nullable
    @Override
    protected String doInBackground(String... params) {
        String jsonData = null;
        Response response;

        OkHttpClient client = new OkHttpClient();
        session = new SessionManager(context);
        shared = context.getSharedPreferences("EPOS", 0);
        user_id = (shared.getString("key_userid", "1"));
        RequestBody body = new FormBody.Builder()
                .add(config.LOGIN_USERID, user_id)
                .add(config.PRODUCT_ID, Config.GET_PROID_FROM_RECYCLER)
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
        listener.relaventinterface(jsonData, user_id);
    }
}


