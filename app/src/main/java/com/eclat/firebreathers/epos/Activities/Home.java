package com.eclat.firebreathers.epos.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.LayerDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eclat.firebreathers.epos.Application.EPOSApp;
import com.eclat.firebreathers.epos.Fragments.Category_Frag;
import com.eclat.firebreathers.epos.Fragments.ColorsGallery;
import com.eclat.firebreathers.epos.Fragments.FullspecFragment;
import com.eclat.firebreathers.epos.Fragments.HomeFrag;
import com.eclat.firebreathers.epos.Fragments.LoginFragment;
import com.eclat.firebreathers.epos.Fragments.Product_All_Frag;
import com.eclat.firebreathers.epos.Fragments.Product_Base_Frag;
import com.eclat.firebreathers.epos.Fragments.Product_Offer_Frag;
import com.eclat.firebreathers.epos.Fragments.Product_Trending_Frag;
import com.eclat.firebreathers.epos.Fragments.SignUpFragment;
import com.eclat.firebreathers.epos.Fragments.Single_product_frag;
import com.eclat.firebreathers.epos.Fragments.Subcategory_Frag;
import com.eclat.firebreathers.epos.Interface.TransactionListner;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.eclat.firebreathers.epos.R;
import com.eclat.firebreathers.epos.Util.Utils2;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.readystatesoftware.viewbadger.BadgeView;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

;

public class Home extends AppCompatActivity implements TransactionListner, NavigationView.OnNavigationItemSelectedListener {
    @NonNull
    public static String homefrag = "homefrag";
    public static String wishlist = "wishlist";
    @NonNull
    public static String loginfrag = "loginfrag";
    public static Home cartobj;
    @NonNull
    public static String category = "categoryfrag";
    @NonNull
    public static String color = "colorfrag";
    @NonNull
    public static String signupfrag = "signupfrag";
    @NonNull
    public static String colorsgallery = "colorsgalleryfrag";
    @NonNull
    public static String prduct_all_frag = "product_all_frag";
    @NonNull
    public static String product_base_frag = "product_base_frag";
    @NonNull
    public static String product_offer_frag = "product_offer_frag";
    @NonNull
    public static String product_trending_frag = "product_trending_frag";
    @NonNull
    public static String single_product_frag = "single_product_frag";
    @NonNull
    public static String subcategory_frag = "subcategory_frag";
    @NonNull
    public static String fullspecfrag = "fullspecfrag";
    public MaterialSearchView searchView;
    public ImageView drawerImage;
    FrameLayout container;
    RelativeLayout relativeLayouthome;
    LinearLayout linearLayouthomesearch;
    SessionManager session;
    SharedPreferences shared;
    int cartcount;
    @Nullable
    String uname, uimg;
    @Nullable
    Bitmap USER_IMAGE;
    @Nullable
    String logstate;
    @Nullable
    String val = null, pro_id = null, backstack = null, backstack1 = null;
    String refresh = null, indentstring = null;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    ListView listView;
    Config config = new Config();
    private RecyclerView mRecyclerView;
    String cart;
    @Override
    protected void onResume() {
        super.onResume();
        if (Config.drawerimg != null) {
            drawerImage.setImageBitmap(Config.drawerimg);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cartobj = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        container = (FrameLayout) findViewById(R.id.frag_container);
        relativeLayouthome = (RelativeLayout) findViewById(R.id.layouthome);
        linearLayouthomesearch = (LinearLayout) findViewById(R.id.layoutsearch);
        listView = (ListView) findViewById(R.id.searchlist);
        setTitle("EPOS");
        shared = getSharedPreferences("EPOS", 0);
        logstate = (shared.getString("islogin", ""));
        Log.d("logstate", logstate);
        cart = (shared.getString("key_cartcount", "0"));
        Log.d("Cart count:", cart);
        //intend
        Intent i = getIntent();
        pro_id = i.getStringExtra("pro_id");
        val = i.getStringExtra("Fragment");
        backstack = i.getStringExtra("backstack");
        backstack1 = i.getStringExtra("backstack1");
        refresh = i.getStringExtra("proallrefresh");
        indentstring = i.getStringExtra("goto");
        if (i.hasExtra("goto")) {
            if (indentstring.equals(config.homefrag)) {
                goTo(homefrag);
            } else goTo(indentstring);

        } else if (i.hasExtra("proallrefresh") && refresh.equals(product_base_frag)) {
            goTo(product_base_frag);
        } else if (i.hasExtra("backstack") && !backstack.equals("")) {
            if (backstack.equals(config.single_product_frag))
                goTo(config.single_product_frag);
            else
                goTo(config.product_base_frag);
        } else if (i.hasExtra("backstack1") && !backstack1.equals("")) {
            if (backstack1.equals(config.product_offer_frag) || (backstack1.equals(config.product_all_frag)) || (backstack1.equals(config.product_trending_frag)))
                goTo(config.product_base_frag);
            else
                goTo(backstack1);
        } else if (i.hasExtra("pro_id") && pro_id != null && val.equals("Single_Frag")) {
            Config.GET_PROID_FROM_RECYCLER = pro_id;
            goTo(single_product_frag);
        } else if ((i.hasExtra("Fragment") && val.equals("Category_Fragment"))) {
            goTo(category);
        } else {
            goTo(homefrag);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_cart);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);
        drawerImage = (ImageView) headerView.findViewById(R.id.drawer_image);
        TextView drawerUsername = (TextView) headerView.findViewById(R.id.drawer_username_cart);
//Preferences
        if (logstate.equals("true")) {
            uname = (shared.getString("key_name", ""));
            uimg = (shared.getString("key_userimg", ""));
            Log.d("Login Username", uname);
            USER_IMAGE = ConvertToImage(uimg);
            drawerImage.setImageBitmap(USER_IMAGE);
            drawerUsername.setText(uname);
            if (Config.cartcount.equals(cart))
                cartcount = Integer.parseInt(Config.cartcount);
            else
                cartcount = Integer.parseInt(cart);


        } else
            drawerUsername.setText(config.drawername);
        if (Config.drawerimg != null) {
            drawerImage.setImageBitmap(Config.drawerimg);
        }
        final android.app.FragmentManager fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new android.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int count = fm.getBackStackEntryCount();
                if (count == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
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
                            goTo(homefrag);
                        }
                    });
                    alertDialog.show();

                }

            }
        });
//EOF
        //Search bar
        searchView = (MaterialSearchView) findViewById(R.id.search_view_tool);
        // searchView.setVoiceSearch(true);
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                if (TextUtils.isEmpty(newText)) {
                    listView.clearTextFilter();
                } else {
                    listView.setFilterText(newText);
                }
                return true;

            }
        });


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic

            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic

            }
        });
    }

    public void updatecount(int count) {
        cartcount = count;
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("key_cartcount", String.valueOf(cartcount));
        editor.apply();
        editor.commit();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        Log.d("Backstack state 1", Config.backstack_state);
        Log.d("Backstack state 2", Config.backstack_state2);
        Log.d("Backstack state 3", Config.backstack_state3);
        Log.d("Backstack state 4", Config.backstack_state4);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (Config.backstack_color.equals(config.colorsgallery)) {
            Config.backstack_color = "";
            goTo(single_product_frag);
        } else if (Config.backstack_fullspec.equals(config.fullspecfrag)) {
            Config.backstack_fullspec = "";
            goTo(single_product_frag);
        } else if (Config.backstack_state2.equals(config.wishlistactivity) && Config.backstack_state.equals(config.single_product_frag)) {
            Intent wishlist = new Intent(Home.this, Wishlist.class);
            startActivity(wishlist);
            Config.backstack_state2 = "";
            finish();

        } else if (Config.backstack_state3.equals(config.wishlistactivity) && Config.backstack_state2.equals(config.single_product_frag)) {

            if (Config.backstack_state.equals(config.single_product_frag) && Config.backstack_state2.equals(config.single_product_frag)) {
                if (Config.backstack_proid.size() != 0) {
                    Config.GET_PROID_FROM_RECYCLER = Config.backstack_proid.get(Config.backstack_proid.size() - 1);
                    Config.backstack_proid.remove(Config.backstack_proid.size() - 1);
                    goTo(single_product_frag);
                } else if (Config.backstack_proid.size() == 0) {
                    Intent wishlist = new Intent(Home.this, Wishlist.class);
                    Config.backstack_state = "";
                    Config.backstack_state2 = "";
                    Config.backstack_state3 = "";
                    Config.backstack_proid.clear();
                    startActivity(wishlist);
                    finish();
                }
            }

        } else if (Config.backstack_state.equals(config.categoryfrag) && Config.backstack_state2.equals(config.homefrag)) {
            goTo(homefrag);
            Config.backstack_state2 = "";
        } else if (Config.backstack_state.equals(config.subcategory_frag) && Config.backstack_state2.equals(config.hometosub)) {
            goTo(homefrag);
            Config.backstack_state2 = "";
        } else if (Config.backstack_state.equals(config.subcategory_frag) && Config.backstack_state2.equals(config.categoryfrag)) {
            goTo(category);
            Config.backstack_state2 = "";
        } else if ((Config.backstack_state.equals(config.product_all_frag) || Config.backstack_state.equals(config.product_trending_frag) || Config.backstack_state.equals(config.product_offer_frag)) && Config.backstack_state2.equals(config.subcategory_frag)) {
            goTo(subcategory_frag);
            Config.backstack_state2 = "";
        } else if (Config.backstack_state.equals(config.single_product_frag) && Config.backstack_state2.equals(config.product_base_frag)) {
            goTo(product_base_frag);
            Config.backstack_state2 = "";
        } else if (Config.backstack_state.equals(config.single_product_frag) && Config.backstack_state3.equals(config.homefrag)) {
            if (Config.backstack_proid.size() != 0) {
                Config.GET_PROID_FROM_RECYCLER = Config.backstack_proid.get(Config.backstack_proid.size() - 1);
                Config.backstack_proid.remove(Config.backstack_proid.size() - 1);
                goTo(single_product_frag);
            } else if (Config.backstack_proid.size() == 0) {
                Config.backstack_state3 = "";
                goTo(homefrag);
            }
        } else if (Config.backstack_state.equals(config.single_product_frag) && Config.backstack_state2.equals(config.single_product_frag)) {
            if (Config.backstack_proid.size() != 0) {
                Config.GET_PROID_FROM_RECYCLER = Config.backstack_proid.get(Config.backstack_proid.size() - 1);
                Config.backstack_proid.remove(Config.backstack_proid.size() - 1);
                goTo(single_product_frag);
            } else if (Config.backstack_proid.size() == 0) {
                Config.backstack_state2 = "";
                goTo(product_base_frag);
            }
        } else if (Config.backstack_state.equals(config.single_product_frag)) {
            goTo(config.product_base_frag);
        } else if (Config.backstack_state.equals(config.product_all_frag)) {
            goTo(config.subcategory_frag);
        } else if (Config.backstack_state.equals(config.product_offer_frag)) {
            goTo(config.subcategory_frag);
        } else if (Config.backstack_state.equals(config.product_trending_frag)) {
            goTo(config.subcategory_frag);
        } else if (Config.backstack_state.equals(config.product_base_frag)) {
            goTo(config.subcategory_frag);
        } else if (Config.backstack_state.equals(config.subcategory_frag)) {
            goTo(config.categoryfrag);
        } else if (Config.backstack_state.equals(config.categoryfrag)) {
            goTo(config.homefrag);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.tool_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        MenuItem cart = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) cart.getIcon();
        Utils2.setBadgeCount(this, icon, cartcount);
        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (logstate.equals("true")) {
                    // Config.backstack_state2=config.homefrag;
                    Intent i = new Intent(Home.this, Cart.class);
                    startActivity(i);

                } else
                    TastyToast.makeText(getApplicationContext(), "Please login as registered user", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                return false;
            }
        });
        MenuItem notification = menu.findItem(R.id.action_notification);
        notification.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                TastyToast.makeText(getApplicationContext(), "No recent notification ", TastyToast.LENGTH_SHORT, TastyToast.INFO);

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
            Intent homeintent = new Intent(Home.this, Home.class);
            Config.backstack_state = "";
            Config.backstack_state2 = "";
            Config.backstack_state3 = "";
            Config.backstack_proid.clear();
            homeintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeintent);
            finish();
        }
        if (id == R.id.category) {
            Config.backstack_state = "";
            Config.backstack_state2 = "";
            Config.backstack_state3 = "";
            Config.backstack_proid.clear();
            goTo(category);
        } else if (id == R.id.offers) {
            Config.backstack_state2 = Config.backstack_state;
            Config.backstack_state3 = "";
            Config.backstack_proid.clear();
            Intent intent = new Intent(this, Offers.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.wishlist) {
            if (logstate.equals("true")) {
                Intent wishlist = new Intent(Home.this, Wishlist.class);
                //Config.backstack_state = "";
                Config.backstack_state2 = config.homefrag;
                Config.backstack_state3 = "";
                Config.backstack_state4 = "";
                Config.backstack_proid.clear();
                startActivity(wishlist);
                finish();
            } else
                TastyToast.makeText(getApplicationContext(), "Please SignUp...", TastyToast.LENGTH_SHORT, TastyToast.WARNING);

        } else if (id == R.id.myorder) {
        } else if (id == R.id.myaccount) {
            if (logstate.equals("true")) {
                Intent myaccount = new Intent(Home.this, Fullregistration.class);
                myaccount.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myaccount.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myaccount);

            } else {
                Intent signup = new Intent(Home.this, LoginActivity.class);
                signup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                signup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(signup);
                finish();
            }
            if (Config.backstack_state2.equals(config.single_product_frag) && Config.backstack_state.equals(config.single_product_frag)) {
                Config.backstack_proid.clear();
            }
        } else if (id == R.id.contactus) {
            Intent contactus = new Intent(this, Contactus.class);
            startActivity(contactus);
            if (Config.backstack_state2.equals(config.single_product_frag) && Config.backstack_state.equals(config.single_product_frag)) {
                Config.backstack_proid.clear();
            }
        } else if (id == R.id.logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
            alertDialog.setTitle("Logout Confirmation");
            alertDialog.setMessage("Are you sure you want to logout?");
            alertDialog.setIcon(R.drawable.logout);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    session = new SessionManager(getApplicationContext());
                    session.logoutUser();
                    Config.backstack_state = "";
                    Config.backstack_state2 = "";
                    Config.backstack_state3 = "";
                    Config.backstack_proid.clear();
                    Home.this.finish();

                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(@NonNull DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } else if (id == R.id.exit) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // conver base64 to image
    @Nullable
    public Bitmap ConvertToImage(@NonNull String uimg) {
        try {
            InputStream stream = new ByteArrayInputStream(Base64.decode(uimg.getBytes(), Base64.DEFAULT));
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(stream), 300, 300);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    //For fragment transaction
    @Override
    public void goTo(@NonNull String frag_name) {
        if (frag_name.equals(homefrag)) {
            HomeFrag hf = new HomeFrag();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, hf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (frag_name.equals(wishlist)) {
            Intent intent = new Intent(Home.this, Wishlist.class);
            startActivity(intent);
        } else if (frag_name.equals(signupfrag)) {
            SignUpFragment sf = new SignUpFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, sf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (frag_name.equals(loginfrag)) {
            LoginFragment lf = new LoginFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, lf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (frag_name.equals(category)) {
            Category_Frag cf = new Category_Frag();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, cf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (frag_name.equals(subcategory_frag)) {
            Subcategory_Frag sf = new Subcategory_Frag();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, sf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (frag_name.equals(fullspecfrag)) {
            FullspecFragment fsf = new FullspecFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, fsf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (frag_name.equals(colorsgallery)) {
            ColorsGallery cgf = new ColorsGallery();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, cgf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (frag_name.equals(single_product_frag)) {
            Single_product_frag spf = new Single_product_frag();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, spf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (frag_name.equals(product_base_frag)) {
            Product_Base_Frag pbf = new Product_Base_Frag();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, pbf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (frag_name.equals(prduct_all_frag)) {
            Product_All_Frag paf = new Product_All_Frag();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, paf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (frag_name.equals(product_offer_frag)) {
            Product_Offer_Frag pof = new Product_Offer_Frag();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, pof);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (frag_name.equals(product_trending_frag)) {
            Product_Trending_Frag pdf = new Product_Trending_Frag();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_container, pdf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        EPOSApp.freeMemory();
    }
}
