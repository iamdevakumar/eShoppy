package com.eclat.firebreathers.epos.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eclat.firebreathers.epos.R;

import java.util.ArrayList;
import java.util.List;

public class Offer_Adapter extends RecyclerView.Adapter<Offer_Adapter.ViewHolder> {
    List<String> offerimgurl = new ArrayList<>();
    List<String> offerid = new ArrayList<>();
    List<String> offerproid = new ArrayList<>();
    Context ctx;

    public Offer_Adapter(Context ctx, List<String> offerimgurl, List<String> offerid, List<String> offerproid) {
        this.offerimgurl = offerimgurl;
        this.offerid = offerid;
        this.offerproid = offerproid;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_offers, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(ctx)
                .load(offerimgurl.get(position))
                .thumbnail(0.1f)
                .placeholder(R.drawable.placeholder)
                .override(600, 200)
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .into(holder.offerimg);
    }

    @Override
    public int getItemCount() {
        return offerimgurl.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView offerimg;

        public ViewHolder(@NonNull View view) {
            super(view);
            offerimg = (ImageView) view.findViewById(R.id.offerimgview);
        }
    }

}