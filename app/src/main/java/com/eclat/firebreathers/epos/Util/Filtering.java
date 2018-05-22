package com.eclat.firebreathers.epos.Util;

/**
 * Created by AndroidPC-1 on 17-12-2016.
 */

import android.util.Log;
import android.widget.Filter;

import com.eclat.firebreathers.epos.Adapters.Product_All_Adapter;
import com.eclat.firebreathers.epos.Modeclasses.Products;

import java.util.ArrayList;
import java.util.List;


public class Filtering extends Filter {
    private final Product_All_Adapter mAdapter;
    private List<Products> beforefiler;
    private List<Products> afterfiler;

    public Filtering(Product_All_Adapter mAdapter, List<Products> productsList) {
        this.mAdapter = mAdapter;
        beforefiler = productsList;
        afterfiler = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        afterfiler.clear();
        final FilterResults results = new FilterResults();
        if (constraint.length() == 0) {
            afterfiler.addAll(beforefiler);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();
            Log.d("beforefilter", String.valueOf(beforefiler.size()));
           /* for (int i = 0; i < beforefiler.size(); i++) {
                Products products = beforefiler.get(i);*/
            for (final Products products : beforefiler) {

                if (products.getProname().startsWith(filterPattern) || products.getProname().contains(filterPattern)) {
                    afterfiler.add(products);
                    // }
                }
                results.values = afterfiler;
                results.count = afterfiler.size();
                return results;
            }
            results.values = afterfiler;
            results.count = afterfiler.size();
            return results;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        mAdapter.productsList.clear();
        mAdapter.productsList.addAll((List<Products>) filterResults.values);
        mAdapter.notifyDataSetChanged();
    }
}

