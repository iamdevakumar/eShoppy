package com.eclat.firebreathers.epos.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eclat.firebreathers.epos.Adapters.Product_Trending_Adapter;
import com.eclat.firebreathers.epos.AsyncTasks.Product_Trending_Async;
import com.eclat.firebreathers.epos.Interface.Product_trending_interface;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Product_Trending_Frag extends Fragment implements Product_trending_interface {
    private RecyclerView mRecyclerView;
    Config config = new Config();
    private RecyclerView.LayoutManager mLayoutManager;
    @NonNull
    List<String> proimgname = new ArrayList<>();
    @NonNull
    List<String> proimgurl = new ArrayList<>();
    @NonNull
    List<String> proprice = new ArrayList<>();
    @NonNull
    List<String> proid = new ArrayList<>();
    List<String> flags = new ArrayList<>();
    int len;

    @Override
    public void onResume() {
        super.onResume();
        Config.backstack_state = config.product_trending_frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Config.backstack_state = config.product_trending_frag;
        View v = inflater.inflate(R.layout.fragment_all_product, container, false);
        new Product_Trending_Async(getContext(), this).execute();
     /*  FloatingActionButton fab = (FloatingActionButton)v.findViewById(R.id.fab);
        FloatingActionButton  fab1 = (FloatingActionButton)v.findViewById(R.id.fab1);
        FloatingActionButton  fab2 = (FloatingActionButton)v.findViewById(R.id.fab2);*/
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return v;
    }

    @Override
    public void productTrendingInterface(String jsonData, String user_id) {
        Product_Trending_Adapter adapter;
        if (user_id.equals("1")) {
            if (jsonData != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonData);
                    String data = jsonObject.getString(config.RESPONSE_DATA);
                    JSONArray arr = new JSONArray(data);
                    len = arr.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject object = arr.getJSONObject(i);
                        proimgurl.add(object.getString(config.PRO_THUMBURL));
                        proimgname.add(object.getString(config.NAME));
                        proprice.add(object.getString(config.NETPRICE));
                        proid.add(object.getString(config.PRODUCT_ID));
                    }
                    adapter = new Product_Trending_Adapter(getContext(), proimgname, proimgurl, proprice, proid, user_id);
                    mRecyclerView.setAdapter(adapter);

                    int i = 0;
                    Log.d("Pro adapter call count:", String.valueOf(i++));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {

            if (jsonData != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonData);
                    String data = jsonObject.getString(config.RESPONSE_DATA);
                    JSONArray arr = new JSONArray(data);
                    len = arr.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject object = arr.getJSONObject(i);
                        proimgurl.add(object.getString(config.PRO_THUMBURL));
                        proimgname.add(object.getString(config.NAME));
                        proprice.add(object.getString(config.NETPRICE));
                        proid.add(object.getString(config.PRODUCT_ID));
                        flags.add(object.getString(config.RESPONSE_FLAG));
                    }
                    adapter = new Product_Trending_Adapter(getContext(), proimgname, proimgurl, proprice, proid, flags, user_id);
                    mRecyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
