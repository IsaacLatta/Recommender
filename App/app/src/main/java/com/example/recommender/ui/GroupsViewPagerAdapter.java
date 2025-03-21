package com.example.recommender.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.recommender.ui.fragments.GroupsFragment;
import com.example.recommender.ui.groupsTabs.friendsTabFragment;
import com.example.recommender.ui.groupsTabs.groupsTabFragment;
import com.example.recommender.ui.groupsTabs.RequestTabFragment;

public class GroupsViewPagerAdapter extends FragmentStateAdapter {

    public GroupsViewPagerAdapter(@NonNull GroupsFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        switch (position){
            case 0:
                return new groupsTabFragment();
            case 1:
                return new friendsTabFragment();
            case 2:
                return new RequestTabFragment();
            default:
                return new groupsTabFragment();
        }
    }

    @Override
    public int getItemCount(){
        return 3;
    }
}
