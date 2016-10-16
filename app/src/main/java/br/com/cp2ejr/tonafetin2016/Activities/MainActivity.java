package br.com.cp2ejr.tonafetin2016.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import br.com.cp2ejr.tonafetin2016.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CallbackManager callbackManager;
    TextView tvUserName, tvCountDown;
    ImageView ivUserFacebook;

    FrameLayout layout;

    android.app.FragmentManager fragmentManager;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Teste git
        fragmentManager = getFragmentManager();
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        int id = item.getItemId();
        layout = (FrameLayout) findViewById(R.id.main_frame);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            layout.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AboutFragment()).commit();

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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
