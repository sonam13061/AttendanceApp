package com.example.attendanceapp.init;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_FROM_GALLEY = 1;
    EditText editnickname;
    TextView editemail, editname;
    ImageView profilepic;
    Button update;
    Uri resultUri;
    FirebaseAuth mAuth;

    private StorageReference mStorage;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    public final static int GALLLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editname = findViewById(R.id.editname);
        editnickname = findViewById(R.id.editnickname);
        update = findViewById(R.id.update);
        editemail = findViewById(R.id.editemail);
        profilepic = findViewById(R.id.profilepic);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        mStorage = FirebaseStorage.getInstance().getReference().child("profile_pics");
        final String uid = mAuth.getCurrentUser().getUid();
        myRef = database.getReference().child("User").child(uid);
        myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String name = (String) dataSnapshot.child("name").getValue();
                                            String email = (String) dataSnapshot.child("email").getValue();
                                            String nickname = (String) dataSnapshot.child("nickname").getValue();

                                            editnickname.setText(nickname);
                                            int position = editnickname.length();
                                            Editable etext = editnickname.getText();
                                            Selection.setSelection(etext, position);
                                            editname.setText(name);
                                            editemail.setText(email);
                                            String profile = (String) dataSnapshot.child("profilepic").getValue();
                                            if (profile.equals("none")) {
                                                profilepic.setImageResource(R.drawable.pic);
                                            } else {
                                                profilepic.setImageURI(Uri.parse(profile));
                                                //  Toast.makeText(ProfileActivity.this, "uri", Toast.LENGTH_SHORT).show();
                                            }
                                        }


            @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    }
        );

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.customdialog);
                TextView delete = dialog.findViewById(R.id.remove);
                TextView change = dialog.findViewById(R.id.Change);
                dialog.show();
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  HashMap<String,Object> cust2=new HashMap<>();
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Are you sure?")
                                .setTitle("Remove profile picture")

                                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }

                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HashMap<String, Object> cust2 = new HashMap<>();
                                        cust2.put("profilepic", "none");
                                        myRef.updateChildren(cust2, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                Toast.makeText(ProfileActivity.this, "Profile picture deleted", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                        profilepic.setImageResource(R.drawable.pic);
                                        dialog.dismiss();


                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        intent.setType("image/*");
//                        startActivityForResult(intent, GALLLERY_CODE);
                        permissionmethod();
                        dialog.dismiss();
                    }
                });


            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> cust1 = new HashMap<>();
                cust1.put("nickname", editnickname.getText().toString());
                if (resultUri != null) {


                    cust1.put("profilepic", resultUri.toString());


                    StorageReference imageref = mStorage.child("profile_pics").child(resultUri.getLastPathSegment());
                    imageref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Toast.makeText(ProfileActivity.this, "Profile picture updated", Toast.LENGTH_SHORT).show();


                        }
                    });
                }
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
        if (requestCode == GALLLERY_CODE && resultCode == RESULT_OK) {
            Uri imageuri = data.getData();

            CropImage.activity(imageuri)
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                profilepic.setImageURI(resultUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public void permissionmethod() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ProfileActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLEY
            );
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_FROM_GALLEY);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLEY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLEY);
                } else {
                    Toast.makeText(this, "Sorry, You didn't allow the app to access gallery", Toast.LENGTH_SHORT).show();
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }

    }
}
