package Contact_Patient_Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.doctorapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.AppointmentsAdapter;
import Models.Appointmentdata;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link searchpatient#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchpatient extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView tv_nodata;
    RecyclerView recyclerView;
    List<Appointmentdata> appointmentdataList;
    AppointmentsAdapter appointmentsAdapter;
    String finalmobile;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public searchpatient() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchpatient.
     */
    // TODO: Rename and change types and number of parameters
    public static searchpatient newInstance(String param1, String param2) {
        searchpatient fragment = new searchpatient();
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
        View view=inflater.inflate(R.layout.fragment_searchpatient, container, false);

        appointmentdataList=new ArrayList<>();
        final String doctormobile = getArguments().getString("doctor_mobile");
        finalmobile=doctormobile;

        tv_nodata=(TextView) view.findViewById(R.id.textView);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                    if(appointmentdata.getDoctornumber().equals(finalmobile)){
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