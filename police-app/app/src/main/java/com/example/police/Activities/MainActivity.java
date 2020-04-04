package com.example.police.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.police.R;
import com.example.police.Utilities.APIClient;
import com.example.police.Utilities.ApiInterface;
import com.example.police.Utilities.SharedPrefManager;
import com.example.police.Utilities.UtilityFunctions;

public class MainActivity extends AppCompatActivity {

    //Field variables
    private final String TAG = "main";


    //Retrofit
    ApiInterface apiInterface;
    //Views
    Toolbar toolbar;
    LinearLayout llScan, llReq;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onStart() {
        super.onStart();
        if (!sharedPrefManager.containsKey(SharedPrefManager.Key.LOGIN_STATUS)) {
            boolean loginStatus = sharedPrefManager.getBoolean(SharedPrefManager.Key.LOGIN_STATUS);
            Log.d(TAG, "onStart: " + loginStatus);
            if (!loginStatus) {
                Toast.makeText(this, "Please login to continue.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intialiseAllViews();
        intialiseRetrofit();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Police");

        llScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanPassActivity.class);
                intent.putExtra("police", true);
                startActivity(intent);
            }
        });

        llReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllPassActivity.class);
                intent.putExtra("police", true);
                startActivity(intent);
            }
        });

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
                UtilityFunctions.clearLoginSession(this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void intialiseAllViews() {
        sharedPrefManager = SharedPrefManager.getInstance(this);
        toolbar = findViewById(R.id.main_toolbar);
        llReq = findViewById(R.id.main_requests_layout);
        llScan = findViewById(R.id.main_scan_layout);
    }

    private void intialiseRetrofit() {
        apiInterface = APIClient.getApiClient().create(ApiInterface.class);
    }
}
