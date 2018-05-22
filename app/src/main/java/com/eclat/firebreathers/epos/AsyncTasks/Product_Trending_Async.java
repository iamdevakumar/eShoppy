package com.eclat.firebreathers.epos.AsyncTasks;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.eclat.firebreathers.epos.Interface.Product_trending_interface;
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


public class Product_Trending_Async extends AsyncTask<String, Integer, String> {
    private Context context;
    private Product_trending_interface listener;
    Config config = new Config();
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String url = config.HOSTURL + config.SINGLE_TRENDING_URL;
    ProgressDialog progress;
    SessionManager session;
    SharedPreferences shared;
    String user_id;

    public Product_Trending_Async(Context ctx, Product_trending_interface listener) {
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
        OkHttpClient client = new OkHttpClient();
        session = new SessionManager(context);
        shared = context.getSharedPreferences("EPOS", 0);
        user_id = (shared.getString("key_userid", "1"));
        RequestBody body = new FormBody.Builder()
                .add(config.CUSTOMERID, user_id)
                .add(config.SUBCATID, Config.sub_categoryid)
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
        listener.productTrendingInterface(jsonData, user_id);
    }
}


