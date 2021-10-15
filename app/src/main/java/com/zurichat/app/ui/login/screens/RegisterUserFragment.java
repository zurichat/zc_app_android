package com.zurichat.app.ui.login.screens;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.zurichat.app.R;
import com.zurichat.app.data.remoteSource.RetrofitClient;
import com.zurichat.app.data.remoteSource.UsersService;
import com.zurichat.app.databinding.FragmentRegisterUserBinding;
import com.zurichat.app.models.RegisterUser;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserFragment extends Fragment {

    private NavController navController;
    private ProgressDialog progressDialog;
    private UsersService usersService;
    private Bundle bundle;

    private FragmentRegisterUserBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        bundle = new Bundle();
        usersService = RetrofitClient.getClient("https://api.zuri.chat/").create(UsersService.class);
        progressDialog = new ProgressDialog(getContext());


        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = binding.userEmail.getEditText().getText().toString();
                String userPassword = binding.userPassword.getEditText().getText().toString();

                if (userEmail.isEmpty()) {
                    binding.userEmail.setError("Email is required");
                } else if (userPassword.isEmpty()) {
                    binding.userEmail.setError(null);
                    binding.userPassword.setError("Password cannot be empty");
                } else if (!isValidPassword(userPassword.trim())) {
                    binding.userPassword.setError("Invalid password pattern");
                } else if (!binding.userPassword.getEditText().getText().toString().equals(binding.confirmUserPassword.getEditText().getText().toString())) {
                    binding.confirmUserPassword.setError("Password does not match");
                } else if (!binding.checkBox.isChecked()) {
                    binding.checkBox.setError("Terms and conditions must be accepted!");
                } else {
                    binding.userPassword.setError(null);
                    binding.confirmUserPassword.setError(null);
                    binding.checkBox.setError(null);
                    bundle.putString("email", userEmail);
                    registerUser("", "", "", userEmail, userPassword, "");
                }

            }
        });

       binding.textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registerUserFragment_to_loginFragment, bundle);
            }
        });
    }

    public void registerUser(String first_name, String last_name, String display_name, String email, String password, String phone) {
        progressDialog.show();
        final RegisterUser registerUser = new RegisterUser(first_name, last_name, display_name, email, password, phone);
        Call<RegisterUser> call = usersService.register(registerUser);
        Log.d("REG", "registerUser: "+registerUser);
        call.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                if (response.code() == 400) {
                    Toast.makeText(getContext(), "User Already Exists!", Toast.LENGTH_LONG).show();
                } else {
                    if (response.body().getMessage().matches("user created")) {
                        Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                        navController.navigate(R.id.action_registerUserFragment_to_emailVerificationFragment, bundle);
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {

            }
        });

    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        pattern.matcher(password);

        //return matcher.matches();
        return true;
    }
}