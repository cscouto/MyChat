package com.coutocode.mychat;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.coutocode.mychat.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAPI {

    public interface FirebaseListener {
        void userCreated();
        void failed(int message);
        void loggedIn();
        void newMessage();
    }

    private DatabaseReference database;
    private FirebaseAuth mAuth;
    FirebaseListener listener;
    FirebaseUser user;
    List<MessageModel> messages;

    FirebaseAPI(){
        database = FirebaseDatabase.getInstance().getReference(Constants.MESSAGE_REFERENCE);
        mAuth = FirebaseAuth.getInstance();
        messages = new ArrayList<>();
    }

    void registerUser(String email, String password, Activity activity){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = mAuth.getCurrentUser();
                    listener.userCreated();
                } else {
                    listener.failed(R.string.auth_failed);
                }
            }
        });
    }

    void signIn(String email, String password, Activity activity){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            listener.loggedIn();
                        } else {
                            listener.failed(R.string.auth_failed);
                        }
                    }
                });
    }

    void sendMessage(String email, String message){
        MessageModel messageModel = new MessageModel();
        messageModel.sender = email;
        messageModel.messageBody = message;
        database.setValue(messageModel);
    }

    void observeData(){
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                messages.add(messageModel);
                listener.newMessage();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
