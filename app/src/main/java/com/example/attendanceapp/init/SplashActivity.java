package com.example.attendanceapp.init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaRouter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.attendanceapp.HomeActivity;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.User;
import com.example.attendanceapp.utils.Constants;
import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    SharedPreferences prefs;
    User userr;
        String value;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


   // DatabaseReference myRef = database.getReference(Constants.User);
//    public CallbackManager mCallbackManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth=FirebaseAuth.getInstance();

        getSupportActionBar().hide();
        prefs=getSharedPreferences("prefs", MODE_PRIVATE);
        hash();

    // onCheck(user);
     splash();

        // value=prefs.getString(Constants.USERTYPE,"");


    }

    public  void splash(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                final FirebaseUser user=mAuth.getCurrentUser();




                if (user == null) {
                    Intent intent = new Intent(SplashActivity.this, selectactivity.class);
                    startActivity(intent);
                    finish();
                }  if ( user!= null && !user.isEmailVerified()) {

                    Intent intent = new Intent(SplashActivity.this, selectactivity.class);
                    startActivity(intent);
                    finish();
                }
                if(user!=null && user.isEmailVerified()){
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

                                    Intent intent = new Intent(SplashActivity.this, HomeTeacherActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else {
                                Toast.makeText(SplashActivity.this, "value is null", Toast.LENGTH_SHORT).show();
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
