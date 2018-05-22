package com.eclat.firebreathers.epos.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eclat.firebreathers.epos.Adapters.Wishlist_Adapter;
import com.eclat.firebreathers.epos.Application.EPOSApp;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.eclat.firebreathers.epos.R;
import com.eclat.firebreathers.epos.Util.Utils2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Wishlist extends AppCompatActivity  {
    @Nullable
    String logstate;
    SessionManager session;
    @Nullable
    String uname, uimg;
    @Nullable
    Bitmap USER_IMAGE;
    private RecyclerView mRecyclerView;
    private Wishlist_Adapter adapter;
    @NonNull
    List<String> proimgurl= new ArrayList<>();
    @NonNull
    List<String> proimgname= new ArrayList<>();
    @NonNull
    List<String> proprice= new ArrayList<>();
    @NonNull
    List<String> proid= new ArrayList<>();

    LinearLayoutManager mLayoutManager;
    LinearLayout registeruser;
    RelativeLayout emptycart;
    public static List<String> temp=new ArrayList<>();
    public static List<String> tempsub=new ArrayList<>();
    public static List<String> tempproid=new ArrayList<>();
    int procount;
    public static Wishlist wishlist;
    Config config=new Config();
    SharedPreferences shared;
     String cartcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        wishlist=this;
        if(Config.backstack_state4.equals("")) {
            Config.backstack_state4 = Config.backstack_state;
        }
        if((Config.wishlistbackstacksingleproid.equals("")&&Config.wishlistbackstacksinglesubcatid.equals(""))){
            Config.wishlistbackstacksingleproid=Config.GET_PROID_FROM_RECYCLER;
            Config.wishlistbackstacksinglesubcatid=Config.sub_categoryid;
        }
        Config.backstack_state=config.wishlistactivity;
        tempproid.add(Config.GET_PROID_FROM_RECYCLER);
        tempsub.add(Config.sub_categoryid);
        temp.add(Config.backstack_state);
        Config.wishlistdelproducts="";
        Config.wishlistdelprosubcatid="";
        super.onCreate(savedInstanceState);
        setTitle("Wishlist");
        shared = getSharedPreferences("EPOS", 0);
        logstate = (shared.getString("islogin", ""));
        Log.d("logstate",logstate);
        cartcount=(shared.getString("key_cartcount","0"));
        setContentView(R.layout.app_bar_navigation_wishlist);
        new Async_complete_wishlist().execute();
        registeruser=(LinearLayout)findViewById(R.id.registered_user);
        emptycart=(RelativeLayout)findViewById(R.id.emptywishlist_mode);
        mRecyclerView = (RecyclerView)findViewById(R.id.wishlistrecyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(Wishlist.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter=new Wishlist_Adapter(Wishlist.this,proimgname,proimgurl,proprice,proid);
        mRecyclerView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }    @Override
    public void onBackPressed() {
        Log.d("wishBackstack state 1",Config.backstack_state);
        Log.d("wishBackstack state 2",Config.backstack_state2);
        Log.d("wishBackstack state 3",Config.backstack_state3);
        Log.d("wishBackstack state 4",Config.backstack_state4);
        if(!Config.backstack_state4.equals(""))
        {
            if(Config.backstack_state4.equals(config.product_all_frag)||Config.backstack_state4.equals(config.product_offer_frag)||Config.backstack_state4.equals(config.product_trending_frag)) {
                Config.backstack_state4=config.product_base_frag;
            }
            if(! Config.wishlistbackstacksingleproid.equals("")&&!Config.wishlistbackstacksinglesubcatid.equals(""))
            {
                Config.GET_PROID_FROM_RECYCLER= Config.wishlistbackstacksingleproid;
                Config.sub_categoryid=Config.wishlistbackstacksinglesubcatid;
            }
            if(Config.backstack_state4.equals(config.cartactivity))
            {
                Config.backstack_state4="";
                this.finish();
            }
                Intent intent = new Intent(Wishlist.this, Home.class);
                intent.putExtra("goto", Config.backstack_state4);
                startActivity(intent);
                Config.backstack_state4="";
                Config.wishlistbackstacksingleproid="";
                Config.wishlistbackstacksinglesubcatid="";
                this.finish();

        }

        else if (Config.backstack_state.equals(config.wishlistactivity)&& Config.backstack_state2.equals(config.single_product_frag))
        {
            Intent homeintent = new Intent(Wishlist.this, Home.class);
            homeintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeintent);
            this.finish();
            Config.backstack_state2="";

        }
        else if (Config.backstack_state.equals(config.wishlistactivity)&& Config.backstack_state2.equals(config.product_base_frag))
        {
            Intent intent=new Intent(Wishlist.this,Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("proallrefresh","product_base_frag");
            startActivity(intent);
            this.finish();
        }
        else if(Config.backstack_state2.equals(config.wishlistactivity)&&Config.backstack_state.equals(config.wishlistactivity))        {
            Intent homeintent = new Intent(Wishlist.this, Home.class);
            String temp1=temp.get(0);
            Config.sub_categoryid=Wishlist.tempsub.get(0);
            Config.GET_PROID_FROM_RECYCLER=tempproid.get(0);
            homeintent.putExtra("backstack1",temp1);
            startActivity(homeintent);
            Config.backstack_state2="";
            Wishlist.tempsub.clear();
            temp.clear();
            finish();

        }
        else if(temp.equals(config.product_all_frag)||temp.equals(config.product_trending_frag)||temp.equals(config.product_offer_frag)||temp.equals(config.single_product_frag))
        {
            Intent homeintent = new Intent(Wishlist.this, Home.class);

            homeintent.putExtra("backstack",temp.get(0));
            startActivity(homeintent);
            Config.backstack_state2="";

            finish();

        }
        else if (Config.backstack_state.equals(config.wishlistactivity))
        {
            finish();
        }else {
            finish();
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_wishlist, menu);
        MenuItem cart=menu.findItem(R.id.action_cart_wishlist);
        LayerDrawable icon = (LayerDrawable) cart.getIcon();
        Utils2.setBadgeCount(this,icon, Integer.parseInt(cartcount));
        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (logstate.equals("true")) {
                    Intent i = new Intent(Wishlist.this, Cart.class);
                    startActivity(i);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    class Async_complete_wishlist extends AsyncTask<String, Integer, String> {


        @Nullable
        String user_id;
        SessionManager session;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL+config.COMPLETE_WISHLIST_URL;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response = null;
            session = new SessionManager(getApplication());
            SharedPreferences shared = getApplication().getSharedPreferences("EPOS", 0);
            user_id = (shared.getString("key_userid", ""));
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(config.LOGIN_USERID,user_id)
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

            if (jsonData != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonData);
                    String status = jsonObject.getString(config.RESPONSE_STATUS);
                    String data = jsonObject.getString(config.RESPONSE_DATA);
                    JSONArray dataarray = new JSONArray(data);
                    procount=dataarray.length();
                    for (int i = 0; i < dataarray.length(); i++) {
                        jsonObject = dataarray.optJSONObject(i);
                        proimgurl.add(config.HOSTURL + config.IMAGE_PATH_URL + jsonObject.getString(config.IMAGEURL));
                        proimgname.add(jsonObject.getString(config.PRODUCT_NAME));
                        proprice.add(jsonObject.getString(config.PRICE));
                        proid.add(jsonObject.getString(config.PRODUCT_ID));
                    }
                    dataSetNotify();
                    if(logstate.equals("true")&& procount>0){
                        registeruser.setVisibility(View.VISIBLE);
                    }else   {
                        registeruser.setVisibility(View.GONE);
                        emptycart.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public void dataSetNotify()
    {

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        EPOSApp.freeMemory();
    }
}
