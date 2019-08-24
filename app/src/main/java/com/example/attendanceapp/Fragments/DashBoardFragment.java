package com.example.attendanceapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.Attendance;
import com.example.attendanceapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import static android.app.DatePickerDialog.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 \
 * to handle interaction events.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment implements OnDateSetListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Attendance> attendanceList=new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(Constants.ATTENDENCE);

    DatabaseReference Ref=database.getReference();
    ListView listView;
    Button b1,b2;
    String date1,date2;
    MyAdapter adapter;
    ImageButton search;
    DatePickerDialog datePickerDialog;
    Calendar c;


    private OnFragmentInteractionListener mListener;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        listView= view.findViewById(R.id.list);
        b1=view.findViewById(R.id.from);
        b2=view.findViewById(R.id.to);
        search=view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date1==null){
                    Toast.makeText(getContext(), "Please select From date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(date2==null){
                    Toast.makeText(getContext(), "Please select To date", Toast.LENGTH_SHORT).show();
                    return;
                }
                getAttendencebydate(date1, date2);

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            c=Calendar.getInstance();
            int day=c.get(Calendar.DAY_OF_MONTH);
            int month=c.get(Calendar.MONTH);
            int year=c.get(Calendar.YEAR);
            datePickerDialog=new DatePickerDialog(getContext(), new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                    String d=String.format("%02d", mday);
                    String m=String.format("%02d", mmonth+1);
                    String y=String.format("%02d", myear);
                    date1=d+"-"+m+"-"+y;
                    b1.setText(date1);
                    date1=y+m+d;
                }
            },day,month,year);
                datePickerDialog.updateDate(year, month, day);
            datePickerDialog.show();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    c=Calendar.getInstance();
                    int day=c.get(Calendar.DAY_OF_MONTH);
                    int month=c.get(Calendar.MONTH);
                    int year=c.get(Calendar.YEAR);
                    datePickerDialog=new DatePickerDialog(getContext(), new OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                            String d=String.format("%02d", mday);
                            String m=String.format("%02d", mmonth+1);
                            String y=String.format("%02d", myear);
                            date2=d+"-"+m+"-"+y;
                            b2.setText(date2);
                            date2=y+m+d;
                        }
                    },day,month,year);
                datePickerDialog.updateDate(year, month, day);
                    datePickerDialog.show();


            }
        });
            adapter=new MyAdapter(getContext(), attendanceList);

            myRef.getRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        listView.setAdapter(adapter);
        getAllattend();


                // Inflate the layout for this fragment
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void getAllattend(){
        String uid;
        uid=FirebaseAuth.getInstance().getUid();
        Query query=myRef.orderByChild("userid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for(DataSnapshot d:dataSnapshot.getChildren()){
                        Attendance attendance=d.getValue(Attendance.class);
                        attendanceList.add(attendance);
                       // Collections.sort(attendanceList);
                        adapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        List<Attendance> attendanceList=new ArrayList<>();

    public MyAdapter(Context context, List<Attendance> resource) {
        super(context, R.layout.attendance_item);
        this.context=context;
        this.attendanceList=resource;

    }


    @Override
    public int getCount() {
        return attendanceList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=getLayoutInflater().inflate(R.layout.attendance_item, parent,false);
        TextView t1,t2,t3;
        t1=view.findViewById(R.id.date);
        t2=view.findViewById(R.id.time);
        t3=view.findViewById(R.id.course);
        Attendance attendance=attendanceList.get(position);
        t1.setText(dateformat(attendance.getDate()));
        t2.setText(attendance.getTime());
        t3.setText(attendance.getSubject());

        return view;
    }
}
List<Attendance> filteredlist=new ArrayList<>();
public void getAttendencebydate(String d1,String d2){

    for(Attendance a:attendanceList){
        if(a.getDate().compareTo(d1)>=0&&a.getDate().compareTo(d2)<=0){
            filteredlist.add(a);
        }
        listView.setAdapter(null);
        MyAdapter adapter=new MyAdapter(getContext(), filteredlist);
        listView.setAdapter(adapter);
    }

}

public  String dateformat(String date){
        String day,mon,year;
        year=date.substring(0,4);
        mon=date.substring(4,6);
        switch (mon){
            case "01":mon="jan"; break;
            case "02":mon="Feb"; break;
            case "03":mon="Mar"; break;
            case "04":mon="Apr"; break;
            case "05":mon="May"; break;
            case "06":mon="jun"; break;
            case "07":mon="jul"; break;
            case "08":mon="Aug"; break;
            case "09":mon="Sep"; break;
            case "10":mon="Oct"; break;
            case "11":mon="Nov"; break;
            case "12":mon="Dec"; break;

        }
        day=date.substring(6,8);

        return day+"-"+mon+"-"+year;


}

}
