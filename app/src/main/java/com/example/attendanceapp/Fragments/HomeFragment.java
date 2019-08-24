package com.example.attendanceapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.Attendance;
import com.example.attendanceapp.model.User;
import com.example.attendanceapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG ="" ;
    ProgressBar progressBar;
    private TextView t1,t2;
    private Button btn;
     String name ;
     String naam;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(Constants.ATTENDENCE);
    DatabaseReference ref=database.getReference(Constants.User);
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static final String MTAG="";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar=view.findViewById(R.id.progress);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();


        //FirebaseUser u=mAuth.getCurrentUser();

        t1=view.findViewById(R.id.welcome);
        t2=view.findViewById(R.id.date);
       final String uid= FirebaseAuth.getInstance().getUid();
        ref.child(uid).addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Log.d(TAG, "OnDataChange Success");

                String nickname = (String) dataSnapshot.child("nickname").getValue();

                t1.setText("Welcome "+nickname);

            }



            @Override

            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "OnDataCancelled");

            }

        });


       // t1.setText("Welcome "+prefs.getString(Constants.NAME,""));
         //t1.setText("Welcome "+naam);
      //  t1.setText("Welcome"+u.getName());
        btn=view.findViewById(R.id.mark);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 btn.setEnabled(false);
                btn.setText("You're marked for the day");
                markAttendance();


            }
        });
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMM-yyyy");
        String Currentdate =dateFormat.format(new Date());
        t2.setText(Currentdate);
        getAttendance();





        // Inflate the layout for this fragment
        return view;
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
            prefs = context.getSharedPreferences("prefs",MODE_PRIVATE);
            editor = prefs.edit();
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


        public void  getcurrentusername( final  FirebaseUser user){

                if(user!=null) {
                    ref = database.getReference().child("User").child(user.getUid()).child("name");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot d:dataSnapshot.getChildren()) {
                              User  u = d.getValue(User.class);
                              naam=u.getName();
                            }

                            //Log.d(MTAG,name);
                            //System.out.print(name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{

                }
        }


    private void markAttendance(){

        Log.d(TAG, "markAttendance function");

        String uid = mAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reff = database.getReference(Constants.User);

        reff.child(uid).addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d(TAG, "Getting name and course");

                String name = (String)(dataSnapshot.child("name").getValue());

                String course = (String)(dataSnapshot.child("course").getValue());

                Attenddetails(name,course);

            }



            @Override

            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "Fail to get name and course");

            }

        });



    }
    public void Attenddetails(String name,String course){
        String year,month,day,time,uid;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        String Currentdateandtime=simpleDateFormat.format(new Date());
        String[] arr=Currentdateandtime.split("/");
        year=arr[0];
        month=arr[1];
        day=arr[2];
        time=arr[3];
       // course=prefs.getString(Constants.COURSE, "not found");
        //name=prefs.getString(Constants.NAME, "not found");
        String date=year+month+day;

        uid= FirebaseAuth.getInstance().getUid();
        Attendance attendance=new Attendance(date, time, course, uid, name, date+"_"+uid);
        myRef.push().setValue(attendance);

        
        editor.putInt(Constants.TODAY_ATTEND, Integer.parseInt(year+month+day));
        editor.commit();


    }
    public  void getAttendance(){
        String year,mon,day,date,uid;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        String currentdate=simpleDateFormat.format(new Date());
        String[] arr=currentdate.split("/");
         day=arr[2];
         mon=arr[1];
         year=arr[0];
            progressBar.setVisibility(View.VISIBLE);
         date=year+mon+day;
         uid= FirebaseAuth.getInstance().getUid();
        Query query=myRef.orderByChild("date_userid").equalTo(date+"_"+uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        t1.setVisibility(View.VISIBLE);
                        t2.setVisibility(View.VISIBLE);
                        btn.setVisibility(View.VISIBLE);
                        btn.setEnabled(false);
                        progressBar.setVisibility(View.GONE);
                        btn.setText("You're already marked");
                    }
                    else{
                        t1.setVisibility(View.VISIBLE);
                        t2.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        btn.setVisibility(View.VISIBLE);
                        btn.setEnabled(true);
                        btn.setText("Mark my attendance");
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
