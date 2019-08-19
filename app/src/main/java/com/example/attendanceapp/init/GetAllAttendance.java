package com.example.attendanceapp.init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceapp.Fragments.DashBoardFragment;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.Attendance;
import com.example.attendanceapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetAllAttendance extends AppCompatActivity {
    List<Attendance> attendlist = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(Constants.ATTENDENCE);
    ListView listView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_attendance);
        listView = findViewById(R.id.listt);
        getAllattend();
        adapter = new GetAllAttendance.MyAdapter(getApplicationContext(), attendlist);
        listView.setAdapter(adapter);


    }

    public void getAllattend() {

       // myRef =FirebaseDatabase.getInstance().getReference().child("Attendance");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
             for(DataSnapshot d:dataSnapshot.getChildren()) {
                 Attendance attendance = d.getValue(Attendance.class);
                 attendlist.add(attendance);


                 adapter.notifyDataSetChanged();
             }
                   // Toast.makeText(GetAllAttendance.this, "Data came", Toast.LENGTH_SHORT).show();
                }
             else{
                    Toast.makeText(GetAllAttendance.this, "Data is null", Toast.LENGTH_SHORT).show();
                    }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GetAllAttendance.this, "Data not came", Toast.LENGTH_SHORT).show();

            }
        });


//        Query query = myRef.orderByChild("userid").equalTo(uid);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    for (DataSnapshot d : dataSnapshot.getChildren()) {
//                        Attendance attendance = d.getValue(Attendance.class);
//                        attendlist.add(attendance);
//                        // Collections.sort(attendanceList);
//                        adapter.notifyDataSetChanged();
//                    }
//
//                }
//
//    }
//
//   @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
//
//
//
        }


        class MyAdapter extends ArrayAdapter<String> {
            Context context;
            List<Attendance> attendList;

            public MyAdapter(Context context, List<Attendance> resource) {
                super(context, R.layout.attendlist);
                this.context = context;
                this.attendList = resource;

            }


            @Override
            public int getCount() {
                return attendList.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.attendlist, parent, false);
                TextView t1, t2, t3, t4;
                t4 = view.findViewById(R.id.nmm);
                t1 = view.findViewById(R.id.date);
                t2 = view.findViewById(R.id.time);
                t3 = view.findViewById(R.id.course);
                Attendance attendance = attendList.get(position);
                t1.setText(dateformat(attendance.getDate()));
                t2.setText(attendance.getTime());
                t3.setText(attendance.getSubject());
                t4.setText(attendance.getName());



                return view;
            }
        }

        public String dateformat (String date){
            String day, mon, year;
            year = date.substring(0, 4);
            mon = date.substring(4, 6);
            switch (mon) {
                case "01":
                    mon = "jan";
                    break;
                case "02":
                    mon = "Feb";
                    break;
                case "03":
                    mon = "Mar";
                    break;
                case "04":
                    mon = "Apr";
                    break;
                case "05":
                    mon = "May";
                    break;
                case "06":
                    mon = "jun";
                    break;
                case "07":
                    mon = "jul";
                    break;
                case "08":
                    mon = "Aug";
                    break;
                case "09":
                    mon = "Sep";
                    break;
                case "10":
                    mon = "Oct";
                    break;
                case "11":
                    mon = "Nov";
                    break;
                case "12":
                    mon = "Dec";
                    break;

            }
            day = date.substring(6, 8);

            return day + "-" + mon + "-" + year;

        }
    }
