package com.example.javainstagramclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.javainstagramclone.R;
import com.example.javainstagramclone.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    String TAG = "com.example.javainstagramclone";
    ActivityMainBinding binding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user= mAuth.getCurrentUser();

        if(user != null){
            Intent intent = new Intent(MainActivity.this,FeedActivity.class);
            startActivity(intent);
            finish();

        }

    }

    public void signInClicked(View view){
        String email = binding.emailText.getText().toString();
        String password=  binding.passwordText.getText().toString();

        if(email.equals("") || password.equals("")){
            Toast.makeText(MainActivity.this, "please enter email and password", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //login successful
                        Log.d(TAG,"signInWithEmail:success");
                        Intent intent = new Intent(MainActivity.this,FeedActivity.class);
                        startActivity(intent);
                        finish();
                    }else{

                        Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    public void signUpClicked(View view){

        String email = binding.emailText.getText().toString();
        String password=  binding.passwordText.getText().toString();

        if(email.equals("") || password.equals("")){
            Toast.makeText(MainActivity.this, "please enter email and password", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Log.d(TAG,"signUp:success");
                    Intent intent = new Intent(MainActivity.this,FeedActivity.class);
                    startActivity(intent);
                    finish();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}