package com.tolstoy.zurichat.ui.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tolstoy.zurichat.ui.onboarding.SlideOneFragment;
import com.tolstoy.zurichat.ui.onboarding.SlideThreeFragment;
import com.tolstoy.zurichat.ui.onboarding.SlideTwoFragment;

public class OnboardingScreenAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public OnboardingScreenAdapter(@NonNull FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                SlideOneFragment tab1 = new SlideOneFragment();
                return tab1;
            case 1:
                SlideTwoFragment tab2 = new SlideTwoFragment();
                return tab2;
            case 2:
                SlideThreeFragment tab3 = new SlideThreeFragment();
                return tab3;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

