package com.eclat.firebreathers.epos.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import org.json.JSONException;
import org.json.JSONObject;
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


public class CartcountAsync extends AsyncTask<String, Integer, String> {
    private Context context;
    Config config = new Config();
    String user_id;
    private String url = config.HOSTURL + config.CART_COUNT_URL;

    public CartcountAsync(Context ctx, String user_id) {
        this.context = ctx;
        this.user_id = user_id;
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
        RequestBody body = new FormBody.Builder()
                .add(config.LOGIN_USERID, user_id)
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
        if (jsonData != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
                String data = jsonObject.getString("count");
                Config.cartcount = data;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


