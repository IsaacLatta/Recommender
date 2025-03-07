package com.example.recommender;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);
        Fragment homefrag = new HomeFragment();
        Fragment groupfrag = new GroupsFragment();
        Fragment addfrag = new AddFragment();
        Fragment ratefrag = new RateFragment();
        Fragment savedfrag = new SavedFragment();
        setCurrentFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                setCurrentFragment(homefrag);
            } else if (id == R.id.groups) {
                setCurrentFragment(groupfrag);
            } else if (id == R.id.add) {
                setCurrentFragment(addfrag);
            }
            return true;
        });

    }

    // Helper method to switch fragments in the container
    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}
