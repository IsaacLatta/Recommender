package com.example.recommender.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recommender.R;
import com.example.recommender.ui.GroupsViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class GroupsFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    GroupsViewPagerAdapter groupsViewPagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        tabLayout = view.findViewById(R.id.grouptab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);

        // Set up ViewPager with the adapter
        groupsViewPagerAdapter = new GroupsViewPagerAdapter(this);
        viewPager2.setAdapter(groupsViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;

    }
}