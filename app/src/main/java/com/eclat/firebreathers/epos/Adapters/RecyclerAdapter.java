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


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<String> trendingsrc = new ArrayList<>();
    Context ctx;
    private Config config = new Config();
    Modelclass mClass;

    public RecyclerAdapter(Context ctx, List<String> trendingsrc, Modelclass mClass) {
        super();
        this.ctx = ctx;
        this.trendingsrc = trendingsrc;
        this.mClass = mClass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trending, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return trendingsrc.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Glide.with(ctx)
                .load(mClass.getTpimg(position))
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(viewHolder.imgView);
        viewHolder.name.setText(mClass.getTpname(position));
        viewHolder.price.setText("₹" + mClass.getTpprice(position));
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Home) ctx).goTo(Home.single_product_frag);
                Config.backstack_state3 = config.homefrag;
                Config.sub_categoryid = mClass.getTpsubcatid(position);
                Config.GET_PROID_FROM_RECYCLER = mClass.getTpproid(position);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgView;
        public TextView name, price;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemLayoutView) {
            super(itemLayoutView);
            imgView = (ImageView) itemLayoutView.findViewById(R.id.catImage);
            name = (TextView) itemLayoutView.findViewById(R.id.item_name);
            price = (TextView) itemLayoutView.findViewById(R.id.tpprice);
            linearLayout = (LinearLayout) itemLayoutView.findViewById(R.id.hometrendingrecycler);
        }
    }

}


