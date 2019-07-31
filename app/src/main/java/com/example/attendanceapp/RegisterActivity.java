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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private Button signup;
    private TextView click;
    private FirebaseAuth mAuth;
    EditText mail, pwd, confirm;

    public void init() {
        signup = findViewById(R.id.signup);
        click = findViewById(R.id.click);

        mail = findViewById(R.id.mail);

        pwd = findViewById(R.id.pw);
        confirm = findViewById(R.id.confirm);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(mail.getText().toString())) {
                    mail.setError("Email field cannot be empty ");
                    return;
                }

                if (TextUtils.isEmpty(pwd.getText().toString())) {
                    pwd.setError("password field cannot be empty ");
                    return;
                }
                if (TextUtils.isEmpty(confirm.getText().toString())) {
                    confirm.setError("Please confirm the password ");
                    return;
                }
                if (!pwd.getText().toString().equals(confirm.getText().toString())) {
                    confirm.setError("Password doesn't match");
                    return;
                }

                signup(mail.getText().toString(), pwd.getText().toString());


            }
        });


        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void signup(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Log.d("tag","already exist");
                        Toast.makeText(RegisterActivity.this, "You have already registered", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Log.d("pass","Length too short");
                        pwd.setError("password length cannot be less than 6 ");
                        confirm.setError("password length cannot be less than 6 ");
                    }

                    updateuser(null);


                } else {

                    FirebaseUser user =mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(RegisterActivity.this, "Verification link sent to"+mail.getText(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Email id doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    updateuser(user);

                }

            }
        });
    }

    public  void updateuser(FirebaseUser user){
        if(user==null){

        }
        else{
            Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();


        }
    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateuser(currentUser);
    }
}


