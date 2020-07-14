package com.example.chatifyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class spalshScreenActivity extends AppCompatActivity {

    ImageView mSplashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spalsh_screen);
        mSplashImage=findViewById(R.id.splash_image);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainintent=new Intent(getApplicationContext(),MainActivity.class);
                Pair pair[]=new Pair[1];
                pair[0]=new Pair<View,String>(mSplashImage,"splash_image");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(spalshScreenActivity.this,pair);
                startActivity(mainintent);
                finish();
            }
        },2000);
    }
}
