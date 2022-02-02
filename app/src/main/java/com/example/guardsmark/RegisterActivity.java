package com.example.guardsmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.guardsmark.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        binding.loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void createUser() {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        String password2 = binding.etPassword2.getText().toString();

        if(TextUtils.isEmpty(email)){
            binding.etEmail.setError("Email can't be empty");
            binding.etEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)){
            binding.etPassword.setError("Password can't be empty");
            binding.etPassword.requestFocus();
        } else if(password.length() < 8){
            binding.etPassword.setError("Can't be less than 8!");
            binding.etPassword.requestFocus();
        } else if(password2.length() < 8){
            binding.etPassword2.setError("Can't be less than 8!");
            binding.etPassword2.requestFocus();
        } else if(!password.equals(password2)){
            binding.etPassword2.setError("Password didn't match");
            binding.etPassword2.requestFocus();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "You Registered Successfully", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}