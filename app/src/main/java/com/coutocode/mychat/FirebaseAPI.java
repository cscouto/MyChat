package com.coutocode.mychat;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.coutocode.mychat.utils.Constants;
import com.coutocode.mychat.widget.ChatAppWidget;
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
    public FirebaseListener listener;
    private FirebaseUser user;
    public List<MessageModel> messages;
    private Context context;

    public FirebaseAPI(Context context){
        this.context = context;
        database = FirebaseDatabase.getInstance().getReference(Constants.MESSAGE_REFERENCE);
        mAuth = FirebaseAuth.getInstance();
        messages = new ArrayList<>();
        user = mAuth.getCurrentUser();
    }

    public void registerUser(String email, String password, Activity activity){
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

    public void signIn(String email, String password, Activity activity){
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

    public void logout(){
        mAuth.signOut();
    }

    public void sendMessage(String message){
        MessageModel messageModel = new MessageModel();
        messageModel.sender = user.getEmail();
        messageModel.messageBody = message;
        database.push().setValue(messageModel);
    }

    public void observeData(){
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                SharedPreferences pref =  context.getSharedPreferences(Constants.PREF_KEY,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.USR_KEY, messageModel != null ? messageModel.sender : null);
                editor.putString(Constants.MSG_KEY, messageModel != null ? messageModel.messageBody : null);
                editor.apply();
                updateWidget();
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

    private void updateWidget(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, ChatAppWidget.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.chat_app_widget);
        SharedPreferences pref =  context.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE);
        String user = pref.getString(Constants.USR_KEY, Constants.USR_KEY);
        String message = pref.getString(Constants.MSG_KEY, Constants.MSG_KEY);
        views.setTextViewText(R.id.tvUser, user);
        views.setTextViewText(R.id.tvMessage, message);
        appWidgetManager.updateAppWidget(thisWidget, views);
    }
}
