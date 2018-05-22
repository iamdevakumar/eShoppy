package com.eclat.firebreathers.epos.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;

/**
 * Created by Eclat-PC on 10/5/2016.
 */


public class Fullspec_List_Adapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public Fullspec_List_Adapter(@NonNull Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        int size = Config.specname.size();
        return size;
    }

    @Nullable
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View adapterview = layoutInflater.inflate(R.layout.adapter_spec, null);
        TextView keytext = (TextView) adapterview.findViewById(R.id.key);
        TextView valtext = (TextView) adapterview.findViewById(R.id.value);
        keytext.setText(Config.specname.get(i));
        valtext.setText(Config.specval.get(i));
        return adapterview;
    }
}


