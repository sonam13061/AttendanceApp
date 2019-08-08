package com.example.attendanceapp.init;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.example.attendanceapp.HomeActivity;
import com.example.attendanceapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        hash();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(SplashActivity.this, selectactivity.class);
                    startActivity(intent);
                    finish();
                } else if(!mAuth.getCurrentUser().isEmailVerified()) {


                    Intent intent = new Intent(SplashActivity.this, selectactivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                                    }


            }



        }, 3000);
    }



    public  void hash() {

     try

    {
        PackageInfo info = getPackageManager().getPackageInfo("com.example.attendanceapp", PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch(
    PackageManager.NameNotFoundException e)

    {
        Log.d("errorrr", e.getMessage());
    } catch(
    NoSuchAlgorithmException e)

    {
        Log.d("errorrr", e.getMessage());
    }
}
}
