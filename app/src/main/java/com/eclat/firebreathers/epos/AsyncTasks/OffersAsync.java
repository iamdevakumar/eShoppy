package com.eclat.firebreathers.epos.AsyncTasks;


import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.eclat.firebreathers.epos.Interface.Offersinterface;
import com.eclat.firebreathers.epos.Modeclasses.Config;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OffersAsync extends AsyncTask<String, Integer, String> {

    private Context context;
    Config config = new Config();
    private Offersinterface listener;
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String url = config.HOSTURL + config.OFFER_URL;

    public OffersAsync(Context ctx, Offersinterface listener) {
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
        Response response = null;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, "json");
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
        listener.offersinterface(jsonData);
    }
}


