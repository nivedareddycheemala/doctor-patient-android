package Nav_Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doctorapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    MaterialEditText et1,et3,et7;
    TextView tv,tv4,tv6,tv7;
    MaterialButton materialButton,materialButton2;
    ProgressDialog pd;
    FirebaseDatabase database;
    DatabaseReference reference;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
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

        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        et1=(MaterialEditText) rootView.findViewById(R.id.usrname);
        et3=(MaterialEditText) rootView.findViewById(R.id.usrage);
        tv4=(TextView) rootView.findViewById(R.id.usrgender);
        tv6=(TextView) rootView.findViewById(R.id.usrdob);
        tv7=(TextView) rootView.findViewById(R.id.usrspeciality);
        et7=(MaterialEditText) rootView.findViewById(R.id.usraddress);
        tv=(TextView) rootView.findViewById(R.id.usrmobile);
        materialButton=(MaterialButton) rootView.findViewById(R.id.updateprofilebtn);
        materialButton2=(MaterialButton) rootView.findViewById(R.id.gotoHome);



        final String mobile = getArguments().getString("doctor_mobile");


       DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Doctors");
       ref.child(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String name=snapshot.child("name").getValue().toString();
               String age=snapshot.child("age").getValue().toString();
               String gender=snapshot.child("gend").getValue().toString();
               String dob=snapshot.child("dateofbirth").getValue().toString();
               String speciality=snapshot.child("speciality").getValue().toString();
               String address=snapshot.child("locationaddress").getValue().toString();
               et1.setText(name);
               tv.setText(mobile);
               et3.setText(age);
               tv4.setText(gender);
               tv6.setText(dob);
               tv7.setText(speciality);
               et7.setText(address);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


        database=FirebaseDatabase.getInstance();
        reference=database.getReference();


        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String et_name=et1.getText().toString();
                String et_age=et3.getText().toString();
                String et_address=et7.getText().toString();

                pd=new ProgressDialog(getActivity());
                pd.setMessage("Updating Please Wait...");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();

                reference.child("Doctors").child(mobile).child("name").setValue(et_name);
                reference.child("Doctors").child(mobile).child("age").setValue(et_age);
                reference.child("Doctors").child(mobile).child("locationaddress").setValue(et_address);
                et1.setText(et_name);
                et3.setText(et_age);
                et7.setText(et_address);
                pd.dismiss();

                Toast.makeText(getActivity(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                Home home=new Home();
                Bundle bundles=new Bundle();
                bundles.putString("doctor_mobile",mobile);
                bundles.putString("doctor_name",et_name);
                home.setArguments(bundles);
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction().replace(R.id.main_body,home,home.getTag()).commit();

            }
        });

        materialButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home home=new Home();
                Bundle bundlescancel=new Bundle();
                bundlescancel.putString("doctor_mobile",mobile);
                bundlescancel.putString("doctor_name",et1.getText().toString());
                home.setArguments(bundlescancel);
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction().replace(R.id.main_body,home,home.getTag()).commit();
            }
        });
        return rootView;
    }
}