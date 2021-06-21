package Nav_Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.doctorapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button btn_profile,btn_patient,btn_feedback,btn_settings;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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

        View view=inflater.inflate(R.layout.fragment_home, container, false);

        final String doctormobile = getArguments().getString("doctor_mobile");
        final String doctorname= getArguments().getString("doctor_name");
        final String doctoremail= getArguments().getString("doctor_email");

        btn_profile=(Button) view.findViewById(R.id.profilebtn);
        btn_patient=(Button) view.findViewById(R.id.patientbtn);
        btn_feedback=(Button) view.findViewById(R.id.feedbackbtn);
        btn_settings=(Button) view.findViewById(R.id.settingsbtn);

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profile doct_profile=new profile();
                Bundle bundle=new Bundle();
                bundle.putString("doctor_mobile",doctormobile);
                doct_profile.setArguments(bundle);
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction().replace(R.id.main_body,doct_profile,doct_profile.getTag()).commit();

            }
        });

        btn_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Patients_Chat patients_chat=new Patients_Chat();
                Bundle bundle2 = new Bundle();
                bundle2.putString("doctor_mobile",doctormobile);
                patients_chat.setArguments(bundle2);
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction().replace(R.id.main_body,patients_chat,patients_chat.getTag()).commit();

            }
        });

        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Feedback myfeedback=new Feedback();
                Bundle bundle3=new Bundle();
                bundle3.putString("doctor_mobile",doctormobile);
                bundle3.putString("doctor_name",doctorname);
                myfeedback.setArguments(bundle3);
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction().replace(R.id.main_body,myfeedback,myfeedback.getTag()).commit();

            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangePassword changePassword=new ChangePassword();
                Bundle bundle4=new Bundle();
                bundle4.putString("doctor_mobile",doctormobile);
                bundle4.putString("doctor_name",doctorname);
                bundle4.putString("doctor_email",doctoremail);
                changePassword.setArguments(bundle4);
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction().replace(R.id.main_body,changePassword,changePassword.getTag()).commit();


            }
        });

        return view;
    }
}