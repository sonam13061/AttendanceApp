package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
   private Button login,forgot;
   private TextView register;
   private EditText uname,pass;
   private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uname=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        forgot=findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ForgotpassActivity.class);
                startActivity(intent);
            }
        });
        mAuth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(uname.getText().toString())) {
                    uname.setError("Email address field cannot be empty");
                    return;
                }

                 if(TextUtils.isEmpty(pass.getText().toString())){
                    pass.setError("Password field cannot be empty");
                    return;
                }



                  login(uname.getText().toString(),pass.getText().toString());


            }
        });
    }
    public void login(String email,String pass){
        mAuth.signInWithEmailAndPassword(email, pass)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            //Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "Welcome : "+user.getEmail(), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);

                            startActivity(intent);

                        } else {

                            // If sign in fails, display a message to the user.

                           // Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(LoginActivity.this, "Email address or password is invalid",

                                    Toast.LENGTH_SHORT).show();



                        }



                        // ...

                    }

                });
    }
}
