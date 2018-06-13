package com.coutocode.mychat.activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.coutocode.mychat.FirebaseAPI;
import com.coutocode.mychat.R;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements FirebaseAPI.FirebaseListener {

    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;

    FirebaseAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        api = new FirebaseAPI();
        api.listener = this;

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()){
                    api.signIn(etEmail.getText().toString(), etPassword.getText().toString(),
                            LoginActivity.this);
                }else{
                    AlertDialog alert = new AlertDialog
                            .Builder(LoginActivity.this)
                            .setMessage(R.string.fill_fields)
                            .create();
                    alert.show();
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

    @Override
    public void userCreated() { }

    @Override
    public void failed(int message) {
        AlertDialog alert = new AlertDialog
                .Builder(LoginActivity.this)
                .setMessage(message)
                .create();
        alert.show();
    }

    @Override
    public void loggedIn() {
        startActivity(new Intent(this, ChatActivity.class));
    }

    @Override
    public void newMessage() { }
}
