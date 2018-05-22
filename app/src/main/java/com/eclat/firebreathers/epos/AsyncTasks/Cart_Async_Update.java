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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by AndroidPC-1 on 09-11-2016.
 */

public class Cart_Async_Update extends AsyncTask<String, Integer, String> {
    Config config = new Config();
    private Context context;
    String user_id, product_id, price = null;
    int qty;
    SessionManager session;
    private String url = config.HOSTURL + config.CART_QTY_UPDATE;
    public Cart_Async_Update(Context ctx, String product_id, int qty) {
        this.context = ctx;
        this.product_id = product_id;
        this.qty = qty;
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
        user_id = (shared.getString("key_userid", "1"));
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add(config.LOGIN_USERID, user_id)
                .add(config.PRODUCT_ID, product_id)
                .add(config.QTY, Integer.toString(qty))
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
