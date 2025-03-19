package com.example.recommender;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.example.recommender.ui.fragments.AddFragment;
import com.example.recommender.ui.fragments.GroupsFragment;
import com.example.recommender.ui.fragments.HomeFragment;
import com.example.recommender.ui.fragments.RateFragment;
import com.example.recommender.ui.fragments.SavedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);
        setCurrentFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                setCurrentFragment(new HomeFragment());
            } else if (id == R.id.groups) {
                Log.d("GROUPS_TAB", "Switching to the groups fragment");
                setCurrentFragment(new GroupsFragment());
            } else if (id == R.id.add) {
                setCurrentFragment(new AddFragment());
            } else if (id == R.id.rate) {
                setCurrentFragment(new RateFragment());
            } else if (id == R.id.saved) {
                setCurrentFragment(new SavedFragment());
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
