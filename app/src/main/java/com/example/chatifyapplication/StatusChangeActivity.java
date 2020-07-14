package com.example.chatifyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusChangeActivity extends AppCompatActivity {

    TextInputLayout mstatuschange;
    ProgressBar mProgressbar;
    Button mChangeStatusBtn;
    DatabaseReference databaseReference;
    FirebaseUser curruser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_change);
        mstatuschange=findViewById(R.id.status_changestatus_edittext);
        mProgressbar=findViewById(R.id.status_progressbar);
        mChangeStatusBtn=findViewById(R.id.status_changestatus_btn);
        String status=getIntent().getStringExtra("status");
        mstatuschange.getEditText().setText(status);

        mChangeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressbar.setVisibility(View.VISIBLE);
                hideKeyboard(v);
                curruser= FirebaseAuth.getInstance().getCurrentUser();
                String uid=curruser.getUid();
                databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("status");
                databaseReference.setValue(mstatuschange.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            mProgressbar.setVisibility(View.GONE);
                            mstatuschange.clearFocus();
                            Toast.makeText(StatusChangeActivity.this, "Status is Successfully Changed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
    public void hideKeyboard(View v)
    {

        InputMethodManager i=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        i.hideSoftInputFromWindow(v.getWindowToken(),0);

    }
}
