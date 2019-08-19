package com.example.attendanceapp.init;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceapp.Fragments.DashBoardFragment;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.Attendance;
import com.example.attendanceapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner s;
    String course,userid,nm,crs="------Select Course-----";
    String sub;
    List<Attendance> attendlist = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(Constants.ATTENDENCE);
    ListView listView;

    MyAdapter adapter;
    DatePickerDialog datePickerDialog;
    Calendar c;
    String date1,date2;
    EditText name,uid;
    Button pickto,pickfrom,search;
    List<String> courselist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        s=findViewById(R.id.spin);
        uid=findViewById(R.id.uid);
        name=findViewById(R.id.nm);
        pickfrom=findViewById(R.id.pickfrom);
        pickto=findViewById(R.id.pickto);
        search=findViewById(R.id.search);
        listView=findViewById(R.id.filter);
        getSupportActionBar().hide();

        getAllattend();
        adapter = new FilterActivity.MyAdapter(getApplicationContext(),attendlist);
        listView.setAdapter(adapter);

        courselist.add("------Select Course-----");
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(FilterActivity.this, android.R.layout.simple_list_item_1,courselist);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);
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
                Toast.makeText(FilterActivity.this, "Error in courses", Toast.LENGTH_SHORT).show();

            }
        });

        pickfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                c= Calendar.getInstance();
                int day=c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);
                datePickerDialog=new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                        String d=String.format("%02d", mday);
                        String m=String.format("%02d", mmonth+1);
                        String y=String.format("%02d", myear);
                        date1=d+"-"+m+"-"+y;
                        pickfrom.setText(date1);
                        date1=y+m+d;
                    }
                },day,month,year);
                datePickerDialog.updateDate(year, month, day);
                datePickerDialog.show();


            }
        });
        pickto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c=Calendar.getInstance();
                int day=c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);
                datePickerDialog=new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                        String d=String.format("%02d", mday);
                        String m=String.format("%02d", mmonth+1);
                        String y=String.format("%02d", myear);
                        date2=d+"-"+m+"-"+y;
                        pickto.setText(date2);
                        date2=y+m+d;
                    }
                },day,month,year);
                datePickerDialog.updateDate(year, month, day);
                datePickerDialog.show();

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userid = uid.getText().toString();
                nm = name.getText().toString();
                sub=s.getSelectedItem().toString();

                List<Attendance> filteredlist = new ArrayList<>();
                for(Attendance a:attendlist){

                    // 1.Name
                    if(TextUtils.isEmpty(userid) && date1==null && date2==null && sub.equals(crs) && !TextUtils.isEmpty(nm)){
                        if(a.getName().equals(nm)) {


                            // filteredlist.clear();
                            filteredlist.add(a);
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                        if(!a.getName().equals(nm)){
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }





                    }
                    //2. Date
                        if ( TextUtils.isEmpty(nm) && TextUtils.isEmpty(userid) && sub.equals(crs) && date1!=null && date2!=null ) {

                            if(a.getDate().compareTo(date1) >= 0 && a.getDate().compareTo(date2) <= 0) {
                                //filteredlist.clear();
                                filteredlist.add(a);
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);
                            }
                            else{
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);

                            }
                        }
                        //3. uid
                        if(date1==null && date2==null && sub.equals(crs) && TextUtils.isEmpty(nm) &&!TextUtils.isEmpty(userid) ){
                            if(a.getUserid().equals(userid)) {


                                filteredlist.add(a);
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);
                            }
                            if(!a.getUserid().equals(userid)){
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);

                            }

                    }
                        //4. Course
                        if( TextUtils.isEmpty(nm) && TextUtils.isEmpty(userid) && date1==null && date2==null ){
                            if (a.getSubject().equals(course)&& !sub.equals(crs)) {
                                filteredlist.add(a);
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);
                            }
                            else {
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);

                            }
                        }
                        //5. Name+date
                        if(  TextUtils.isEmpty(userid) && sub.equals(crs) &&!TextUtils.isEmpty(nm) && date1!=null && date2!=null  ){
                            if( a.getName().equals(nm)  && a.getDate().compareTo(date1) >= 0 && a.getDate().compareTo(date2) <= 0)
                            {
                                filteredlist.add(a);
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);
                            }
                            else {
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);

                            }


                        }

                        //6. Name +course
                        if( TextUtils.isEmpty(userid)  && date1==null && date2==null && !TextUtils.isEmpty(nm)  && !sub.equals(crs)){

                            if( a.getName().equals(nm) && a.getSubject().equals(course)) {
                                filteredlist.add(a);
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);
                            }

                             else {
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);

                            }
                        }
                        //7. Name+uid
                        if(!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(nm) && sub.equals(crs) && date1==null && date2==null ){
                            if(a.getName().equals(nm) && a.getUserid().equals(userid)){
                                filteredlist.add(a);
                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);

                            }
                            else {

                                listView.setAdapter(null);
                                MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                                listView.setAdapter(adapter);
                            }
                        }

                        // 8. date+course

                    if(TextUtils.isEmpty(nm) && TextUtils.isEmpty(userid) && date1!=null && date2!=null && !sub.equals(crs)  ){
                        if( a.getSubject().equals(course) && a.getDate().compareTo(date1) >= 0 && a.getDate().compareTo(date2) <= 0){
                            filteredlist.add(a);
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                        else{
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                    }
                    //9. date+ uid
                    if(TextUtils.isEmpty(nm) && !TextUtils.isEmpty(userid) && date1!=null && date2!=null && sub.equals(course)){
                        if( a.getUserid().equals(userid)  && a.getDate().compareTo(date1) >= 0 && a.getDate().compareTo(date2) <= 0){
                            filteredlist.add(a);
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                        else{
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }

                    }
                    //10. course+uid
                    if( !TextUtils.isEmpty(userid) && date1==null && date2==null && TextUtils.isEmpty(nm)  ){

                        if( a.getUserid().equals(userid)    && !sub.equals(crs) && a.getSubject().equals(course)) {
                            filteredlist.add(a);
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                        else{
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                    }
                    //11. name +date+course
                    if(!TextUtils.isEmpty(nm) && TextUtils.isEmpty(userid) && date1!=null && date2!=null && !sub.equals(crs)  ){
                        if( a.getSubject().equals(course) && a.getDate().compareTo(date1) >= 0 && a.getDate().compareTo(date2) <= 0 && a.getName().equals(nm)){
                            filteredlist.add(a);
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                        else{
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                    }
                    //12. name +date +uid

                    if(!TextUtils.isEmpty(nm) && !TextUtils.isEmpty(userid) && date1!=null && date2!=null && sub.equals(crs)  ){
                        if( a.getName().equals(nm) && a.getUserid().equals(userid) && a.getDate().compareTo(date1) >= 0 && a.getDate().compareTo(date2) <= 0){
                            filteredlist.add(a);
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                        else{
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                    }
                    //13. Name+uid+course
                    if(!TextUtils.isEmpty(nm) && !TextUtils.isEmpty(userid) && date1==null && date2==null && !sub.equals(crs)  ){
                        if( a.getName().equals(nm) && a.getUserid().equals(userid) && a.getSubject().equals(course)){
                            filteredlist.add(a);
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                        else{
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                    }
                    //14. date+course+uid
                    if(TextUtils.isEmpty(nm) && !TextUtils.isEmpty(userid) && date1!=null && date2!=null && !sub.equals(crs)  ){
                        if(a.getSubject().equals(course) && a.getName().equals(nm) && a.getUserid().equals(userid) && a.getDate().compareTo(date1) >= 0 && a.getDate().compareTo(date2) <= 0){
                            filteredlist.add(a);
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                        else{
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                    }

                    //15. name+date+course+uid

                    if(!TextUtils.isEmpty(nm) && !TextUtils.isEmpty(userid) && date1!=null && date2!=null && !sub.equals(crs)  ){
                        if( a.getSubject().equals(course) && a.getName().equals(nm) && a.getUserid().equals(userid) && a.getDate().compareTo(date1) >= 0 && a.getDate().compareTo(date2) <= 0){
                            filteredlist.add(a);
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                        else{
                            listView.setAdapter(null);
                            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredlist);
                            listView.setAdapter(adapter);
                        }
                    }








                }








            }

        });

    }
    public void getAllattend() {

        // myRef =FirebaseDatabase.getInstance().getReference().child("Attendance");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Attendance attendance = d.getValue(Attendance.class);
                        attendlist.add(attendance);


                        adapter.notifyDataSetChanged();
                    }
                    // Toast.makeText(GetAllAttendance.this, "Data came", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FilterActivity.this, "Data is null", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FilterActivity.this, "Data not came", Toast.LENGTH_SHORT).show();

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
