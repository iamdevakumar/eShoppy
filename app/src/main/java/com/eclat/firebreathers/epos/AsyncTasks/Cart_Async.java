package com.eclat.firebreathers.epos.AsyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.eclat.firebreathers.epos.Activities.Cart;
import com.eclat.firebreathers.epos.Activities.Home;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.sdsmdg.tastytoast.TastyToast;

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

public class Cart_Async extends AsyncTask<String, Integer, String> {
    private Context context;
    Config config = new Config();
    @Nullable
    String user_id = null, product_id = null;
    int flag;
    SessionManager session;
    private String url = config.HOSTURL + config.CART_ADDREMOVE_URL;

    public Cart_Async(Context ctx, String product_id, int flag) {
        this.context = ctx;
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
        user_id = (shared.getString("key_userid", "1"));
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("customer_id", user_id)
                .add("product_id", product_id)
                .add("flag", String.valueOf(flag))
                .build();
        Request request = new Request.Builder()
                .url(url)
               // .url("http://www.eepos.co.in/webservice_opn/addremovecart.php?customer_id=4&product_id="++"&flag=0")
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
                Config.imageurl.clear();
                jsonObject = new JSONObject(jsonData);
                String data = jsonObject.getString("count");
                Config.cartcount = data;
                Cart.cart.updatecount(Integer.parseInt(data));
                Home.cartobj.updatecount(Integer.parseInt(data));
                TastyToast.makeText(context, data, Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                Config.cartdelproducts = "";
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}