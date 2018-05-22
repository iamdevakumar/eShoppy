package com.eclat.firebreathers.epos.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eclat.firebreathers.epos.Activities.Cart;
import com.eclat.firebreathers.epos.AsyncTasks.Cart_Async;
import com.eclat.firebreathers.epos.AsyncTasks.Cart_Async_Update;
import com.eclat.firebreathers.epos.R;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<String> proimgname = new ArrayList<>();
    List<String> proimgurl = new ArrayList<>();
    List<String> proprice = new ArrayList<>();
    List<Integer> qty=new ArrayList<>();
    List<String> proid = new ArrayList<>();
    Context ctx;
    List<Integer> items = new ArrayList<>();
    //int items;
    public CartAdapter(Context ctx, List<String> proimgname, List<String> proimgurl, List<String> proprice, List<String> proid, List<Integer> qty) {
        this.proid = proid;
        this.proimgname = proimgname;
        this.proimgurl = proimgurl;
        this.proprice = proprice;
        this.ctx = ctx;
        this.qty = qty;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(ctx)
                .load(proimgurl.get(position))
                .thumbnail(0.1f)
                .placeholder(R.drawable.placeholder)
                .override(300, 100)
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .into(holder.proimg);
        holder.proname.setText(proimgname.get(position));
        holder.proprice.setText("Rs." + proprice.get(position));
        holder.delpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String prodid = (proid.get(position));
                    proimgurl.remove(position);
                    proimgname.remove(position);
                    proprice.remove(position);
                    proid.remove(position);
                    qty.remove(position);
                    notifyItemRemoved(position);
                    new Cart_Async(ctx,prodid,8).execute();
                    Cart.cart.updatecount(proimgurl.size());
                    Cart.cart.dataSetNotify();
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Cart) ctx).finish();
                    Intent intent = new Intent(ctx, Cart.class);
                    ctx.startActivity(intent);
                }
            }
        });
        holder.item_count.setText((String.valueOf(qty.get(position))));
        //String dummy=(String.valueOf(qty.get(position)));
        items.add(position,qty.get(position));

        holder.item_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(holder.item_count.getText().toString().isEmpty()) {
                    holder.item_count.setText("1");
                    TastyToast.makeText(ctx, "you can't add product with '0' count", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                }
                items.add(position,Integer.parseInt(holder.item_count.getText().toString()));
                holder.totalprice.setText("Rs." + ((items.get(position))) * Float.parseFloat(proprice.get(position)));
            }

            @Override
            public void afterTextChanged(Editable editable) {
               /* if(holder.item_count.getText().toString().isEmpty()) {
                    holder.item_count.setText("1");
                    TastyToast.makeText(ctx, "you can't add product with '0' count", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                }
                items= Integer.parseInt(holder.item_count.getText().toString());
                holder.totalprice.setText("Rs." + items * Float.parseFloat(proprice.get(position)));*/
            }
        });

        holder.totalprice.setText("Rs." + (qty.get(position) * Float.parseFloat(proprice.get(position))));
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = items.get(position) + 1;
                qty.set(position, count);
                holder.item_count.setText((String.valueOf(qty.get(position))));
                holder.totalprice.setText("Rs." + items.get(position) * Float.parseFloat(proprice.get(position)));
                new Cart_Async_Update(ctx, proid.get(position), qty.get(position)).execute();
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = items.get(position) - 1;
                if (count == 0) {
                    count = 1;
                    qty.set(position, count);
                } else
                    qty.set(position, count);
                holder.item_count.setText((String.valueOf(qty.get(position))));
                holder.totalprice.setText("Rs." + items.get(position)* Float.parseFloat(proprice.get(position)));
                new Cart_Async_Update(ctx, proid.get(position), qty.get(position)).execute();
            }
        });


    }

    @Override
    public int getItemCount() {
        return proimgurl.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView proname, proprice,  totalprice;
        ImageView proimg, delpro;
        ImageView plus, minus;
        EditText item_count;
        android.support.v7.widget.CardView cardView;
        RelativeLayout relativeLayout;
        LinearLayout cartparent;

        public ViewHolder(@NonNull View view) {
            super(view);
            proname = (TextView) view.findViewById(R.id.pro_name_cart);
            proprice = (TextView) view.findViewById(R.id.pro_price_cart);
            cartparent = (LinearLayout) view.findViewById(R.id.cartparent);
            proimg = (ImageView) view.findViewById(R.id.pro_img_cart);
            delpro = (ImageView) view.findViewById(R.id.deleteproduct_fromcart);
            item_count = (EditText) view.findViewById(R.id.quantity);
            totalprice = (TextView) view.findViewById(R.id.pro_price_totalprice);
            plus = (ImageView) view.findViewById(R.id.plus);
            minus = (ImageView) view.findViewById(R.id.minus);
            cardView = (CardView) view.findViewById(R.id.adaptercard);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.adapter_empty_mode);
        }
    }

}
