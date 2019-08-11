package com.example.attendanceapp.init;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.attendanceapp.R;

public class selectactivity extends AppCompatActivity {
    ImageView teacher,student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectactivity);
        teacher=findViewById(R.id.teacher);
        student=findViewById(R.id.stud);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(selectactivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(selectactivity.this,LoginTeacherActivity.class);
                startActivity(intent);


            }
        });
    }
}
