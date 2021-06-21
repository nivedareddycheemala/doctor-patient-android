package com.example.patientapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import DiseasePrediction.Symptoms;

public class searchdisease extends Fragment {

    MaterialButton materialButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_searchdisease, container, false);

        final String patientname = getArguments().getString("name");
        final String gender = getArguments().getString("gender");

        materialButton=(MaterialButton) view.findViewById(R.id.getstarted);

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(getActivity(), Symptoms.class);
                i.putExtra("name",patientname);
                i.putExtra("gender",gender);
                startActivity(i);

            }
        });

        return view;
    }



}