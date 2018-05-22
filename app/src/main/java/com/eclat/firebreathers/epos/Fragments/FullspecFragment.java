package com.eclat.firebreathers.epos.Fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eclat.firebreathers.epos.Adapters.Fullspec_List_Adapter;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;

/**
 * Created by Eclat-PC on 9/20/2016.
 */
public class FullspecFragment extends Fragment {
    Fullspec_List_Adapter adapter;
    ListView listView;
    Config config=new Config();
    FragmentTransaction fragmentTransaction;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Config.backstack_fullspec=config.fullspecfrag;
        View view = inflater.inflate(R.layout.fragment_fullspec, container, false);
        listView=(ListView)view.findViewById(R.id.fullspec);
        adapter=new Fullspec_List_Adapter(getActivity());
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
