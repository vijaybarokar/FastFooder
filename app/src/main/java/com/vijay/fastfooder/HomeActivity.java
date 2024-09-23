package com.vijay.fastfooder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_home);


        bottomNavigationView = findViewById (R.id.homeBottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener (this);
        bottomNavigationView.setSelectedItemId (R.id.homeBottomNavigationMenuHome); // bydefault opened fragment
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate (R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId ()==R.id.homeMenuMyOffers)
        {

        } else if (item.getItemId ()==R.id.homeMenuCart)  {

        } else if (item.getItemId ()==R.id.homeMenuMyProfile) {
            Intent intent = new Intent (HomeActivity.this,MyProfileActivity.class);
            startActivity (intent);
        }

        return true;
    }

    HomeFragment homeFragment = new HomeFragment ();
    CategoryFragment categoryFragment = new CategoryFragment ();
    MyOrderFragment myOrderFragment = new MyOrderFragment ();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId ()==R.id.homeBottomNavigationMenuHome)
        {
            getSupportFragmentManager ().beginTransaction ().replace (R.id.homeFrameLayout,homeFragment).commit ();
        } else if (item.getItemId ()==R.id.homeBottomNavigationMenuCategory) {
            getSupportFragmentManager().beginTransaction ().replace (R.id.homeFrameLayout,categoryFragment).commit ();
        } else if (item.getItemId ()==R.id.homeBottomNavigationMenuMyOrder) {
            getSupportFragmentManager().beginTransaction ().replace (R.id.homeFrameLayout,myOrderFragment).commit ();

        }


        return true;
    }
}
