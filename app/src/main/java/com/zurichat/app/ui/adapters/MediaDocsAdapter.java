package com.zurichat.app.ui.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zurichat.app.R;
import com.zurichat.app.ui.channel_info.fragments.DocsFragment;
import com.zurichat.app.ui.channel_info.fragments.LinksFragment;
import com.zurichat.app.ui.channel_info.fragments.MediaFragment;

public class MediaDocsAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.tab_title_media, R.string.tab_title_docs, R.string.tab_title_links};
    private final Context mContext;

    public MediaDocsAdapter(@NonNull FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MediaFragment tab_1 = new MediaFragment();
                return tab_1;
            case 1:
                DocsFragment tab_2 = new DocsFragment();
                return tab_2;
            case 2:
                LinksFragment tab_3 = new LinksFragment();
                return tab_3;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}
