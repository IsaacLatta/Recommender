package com.example.recommender;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.recommender.groupsTabs.friendsTabFragment;
import com.example.recommender.groupsTabs.groupsTabFragment;
import com.example.recommender.groupsTabs.requestTabFragment;

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
                return new requestTabFragment();
            default:
                return new groupsTabFragment();
        }
    }

    @Override
    public int getItemCount(){
        return 3;
    }
}
