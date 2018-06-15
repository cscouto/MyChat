package com.coutocode.mychat.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

        api =  new FirebaseAPI(this);
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
                    Toast.makeText(ChatActivity.this,
                            R.string.enter_message,
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            api.logout();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void updateUI(){
        ChatAdapter adapter = new ChatAdapter(api.messages);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void userCreated() { }

    @Override
    public void failed(String message) { }

    @Override
    public void loggedIn() { }

    @Override
    public void newMessage() {
        updateUI();
    }
}
