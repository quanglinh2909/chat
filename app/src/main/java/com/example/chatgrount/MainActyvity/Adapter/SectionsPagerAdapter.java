package com.example.chatgrount.MainActyvity.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.example.chatgrount.MainActyvity.Fragment.AllBanBeFragment;
import com.example.chatgrount.MainActyvity.Fragment.FriendsFragment;
import com.example.chatgrount.MainActyvity.Fragment.LoiMoiKetBanFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            case 1:
                LoiMoiKetBanFragment loiMoiKetBanFragment = new LoiMoiKetBanFragment();
                return loiMoiKetBanFragment;
            case 2 :
                AllBanBeFragment allBanBeFragment = new AllBanBeFragment();
                return allBanBeFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){

            case 0:
                return "Nhắn Tin";
            case 1:
                return  "Lời mời kết bạn";
            case 2:
                return  "Bạn bè";
            default:
                return null;

        }
    }
}
