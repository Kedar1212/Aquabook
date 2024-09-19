package com.example.aquabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // If the user is not logged in, redirect to the LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish the MainActivity, so user can't go back without logging in
        }



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Handle item clicks here
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    break;
//                case R.id.search_menu:
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
//                    break;
                case R.id.mycart:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).commit();
                    break;
                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    break;
            }
            return true;
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderHistoryFragment()).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
       switch (item.getItemId()){
           case R.id.nav_home:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
               break;

           case R.id.nav_about:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
               break;

           case R.id.nav_customer:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddServiceFragment()).commit();
               break;

           case R.id.nav_service:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
               break;

           case R.id.nav_share:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
               break;

           case R.id.nav_logout:
               Intent intent = new Intent(MainActivity.this, LoginActivity.class);
               startActivity(intent);
               finish();
               break;




       }

       drawerLayout.closeDrawer(GravityCompat.START);
       return true;

    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }


    }


}