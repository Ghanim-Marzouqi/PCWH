package com.example.pcwh;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.pcwh.fragments.AboutUsFragment;
import com.example.pcwh.fragments.CameraHackInfoFragment;
import com.example.pcwh.fragments.HackRisksFragment;
import com.example.pcwh.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    // customer name and email
    TextView drawer_user_name;
    TextView drawer_user_email;
    ImageView drawer_user_image;

    DrawerLayout drawer;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    FragmentManager manager;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

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

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // drawer
        drawer = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navigationView = findViewById(R.id.nav_view);

        // drawer header
        View headerView = navigationView.getHeaderView(0);
        drawer_user_name = headerView.findViewById(R.id.drawer_user_name);
        drawer_user_email = headerView.findViewById(R.id.drawer_user_email);
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
    }

    @Override
    protected void onStart() {
        // Get logged in user from Firebase Authentication
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null && !firebaseUser.getUid().isEmpty()) {
            DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
            docRef.get().addOnCompleteListener(dbTask -> {
                if (dbTask.isSuccessful()) {
                    DocumentSnapshot document = dbTask.getResult();
                    assert document != null;
                    if (document.exists()) {
                        // Instantiate logged in user
                        User user = document.toObject(User.class);

                        String name = user.getName();
                        String email = user.getEmail();
                        String imageUrl = user.getImageUrl();

                        // set Drawer views
                        drawer_user_name.setText(name);
                        drawer_user_email.setText(email);

                        if (!imageUrl.isEmpty()) {
                            Picasso.with(MainActivity.this)
                                    .load(imageUrl)
                                    .resize(96, 96)
                                    .centerCrop()
                                    .placeholder(R.drawable.person_placeholder)
                                    .into(drawer_user_image);
                        }
                    } else {
                        Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Cannot get user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        super.onStart();
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
            // Sign Out user
            auth.signOut();
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
                    composeEmail(new String[]{"lubabaalriyami@gmail.com"});
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

    public void composeEmail(String[] addresses) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","lubabaalriyami@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact PCWH");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Thank you for contacting us. Please write us what is in your mind: ");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}