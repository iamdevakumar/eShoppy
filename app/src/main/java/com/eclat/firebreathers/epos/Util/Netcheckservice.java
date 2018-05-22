package com.eclat.firebreathers.epos.Util;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by AndroidPC-1 on 16-11-2016.
 */
public class Netcheckservice extends IntentService {
   public boolean isNetworkConnected;
    public Netcheckservice(String name) {
        super(name);
    }

    @Override
        protected void onHandleIntent(Intent intent) {
            Bundle extras = intent.getExtras();
            isNetworkConnected = extras.getBoolean("isNetworkConnected");

        }

    }


