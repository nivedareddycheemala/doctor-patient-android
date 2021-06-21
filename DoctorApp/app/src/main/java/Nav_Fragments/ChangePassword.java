package Nav_Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.doctorapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePassword extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button submit_button,cancel_button;
    FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangePassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePassword.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePassword newInstance(String param1, String param2) {
        ChangePassword fragment = new ChangePassword();
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
        View view=inflater.inflate(R.layout.fragment_change_password, container, false);

        submit_button=(Button) view.findViewById(R.id.submitforchange);
        cancel_button=(Button) view.findViewById(R.id.canceltohome);
        mAuth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final String doctor_email=getArguments().getString("doctor_email");
        final String doctor_name=getArguments().getString("doctor_name");
        final String doctor_mobile=getArguments().getString("doctor_mobile");

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog.show();

                mAuth.sendPasswordResetEmail(doctor_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Please Open Your mail to Change Password", Toast.LENGTH_SHORT).show();
                        Home home=new Home();
                        Bundle bundle=new Bundle();
                        bundle.putString("doctor_name",doctor_name);
                        bundle.putString("doctor_mobile",doctor_mobile);
                        home.setArguments(bundle);
                        FragmentManager manager=getFragmentManager();
                        manager.beginTransaction().replace(R.id.main_body,home,home.getTag()).commit();
                        progressDialog.dismiss();

                    }
                });

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Home home=new Home();
                Bundle bundle2=new Bundle();
                bundle2.putString("doctor_name",doctor_name);
                bundle2.putString("doctor_mobile",doctor_mobile);
                home.setArguments(bundle2);
                FragmentManager manager=getFragmentManager();
                manager.beginTransaction().replace(R.id.main_body,home,home.getTag()).commit();

            }
        });

        return view;
    }
}