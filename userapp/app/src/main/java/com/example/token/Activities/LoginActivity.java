package com.example.token.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.token.Modals.LoginCredential;
import com.example.token.Modals.LoginResult;
import com.example.token.Modals.User;
import com.example.token.R;
import com.example.token.Utilities.APIClient;
import com.example.token.Utilities.ApiInterface;
import com.example.token.Utilities.SharedPrefManager;
import com.example.token.Utilities.UtilityFunctions;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //Field Variables
    private final String TAG = "login";
    private SharedPrefManager sharedPrefManager;

    //Views
    private TextInputEditText etLogin, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    // Retrofit
    ApiInterface apiInterface;



    @Override
    protected void onStart() {
        if(sharedPrefManager.containsKey(SharedPrefManager.Key.LOGIN_STATUS)){
            boolean loginStatus = sharedPrefManager.getBoolean(SharedPrefManager.Key.LOGIN_STATUS);
            if(loginStatus){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialiseAllViews();
        intialiseRetrofit();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    private void login() {
        String password = etPassword.getText().toString().trim();
        String email = etLogin.getText().toString().trim();
        if(email.length()!=0 && password.length() != 0){
            LoginCredential loginCredential = new LoginCredential(email, password);
            final Call<LoginResult> loginCall = apiInterface.login(loginCredential);
            loginCall.enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    LoginResult loginResult = response.body();

                    if(loginResult.getStatus() == 200){
                        User user = loginResult.getUser();
                        UtilityFunctions.setLoginSession(user, LoginActivity.this, user.getToken());
                        sharedPrefManager.put(SharedPrefManager.Key.USER_ID, user.getId());
                        Log.d(TAG, "onResponse: "+user.getId());
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }else if(loginResult.getStatus() == 404){
                        Log.d(TAG, "onResponse: "+response.body());
                    }else if(loginResult.getStatus() == 402){
                        Log.d(TAG, "onResponse: "+response.body());
                    }
                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+ t.getMessage());
                    Toast.makeText(LoginActivity.this, "Login failed, please try again sometime later.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            if(email.length() == 0){
                etLogin.setError("Email field cannot be empty");
            }
            if(password.length() == 0){
                etPassword.setError("Password field cannot be empty");
            }
        }
    }




    private void initialiseAllViews() {
        sharedPrefManager = SharedPrefManager.getInstance(this);

        etLogin = findViewById(R.id.login_email);
        etPassword = findViewById(R.id.login_password);
        tvRegister = findViewById(R.id.login_register);
        btnLogin = findViewById(R.id.login_login);
    }

    private void intialiseRetrofit() {
        apiInterface = APIClient.getApiClient().create(ApiInterface.class);
    }



}
