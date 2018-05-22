package com.eclat.firebreathers.epos.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.eclat.firebreathers.epos.Activities.Home;
import com.eclat.firebreathers.epos.Adapters.SubCategoryAdapter;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;

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

public class Subcategory_Frag extends Fragment {
    Config config = new Config();
    int len;
    @NonNull
    List<String> itemname = new ArrayList<>();
    @NonNull
    List<String> imgurl = new ArrayList<>();
    @NonNull
    List<String> subcatid = new ArrayList<>();
    SubCategoryAdapter categoryAdapter;

    @Override
    public void onResume() {
        super.onResume();
        Config.backstack_state = config.subcategory_frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MaincatAsync().execute(config.HOSTURL + config.Get_Subcat);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(Config.categoryname);
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);
        GridView gv = (GridView) view.findViewById(R.id.gridView2);
        categoryAdapter = new SubCategoryAdapter(getActivity(), itemname, imgurl);
        gv.setAdapter(categoryAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Config.backstack_state2=config.subcategory_frag;
                Config.sub_categoryid = subcatid.get(i);
                Config.subcategoryname = itemname.get(i);
                ((Home) getActivity()).goTo(Home.product_base_frag);
            }
        });
        getActivity().supportInvalidateOptionsMenu();
        return view;
    }



    //Async Task def
    public class MaincatAsync extends AsyncTask<String, Integer, String> {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.Get_Subcat;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response;

            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(config.CAT_ID, Config.categoryid)
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
                try {
                    JSONObject object = new JSONObject(jsonData);
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
                                imgurl.add(config.HOSTURL + config.IMAGE_PATH_URL + object1.getString(config.IMAGEURL));
                                itemname.add(object1.getString(config.CAT_NAME));
                                subcatid.add(object1.getString(config.CATEGORY_ID));
                            }
                            categoryAdapter.notifyDataSetChanged();
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
    @Override
    public void onPause() {
        super.onPause();
        Config.backstack_state4=config.subcategory_frag;
    }
}


