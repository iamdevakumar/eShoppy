package com.eclat.firebreathers.epos.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.eclat.firebreathers.epos.Fragments.Product_All_Frag;
import com.eclat.firebreathers.epos.Fragments.Product_Offer_Frag;
import com.eclat.firebreathers.epos.Fragments.Product_Trending_Frag;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Product_All_Frag tab0 = new Product_All_Frag();
                return tab0;

            case 1:
                Product_Trending_Frag tab1 = new Product_Trending_Frag();
                return tab1;

            case 2:
                Product_Offer_Frag tab2 = new Product_Offer_Frag();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}