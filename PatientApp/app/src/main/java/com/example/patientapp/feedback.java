package com.example.patientapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import Models.Feedback;

public class feedback extends Fragment {

    EditText feedback;
    MaterialButton sendFeedback;

    DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.fragment_feedback, container, false);

        final String patientmobile = getArguments().getString("patient_mobile");
        final String patientname = getArguments().getString("patient_name");

        feedback=(EditText) view.findViewById(R.id.feedback);

        sendFeedback=(MaterialButton) view.findViewById(R.id.sendFeedback);

        reference=FirebaseDatabase.getInstance().getReference();



        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String feedbackStr=feedback.getText().toString();

                if(feedbackStr.isEmpty()){
                    Toast.makeText(getActivity(), "Please Enter Feedback", Toast.LENGTH_SHORT).show();
                }

                else {

                    Feedback feedback = new Feedback(feedbackStr, patientname, patientmobile);

                    reference.child("Feedbacks").push().setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getActivity(), "Feedback Submitted Successfully", Toast.LENGTH_SHORT).show();
                            Home home=new Home();
                            FragmentManager manager=getFragmentManager();
                            manager.beginTransaction().replace(R.id.main_body,home,home.getTag()).commit();

                        }
                    });

                }

            }
        });
        return view;
    }
}