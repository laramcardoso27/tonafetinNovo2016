package br.com.cp2ejr.tonafetin2016.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import br.com.cp2ejr.tonafetin2016.Controls.HTTPControl;
import br.com.cp2ejr.tonafetin2016.Models.User;
import br.com.cp2ejr.tonafetin2016.R;

import static br.com.cp2ejr.tonafetin2016.R.id.rankingrelativelayout;
import static br.com.cp2ejr.tonafetin2016.R.id.voterelativelayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CallbackManager callbackManager;
    TextView tvUserName, tvCountDown;
    ImageView ivUserFacebook;

    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Teste git

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        callbackManager = CallbackManager.Factory.create();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main,
                navigationView, false);

        tvUserName = (TextView) headerView.findViewById(R.id.tvUserName);
        ivUserFacebook = (ImageView) headerView.findViewById(R.id.ivUserFacebook);

        navigationView.setNavigationItemSelectedListener(this);

        tvCountDown = (TextView) findViewById(R.id.contagem);


        Button btnVoteNow = (Button) findViewById(R.id.btnVoteNow);
        btnVoteNow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //vai pra tela de voto
                layout = (FrameLayout) findViewById(R.id.main_frame);
                android.app.FragmentManager fragmentManager = getFragmentManager();
                layout.setVisibility(View.INVISIBLE);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new VoteFragment()).commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        layout = (FrameLayout) findViewById(R.id.main_frame);
        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home) {
            // muda para a tela principal
            layout.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new MainFragment()).commit();

        } else if (id == R.id.nav_send) {
            // muda para o voto
           layout.setVisibility(View.INVISIBLE);
           fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new VoteFragment()).commit();

        } else if (id == R.id.nav_ranking) {
            // muda para o ranking
            layout.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new RankingFragment()).commit();

        } else if (id == R.id.nav_gallery) {
            // muda para a galeria
            layout.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new InstagramFragment()).commit();

        } else if (id == R.id.nav_schedule) {
            // muda para a programação
            layout.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ScheduleFragment()).commit();

        } else if (id == R.id.nav_help) {
            // muda para ajuda
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
