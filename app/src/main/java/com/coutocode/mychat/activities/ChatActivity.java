package com.coutocode.mychat.activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.coutocode.mychat.ChatAdapter;
import com.coutocode.mychat.FirebaseAPI;
import com.coutocode.mychat.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements FirebaseAPI.FirebaseListener {

    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    FirebaseAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        api =  new FirebaseAPI();
        api.listener = this;
        api.observeData();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etMessage.getText().toString().isEmpty()){
                    api.sendMessage(etMessage.getText().toString());
                    etMessage.setText("");
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setMessage(R.string.enter_message).create();
                }
            }
        });
    }

    void updateUI(){
        ChatAdapter adapter = new ChatAdapter(api.messages);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void userCreated() { }

    @Override
    public void failed(int message) { }

    @Override
    public void loggedIn() { }

    @Override
    public void newMessage() {
        updateUI();
    }
}
