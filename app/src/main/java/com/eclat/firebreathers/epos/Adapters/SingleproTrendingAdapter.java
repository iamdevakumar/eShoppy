package com.eclat.firebreathers.epos.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclat.firebreathers.epos.Activities.Home;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.Modelclass;
import com.eclat.firebreathers.epos.R;

import java.util.ArrayList;
import java.util.List;


public class SingleproTrendingAdapter extends RecyclerView.Adapter<SingleproTrendingAdapter.ViewHolder> {
    List<String> relaventitems = new ArrayList<>();
    Context ctx;
    Modelclass mClass;
    String user_id;
    Config config = new Config();

    public SingleproTrendingAdapter(Context ctx, List<String> relaventitems, Modelclass mClass, String user_id) {
        super();
        this.ctx = ctx;
        this.relaventitems = relaventitems;
        this.mClass = mClass;
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return relaventitems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Glide.with(ctx)
                .load(mClass.getRel_img(position))
                .thumbnail(0.1f)
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(viewHolder.imgView);
        viewHolder.name.setText(mClass.getRel_name(position));
        viewHolder.price.setText("₹" + mClass.getRel_price(position));
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.backstack_state2 = config.single_product_frag;
                Config.backstack_proid.add(Config.GET_PROID_FROM_RECYCLER);
                Config.productname=mClass.getRel_name(position);
                Config.GET_PROID_FROM_RECYCLER = mClass.getRel_proid(position);
                ((Home) ctx).goTo(Home.single_product_frag);

            }
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        public TextView name, price;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemLayoutView) {
            super(itemLayoutView);
            imgView = (ImageView) itemLayoutView.findViewById(R.id.catImage);
            name = (TextView) itemLayoutView.findViewById(R.id.item_name);
            price = (TextView) itemLayoutView.findViewById(R.id.tpprice);
            linearLayout = (LinearLayout) itemLayoutView.findViewById(R.id.singlecategorytrending);
        }
    }
}


