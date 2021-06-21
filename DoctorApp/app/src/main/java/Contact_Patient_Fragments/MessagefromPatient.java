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
import android.widget.Toast;

import com.example.doctorapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.PatientAdapter;
import Models.Chat;
import Models.Patient;


public class MessagefromPatient extends Fragment {


    private RecyclerView recyclerView;
    TextView tv_nochats;
    private PatientAdapter patientAdapter;

    String dmob;

    private List<Patient> patients;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private List<String> patientList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_messagefrom_patient, container, false);

        final String doctormobile = getArguments().getString("doctor_mobile");
        dmob=doctormobile;
        tv_nochats=view.findViewById(R.id.textView);

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        patientList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                patientList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Chat chat=dataSnapshot.getValue(Chat.class);

                    if(chat.getSender().equals(doctormobile) && (!patientList.contains(chat.getReceiver()))){
                        patientList.add(chat.getReceiver());
                    }

                    if(chat.getReceiver().equals(doctormobile) &&(!patientList.contains(chat.getSender()))){
                        patientList.add(chat.getSender());
                    }
                }

                readChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void readChats(){
        patients=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Patients");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                patients.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Patient patient=dataSnapshot.getValue(Patient.class);

                    for(String id: patientList){
                        if(patient.getPhone().equals(id)){

                            patients.add(patient);
                        }
                    }
                }

                if(patients.isEmpty()){
                    tv_nochats.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                else{
                    tv_nochats.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    patientAdapter = new PatientAdapter(getContext(),patients,dmob);
                    recyclerView.setAdapter(patientAdapter);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}