package com.example.attendanceapp.init;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.attendanceapp.Fragments.HomeFragment;
import com.example.attendanceapp.HomeActivity;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.User;
import com.example.attendanceapp.utils.Constants;
import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeTeacherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button getall,getfilterd;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    TextView name,email;
    DatabaseReference myRef=database.getReference(Constants.User);
    TextView welteacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getall=findViewById(R.id.getall);

        getfilterd=findViewById(R.id.getfiltered);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String uid=FirebaseAuth.getInstance().getUid();

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                 String nickname =(String) dataSnapshot.child("nickname").getValue();


                welteacher.setText("Welcome " + nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        welteacher=findViewById(R.id.welteacher);


        getall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeTeacherActivity.this,GetAllAttendance.class);
                startActivity(intent);
            }
        });
        getfilterd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeTeacherActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View Headerview=navigationView.getHeaderView(0);
        name=Headerview.findViewById(R.id.nm);
        email=Headerview.findViewById(R.id.em);
        setheader();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    public  void setheader(){
        FirebaseUser user=mAuth.getCurrentUser();
        String uid=user.getUid();

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nm= (String) dataSnapshot.child("name").getValue();
                String em=(String) dataSnapshot.child("email").getValue();
                name.setText(nm);
                email.setText(em);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_teacher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent=new Intent(HomeTeacherActivity.this,ProfileActivity.class);
            startActivity(intent);
            // Handle the camera action
        }
        else if(id==R.id.nav_logout){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?")
                    .setTitle("Logout")

                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent=new Intent(HomeTeacherActivity.this,LoginTeacherActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(HomeTeacherActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog alert=builder.create();
            alert.show();

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
