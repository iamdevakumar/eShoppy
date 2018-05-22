package com.eclat.firebreathers.epos.Activities;

import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.eclat.firebreathers.epos.AsyncTasks.Cart_Async;
import com.eclat.firebreathers.epos.AsyncTasks.CartcountAsync;
import com.eclat.firebreathers.epos.Modeclasses.Config;
import com.eclat.firebreathers.epos.Modeclasses.SessionManager;
import com.eclat.firebreathers.epos.R;
public class Splash extends AppCompatActivity {
    ImageView logo;
    Animation zoomin, zoomout, logoMoveAnimation, tran, fadein, fadeout,rotate, fadeinout;
    private static int splashtime = 3000;
    @NonNull
  //  public static ArrayList<AnimatorSet> animatorSetList = new ArrayList<AnimatorSet>();
    SessionManager session;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        session = new SessionManager(getApplicationContext());
        SharedPreferences shared = getSharedPreferences("EPOS", 0);
        String logstate = (shared.getString("islogin",""));
        String user_id = (shared.getString("key_userid", ""));
        Log.d("Login Status: ", logstate);
        animate();
        if (logstate.equals("")||logstate.equals("false")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    session.checkLogin();
                    finish();
                }
            }, splashtime);

        } else {
            new CartcountAsync(getApplication(),user_id).execute();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Splash.this, Home.class);
                        startActivity(intent);
                        finish();
                    }
            }, splashtime);
        }
    }

    public void animate()
    {
        logo = (ImageView) findViewById(R.id.splashlogo);
        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        tran = AnimationUtils.loadAnimation(this, R.anim.translate);
        logoMoveAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_and_translate);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        fadeinout=AnimationUtils.loadAnimation(this,R.anim.fadeinandout);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        AnimationSet s = new AnimationSet(false);
        s.addAnimation(fadeinout);
       // s.addAnimation(rotate);
        logo.startAnimation(s);
    }
}

