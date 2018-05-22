package com.eclat.firebreathers.epos.Activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.eclat.firebreathers.epos.Adapters.CartAdapter;
import com.eclat.firebreathers.epos.Application.EPOSApp;
import com.eclat.firebreathers.epos.AsyncTasks.Cart_Async;
import com.eclat.firebreathers.epos.Interface.Cartupdaterinterface;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.eclat.firebreathers.epos.R;
import com.eclat.firebreathers.epos.Util.Utils2;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Cart extends AppCompatActivity implements Cartupdaterinterface {
    RecyclerView mRecyclerView;
    private CartAdapter adapter;
    CoordinatorLayout bottombtns;
    Button checkoutbtn;
    @NonNull
    List<String> proimgurl = new ArrayList<>();
    @NonNull
    List<String> proimgname = new ArrayList<>();
    @NonNull
    List<String> proprice = new ArrayList<>();
    @NonNull
    List<String> proid = new ArrayList<>();
    List<Integer> qty = new ArrayList<>();
    SessionManager session;
    @Nullable
    String uname, uimg;
    @Nullable
    Bitmap USER_IMAGE;
    @Nullable
    String logstate;
    int procount;
    int cartcount;
    private RecyclerView.LayoutManager mLayoutManager;
    LinearLayout registeruser;
    RelativeLayout emptycart;
    public static Cart cart;
    Config config = new Config();
    SharedPreferences shared;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cart = this;
        Config.backstack_state4 = config.cartactivity;
        // Config.exit_state_cart = config.cartactivity;
        super.onCreate(savedInstanceState);
        shared = getSharedPreferences("EPOS", 0);
        logstate = (shared.getString("islogin", ""));
        cartcount = Integer.parseInt(Config.cartcount);
        setContentView(R.layout.app_bar_navigatin_cart);
        registeruser = (LinearLayout) findViewById(R.id.reg_user);
        emptycart = (RelativeLayout) findViewById(R.id.empty_mode);
        bottombtns=(CoordinatorLayout)findViewById(R.id.mLlayoutBottomButtons);
        checkoutbtn=(Button)findViewById(R.id.paybtn);
        new Async_Cart().execute();
        mRecyclerView = (RecyclerView) findViewById(R.id.cartrecyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(Cart.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new CartAdapter(Cart.this, proimgname, proimgurl, proprice, proid, qty);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                bottombtns.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    bottombtns.setVisibility(View.INVISIBLE);
                else if (dy < 0)
                    bottombtns.setVisibility(View.VISIBLE);
            }
        });
        checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        setTitle("Cart");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {

        /*else if(Config.backstack_state.equals(config.cartactivity)&&Config.backstack_state2.equals(config.homefrag)){
            Config.backstack_state2="";
            Intent intent=new Intent(Cart.this,Home.class);
            intent.putExtra("goto",config.homefrag);
            startActivity(intent);
            this.finish();
        }*/  if (Config.backstack_state4.equals(config.cartactivity)) {
            Config.backstack_state4="";
            finish();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_cart, menu);
        MenuItem cart = menu.findItem(R.id.action_cart_cart);
        LayerDrawable icon = (LayerDrawable) cart.getIcon();
        Utils2.setBadgeCount(this, icon, cartcount);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    public void updatecount(int count) {
        cartcount = count;
        invalidateOptionsMenu();
    }

    @Override
    public void cartupdaterinterface(String jsonData) {
        SharedPreferences.Editor datasubmit;
        if (jsonData != null) {
            JSONObject jsonObject = null;
            try {
                Config.imageurl.clear();
                jsonObject = new JSONObject(jsonData);
                String data = jsonObject.getString("count");
                datasubmit = shared.edit();
                datasubmit.putString("key_cartcount", data);
                datasubmit.apply();
                datasubmit.commit();
                this.invalidateOptionsMenu();
                TastyToast.makeText(getApplication(), data, Toast.LENGTH_SHORT, TastyToast.SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //async showing all cart
    class Async_Cart extends AsyncTask<String, Integer, String> {

        @Nullable
        String user_id;
        SessionManager session;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.GET_CART_URL;


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
            user_id = (shared.getString("key_userid", "1"));
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
        protected void onPostExecute(@Nullable String jsonData) {
            super.onPostExecute(jsonData);
            if (jsonData != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonData);
                    String status = jsonObject.getString(config.RESPONSE_STATUS);
                    String data = jsonObject.getString(config.RESPONSE_DATA);
                    JSONArray dataarray = new JSONArray(data);
                    procount = dataarray.length();
                    for (int i = 0; i < dataarray.length(); i++) {
                        jsonObject = dataarray.optJSONObject(i);
                        proimgurl.add(config.HOSTURL + config.IMAGE_PATH_URL + jsonObject.getString(config.IMAGEURL));
                        proimgname.add(jsonObject.getString(config.PRODUCT_NAME));
                        proprice.add(jsonObject.getString(config.PRICE));
                        proid.add(jsonObject.getString(config.PRODUCT_ID));
                        qty.add((jsonObject.getInt(config.QTY)));
                    }
                    adapter.notifyDataSetChanged();
                    assert logstate != null;
                    if (logstate.equals("true") && procount > 0) {
                        registeruser.setVisibility(View.VISIBLE);
                    } else {
                        registeruser.setVisibility(View.GONE);
                        emptycart.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void dataSetNotify() {

        adapter.notifyDataSetChanged();
    }

    public void cartdelasynccall() {
        new Cart_Async(getApplication(), Config.cartdelproducts, 0).execute();
    }

    @Nullable
    public Bitmap ConvertToImage(@NonNull String uimg) {
        try {
            InputStream stream = new ByteArrayInputStream(Base64.decode(uimg.getBytes(), Base64.DEFAULT));
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cartdelasynccall();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Glide.get(getApplication()).clearMemory();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        EPOSApp.freeMemory();
    }
}



