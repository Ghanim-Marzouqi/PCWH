package com.example.pcwh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.pcwh.fragments.AboutUsFragment;
import com.example.pcwh.fragments.CameraHackInfoFragment;
import com.example.pcwh.fragments.HackRisksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // customer name and email
    String name = "", email = "", image = "";

    TextView drawer_user_name;
    TextView drawer_user_email;
    ImageView drawer_user_image;

    DrawerLayout drawer;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // show home button with title
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("About Us");
        }

        // drawer
        drawer = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navigationView = findViewById(R.id.nav_view);

        // drawer header
        View headerView = navigationView.getHeaderView(0);
        drawer_user_name = headerView.findViewById(R.id.drawer_user_name);
        drawer_user_email= headerView.findViewById(R.id.drawer_user_email);
        drawer_user_image = headerView.findViewById(R.id.iv_profile);
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // go to profile page
        drawer_user_image.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        // bottom navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_about:
                    getFragment("ABOUT_US");
                    if(getSupportActionBar() != null)
                        getSupportActionBar().setTitle("About Us");
                    return true;
                case R.id.nav_camera_hack_info:
                    getFragment("CAMERA_HACK_INFO");
                    if(getSupportActionBar() != null)
                        getSupportActionBar().setTitle("Camera Hack Info");
                    return true;
                case R.id.nav_hack_risks:
                    getFragment("HACK_RISKS");
                    if(getSupportActionBar() != null)
                        getSupportActionBar().setTitle("Hack Risks");
                    return true;
            }
            return false;
        });

        // set first selection tab
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment, new AboutUsFragment()).commit();

        // check for shared preferences
        SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
        String data = sp.getString("user", "");

        // check of there is any available data
        if(!data.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(data);

                // get user name
                name = jsonObject.getString("name");
                email = jsonObject.getString("email");
                image = jsonObject.getString("image");

                // set Drawer views
                drawer_user_name.setText(name);
                drawer_user_email.setText(email);
                Picasso.with(this)
                        .load(image)
                        .resize(96, 96)
                        .centerCrop()
                        .placeholder(R.drawable.person_placeholder)
                        .into(drawer_user_image);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }

        if(id == R.id.logout) {
            // clear Shared Preferences
            SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
            sp.edit().clear().apply();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getFragment(String fragmentName) {
        switch (fragmentName) {
            case "ABOUT_US":
                manager.beginTransaction().replace(R.id.fragment, new AboutUsFragment()).commit();
                break;
            case "CAMERA_HACK_INFO":
                manager.beginTransaction().replace(R.id.fragment, new CameraHackInfoFragment()).commit();
                break;
            case "HACK_RISKS":
                manager.beginTransaction().replace(R.id.fragment, new HackRisksFragment()).commit();
                break;
        }
    }

    private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    return true;
                case R.id.nav_share:
                    shareApp();
                    return true;
                case R.id.nav_contact_us:
                    // Contact us
                    return true;
            }
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
    };

    private void shareApp() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "This application for Camera Privacy";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "PCWH App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}