package com.example.attendanceapp.init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.Student;
import com.example.attendanceapp.model.Teacher;
import com.example.attendanceapp.model.User;

import com.example.attendanceapp.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterTeacherActivity extends AppCompatActivity {
    private Button signup;
    private TextView click;
    private FirebaseAuth mAuth;
    public static final String MTAG="";


    EditText nm, mail, pwd, confirm,pin;
    String name,email,pass,usertype1,key,course,nickname;
    int pinn;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(Constants.User);

    public void init() {
        signup = findViewById(R.id.signupteacher);
        click = findViewById(R.id.clickteacher);
        nm=findViewById(R.id.nameteacher);
        pin=findViewById(R.id.pin);
        prefs=getSharedPreferences("prefs", MODE_PRIVATE);
        editor=prefs.edit();

        mail = findViewById(R.id.mailteacher);

        pwd = findViewById(R.id.pwteacher);
        confirm = findViewById(R.id.confirmteacher);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);
        init();
        DatabaseReference myRefs=database.getReference(Constants.PIN);
        myRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    key= String.valueOf(d.getValue());

                }
                System.out.println(key);
                Log.d(MTAG ,key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=mail.getText().toString();
                name=nm.getText().toString();
                pass=pwd.getText().toString();
                pinn= Integer.parseInt(pin.getText().toString());
                usertype1="Teacher";
                course=null;
                nickname=nm.getText().toString();

                if(pinn!=Integer.parseInt(key)){
                    pin.setError("pin is wrong");
                    return;
                }
                if(TextUtils.isEmpty(pin.getText().toString())) {
                    pin.setError("Email field cannot be empty ");
                    return;


                }
                if (TextUtils.isEmpty(name)) {
                    mail.setError("Email field cannot be empty ");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    mail.setError("Email field cannot be empty ");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
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
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    mail.setError("Please enter valid email");

                    mail.requestFocus();

                    return;

                }

                if(pass.length()<6){

                    pwd.setError("Please enter password of minimum 6 digits");

                    pwd.requestFocus();

                    return;

                }
                signup(name,email,pass, usertype1,pinn,course,nickname);




            }
        });
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterTeacherActivity.this,LoginTeacherActivity.class);
                startActivity(intent);
            }
        });

    }
    public void signup(final  String name, final String email , final String password, final String usertype, final int pinn, final  String course, final String nickname) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Log.d("tag","already exist");
                        Toast.makeText(RegisterTeacherActivity.this, "You have already registered", Toast.LENGTH_SHORT).show();
                    }


                    //updateuser(null);

                } else {

                    final FirebaseUser user =mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                //Toast.makeText(RegisterActivity.this, "Verification link sent to"+mail.getText(), Toast.LENGTH_SHORT).show();
                                User s=new User(name, email,course, usertype, pinn,nickname);
                                myRef.child(user.getUid()).setValue(s);
                                editor.putString(Constants.NAME, name);
                                editor.putString(Constants.EMAIL, email);
                               // editor.putString(Constants, pinn);
                                editor.putString(Constants.USERTYPE, usertype);
                                editor.commit();
                                Intent intent=new Intent(RegisterTeacherActivity.this,WelcomeActivity.class);
                                intent.putExtra("email", user.getEmail());
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(RegisterTeacherActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //updateuser(user);

                }

            }
        });
    }

}
