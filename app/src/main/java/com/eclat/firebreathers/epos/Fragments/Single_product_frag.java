package com.eclat.firebreathers.epos.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.eclat.firebreathers.epos.Activities.Home;
import com.eclat.firebreathers.epos.Adapters.Single_product_viewpager_adapter;
import com.eclat.firebreathers.epos.Adapters.SingleproTrendingAdapter;
import com.eclat.firebreathers.epos.AsyncTasks.Relaventitemasync;
import com.eclat.firebreathers.epos.Interface.Relaventinterface;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.Modelclass;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.eclat.firebreathers.epos.R;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Single_product_frag extends Fragment implements Relaventinterface {
    Config config = new Config();
    ImageView cart, wishlist;
    TextView pro_name, pro_price, pro_netprice, viewmore,desc,per_offer;
    ViewPager pager;
    LinearLayout linearLayout;
    private RecyclerView relaventrecycleview;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    SessionManager session;
    String status="";
    SharedPreferences shared;
    String user_id;
    Single_product_viewpager_adapter single_product_viewpager_adapter;
    @NonNull
    Modelclass mClass = new Modelclass();
    String product_id;
    int flag, len, len1;
    Spinner spinner;
    @NonNull
    List<String> relaventitems = new ArrayList<>();
    String price;
    Boolean cartstat=true;
    Typeface typeface;
    public Single_product_frag() {
    }

    @Override
    public void onResume() {
        super.onResume();
        //Config.imageurl.clear();
        Config.specname.clear();
        Config.specval.clear();
        Config.backstack_state=config.single_product_frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared = getActivity().getSharedPreferences("EPOS", 0);
        user_id = (shared.getString("key_userid", "1"));
        new Single_pro_async().execute();
        new Relaventitemasync(getActivity(), this).execute();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<String> units=new ArrayList<>();
        units.add("100g");
        units.add("200g");
        units.add("500g");
        units.add("1kg");

        AssetManager am = getActivity().getAssets();
        typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "Play-Regular.ttf"));

        /*if (Config.backstack_state2.equals(config.wishlistactivity) && Config.backstack_state.equals(config.wishlistactivity)) {
            Config.backstack_state2 = Config.backstack_state;
        } else if (Config.backstack_state2.equals(config.single_product_frag) && Config.backstack_state.equals(config.single_product_frag)) {
            Config.backstack_state2 = config.single_product_frag;
        } else Config.backstack_state2 = config.product_base_frag;*/
        getActivity().setTitle(Config.productname);
        View view = inflater.inflate(R.layout.fragment_single_product_frag, container, false);
        pager = (ViewPager) view.findViewById(R.id.single_pro_pager);
        pro_name = (TextView) view.findViewById(R.id.productname);
        per_offer = (TextView) view.findViewById(R.id.offerpercentage);
        pro_name.setTypeface(typeface);
        spinner=(Spinner)view.findViewById(R.id.unit);
        pro_price = (TextView) view.findViewById(R.id.price);
        pro_netprice = (TextView) view.findViewById(R.id.netprice);
        wishlist = (ImageView) view.findViewById(R.id.wishlistheart);
        cart = (ImageView) view.findViewById(R.id.addtocart);

        ArrayAdapter<String> sadapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, units);
        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sadapter);


        if(status.equals("FAILED")) {
            cart.setImageResource(R.drawable.filledcart);
        }
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
                scale.setDuration(500);
                scale.setInterpolator(new OvershootInterpolator());
                cart.startAnimation(scale);
                if(Config.Cart.equals("0"))  {
                        int flag = 1;
                        new Cart_Async(Config.GET_PROID_FROM_RECYCLER, flag).execute();
                        Config.Cart="1";
                        TastyToast.makeText(getActivity(), "Added on cart", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        cart.setImageResource(R.drawable.filledcart);
                    }

                else TastyToast.makeText(getActivity(),"Already added",TastyToast.LENGTH_SHORT,TastyToast.INFO);
            }
        });
        linearLayout = (LinearLayout) view.findViewById(R.id.wishlistlayout);
        if (user_id.equals("1")) {
            wishlist.setVisibility(View.INVISIBLE);
            cart.setVisibility(View.INVISIBLE);
            linearLayout.setEnabled(false);
        }
        final Bitmap empty_bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.wishlistempty);
        final Bitmap filled_bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.wishlistfill);
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) wishlist.getDrawable();
                Bitmap current_whishlistbitmap = drawable.getBitmap();
                product_id = Config.GET_PROID_FROM_RECYCLER;
                if (empty_bitmap.sameAs(current_whishlistbitmap)) {
                    wishlist.setImageResource(R.drawable.wishlistfill);
                    flag = 1;
                    new Async_Wishlist(product_id, flag, Config.sub_categoryid).execute();
                }
                if (filled_bitmap.sameAs(current_whishlistbitmap)) {
                    wishlist.setImageResource(R.drawable.wishlistempty);
                    flag = 0;
                    new Async_Wishlist(product_id, flag, Config.sub_categoryid).execute();
                }
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) wishlist.getDrawable();
                Bitmap current_whishlistbitmap = drawable.getBitmap();
                product_id = Config.GET_PROID_FROM_RECYCLER;
                if (empty_bitmap.sameAs(current_whishlistbitmap)) {
                    wishlist.setImageResource(R.drawable.wishlistfill);
                    flag = 1;
                    new Async_Wishlist(product_id, flag, Config.sub_categoryid).execute();
                }
                if (filled_bitmap.sameAs(current_whishlistbitmap)) {
                    wishlist.setImageResource(R.drawable.wishlistempty);
                    flag = 0;
                    new Async_Wishlist(product_id, flag, Config.sub_categoryid).execute();
                }
            }
        });
        desc=(TextView)view.findViewById(R.id.description);

        viewmore = (TextView) view.findViewById(R.id.viewmore);

            viewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Config.specname.size()!=0 &Config.specval.size()!=0) {
                    if (Config.backstack_state2.equals(config.homefrag)) {
                        Config.backstack_state2 = config.homefrag;
                    } else {
                        Config.backstack_state2 = config.single_product_frag;
                    }
                    ((Home) getActivity()).goTo(Home.fullspecfrag);
                }
                    else TastyToast.makeText(getActivity(),"Specification not available",TastyToast.LENGTH_SHORT,TastyToast.INFO);
            }
            });
        relaventrecycleview = (RecyclerView) view.findViewById(R.id.singleproductrecycleview);
        relaventrecycleview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        relaventrecycleview.setLayoutManager(layoutManager);
        adapter = new SingleproTrendingAdapter(getActivity(), relaventitems, mClass, user_id);
        relaventrecycleview.setAdapter(adapter);
        return view;
    }

    @Override
    public void relaventinterface(String responseJson, String userid) {
        if (userid.equals("1")) {
            if (responseJson != null) {
                try {
                    JSONObject object = new JSONObject(responseJson);
                    String status = object.getString(config.RESPONSE_STATUS);
                    Log.d("status", status);
                    if (status.equals(config.response_success)) {
                        Config.LOGIN_STATUS = "true";
                        try {
                            String data = object.getString(config.RESPONSE_DATA);
                            Log.d("data", data);
                            JSONArray arr = new JSONArray(data);
                            len = arr.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject arrayobject = arr.getJSONObject(i);
                                mClass.setRel_proid(arrayobject.getString(config.PRODUCT_ID), i);
                                mClass.setRel_name(arrayobject.getString(config.PRODUCT_NAME), i);
                                mClass.setRel_price(arrayobject.getString(config.PRICE), i);
                                mClass.setRel_img(config.HOSTURL + config.IMAGE_PATH_URL + arrayobject.getString(config.IMAGEURL), i);
                                relaventitems.add("");
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (responseJson != null) {
                JSONObject jsonObject = null;
                try {
                    JSONObject object = new JSONObject(responseJson);
                    String status = object.getString(config.RESPONSE_STATUS);
                    Log.d("status", status);
                    try {
                        String data = object.getString(config.RESPONSE_DATA);
                        Log.d("data", data);
                        JSONArray arr1 = new JSONArray(data);
                        len1 = arr1.length();
                        for (int i = 0; i < len1; i++) {
                            JSONObject arrayobject = arr1.getJSONObject(i);
                            mClass.setRel_proid(arrayobject.getString(config.PRODUCT_ID), i);
                            mClass.setRel_name(arrayobject.getString(config.PRODUCT_NAME), i);
                            mClass.setRel_price(arrayobject.getString(config.PRICE), i);
                            mClass.setRel_img(config.HOSTURL + config.IMAGE_PATH_URL + arrayobject.getString(config.IMAGEURL), i);
                            mClass.setRel_flag(arrayobject.getString(config.RESPONSE_FLAG), i);
                            relaventitems.add("");
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    public class Single_pro_async extends AsyncTask<String, Integer, String> {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.SINGLE_PRODUCT_URL;
        ProgressDialog progress;
        JSONObject jsonObject, jsonObject1 = null,jsonObject2 = null,jsonObject3 = null;
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getContext());
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
            RequestBody body = new FormBody.Builder()
                    .add(config.LOGIN_USERID, user_id)
                    .add(config.PRODUCT_ID, Config.GET_PROID_FROM_RECYCLER)
                  //  .add(config.CAT_ID, Config.sub_categoryid)
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
            if (jsonData != null) {

                try {
                    Config.imageurl.clear();
                    jsonObject = new JSONObject(jsonData);
                    String status = jsonObject.getString(config.RESPONSE_STATUS);
                    String data = jsonObject.getString(config.RESPONSE_DATA);
                   String img = jsonObject.getString(config.RESPONSE_IMAGES);
                    JSONArray dataarray = new JSONArray(data);
                    JSONArray imgarray = new JSONArray(img);
                    jsonObject1 = dataarray.optJSONObject(0);
                    Config.Title=(jsonObject1.getString(config.PRODUCT_NAME));
                    Config.Description=(jsonObject1.getString(config.PRODUCT_DESC));
                    Config.Price=(jsonObject1.getString(config.PRICE));
                    Config.imageurl.add(config.HOSTURL + config.IMAGE_PATH_URL + jsonObject1.getString(config.IMAGEURL));
                    Config.Flag=(jsonObject1.getString(config.RESPONSE_FLAG));
                    Config.Cart=(jsonObject1.getString(config.CART_FLAG));

                        for (int i = 1; i < dataarray.length(); i++) {
                            jsonObject2 = dataarray.optJSONObject(i);
                            Log.d("Null Check",jsonObject2.getString(config.SPEC_NAME));
                            if(!jsonObject2.getString(config.SPEC_NAME).equals("null")) {
                            Config.specname.add(jsonObject2.getString(config.SPEC_NAME));
                            Config.specval.add(jsonObject2.getString(config.SPEC_VALUE));
                        }
                    }
                    if(imgarray.length()>0) {
                        for (int j = 0; j < imgarray.length(); j++) {
                            jsonObject3 = imgarray.optJSONObject(j);
                            Config.imageurl.add(config.HOSTURL + config.IMAGE_PATH_URL + jsonObject3.getString(config.IMAGEURL));
                        }
                    }
                    single_product_viewpager_adapter = new Single_product_viewpager_adapter(getActivity(), Config.imageurl);
                    pager.setAdapter(single_product_viewpager_adapter);
                    single_product_viewpager_adapter.notifyDataSetChanged();
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    pro_name.setText(Config.Title);
                    pro_price.setText("₹" + Config.Price);
                    pro_price.setPaintFlags(pro_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    pro_netprice.setText("₹" + Config.Price);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i == 0) {
                                Float newprice = Float.valueOf(Config.Price) * 1;
                                pro_netprice.setText("₹" + newprice);

                            } else if (i == 1) {
                                Float off = (Float.valueOf(Config.Price) * 2)*(5/100);
                                Float newprice = (Float.valueOf(Config.Price) * 2)-off;
                                pro_netprice.setText("₹" + newprice);
                                per_offer.setText("5%");
                            } else if (i == 2) {
                                Float off = (Float.valueOf(Config.Price) * 5)*(7/100);
                                Float newprice = (Float.valueOf(Config.Price) * 5)-off;
                                pro_netprice.setText("₹" + newprice);
                                per_offer.setText("7%");
                            }
                            else if (i == 3) {
                                Float off = (Float.valueOf(Config.Price) * 10)*(10/100);
                                Float newprice = (Float.valueOf(Config.Price) * 10)-off;
                                pro_netprice.setText("₹" + newprice);
                                per_offer.setText("10%");
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    if(Config.Cart.equals("1"))
                    {
                        cart.setImageResource(R.drawable.filledcart);
                    }
                    desc.setText(Config.Description);
                    if (!user_id.equals("")) {
                        if (Config.Flag.equals("1")) {
                            wishlist.setImageResource(R.drawable.wishlistfill);
                        } else {
                            wishlist.setImageResource(R.drawable.wishlistempty);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

// WISHLIST

    class Async_Wishlist extends AsyncTask<String, Integer, String> {
        @Nullable
        String user_id, product_id = null, subcat_id = null;
        int flag;
        SessionManager session;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.WISHLISTADDREMOVE_URL;

        public Async_Wishlist(String product_id, int flag, String subcat_id) {
            this.product_id = product_id;
            this.flag = flag;
            this.subcat_id = subcat_id;
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
            session = new SessionManager(getContext());
            SharedPreferences shared = getContext().getSharedPreferences("EPOS", 0);
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

    //async
    class Cart_Async extends AsyncTask<String, Integer, String> {
        private Context context;
        SharedPreferences shared;
        @Nullable
        String user_id, product_id;
        int flag;
        SessionManager session;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.CART_ADDREMOVE_URL;

        public Cart_Async(String product_id, int flag) {
            context = getActivity();
            this.product_id = product_id;
            this.flag = flag;
            shared = context.getSharedPreferences("EPOS", 0);
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
            user_id = (shared.getString("key_userid", "1"));
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
            if (jsonData != null) {
                JSONObject jsonObject = null,jsonObject1 = null;
                try {

                    jsonObject = new JSONObject(jsonData);
                    status=jsonObject.getString("status");
                    String data = jsonObject.getString("data");
                    jsonObject1=new JSONObject(data);
                    Config.cartcount =jsonObject1.getString("count");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Home.cartobj.updatecount(Integer.parseInt(Config.cartcount));
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }
}



