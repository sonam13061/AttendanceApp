package com.example.attendanceapp.init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceapp.HomeActivity;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.Student;
import com.example.attendanceapp.model.User;
import com.example.attendanceapp.utils.Constants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
   private Button login,forgot;
   private TextView register;
   SharedPreferences prefs;
   SharedPreferences.Editor editor;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(Constants.STUDENT);
   private EditText uname,pass;
   private FirebaseAuth mAuth;
   public CallbackManager mCallbackManager;
   public static final String TAG="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        prefs=getSharedPreferences("prefs", MODE_PRIVATE);
        editor=prefs.edit();
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
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
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
    public  void  getuserdata(FirebaseUser user){
        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User s=dataSnapshot.getValue(User.class);
                editor.putString(Constants.NAME, s.getName());
                editor.putString(Constants.EMAIL,s.getEmail());
                editor.putString(Constants.COURSE, s.getCourse());
                editor.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void login(String email,String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            //Log.d(TAG, "signInWithEmail:success");
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                getuserdata(user);
                                Authcheck();

                                Toast.makeText(LoginActivity.this, "Welcome : " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            // If sign in fails, display a message to the user.

                            // Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(LoginActivity.this, "Email address or password is invalid",

                                    Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }




                        // ...








        public void Authcheck(){


            String usertype = prefs.getString(Constants.USERTYPE, "");
            String usertype1 = "Teacher";
            String usertype2 = "Student";
            if (usertype.equals(usertype2)) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                startActivity(intent);

            } else if (usertype.equals(usertype1)) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Please login through Teacher portal", Toast.LENGTH_SHORT).show();
            }

        }



    public  void onfblogin(View view){
         mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }



            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
// ...

    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //mAuth.getCurrentUser().isEmailVerified();
                            Toast.makeText(LoginActivity.this, "Welcome "+user.getEmail(), Toast.LENGTH_SHORT).show();

                            finish();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void onAuthsuccess(FirebaseUser user){
        if(user!=null){
            myRef=FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid()).child("usertype");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value=dataSnapshot.getValue(String.class);
                    if(value.equals("Teacher")){
                        Intent intent=new Intent(LoginActivity.this,HomeTeacherActivity.class);
                        startActivity(intent);
                    }
                    else if(value.equals("Student"))
                    {
                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
