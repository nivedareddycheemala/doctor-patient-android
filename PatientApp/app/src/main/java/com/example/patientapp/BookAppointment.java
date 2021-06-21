package com.example.patientapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.AppointmentsAdapter;
import Models.Appointmentdata;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookAppointment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookAppointment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tv_nodata;
    FloatingActionButton bookappointment;
    RecyclerView recyclerView;

    List<Appointmentdata> appointmentdataList;
    List<Appointmentdata> personalappointments;

    List<String> patients;
    String final_name;

    AppointmentsAdapter appointmentsAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookAppointment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookAppointment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookAppointment newInstance(String param1, String param2) {
        BookAppointment fragment = new BookAppointment();
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
        View view=inflater.inflate(R.layout.fragment_book_appointment, container, false);

        appointmentdataList=new ArrayList<>();
        personalappointments=new ArrayList<>();
        patients=new ArrayList<>();

        final String patientmobile = getArguments().getString("patient_mobile");
        final String patientname = getArguments().getString("patient_name");
        final_name=patientname;


        recyclerView=(RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tv_nodata=(TextView) view.findViewById(R.id.textView);
        bookappointment=(FloatingActionButton) view.findViewById(R.id.fab);

        bookappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),DetailsforAppointment.class);
                i.putExtra("patient_mobile",patientmobile);
                i.putExtra("patient_name",patientname);
                startActivity(i);
            }
        });


        readAppointments();


        return view;
    }

    private void readAppointments(){

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Appointments");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                appointmentdataList.clear();
                int temp=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Appointmentdata appointmentdata=dataSnapshot.getValue(Appointmentdata.class);
                    if(appointmentdata.getPatientname().equals(final_name)){
                        temp=temp+1;
                        tv_nodata.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        appointmentdataList.add(appointmentdata);
                    }
                }

                if(temp==0){
                    tv_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                appointmentsAdapter=new AppointmentsAdapter(getContext(),appointmentdataList);
                recyclerView.setAdapter(appointmentsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}