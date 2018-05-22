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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eclat.firebreathers.epos.Activities.Home;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;

import java.util.ArrayList;
import java.util.List;


public class Maincategory_adapter extends RecyclerView.Adapter<Maincategory_adapter.Holder> {
    Context ctx;
    ImageView imgView;
    TextView name;
    Holder viewHolder;
    List<String> catimgurl = new ArrayList<>();
    List<String> catname = new ArrayList<>();
    List<String> cat_id = new ArrayList<>();
    Config config = new Config();

    public Maincategory_adapter(Context ctx, List<String> catimgurl, List<String> catname, List<String> cat_id) {
        super();
        this.ctx = ctx;
        this.catimgurl = catimgurl;
        this.catname = catname;
        this.cat_id = cat_id;

    }

    @Override
    public Maincategory_adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_maincategory, null);
        viewHolder = new Holder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Maincategory_adapter.Holder viewHolder, final int position) {

        Glide.with(ctx)
                .load(catimgurl.get(position))
                // .thumbnail(0.1f)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .into(imgView);
        name.setText(catname.get(position));
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Home) ctx).goTo(Home.subcategory_frag);
                Config.backstack_state2 = config.hometosub;
                Config.categoryid = cat_id.get(position);
                Config.categoryname = catname.get(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return catimgurl.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        public Holder(@NonNull View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.category_name);
            imgView = (ImageView) view.findViewById(R.id.category_img);
            linearLayout = (LinearLayout) view.findViewById(R.id.homemaincat);
        }
    }

}


