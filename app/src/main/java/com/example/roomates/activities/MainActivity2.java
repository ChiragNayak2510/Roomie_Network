package com.example.roomates.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.roomates.R;
import com.example.roomates.Fragment.HomeFragment;
import com.example.roomates.Fragment.ProfileFragment;
import com.example.roomates.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);

        Bundle intent =getIntent().getExtras();
        if (intent!=null){
//            String publisher=intent.getString("publisherid");
//            SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
//            editor.putString("profileid",publisher);
//            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            int itemId = item.getItemId();
//            switch (itemId){
//                case (itemId == R.id.nav_home) :
//                    selectedFragment = new HomeFragment();
//                    break;
//                case R.id.nav_search:
//                    selectedFragment = new SearchFragment();
//                    break;
//                case R.id.nav_add:
//
//                    selectedFragment = null;
//                    startActivity(new Intent(MainActivity.this,PostActivity.class));
//                    break;
//                case R.id.nav_profile:
//                    SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
//                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    editor.apply();
//                    selectedFragment = new ProfileFragment();
//                    break;
//            }
//            if(selectedFragment!=null){
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
//            }
//            return true;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_search) {
                        startActivity(new Intent(MainActivity2.this,UsersActivity.class));
            } else if (itemId == R.id.nav_add) {
                startActivity(new Intent(MainActivity2.this, PostActivity.class));
                return true; // Return early to prevent fragment transaction
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        }
    };
}