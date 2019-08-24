package com.example.attendanceapp.init;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    EditText editnickname;
    TextView editemail,editname;
    ImageView profilepic;
    Button update;
    FirebaseAuth mAuth;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    public  final static  int GALLLERY_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editname=findViewById(R.id.editname);
        editnickname=findViewById(R.id.editnickname);
        update=findViewById(R.id.update);
        editemail=findViewById(R.id.editemail);
        profilepic=findViewById(R.id.profilepic);
        mAuth=FirebaseAuth.getInstance();
        final String uid=mAuth.getCurrentUser().getUid();
        myRef=database.getReference().child("User").child(uid);
        myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            String name=(String) dataSnapshot.child("name").getValue();
                                            String email=(String) dataSnapshot.child("email").getValue();
                                            String nickname=(String) dataSnapshot.child("nickname").getValue();

                                            editnickname.setText(nickname);
                                            editname.setText(name);
                                            editemail.setText(email);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    }
        );
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLLERY_CODE);


            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> cust1=new HashMap<>();


                cust1.put("nickname",editnickname.getText().toString());

                myRef.updateChildren(cust1, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLLERY_CODE && resultCode==RESULT_OK){

        }
    }
}
