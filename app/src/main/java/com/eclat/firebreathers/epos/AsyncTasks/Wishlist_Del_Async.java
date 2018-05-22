package com.eclat.firebreathers.epos.AsyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

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

/**
 * Created by AndroidPC-1 on 12-11-2016.
 */

public class Wishlist_Del_Async extends AsyncTask<String, Integer, String> {

    Config config = new Config();
    private Context context;
    String user_id, product_id;
    int flag;
    SessionManager session;
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String url = config.HOSTURL + config.WISHLISTADDREMOVE_URL;

    public Wishlist_Del_Async(Context ctx,String user_id,String product_id, int flag) {
        context = ctx;
        this.user_id = user_id;
        this.product_id = product_id;
        this.flag = flag;

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
        session = new SessionManager(context);
        SharedPreferences shared = context.getSharedPreferences("EPOS", 0);
        user_id = (shared.getString("key_userid", ""));
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add(config.LOGIN_USERID, user_id)
                .add(config.PRODUCT_ID, product_id)
                .add(config.RESPONSE_FLAG, String.valueOf(flag))
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

    }
}

