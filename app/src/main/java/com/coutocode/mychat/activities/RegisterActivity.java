package com.coutocode.mychat.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.coutocode.mychat.FirebaseAPI;
import com.coutocode.mychat.R;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements FirebaseAPI.FirebaseListener {

    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    FirebaseAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        api = new FirebaseAPI(this);
        api.listener = this;

        ButterKnife.bind(this);

        progressBar.setVisibility(View.GONE);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()){
                    if (isNetworkAvailable()){
                        progressBar.setVisibility(View.VISIBLE);
                        api.registerUser(etEmail.getText().toString(), etPassword.getText().toString(),
                                RegisterActivity.this);
                    }else{
                        Toast.makeText(RegisterActivity.this,
                                R.string.no_internet,
                                Toast.LENGTH_LONG)
                                .show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this,
                            R.string.fill_fields,
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ?
                connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void userCreated() {
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(this, ChatActivity.class));
    }

    @Override
    public void failed(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(RegisterActivity.this,
                message,
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void loggedIn() { }

    @Override
    public void newMessage() { }
}
