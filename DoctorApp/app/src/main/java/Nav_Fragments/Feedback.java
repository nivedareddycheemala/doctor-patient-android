package Nav_Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doctorapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Feedback extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText feedback;
    MaterialButton sendFeedback;

    DatabaseReference reference;

    public Feedback() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Feedback.
     */
    // TODO: Rename and change types and number of parameters
    public static Feedback newInstance(String param1, String param2) {
        Feedback fragment = new Feedback();
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
        View view= inflater.inflate(R.layout.fragment_feedback, container, false);

        final String doctormobile = getArguments().getString("doctor_mobile");
        final String doctorname = getArguments().getString("doctor_name");

        feedback=(EditText) view.findViewById(R.id.feedback);

        sendFeedback=(MaterialButton) view.findViewById(R.id.sendFeedback);

        reference= FirebaseDatabase.getInstance().getReference();



        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String feedbackStr=feedback.getText().toString();

                if(feedbackStr.isEmpty()){
                    Toast.makeText(getActivity(), "Please Enter Feedback", Toast.LENGTH_SHORT).show();
                }

                else {

                    Models.Feedback feedback=new Models.Feedback(feedbackStr,doctorname,doctormobile);

                    reference.child("Feedbacks").push().setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getActivity(), "Feedback Submitted Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        });
        return view;
    }
}