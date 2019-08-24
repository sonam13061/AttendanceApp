package com.example.attendanceapp.init;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.attendanceapp.R;

public class WelcomeActivity extends AppCompatActivity {
    LottieAnimationView animationView;
    TextView textView;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        animationView = findViewById(R.id.lottieanim);
        textView = findViewById(R.id.verification);
        btn = findViewById(R.id.clickk);
        animationView.playAnimation();



        Bundle extras = getIntent().getExtras();
        String email = extras.getString("email");
        textView.setText("You are registered successfully.Verfication link sent to "+email+".Please Verify your email" );
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginTeacherActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
    }

