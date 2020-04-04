package com.example.police.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.police.Modals.RegistrationResult;
import com.example.police.Modals.User;
import com.example.police.R;
import com.example.police.Utilities.APIClient;
import com.example.police.Utilities.ApiInterface;
import com.example.police.Utilities.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    //Field varibales
    private final String TAG = "register";
    //Retrofit
    ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;
    //Views
    private TextInputEditText etName, etPassword, etRepeatPassword, etPhone, etEmail;
    private TextView tvLogin;
    private Button btnRegister;
    ProgressDialog progress;

    private void showMessage(String titile, String Message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("UI thread", "I am the UI thread");
                progress.setTitle(titile);
                progress.setMessage(Message);
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
            }
        });

    }

    private void hideMessage() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("UI thread", "I am the UI thread from Barcode Fragment");
                progress.hide();

            }
        });
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        intialiseAllViews();
        intialiseRetrofit();
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sharedPrefManager.containsKey(SharedPrefManager.Key.LOGIN_STATUS)) {
            boolean loginStatus = sharedPrefManager.getBoolean(SharedPrefManager.Key.LOGIN_STATUS);
            if (loginStatus) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }
    }


    private void register() {
        showMessage("Register", "Please wait while we create your account");
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        final String repPass = etRepeatPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.length() != 0 && phone.length() != 0 && pass.length() != 0 && repPass.length() != 0 && etEmail.length() != 0) {
            if (pass.equals(repPass) != true) {
                etRepeatPassword.setError("Passwords need to match");
                return;
            }

            List<String> passes = new ArrayList<>();
            final User user = new User(name, email, pass, passes, phone);
            Call<RegistrationResult> registerCall = apiInterface.register(user);
            registerCall.enqueue(new Callback<RegistrationResult>() {
                @Override
                public void onResponse(Call<RegistrationResult> call, Response<RegistrationResult> response) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful." + response.message(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: " + response.body().getId());
                    sharedPrefManager.put(SharedPrefManager.Key.USER_ID, response.body().getId());
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    hideMessage();
                    finishAffinity();
                }

                @Override
                public void onFailure(Call<RegistrationResult> call, Throwable t) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    hideMessage();
                }
            });

        } else {
            if (pass.length() == 0 || repPass.length() == 0) {
                if (pass.length() == 0) etPassword.setError("Password field cannot be empty");
                if (repPass.length() == 0) etPassword.setError("Password field cannot be empty");
            }

            if (name.length() == 0) {
                etName.setError("Name cannot be empty");
            }
            if (phone.length() == 0) {
                etPhone.setError("Phone number cannot be empty");
            }
        }

    }

    private void intialiseAllViews() {
        sharedPrefManager = SharedPrefManager.getInstance(this);

        etName = findViewById(R.id.register_name);
        etPassword = findViewById(R.id.register_password);
        etRepeatPassword = findViewById(R.id.register_repeat_password);
        etPhone = findViewById(R.id.register_mobile);
        tvLogin = findViewById(R.id.register_login);
        btnRegister = findViewById(R.id.register_submit);
        etEmail = findViewById(R.id.register_email);
        progress = new ProgressDialog(this);
    }

    private void intialiseRetrofit() {
        apiInterface = APIClient.getApiClient().create(ApiInterface.class);
    }

}
