package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.patientapp.Doctor;
import com.example.patientapp.MessageActivity;
import com.example.patientapp.Patient;
import com.example.patientapp.R;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private Context mContext;
    private List<Doctor> doctorList;
    private String patientmobile;

    public DoctorAdapter(Context mContext, List<Doctor> doctorList,String patientmobile){

        this.mContext=mContext;
        this.doctorList=doctorList;
        this.patientmobile=patientmobile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.doctor_item,parent,false);
        return new DoctorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final Doctor doctor=doctorList.get(position);
        holder.doctorname.setText(doctor.getName());
        if(doctor.getImageURL().equals("default")){

            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }

        else{
            Glide.with(holder.itemView.getContext()).load(doctor.getImageURL()).into(holder.profile_image);
        }

        if(doctor.getStatus().equals("online")){
            holder.img_on.setVisibility(View.VISIBLE);
            holder.img_off.setVisibility(View.GONE);
        }

        else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.VISIBLE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mContext, MessageActivity.class);
                intent.putExtra("mobileno",doctor.getPhone());
                intent.putExtra("patient_mobile",patientmobile);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView doctorname;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorname=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);
            img_on=itemView.findViewById(R.id.img_on);
            img_off=itemView.findViewById(R.id.img_off);
        }
    }


}
