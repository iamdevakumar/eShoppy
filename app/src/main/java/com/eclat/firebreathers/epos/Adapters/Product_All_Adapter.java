package com.eclat.firebreathers.epos.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eclat.firebreathers.epos.Activities.Home;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.Products;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.eclat.firebreathers.epos.R;
import com.eclat.firebreathers.epos.Util.Filtering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Product_All_Adapter extends RecyclerView.Adapter<Product_All_Adapter.ViewHolder> implements Filterable {
    public List<Products> productsList;
    private List<Products> forfilterproductsList =null;
    private String product_id;
    private String user_id;
       private  int flag;
    private  Context ctx;
    Config config = new Config();
    private Filtering mFilter;

    public Product_All_Adapter(Context ctx, List<Products> productsList, String user_id) {
        this.ctx = ctx;
        this.productsList=productsList;
        if(productsList!=null)
            forfilterproductsList=new ArrayList<>(productsList);
        this.user_id = user_id;
        mFilter = new Filtering(Product_All_Adapter.this,productsList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_productcard, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Products products=productsList.get(position);
        final Bitmap empty_bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.wishlistempty);
        final Bitmap filled_bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.wishlistfill);
        Glide.with(ctx)
                .load(products.getProimgurl())
                .thumbnail(0.1f)
                .placeholder(R.drawable.placeholder)
                .override(300, 100)
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .into(holder.proimg);
        holder.proname.setText(products.getProname());
        holder.proprice.setText("₹" + products.getProprice());

        if (user_id.equals("1")) {
            holder.prowishlist.setVisibility(View.INVISIBLE);
            holder.layoutImg.setEnabled(false);
        } else if (!user_id.equals("1")) {
            if (products.getFlags().equals("1")) {
                holder.prowishlist.setImageBitmap(filled_bitmap);
            } else {
                holder.prowishlist.setImageBitmap(empty_bitmap);
            }
        } else
            holder.prowishlist.setImageBitmap(empty_bitmap);


        holder.layoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.backstack_state2=config.product_base_frag;
                Config.productname = products.getProname();
                Config.GET_PROID_FROM_RECYCLER = products.getProid();
                ((Home) ctx).goTo(Home.single_product_frag);
            }
        });
        holder.layoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.backstack_state2=config.product_base_frag;
                Config.productname = products.getProname();
                Config.GET_PROID_FROM_RECYCLER = products.getProid();
                ((Home) ctx).goTo(Home.single_product_frag);

            }
        });
        holder.prowishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user_id.equals("1")) {
                    BitmapDrawable drawable = (BitmapDrawable) holder.prowishlist.getDrawable();
                    Bitmap current_whishlistbitmap = drawable.getBitmap();
                    product_id = products.getProid();
                    if (empty_bitmap.sameAs(current_whishlistbitmap)) {
                        holder.prowishlist.setImageResource(R.drawable.wishlistfill);
                        Log.d("Filled:", products.getProid());
                        products.setFlags("1");
                        flag = 1;
                        new Async_Wishlist(product_id, flag, Config.sub_categoryid).execute();
                    }
                    if (filled_bitmap.sameAs(current_whishlistbitmap)) {
                        holder.prowishlist.setImageResource(R.drawable.wishlistempty);
                        products.setFlags("0");
                        flag = 0;
                        Log.d("UnFilled:", products.getProid());
                        new Async_Wishlist(product_id, flag, Config.sub_categoryid).execute();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public Filter getFilter() {
        for(int i=0;i<productsList.size();i++)
        Log.d("Original", productsList.get(i).toString());
        for(int j=0;j<productsList.size();j++)
        Log.d("list copy", forfilterproductsList.get(j).toString());
        return mFilter;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView proname, proprice;
        public ImageView proimg, prowishlist;
        LinearLayout layoutImg, layoutContent;

        public ViewHolder(@NonNull View view) {
            super(view);
            proname = (TextView) view.findViewById(R.id.pro_name);
            proprice = (TextView) view.findViewById(R.id.pro_price);
            proimg = (ImageView) view.findViewById(R.id.pro_img);
            prowishlist = (ImageView) view.findViewById(R.id.prowishlist);
            layoutImg = (LinearLayout) view.findViewById(R.id.layoutimg);
            layoutContent = (LinearLayout) view.findViewById(R.id.layoutcontent);
        }
    }

    //async
    class Async_Wishlist extends AsyncTask<String, Integer, String> {


        private Context context;
        @Nullable
        String user_id, product_id = null, subcat_id = null;
        int flag;
        SessionManager session;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private String url = config.HOSTURL + config.WISHLISTADDREMOVE_URL;

        public Async_Wishlist(String product_id, int flag, String subcat_id) {
            context = ctx;
            this.product_id = product_id;
            this.subcat_id = subcat_id;
            this.flag = flag;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Nullable
        @Override
        protected String doInBackground(String... params) {
            String jsonData = null;
            Response response = null;
            session = new SessionManager(context);
            SharedPreferences shared = context.getSharedPreferences("EPOS", 0);
            user_id = (shared.getString("key_userid", ""));
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add(config.LOGIN_USERID, user_id)
                    .add(config.PRODUCT_ID, product_id)
                    .add(config.RESPONSE_FLAG, String.valueOf(flag))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            try {
                response = call.execute();
                if (response.isSuccessful()) {
                    jsonData = response.body().string();
                } else {
                    jsonData = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonData;
        }


        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);

        }
    }



}