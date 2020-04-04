package com.example.token.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.token.Adapters.PassAdapter;
import com.example.token.Modals.Pass;
import com.example.token.Modals.UserPassesResult;
import com.example.token.R;
import com.example.token.Utilities.APIClient;
import com.example.token.Utilities.ApiInterface;
import com.example.token.Utilities.SharedPrefManager;
import com.example.token.Utilities.UtilityFunctions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //Field Variables
    private static final String TAG= "main";
    private SharedPrefManager sharedPrefManager;
    RecyclerView.LayoutManager layoutManager;
    PassAdapter passAdapter;
    List<Pass> passes = new ArrayList<>();

    //Views
    FloatingActionButton fbAddPass;
    RecyclerView rvPasses;
    TextView tvEmptyStateText;
    Toolbar toolbar;

    // Retrofit
    ApiInterface apiInterface;



    @Override
    protected void onStart() {

        if (sharedPrefManager.containsKey(SharedPrefManager.Key.LOGIN_STATUS)) {
            boolean loginStatus = sharedPrefManager.getBoolean(SharedPrefManager.Key.LOGIN_STATUS);
            if (!loginStatus) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }else{
                if(!isFinishing())
                    fetchData();
            }
        }else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiliseAllViews();
        intialiseRetrofit();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Passes");
        toolbar.inflateMenu(R.menu.main_menu);


//        UtilityFunctions.clearLoginSession(MainActivity.this);
        fbAddPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPassActivity.class);
                startActivity(intent);
            }
        });

        setRecyclerView();
        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_main_logout:
                logout();
                return true;
//            case R.id.menu_main_police:
//                Intent intent = new Intent(MainActivity.this, ScanPassActivity.class);
//                intent.putExtra("police", true);
//                askPolice(intent);
//                return true;
//            case R.id.menu_all_pass:
//                Intent police_intent = new Intent(MainActivity.this, AllPassActivity.class);
//                police_intent.putExtra("police", true);
//                askPolice(police_intent);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void askPolice(Intent intent) {
        final LayoutInflater inflater = getLayoutInflater();

        final View alertLayout = inflater.inflate(R.layout.dialog_police_verification, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);

        TextInputEditText etPassword = alertLayout.findViewById(R.id.police_password);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                String pass = etPassword.getText().toString().trim();
                if(pass.length()!=0){
                    if(pass.equalsIgnoreCase("123456")){

                        startActivity(intent);
                    }else{
                        etPassword.setError("Wrong  Password");
                    }
                }else{
                    etPassword.setError("Password field cannot be empty");
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        Dialog d = alert.create();
        d.show();
    }

    private void logout() {
        UtilityFunctions.clearLoginSession(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void fetchData() {

        Call<UserPassesResult> getPassesCall = apiInterface.getPasses(sharedPrefManager.getString(SharedPrefManager.Key.TOKEN));
        getPassesCall.enqueue(new Callback<UserPassesResult>() {
            @Override
            public void onResponse(Call<UserPassesResult> call, Response<UserPassesResult> response) {
                passes.clear();
                UserPassesResult userPassesResult = response.body();
                if(userPassesResult!=null){
                    List<Pass> p = userPassesResult.getPasses();
                    passes.addAll(p);
                }

                Log.d("main", sharedPrefManager.getString(SharedPrefManager.Key.USER_ID)+ "onResponse: "+sharedPrefManager.getString(SharedPrefManager.Key.TOKEN) + response.message()+response.code());
                if(passes.size() == 0){
                    tvEmptyStateText.setVisibility(View.VISIBLE);
                }else{
                    tvEmptyStateText.setVisibility(View.GONE);
                }
                passAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UserPassesResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch passes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView() {
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        rvPasses.setLayoutManager(layoutManager);
        passAdapter = new PassAdapter(this, passes,false);
        rvPasses.hasFixedSize();
        rvPasses.setAdapter(passAdapter);
//        rvPasses.addItemDecoration(new DividerItemDecoration(rvNotes.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initiliseAllViews() {
        sharedPrefManager = SharedPrefManager.getInstance(this);
        fbAddPass = findViewById(R.id.main_add_pass);
        rvPasses = findViewById(R.id.mian_pass_recycler);
        toolbar = findViewById(R.id.main_toolbar);
        tvEmptyStateText = findViewById(R.id.main_empty);
    }

    private void intialiseRetrofit() {
        apiInterface = APIClient.getApiClient().create(ApiInterface.class);
    }

}
