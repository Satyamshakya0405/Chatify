package com.example.chatifyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Toolbar mtoolbar;
    TextInputLayout mEmail,mPassword;
    Button mLoginbtn;
    private FirebaseAuth mAuth;
    ProgressBar mprogressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mtoolbar=findViewById(R.id.login_toolbar);
        mEmail=findViewById(R.id.login_email);
        mPassword=findViewById(R.id.login_password);
        mLoginbtn=findViewById(R.id.login_btn);
        mprogressbar=findViewById(R.id.login_progressbar);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();
                if(!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password))
                {

                    InputMethodManager i=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    i.hideSoftInputFromWindow(v.getWindowToken(),0);
                    mprogressbar.setVisibility(View.VISIBLE);
                    loginUser(email,password);
                }
            }
        });
    }
    public void loginUser(String email,String Password)
    {
        mAuth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent mainIntent=new Intent(getApplicationContext(),MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    mprogressbar.setVisibility(View.GONE);
                    finish();
                }
                else
                {
                    mprogressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
