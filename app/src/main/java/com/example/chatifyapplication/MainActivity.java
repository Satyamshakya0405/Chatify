package com.example.chatifyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Toolbar mtoolbar;
    ViewPager mViewPager;
    SectionsPagerAdapter mAdapter;
    TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mtoolbar=findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Chatify");

        mViewPager=findViewById(R.id.main_tab_pager);
        mAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mTabLayout=findViewById(R.id.main_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

    }
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            sendtostart();
        }
    }

    private void sendtostart() {
        Intent startIntent=new Intent(getApplicationContext(),StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.main_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId()==R.id.main_logout_btn)
         {
             FirebaseAuth.getInstance().signOut();
             sendtostart();
         }
         if(item.getItemId()==R.id.main_accountsetting_btn);
        {
            Intent settingsintent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settingsintent);
        }
//         if(item.getItemId()==R.id.main_allusers_btn)
//        {
//            Intent usersIntent=new Intent(MainActivity.this,UsersActivity.class);
//                startActivity(usersIntent);
//        }
         return true;
    }
}
