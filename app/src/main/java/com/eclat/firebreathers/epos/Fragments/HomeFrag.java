package com.eclat.firebreathers.epos.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eclat.firebreathers.epos.Activities.Home;
import com.eclat.firebreathers.epos.Activities.Offers;
import com.eclat.firebreathers.epos.Adapters.CarousalviewadsAdapter;
import com.eclat.firebreathers.epos.Adapters.Maincategory_adapter;
import com.eclat.firebreathers.epos.Adapters.RecyclerAdapter;
import com.eclat.firebreathers.epos.AsyncTasks.HomeMainCategoryAsync;
import com.eclat.firebreathers.epos.AsyncTasks.TrendingAsync;
import com.eclat.firebreathers.epos.Interface.OnTaskCompleted;
import com.eclat.firebreathers.epos.Interface.Trendinginterface;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.Modelclass;
import com.eclat.firebreathers.epos.R;
import com.eclat.firebreathers.epos.Util.autoscrollviewpager.AutoScrollViewPager;
import com.synnapps.carouselview.CarouselView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFrag extends Fragment implements OnTaskCompleted, Trendinginterface, ViewPager.OnPageChangeListener{
    CarouselView carouselView;
    int length, len, len1;
    AutoScrollViewPager pager;

    CarousalviewadsAdapter carousalviewAds;
    Config config = new Config();
    @NonNull
    List<String> imgurls = new ArrayList<>();
    @NonNull
    List<String> catimgurl = new ArrayList<>();
    @NonNull
    List<String> catname = new ArrayList<>();
    @NonNull
    List<String> cat_id = new ArrayList<>();
    @NonNull
    List<String> trendingsrc = new ArrayList<>();
    @NonNull
    OkHttpClient client = new OkHttpClient();
    private RecyclerView trendRecyclerView, category_recycleview;
    LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    @NonNull
    Modelclass mClass = new Modelclass();
    Maincategory_adapter mAdapter;
    ImageView more_maincat;
    EditText search;
    Button clrtxt, viewall;
    public int dotsCount;
    public ImageView[] dots;

    public LinearLayout pager_indicator;

    @Override
    public void onResume() {
        super.onResume();
        Config.fragtag = "HomeFragment";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new CarouselAdsAsync().execute(config.HOSTURL + config.Get_Advertisement);
        new HomeMainCategoryAsync(getActivity(), this).execute();
        new TrendingAsync(getActivity(), this).execute();
        Config.backstack_state = config.homefrag;
        getActivity().setTitle("EPOS");
        View view = inflater.inflate(R.layout.fragment_home_new, container, false);
        pager = (AutoScrollViewPager) view.findViewById(R.id.ad_pager);
        search = (EditText) view.findViewById(R.id.searchbox);
        clrtxt = (Button) view.findViewById(R.id.cleartextbtn);
        viewall = (Button) view.findViewById(R.id.all);
        clrtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
            }
        });
        //Carouselview for Main Category products
       /* carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setPageCount(3);
        carouselView.setImageListener(imageListener);*/

        //Listview for Main Category products
        category_recycleview = (RecyclerView) view.findViewById(R.id.cathorizontalview);
        category_recycleview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        category_recycleview.setLayoutManager(layoutManager);
        mAdapter = new Maincategory_adapter(getActivity(), catimgurl, catname, cat_id);
        category_recycleview.setAdapter(mAdapter);
        pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        pager.setOnPageChangeListener(this);


        //Offer view
        LinearLayout offerlay = (LinearLayout) view.findViewById(R.id.offerlayout);
        offerlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.backstack_state2 = config.homefrag;
                Intent intent = new Intent(getActivity(), Offers.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        //Trending view
        trendRecyclerView = (RecyclerView) view.findViewById(R.id.horizontal_recycler_view);
        trendRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trendRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(getActivity(), trendingsrc, mClass);
        trendRecyclerView.setAdapter(adapter);
        more_maincat = (ImageView) view.findViewById(R.id.more_cat);
        more_maincat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.backstack_state2 = config.homefrag;
                ((Home) getActivity()).goTo(Home.category);
            }
        });
        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @NonNull
    public String getJSONDATA(@NonNull String url, @NonNull String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private void setPageViewIndicator() {

        // String[] color={Config.colorcode};
        Log.d("###setPageViewIndicator", " : called");


        dotsCount = carousalviewAds.getCount();
        dots = new ImageView[dotsCount];
        for (int i = 0; i <dotsCount; i++) {

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            dots[i].setColorFilter(R.drawable.selecteditem_dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            final int presentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pager.setCurrentItem(presentPosition);
                    return true;
                }

            });
            pager_indicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        Log.d("###onPageSelected, pos ", String.valueOf(position));
        for (int i = 0; i <dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

    }

    @Override
    public void onPageSelected(final int position) {

        dots[position].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pager.setCurrentItem(position);
            }
        });
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //AsyncHTTPtask
    public class CarouselAdsAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String data = null;
            try {
                data = getJSONDATA(config.HOSTURL + config.Get_Advertisement, "");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(@Nullable String responseJsondata) {
            if (responseJsondata != null) {
                try {
                    JSONObject object = new JSONObject(responseJsondata);
                    String status = object.getString(config.RESPONSE_STATUS);
                    Log.d("Trending Status", status);
                    if (status.equals(config.response_success)) {
                        Config.LOGIN_STATUS = "true";
                        try {
                            String data = object.getString(config.RESPONSE_DATA);
                            Log.d("Trending Data", data);
                            JSONArray arr = new JSONArray(data);
                            len = arr.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject object1 = arr.getJSONObject(i);
                                imgurls.add(config.HOSTURL + config.IMAGE_PATH_URL + object1.getString(config.IMAGEURL));
                            }
                            length = imgurls.size();
                            carousalviewAds = new CarousalviewadsAdapter(getActivity(),imgurls);
                            pager.setAdapter(carousalviewAds);
                            pager.setScrollFactgor(5);
                            pager.setOffscreenPageLimit(4);
                            pager.startAutoScroll(2000);
                            setPageViewIndicator();

                           /* carousalviewAds.notifyDataSetChanged();*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Interface for main category
    @Override
    public void onTaskCompleted(@Nullable String responseJson) {
        if (responseJson != null) {
            try {
                JSONObject object = new JSONObject(responseJson);
                String status = object.getString(config.RESPONSE_STATUS);
                Log.d("Trending Status", status);
                if (status.equals(config.response_success)) {
                    Config.LOGIN_STATUS = "true";
                    try {
                        String data = object.getString(config.RESPONSE_DATA);
                        Log.d("Trending Data", data);
                        JSONArray arr = new JSONArray(data);
                        len = arr.length();
                        if (len <= 4) {
                            for (int i = 0; i < len; i++) {
                                JSONObject object1 = arr.getJSONObject(i);
                                catimgurl.add(config.HOSTURL + config.IMAGE_PATH_URL + object1.getString(config.IMAGEURL));
                                catname.add(object1.getString(config.CAT_NAME));
                                cat_id.add(object1.getString(config.CATEGORY_ID));
                            }


                        } else {
                            for (int i = 0; i < 4; i++) {
                                JSONObject object1 = arr.getJSONObject(i);
                                catimgurl.add(config.HOSTURL + config.IMAGE_PATH_URL + object1.getString(config.IMAGEURL));
                                catname.add(object1.getString(config.CAT_NAME));
                                cat_id.add(object1.getString(config.CATEGORY_ID));
                            }
                        }
                        mAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

        // Trending interface
        @Override
        public void trendinginterface (@Nullable String responseJson){
            if (responseJson != null) {
                try {
                    JSONObject object = new JSONObject(responseJson);
                    String status = object.getString(config.RESPONSE_STATUS);
                    Log.d("Trending Status", status);
                    if (status.equals(config.response_success)) {
                        Config.LOGIN_STATUS = "true";
                        try {
                            String data = object.getString(config.RESPONSE_DATA);
                            Log.d("Trending Data", data);
                            JSONArray arr = new JSONArray(data);
                            len1 = arr.length();
                            if (len1 <= 6) {
                                for (int i = 0; i < len1; i++) {
                                    JSONObject arrayobject = arr.getJSONObject(i);
                                    mClass.setTpproid(arrayobject.getString(config.PRODUCT_ID), i);
                                    mClass.setTpname(arrayobject.getString(config.NAME), i);
                                    mClass.setTpprice(arrayobject.getString(config.NETPRICE), i);
                                    mClass.setTpimg(arrayobject.getString(config.PRO_THUMBURL), i);
                                    mClass.setTpsubcatid(arrayobject.getString(config.SUBCATID), i);
                                    trendingsrc.add("");
                                }
                            } else {
                                for (int i = 0; i < 6; i++) {
                                    JSONObject arrayobject = arr.getJSONObject(i);
                                    mClass.setTpproid(arrayobject.getString(config.PRODUCT_ID), i);
                                    mClass.setTpname(arrayobject.getString(config.NAME), i);
                                    mClass.setTpprice(arrayobject.getString(config.NETPRICE), i);
                                    mClass.setTpimg(arrayobject.getString(config.PRO_THUMBURL), i);
                                    mClass.setTpsubcatid(arrayobject.getString(config.SUBCATID), i);
                                    trendingsrc.add("");
                                }

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
        }
    }







