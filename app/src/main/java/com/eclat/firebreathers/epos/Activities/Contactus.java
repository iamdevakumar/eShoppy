package com.eclat.firebreathers.epos.Activities;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.widget.TextView;

import com.eclat.firebreathers.epos.Application.EPOSApp;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Contactus extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Config config=new Config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        TextView foo = (TextView) findViewById(R.id.address);
        foo.setText(Html.fromHtml(getString(R.string.Address)));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String add = "Eclat Technologies";
        LatLng Eclat = new LatLng(11.941483, 79.809140);
        mMap.addMarker(new MarkerOptions().position(Eclat).title(add));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Eclat));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        EPOSApp.freeMemory();
    }
}
