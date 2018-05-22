package com.eclat.firebreathers.epos.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 10/4/2016.
 */

public class CarousalviewadsAdapter extends PagerAdapter {
    Context context;
    List<String> imageurl = new ArrayList<>();
    LayoutInflater layoutInflater;
    Config config=new Config();

    public CarousalviewadsAdapter(@NonNull Context context, List<String> imgurls) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageurl = imgurls;
    }

    @Override
    public int getCount() {
        if (imageurl != null) {
            return imageurl.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.adapter_carousal_ads, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.carouseladsview);
        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
            }
        });*/
        Glide.with(context)
                .load(imageurl.get(position))
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}