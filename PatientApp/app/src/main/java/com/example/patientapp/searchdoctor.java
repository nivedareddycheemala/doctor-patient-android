package com.example.patientapp;

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

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import DoctorFragments.ChatsFragment;
import DoctorFragments.DoctorSearch;
import DoctorFragments.DoctorslistFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link searchdoctor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchdoctor extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference reference;

    String final_patient_mobile;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public searchdoctor() {
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
    public static searchdoctor newInstance(String param1, String param2) {
        searchdoctor fragment = new searchdoctor();
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

        final View rootView = inflater.inflate(R.layout.fragment_searchdoctor, container, false);

        String patientmobile = getArguments().getString("patient_mobile");


        TabLayout tabLayout=(TabLayout) rootView.findViewById(R.id.tab_layout);
        ViewPager viewPager=(ViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());

        Bundle b=new Bundle();
        b.putString("patient_mobile",patientmobile);

        DoctorslistFragment doctorslistFragment=new DoctorslistFragment();
        doctorslistFragment.setArguments(b);
        DoctorSearch doctorSearch=new DoctorSearch();
        doctorSearch.setArguments(b);
        ChatsFragment chatsFragment=new ChatsFragment();
        chatsFragment.setArguments(b);

        viewPagerAdapter.addFragment(chatsFragment,"Chats");
        viewPagerAdapter.addFragment(doctorslistFragment,"Doctors");
        viewPagerAdapter.addFragment(doctorSearch,"Search");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);




        // Inflate the layout for this fragment
        return rootView;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter{

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