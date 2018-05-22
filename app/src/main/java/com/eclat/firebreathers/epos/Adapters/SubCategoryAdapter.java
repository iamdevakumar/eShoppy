package com.eclat.firebreathers.epos.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eclat.firebreathers.epos.R;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends ArrayAdapter<String> {

    @NonNull
    private final Context context;
    List<String> itemname = new ArrayList<>();
    List<String> imgurl = new ArrayList<>();

    public SubCategoryAdapter(@NonNull Context context, List<String> itemname, List<String> imgurl) {
        super(context, R.layout.adpater_grid);
        this.context = context;
        this.itemname = itemname;
        this.imgurl = imgurl;
    }

    @Override
    public int getCount() {
        return imgurl.size();
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(String item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adpater_grid, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView2);

        Glide.with(context)
                .load(imgurl.get(position))
                // .thumbnail(0.1f)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(imageView);
        txtTitle.setText(itemname.get(position));
        return rowView;
    }
}