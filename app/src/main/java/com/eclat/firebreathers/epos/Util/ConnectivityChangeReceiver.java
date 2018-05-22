package com.eclat.firebreathers.epos.Util;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by AndroidPC-1 on 16-11-2016.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                Netcheckservice.class.getName());
        intent.putExtra("isNetworkConnected",isConnected(context));
       context.startService(intent.setComponent(comp));

    }
}
