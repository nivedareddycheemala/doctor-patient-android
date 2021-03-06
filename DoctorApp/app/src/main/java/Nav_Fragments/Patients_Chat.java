package Nav_Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doctorapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import Contact_Patient_Fragments.MessagefromPatient;
import Contact_Patient_Fragments.searchpatient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Patients_Chat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Patients_Chat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference reference;
    String final_doctor_mobile;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Patients_Chat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchdoctor.
     */
    // TODO: Rename and change types and number of parameters
    public static Patients_Chat newInstance(String param1, String param2) {
        Patients_Chat fragment = new Patients_Chat();
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

        final View rootView = inflater.inflate(R.layout.fragment_patients__chat, container, false);

        String doctormobile = getArguments().getString("doctor_mobile");


        TabLayout tabLayout=(TabLayout) rootView.findViewById(R.id.tab_layout);
        ViewPager viewPager=(ViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());

        Bundle b=new Bundle();
        b.putString("doctor_mobile",doctormobile);

        MessagefromPatient messagefromPatient=new MessagefromPatient();
        messagefromPatient.setArguments(b);

        searchpatient srchpatient=new searchpatient();
        srchpatient.setArguments(b);

        viewPagerAdapter.addFragment(messagefromPatient,"Patient Messages");
        viewPagerAdapter.addFragment(srchpatient,"Appointments");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        // Inflate the layout for this fragment
        return rootView;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();

        }



        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}