package com.eclat.firebreathers.epos.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eclat.firebreathers.epos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndroidPC-1 on 07-11-2016.
 */

public class search extends BaseAdapter {
    Context context;
    List<String> proimgname = new ArrayList<>();

    public search(Context context, List<String> proimgname) {
        this.context = context;
        this.proimgname = proimgname;
    }

    @Override
    public int getCount() {
        return proimgname.size();
    }

    @Override
    public Object getItem(int position) {
        return proimgname.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.adapter_search_home_item, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.searchviewtext);
        tvTitle.setText(proimgname.get(position));
        return view;
    }
}

