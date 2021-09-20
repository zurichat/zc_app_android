package com.tolstoy.zurichat.ui.login.screens;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.data.remoteSource.RetrofitClient;
import com.tolstoy.zurichat.data.remoteSource.RetrofitService;
import com.tolstoy.zurichat.models.VerifyEmail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EmailVerificationFragment extends Fragment {

    private RetrofitService retrofitService;
    private NavController navController;
    private Bundle bundle;
    private TextView txt_enter_code;
    private Button btnVerifyEmail;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final PinView pinView = view.findViewById(R.id.pinView);
        navController = Navigation.findNavController(view);
        bundle = new Bundle();
        btnVerifyEmail = view.findViewById(R.id.btn_verifyEmail);
        txt_enter_code = view.findViewById(R.id.textView_enter_code);
        txt_enter_code.setText("Please enter the code sent to\n"+getArguments().getString("email"));
        progressDialog = new ProgressDialog(getContext());

        retrofitService = RetrofitClient.getClient("https://api.zuri.chat/").create(RetrofitService.class);
        pinView.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        btnVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinView.length() == 6){
                    verifyEmail(pinView.getText().toString());
                }else{

                }
            }
        });
    }


    public void verifyEmail(String code){
        progressDialog.setMessage("Verifying...");
        progressDialog.show();
        final VerifyEmail verifyEmail = new VerifyEmail(code);
        Call<VerifyEmail> call = retrofitService.verifyEmail(verifyEmail);
        call.enqueue(new Callback<VerifyEmail>() {
            @Override
            public void onResponse(Call<VerifyEmail> call, Response<VerifyEmail> response) {
                if (response.code() == 400){
                    Toast.makeText(getContext(), "Account confirmation code used or expired, confirm and try again", Toast.LENGTH_LONG).show();

                }else if(response.code() == 200){
                    Toast.makeText(getContext(), "Verification Successful", Toast.LENGTH_LONG).show();
                    bundle.putString("email", getArguments().getString("email"));
                    navController.navigate(R.id.action_emailVerificationFragment_to_loginFragment, bundle);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<VerifyEmail> call, Throwable t) {

            }
        });
    }


}