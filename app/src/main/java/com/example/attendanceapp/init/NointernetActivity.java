package com.example.attendanceapp.init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.attendanceapp.HomeActivity;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NointernetActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    Button retry,exit,settings;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    User userr;
    String value;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernet);
        getSupportActionBar().hide();
        lottieAnimationView=findViewById(R.id.lottie);
        mAuth=FirebaseAuth.getInstance();
        lottieAnimationView.playAnimation();
        retry=findViewById(R.id.retry);
        exit=findViewById(R.id.exit);
        settings=findViewById(R.id.settings);
        progressBar=findViewById(R.id.progress);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Settings.ACTION_SETTINGS);
                startActivityForResult(intent, 0);
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser user=mAuth.getCurrentUser();

                progressBar.setVisibility(View.VISIBLE);
                if(!isNetworkAvailable()){
                  //  progressBar.setVisibility(View.GONE);


                }

                if ( isNetworkAvailable() && user == null) {
                    Intent intent = new Intent(NointernetActivity.this, selectactivity.class);
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.GONE);
                }  if (isNetworkAvailable() && user!= null && !user.isEmailVerified()) {

                    Intent intent = new Intent(NointernetActivity.this, selectactivity.class);
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.GONE);
                }
                if( isNetworkAvailable() && user!=null && user.isEmailVerified()){
                    final String teacher="Teacher";
                    String student="Student";
                    // final String[] value = new String[1];
                    myRef = database.getReference().child("User").child(user.getUid());
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                            userr= dataSnapshot.getValue(User.class);



                            //  Toast.makeText(SplashActivity.this, userr.getName(), Toast.LENGTH_SHORT).show();

                            if(userr!=null) {
                                if (userr.getUsertype().equals(teacher)) {

                                    Intent intent = new Intent(NointernetActivity.this, HomeTeacherActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Intent intent = new Intent(NointernetActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                            else {
                                Toast.makeText(NointernetActivity.this, "value is null", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }


                            //value =  dataSnapshot.getValue(String.class);
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            value = null;
                            Log.d("sonam", databaseError.toString());
                        }
                    });



                }


            }


        });

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
