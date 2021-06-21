package DoctorFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientapp.Doctor;
import com.example.patientapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.DoctorAdapter;
import Models.Chat;


public class ChatsFragment extends Fragment {


  private RecyclerView recyclerView;
  TextView tv_nochats;
  private DoctorAdapter doctorAdapter;

  String pmob;

  private List<Doctor> doctors;

  FirebaseUser firebaseUser;
  DatabaseReference reference;

  private List<String> doctorsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        tv_nochats=view.findViewById(R.id.textView);

        final String patientmobile = getArguments().getString("patient_mobile");
        pmob=patientmobile;

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        doctorsList=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                doctorsList.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Chat chat=dataSnapshot.getValue(Chat.class);

                    if(chat.getSender().equals(patientmobile) && (!doctorsList.contains(chat.getReceiver()))){
                        doctorsList.add(chat.getReceiver());
                    }

                    if(chat.getReceiver().equals(patientmobile) &&(!doctorsList.contains(chat.getSender()))){
                        doctorsList.add(chat.getSender());
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
        doctors=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Doctors");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                doctors.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Doctor doctor=dataSnapshot.getValue(Doctor.class);

                    for(String id: doctorsList){
                        if(doctor.getPhone().equals(id)){

                            doctors.add(doctor);
                        }
                    }
                }

                if(doctors.isEmpty()){
                    tv_nochats.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                else{
                    tv_nochats.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    doctorAdapter = new DoctorAdapter(getContext(),doctors,pmob);
                    recyclerView.setAdapter(doctorAdapter);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}