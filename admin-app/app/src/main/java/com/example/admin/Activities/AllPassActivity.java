package com.example.admin.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Adapters.PassAdapter;
import com.example.admin.Modals.Pass;
import com.example.admin.Modals.UserPassesResult;
import com.example.admin.R;
import com.example.admin.Utilities.APIClient;
import com.example.admin.Utilities.ApiInterface;
import com.example.admin.Utilities.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPassActivity extends AppCompatActivity {
    //Field Variables
    private static final String TAG= "allpass";
    private SharedPrefManager sharedPrefManager;
    RecyclerView.LayoutManager layoutManager;
    PassAdapter passAdapter;
    int initialStatus;
    List<Pass> passes = new ArrayList<>();

    //Views
    TextView tvEmptyState;
    RecyclerView rvPasses;
    Toolbar toolbar;
    ProgressDialog progress;

    // Retrofit
    ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pass);


        initiliseAllViews();
        intialiseRetrofit();
        initialStatus = getIntent().getIntExtra("status", 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pending Passes");
        toolbar.inflateMenu(R.menu.all_pass_menu);

        setRecyclerView();
        fetchData(initialStatus);

    }

    private void setFilter() {
        LayoutInflater inflater = getLayoutInflater();

        View alertLayout = inflater.inflate(R.layout.dialog_filter_choice, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);

//        RadioButton rbAll = alertLayout.findViewById(R.id.filter_all);
        final RadioButton rbApproved = alertLayout.findViewById(R.id.filter_approved);
        final RadioButton rbRejected = alertLayout.findViewById(R.id.filter_rejected);
        RadioButton rbPending = alertLayout.findViewById(R.id.filter_pending);
//
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int id) {
                if (rbApproved.isChecked()) {
                    fetchData(1);
                }
                else if (rbPending.isChecked()) {
                    fetchData(0);
                }
                else if(rbRejected.isChecked()){
                    fetchData(-1);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_pass_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.all_pass_menu_filter:
                setFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void fetchData(int status) {
        showMessage("Loading", "Please wait while we load the passes for you");
        String statusText = "";
        if(status == 1){
            statusText = "Accepted";
            getSupportActionBar().setTitle("Accepted Passes");
        }else if(status == 0){
            statusText = "Pending";
            getSupportActionBar().setTitle("Pending Passes");
        }else{
            statusText = "Rejected";
            getSupportActionBar().setTitle("Rejected Passes");
        }
        Call<UserPassesResult> getStatusPassCall = apiInterface.getStatusPass(status);
        String finalStatusText = statusText;
        getStatusPassCall.enqueue(new Callback<UserPassesResult>() {
            @Override
            public void onResponse(Call<UserPassesResult> call, Response<UserPassesResult> response) {
                passes.clear();
                UserPassesResult userPassesResult = response.body();
                if(userPassesResult!=null){
                    List<Pass> p = userPassesResult.getPasses();
                    passes.addAll(p);
                }
//                Log.d("main", sharedPrefManager.getString(SharedPrefManager.Key.USER_ID)+ "onResponse: "+userPassesResult + response.message()+response.code());
                if(passes.size() == 0){
                    tvEmptyState.setText("No "+ finalStatusText +" passes found");
                    tvEmptyState.setVisibility(View.VISIBLE);
                }else{
                    tvEmptyState.setText("No "+ finalStatusText +" passes found");
                    tvEmptyState.setVisibility(View.GONE);
                }
                passAdapter.notifyDataSetChanged();
                hideMessage();
            }

            @Override
            public void onFailure(Call<UserPassesResult> call, Throwable t) {

                Toast.makeText(AllPassActivity.this, "Failed to fetch passes", Toast.LENGTH_SHORT).show();
                hideMessage();
            }
        });
    }

    private void setRecyclerView() {
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        rvPasses.setLayoutManager(layoutManager);
        passAdapter = new PassAdapter(this, passes, true);
        rvPasses.hasFixedSize();
        rvPasses.setAdapter(passAdapter);
    }

    private void initiliseAllViews() {
        sharedPrefManager = SharedPrefManager.getInstance(this);
        rvPasses = findViewById(R.id.allpass_recycler);
        toolbar = findViewById(R.id.allpass_toolbar);
        progress = new ProgressDialog(this);
        tvEmptyState = findViewById(R.id.empty_state_text);
    }

    private void intialiseRetrofit() {
        apiInterface = APIClient.getApiClient().create(ApiInterface.class);
    }

}
