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

import com.eclat.firebreathers.epos.Adapters.Product_All_Adapter;
import com.eclat.firebreathers.epos.AsyncTasks.ProductAsync;
import com.eclat.firebreathers.epos.Interface.Productinterface_and_cartcount;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.Products;
import com.eclat.firebreathers.epos.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class Product_All_Frag extends Fragment implements Productinterface_and_cartcount {
    private RecyclerView mRecyclerView;
    Config config = new Config();
    Product_All_Adapter adapter;
    MaterialSearchView searchView;
    List<Products> productsList=new ArrayList<>();
    public String uid;
    @NonNull
    int len;

    @Override
    public void onResume() {
        Config.backstack_state = config.product_all_frag;
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Config.backstack_state = config.product_all_frag;
        View v = inflater.inflate(R.layout.fragment_all_product, container, false);
        new ProductAsync(getContext(), this).execute();
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Search bar
        searchView = (MaterialSearchView)getActivity().findViewById(R.id.search_view_tool);
//        searchView.setVoiceSearch(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
                return true;
            }
        });


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic

            }

            @Override
            public void onSearchViewClosed() {

            }
        });
        return v;
    }


    @Override
    public void productinterface(@Nullable String jsonResponse, @NonNull String user_id) {
            uid=user_id;
        if (user_id.equals("1")) {
            if (jsonResponse != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonResponse);
                    String status = jsonObject.getString(config.RESPONSE_STATUS);
                    String data = jsonObject.getString(config.RESPONSE_DATA);
                    JSONArray arr = new JSONArray(data);
                    len = arr.length();
                    for (int i = 0; i < len; i++) {
                        Products products= new Products();
                        JSONObject object = arr.getJSONObject(i);
                        products.setProimgurl(config.HOSTURL + config.IMAGE_PATH_URL + object.getString(config.IMAGEURL));
                        products.setProname(object.getString(config.PRODUCT_NAME));
                        products.setProprice(object.getString(config.PRICE));
                        products.setProid(object.getString(config.PRODUCT_ID));
                        productsList.add(products);
                    }
                    adapter = new Product_All_Adapter(getActivity(),productsList, user_id);
                    mRecyclerView.setAdapter(adapter);

                    int i = 0;
                    Log.d("Pro adapter call count:", String.valueOf(i++));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {

            if (jsonResponse != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonResponse);
                    String status = jsonObject.getString(config.RESPONSE_STATUS);
                    String data = jsonObject.getString(config.RESPONSE_DATA);
                    JSONArray arr = new JSONArray(data);
                    len = arr.length();
                    for (int i = 0; i < len; i++) {
                        Products products= new Products();
                        JSONObject object = arr.getJSONObject(i);
                        products.setProimgurl(config.HOSTURL + config.IMAGE_PATH_URL + object.getString(config.IMAGEURL));
                        products.setProname(object.getString(config.PRODUCT_NAME));
                        products.setProprice(object.getString(config.PRICE));
                        products.setProid(object.getString(config.PRODUCT_ID));
                        products.setFlags(object.getString(config.RESPONSE_FLAG));
                        productsList.add(products);
                    }
                    adapter = new Product_All_Adapter(getActivity(),productsList, user_id);
                    mRecyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void updateadapter()
    {
        adapter = new Product_All_Adapter(getContext(),productsList, uid);
        mRecyclerView.setAdapter(adapter);
    }
}


