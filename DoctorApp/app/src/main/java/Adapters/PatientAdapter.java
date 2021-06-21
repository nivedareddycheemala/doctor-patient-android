package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Doctor;
import com.example.doctorapp.MessageActivity;
import com.example.doctorapp.R;

import java.util.List;

import Models.Patient;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

private Context mContext;
private List<Patient> patientList;
private String doctormobile;

public PatientAdapter(Context mContext, List<Patient> patientList,String doctormobile){

        this.mContext=mContext;
        this.patientList=patientList;
        this.doctormobile=doctormobile;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.patient_item,parent,false);
        return new PatientAdapter.ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

final Patient patient=patientList.get(position);
        holder.patientname.setText(patient.getName());
        if(patient.getImageURL().equals("default")){

            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }

        else{
            Glide.with(holder.itemView.getContext()).load(patient.getImageURL()).into(holder.profile_image);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        Intent intent=new Intent(mContext, MessageActivity.class);
        intent.putExtra("mobileno",patient.getPhone());
        intent.putExtra("doctor_mobile",doctormobile);
        mContext.startActivity(intent);
        }
        });


        }

@Override
public int getItemCount() {
        return patientList.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView patientname;
    public ImageView profile_image;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        patientname=itemView.findViewById(R.id.username);
        profile_image=itemView.findViewById(R.id.profile_image);
    }
}



}
