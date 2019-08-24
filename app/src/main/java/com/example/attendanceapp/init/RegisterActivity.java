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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.Student;
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

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button signup;
    private TextView click;
    private FirebaseAuth mAuth;
    private Spinner spinner;
    EditText nm, mail, pwd, confirm;
    String name,email,pass,course,usertype,nickname;
    int pin;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(Constants.User);
    List<String> courselist=new ArrayList<>();
    String course_s="   ------Select Course------";

    public void init() {
        signup = findViewById(R.id.signup);
        click = findViewById(R.id.click);
        nm=findViewById(R.id.name);
        spinner=findViewById(R.id.s1);
        prefs=getSharedPreferences("prefs", MODE_PRIVATE);
        editor=prefs.edit();

        mail = findViewById(R.id.mail);

        pwd = findViewById(R.id.pw);
        confirm = findViewById(R.id.confirm);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        init();
        courselist.add("   ------Select Course------");
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1,courselist);
            spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        DatabaseReference courseRef=database.getReference(Constants.COURSES);
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    courselist.add(String.valueOf(d.getValue()));
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "Error in courses", Toast.LENGTH_SHORT).show();

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=mail.getText().toString();
                name=nm.getText().toString();
                pass=pwd.getText().toString();
                course=spinner.getSelectedItem().toString();
                pin= 0;
                usertype="Student";
                nickname=nm.getText().toString();


                if(course.equals(course_s)) {
                    Toast.makeText(RegisterActivity.this, "Please select course", Toast.LENGTH_SHORT).show();
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

                signup(name,email, pass,course,usertype,pin,nickname );


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

    public void signup(final  String name, final String email, final String password, final String course, final  String type, final int pin, final String nickname) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Log.d("tag","already exist");
                        Toast.makeText(RegisterActivity.this, "You have already registered", Toast.LENGTH_SHORT).show();
                    }


                    //updateuser(null);

                } else {

                    final FirebaseUser user =mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                //Toast.makeText(RegisterActivity.this, "Verification link sent to"+mail.getText(), Toast.LENGTH_SHORT).show();
                                 User s=new User(name, email, course,type,pin,nickname);
                                myRef.child(user.getUid()).setValue(s);
                                editor.putString(Constants.NAME, name);
                                editor.putString(Constants.EMAIL, email);
                                editor.putString(Constants.COURSE, course);
                                editor.putString(Constants.USERTYPE,type);

                                editor.commit();
                                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                intent.putExtra("email", user.getEmail());
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //updateuser(user);

                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i>0){
            course=courselist.get(i);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /*public  void updateuser(FirebaseUser user){
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
    }*/
}


