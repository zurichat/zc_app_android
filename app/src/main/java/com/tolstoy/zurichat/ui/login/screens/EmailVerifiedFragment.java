package com.tolstoy.zurichat.ui.login.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tolstoy.zurichat.R;


public class EmailVerifiedFragment extends Fragment {

    private NavController navController;
    private Bundle bundle;
    private Button btn_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_verified, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = new Bundle();
        navController = Navigation.findNavController(view);
        btn_login = view.findViewById(R.id.btn_goToLogin);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("email", getArguments().getString("email"));
                navController.navigate(R.id.action_emailVerifiedFragment_to_loginFragment, bundle);
            }
        });
    }
}