package com.eclat.firebreathers.epos.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.eclat.firebreathers.epos.Activities.Home;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eclat-PC on 10/5/2016.
 */
public class Single_product_viewpager_adapter extends PagerAdapter {
    Context context;
    List<String> imageurl = new ArrayList<>();
    LayoutInflater layoutInflater;
    Config config=new Config();
    String Imageurl;

    public Single_product_viewpager_adapter(@NonNull Context context, List<String> imageurl) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageurl = imageurl;
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
        View itemView = layoutInflater.inflate(R.layout.adapter_single_product_viewpage, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.adapterimg);
        Glide.with(context)
                .load(imageurl.get(position))
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Config.backstack_state2.equals(config.homefrag)){
                    Config.backstack_state2=config.homefrag;
                }
                else {
                    Config.backstack_state2 = config.single_product_frag;
                }
                ((Home) context).goTo(Home.colorsgallery);
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