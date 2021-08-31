package com.tolstoy.zurichat.ui.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.ui.login.LoginActivity;


public class SlideThreeFragment extends Fragment {

    private String show_intro = "Show Intro";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slide_three, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.getRootView().findViewById(R.id.pager);
        sharedPreferences = getActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Button btnNext = view.findViewById(R.id.btn_next);
        TextView skip = view.findViewById(R.id.skip);

        btnNext.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            editor.putBoolean(show_intro, false);
            editor.apply();
        });
        skip.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
    }
}