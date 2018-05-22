package com.eclat.firebreathers.epos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eclat.firebreathers.epos.Activities.Home;
import com.eclat.firebreathers.epos.Activities.Wishlist;
import com.eclat.firebreathers.epos.AsyncTasks.Wishlist_Del_Async;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.eclat.firebreathers.epos.R;

import java.util.ArrayList;
import java.util.List;

public class Wishlist_Adapter extends RecyclerView.Adapter<Wishlist_Adapter.ViewHolder> {

    List<String> proimgname = new ArrayList<>();
    List<String> proimgurl = new ArrayList<>();
    List<String> proprice = new ArrayList<>();
    List<String> proid = new ArrayList<>();
    int delflagstate=0;
    String user_id;
    @NonNull
    Context ctx;
    SessionManager session;
    Config config = new Config();


    public Wishlist_Adapter(Context ctx, List<String> proimgname, List<String> proimgurl, List<String> proprice, List<String> proid) {
        this.proid = proid;
        this.proimgname = proimgname;
        this.proimgurl = proimgurl;
        this.proprice = proprice;

        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_wishlist, parent, false);
        session = new SessionManager(ctx);
        SharedPreferences shared = ctx.getSharedPreferences("EPOS", 0);
        user_id = (shared.getString("key_userid", "1"));
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(ctx)
                .load(proimgurl.get(position))
                .thumbnail(0.1f)
                .placeholder(R.drawable.placeholder)
                .override(600, 200)
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .into(holder.proimg);
        holder.proname.setText(proimgname.get(position));
        Log.d("Wishlist Products",proimgname.get(position));
        holder.proprice.setText(proprice.get(position));
        holder.delpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prodid = (proid.get(position));
                proimgurl.remove(position);
                proimgname.remove(position);
                proprice.remove(position);
                proid.remove(position);
                notifyItemRemoved(position);
                new Wishlist_Del_Async(ctx,user_id,prodid,delflagstate).execute();
                Wishlist.wishlist.dataSetNotify();
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, Home.class);
                i.putExtra("Fragment", "Single_Frag");
                i.putExtra("pro_id", proid.get(position));
                Config.productname = proimgname.get(position);
                Config.backstack_state3=config.wishlistactivity;
                Config.backstack_state2 = config.wishlistactivity;//wishlist
                ctx.startActivity(i);
                Wishlist.wishlist.finish();
            }
        });

        holder.proimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, Home.class);
                i.putExtra("Fragment", "Single_Frag");
                i.putExtra("pro_id", proid.get(position));
                Config.productname = proimgname.get(position);
                Config.backstack_state3 = config.wishlistactivity;
                Config.backstack_state2 = config.wishlistactivity;
                ctx.startActivity(i);
                Wishlist.wishlist.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return proimgurl.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView proname, proprice;
        public ImageView proimg, delpro;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View view) {
            super(view);
            proname = (TextView) view.findViewById(R.id.pro_name_wishlist);
            proprice = (TextView) view.findViewById(R.id.pro_price_wishlist);
            proimg = (ImageView) view.findViewById(R.id.pro_img_wishlist);
            delpro = (ImageView) view.findViewById(R.id.deleteproduct);
            linearLayout = (LinearLayout) view.findViewById(R.id.layout_wishlist_adapter);

        }

    }

}