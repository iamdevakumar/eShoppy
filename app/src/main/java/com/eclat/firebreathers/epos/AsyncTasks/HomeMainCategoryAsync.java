package com.eclat.firebreathers.epos.AsyncTasks;


import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.eclat.firebreathers.epos.Interface.OnTaskCompleted;
import com.eclat.firebreathers.epos.Modeclasses.Config;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Acer on 9/14/2016.
 */

public class HomeMainCategoryAsync extends AsyncTask<String, Integer, String> {

    private Context context;
    private OnTaskCompleted listener;
    Config config = new Config();
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String url = config.HOSTURL + config.Get_Category;

    public HomeMainCategoryAsync(Context ctx, OnTaskCompleted listener) {
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
        listener.onTaskCompleted(jsonData);
    }
}


