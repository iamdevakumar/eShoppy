package com.eclat.firebreathers.epos.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eclat.firebreathers.epos.Adapters.Singlepagegalleryviewpager;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;

/**
 * Created by Eclat-PC on 10/5/2016.
 */
public class ColorsGallery extends Fragment {
    ViewPager pager;
    Config config=new Config();
    Singlepagegalleryviewpager singlepagegalleryviewpager;
    int Pos;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Config.backstack_color=config.colorsgallery;
        View view4 = inflater.inflate(R.layout.singleproductgallery, container, false);
        pager=(ViewPager)view4.findViewById(R.id.gallery);
        singlepagegalleryviewpager=new Singlepagegalleryviewpager(getActivity(),Config.imageurl);
        pager.setAdapter(singlepagegalleryviewpager);
        Log.d("colorsGallery","true");
        return view4;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}


