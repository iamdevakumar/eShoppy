package com.eclat.firebreathers.epos.AsyncTasks;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.eclat.firebreathers.epos.Interface.Productinterface_and_cartcount;
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


public class ProductAsync extends AsyncTask<String, Integer, String> {

    private Context context;
    private Productinterface_and_cartcount listener;
    ProgressDialog progress;
    SessionManager session;
    SharedPreferences shared;
    Config config = new Config();
    @Nullable
    String user_id;
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String url = config.HOSTURL + config.Get_Product;

    public ProductAsync(Context ctx, Productinterface_and_cartcount listener) {
        context = ctx;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setMessage("Please wait ....");
        progress.setTitle("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Nullable
    @Override
    protected String doInBackground(String... params) {
        String jsonData = null;
        Response response = null;
        session = new SessionManager(context);
        shared = context.getSharedPreferences("EPOS", 0);
        user_id = (shared.getString("key_userid", "1"));
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add(config.CUSTOMERID, user_id)
                .add(config.CAT_ID, Config.sub_categoryid)
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
        progress.dismiss();
        listener.productinterface(jsonData, user_id);
    }
}


