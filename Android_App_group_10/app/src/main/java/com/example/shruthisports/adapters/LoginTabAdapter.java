package com.example.shruthisports.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.shruthisports.fragments.LoginFragment;
import com.example.shruthisports.fragments.RegisterFragment;

public class LoginTabAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    //Constructor for TabAdapter
    public LoginTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    //returning tab fragment based on the given index
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LoginFragment loginFragment = new LoginFragment();
                return loginFragment;
            case 1:
                RegisterFragment registerFragment = new RegisterFragment();
                return registerFragment;
            default:
                return null;
        }
    }

    //function to return total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}

