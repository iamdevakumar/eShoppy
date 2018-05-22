package com.eclat.firebreathers.epos.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.eclat.firebreathers.epos.R;
import com.eclat.firebreathers.epos.Util.TouchImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 10/4/2016.
 */

public class Singlepagegalleryviewpager extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<String> imageurl = new ArrayList<>();
    String Imageurl;

    public Singlepagegalleryviewpager(@NonNull Context context, List<String> imageurl) {
        this.context = context;
        this.imageurl = imageurl;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return imageurl.size() ;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.gallery_image, container, false);
        TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.singleproduct_imageview);
        Glide.with(context)
                .load(imageurl.get(position))
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
        Glide.get(context).clearMemory();
    }

}