package com.eclat.firebreathers.epos.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eclat.firebreathers.epos.Adapters.Offer_Adapter;
import com.eclat.firebreathers.epos.Application.EPOSApp;
import com.eclat.firebreathers.epos.AsyncTasks.OffersAsync;
import com.eclat.firebreathers.epos.Interface.Offersinterface;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.eclat.firebreathers.epos.R;
import com.eclat.firebreathers.epos.Util.Utils2;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Offers extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Offersinterface {
    private RecyclerView mRecyclerView;
    private Offer_Adapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SessionManager session;
    SharedPreferences shared;
    @NonNull
    List<String> offerimgurl = new ArrayList<>();
    @NonNull
    List<String> offerid = new ArrayList<>();
    @NonNull
    List<String> offerproid = new ArrayList<>();
    @Nullable
    String logstate;
    @Nullable
    String uname, uimg;
    @Nullable
    Bitmap USER_IMAGE;
    int len, cartcount;
    public static Offers offers;
    Config config = new Config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        offers = this;

        Config.backstack_state=config.offeractivity;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new OffersAsync(getApplicationContext(), this).execute();
        mRecyclerView = (RecyclerView) findViewById(R.id.offersrecyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new Offer_Adapter(getApplication(), offerimgurl, offerid, offerproid);
        mRecyclerView.setAdapter(adapter);
        setTitle("Offer Zone");
        shared = getSharedPreferences("EPOS", 0);
        String cart = (shared.getString("key_cartcount", "0"));
        logstate = (shared.getString("islogin", ""));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.offersdrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_cart);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);
        ImageView drawerImage = (ImageView) headerView.findViewById(R.id.navheader_image);
        TextView drawerUsername = (TextView) headerView.findViewById(R.id.navheader_username);
        if (logstate.equals("true")) {
            uname = (shared.getString("key_name", ""));
            uimg = (shared.getString("key_userimg", ""));
            USER_IMAGE = ConvertToImage(uimg);
            drawerImage.setImageBitmap(USER_IMAGE);
            drawerUsername.setText(uname);
            if (Config.cartcount.equals(cart))
                cartcount = Integer.parseInt(cart);
            else
                cartcount = Integer.parseInt(Config.cartcount);
        } else drawerUsername.setText(config.drawername);
        //EOF
       /* FragmentManager fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Offers.this);
                    alertDialog.setTitle("Exiting App Confirmation");
                    alertDialog.setMessage("Are you sure you want to Exit?");
                    alertDialog.setIcon(R.drawable.exit);
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    alertDialog.show();
                }
            }
        });*/
    }

    @Override
    public void offersinterface(@Nullable String jsonResponse) {
        if (jsonResponse != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonResponse);
                String data = jsonObject.getString(config.RESPONSE_DATA);
                JSONArray arr = new JSONArray(data);
                len = arr.length();
                for (int i = 0; i < len; i++) {
                    JSONObject object = arr.getJSONObject(i);
                    offerimgurl.add(object.getString(config.OFFER_IMAGE_URL));
                    offerid.add(object.getString(config.OFFER_ID));
                    offerproid.add(object.getString(config.PRODUCT_ID));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.offersdrawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(Config.backstack_state.equals(config.offeractivity)&&Config.backstack_state2.equals(config.wishlistactivity)){
            Config.backstack_state2="";
            Intent intent=new Intent(Offers.this,Wishlist.class);
            startActivity(intent);
            this.finish();
        }else if(Config.backstack_state.equals(config.offeractivity)&&Config.backstack_state2.equals(config.cartactivity)){
            Config.backstack_state2="";
            Intent intent=new Intent(Offers.this,Cart.class);
            startActivity(intent);
            this.finish();
        }
        else if(Config.backstack_state.equals(config.offeractivity)&&!Config.backstack_state2.equals("")){
            Intent intent=new Intent(Offers.this,Home.class);
            intent.putExtra("goto",Config.backstack_state2);
            startActivity(intent);
            this.finish();
        }else {
            finish();
        }
    }

    public void updatecount(int count) {
        cartcount = count;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.offers, menu);
        MenuItem cart = menu.findItem(R.id.action_cart_offer);
        LayerDrawable icon = (LayerDrawable) cart.getIcon();
        // Update LayerDrawable's BadgeDrawable
        Utils2.setBadgeCount(this, icon, cartcount);
        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (logstate.equals("true")) {
                    Intent i = new Intent(Offers.this, Cart.class);
                    startActivity(i);
                }else
                    TastyToast.makeText(getApplicationContext(), "Please login as registered user", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Home) {
            Intent homeintent = new Intent(Offers.this, Home.class);
            homeintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeintent);
            this.finish();
        } else if (id == R.id.category) {
            Intent i = new Intent(Offers.this, Home.class);
            i.putExtra("Fragment", "Category_Fragment");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            this.finish();
        }  else if (id == R.id.wishlist) {
            if (logstate.equals("true")) {
                Intent wishlist = new Intent(Offers.this, Wishlist.class);
                startActivity(wishlist);
            } else
                TastyToast.makeText(getApplicationContext(), "Please SignUp...", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
        } else if (id == R.id.myorder) {
        } else if (id == R.id.myaccount) {
            Intent myaccount = new Intent(Offers.this, Fullregistration.class);
            startActivity(myaccount);
        } else if (id == R.id.contactus) {
            Intent contactus = new Intent(this, Contactus.class);
            startActivity(contactus);
        } else if (id == R.id.logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Offers.this);
            alertDialog.setTitle("Logout Confirmation");
            alertDialog.setMessage("Are you sure you want to logout?");
            alertDialog.setIcon(R.drawable.logout);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    session = new SessionManager(getApplicationContext());
                    session.logoutUser();
                    Offers.this.finish();
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(@NonNull DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.offersdrawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Nullable
    public Bitmap ConvertToImage(@NonNull String uimg) {
        try {
            InputStream stream = new ByteArrayInputStream(Base64.decode(uimg.getBytes(), Base64.DEFAULT));
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        EPOSApp.freeMemory();
    }

}
