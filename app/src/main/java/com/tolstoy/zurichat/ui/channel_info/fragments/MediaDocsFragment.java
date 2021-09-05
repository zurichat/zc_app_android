package com.tolstoy.zurichat.ui.channel_info.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.ui.adapters.MediaAdapter;
import com.tolstoy.zurichat.ui.adapters.MediaDocsAdapter;

public class MediaDocsFragment extends Fragment {

    private ViewPager viewPager;
    private MediaDocsAdapter adapter;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_docs, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MediaDocsAdapter(getChildFragmentManager(), getContext());
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}