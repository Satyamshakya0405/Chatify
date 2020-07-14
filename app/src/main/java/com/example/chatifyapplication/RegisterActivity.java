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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout mDisplayName,mEmail,mPassword;
    Button mRegbtn;
    private FirebaseAuth mAuth;
    Toolbar mtoolbar;
    ProgressBar mprogressbar;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDisplayName=findViewById(R.id.reg_displayname);
        mEmail=findViewById(R.id.reg_email);
        mPassword=findViewById(R.id.reg_password);
        mRegbtn=findViewById(R.id.Register_reg_btn);
        mAuth = FirebaseAuth.getInstance();
        mtoolbar=findViewById(R.id.register_toolbar);
        mprogressbar=findViewById(R.id.reg_progressbar);
        mprogressbar.setVisibility(View.INVISIBLE);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRegbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name=mDisplayName.getEditText().getText().toString();
                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();
                if(!validateName()||!validateEmail()||!validatePassword()){
                    return ;
                }
                hideKeyboard(v);
                mprogressbar.setVisibility(View.VISIBLE);

                registeruser(display_name, email, password);

            }
        });
    }
    private void registeruser(final String display_name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
                            String id=currentuser.getUid();
                            databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(id);
                            String display_name=mDisplayName.getEditText().getText().toString();
                            HashMap<String,String> usermap=new HashMap<>();
                            usermap.put("name",display_name);
                            usermap.put("status","Hi i m using chatify");
                            usermap.put("image","default");
                            usermap.put("thumb_image","default");

                            databaseReference.setValue(usermap);
                            Intent mainIntent= new Intent(getApplicationContext(),StartActivity.class);
                            mprogressbar.setVisibility(View.GONE);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mprogressbar.setVisibility(View.INVISIBLE);
//                            updateUI(null);
                        }
                    }
                });

    }
    public void hideKeyboard(View v)
    {

        InputMethodManager i=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        i.hideSoftInputFromWindow(v.getWindowToken(),0);

    }
    private Boolean validateName()
    {
        String val=mDisplayName.getEditText().getText().toString();
        if(val.isEmpty())
        {
            mDisplayName.setError("Field cannot be empty");
            return false;
        }
        else
        {
            mDisplayName.setError(null);
            mDisplayName.setErrorEnabled(true);
            return true;
        }
    }
    private Boolean validateEmail() {
        String val=mEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty())
        {
            mEmail.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern))
        {
            mEmail.setError("Invalid Email");
            return false;
        }
        else
        {
            mEmail.setError(null);
            mEmail.setErrorEnabled(true);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = mPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            mPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            mPassword.setError("Password is too weak");
            return false;
        } else {
            mPassword.setError(null);
            mPassword.setErrorEnabled(false);
            return true;
        }
    }
}
