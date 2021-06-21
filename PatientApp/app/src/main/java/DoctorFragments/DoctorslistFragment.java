package DoctorFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.patientapp.Doctor;
import com.example.patientapp.Patient;
import com.example.patientapp.R;
import com.example.patientapp.profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Adapter.DoctorAdapter;


public class DoctorslistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorsList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DoctorslistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters

    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
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



        View rootview=inflater.inflate(R.layout.fragment_doctorslist,container,false);


        recyclerView=(RecyclerView) rootview.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        doctorsList=new ArrayList<>();

        readDoctors();

        return rootview;
    }

    private void readDoctors() {

        final String patientmobile = getArguments().getString("patient_mobile");

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Doctors");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                doctorsList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Doctor doctors=dataSnapshot.getValue(Doctor.class);

                    doctorsList.add(doctors);

                }

                doctorAdapter =new DoctorAdapter(getContext(),doctorsList,patientmobile);
                recyclerView.setAdapter(doctorAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}