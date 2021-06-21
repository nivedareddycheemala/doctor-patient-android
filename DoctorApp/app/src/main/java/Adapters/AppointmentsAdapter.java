package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Models.Appointmentdata;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.MyViewHolder> {

    String finaldocmobile="";

    Context context;
    List<Appointmentdata> appointmentdataList;
    public AppointmentsAdapter(Context context, List<Appointmentdata> personalappointments) {

        this.context=context;
        this.appointmentdataList=personalappointments;
    }

    @NonNull
    @Override
    public AppointmentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.appointment_item,parent,false);
        return new AppointmentsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsAdapter.MyViewHolder holder, int position) {

        Appointmentdata appointmentdata=appointmentdataList.get(position);
        holder.rname.setText(appointmentdata.getPatientname());
        holder.rappointdate.setText(appointmentdata.getAppointmentdate());
        holder.rmobile.setText(appointmentdata.getPatientnumber());
        holder.statusappt.setText(appointmentdata.getApptstatus());
        String docmobile=appointmentdata.getDoctornumber();
        finaldocmobile=docmobile;

    }

    @Override
    public int getItemCount() {
        return appointmentdataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rname,rappointdate,rmobile,cancelappt,approveappt,statusappt;
        ImageView delete,edit;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            rname=(TextView) itemView.findViewById(R.id.readPatname);
            rappointdate=(TextView) itemView.findViewById(R.id.readappointdate);
            rmobile=(TextView) itemView.findViewById(R.id.readmobile);
            cancelappt=(TextView) itemView.findViewById(R.id.cancelAPPT);
            approveappt=(TextView) itemView.findViewById(R.id.approveAPPT);
            statusappt=(TextView) itemView.findViewById(R.id.statusAPPT);

            cancelappt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Appointments");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()) {

                                String keys=dataSnapshot.getKey();

                                String pnumber=dataSnapshot.child("patientnumber").getValue().toString();
                                String dnumber=dataSnapshot.child("doctornumber").getValue().toString();
                                if (pnumber.equals(rmobile.getText().toString()) && dnumber.equals(finaldocmobile)) {
                                    databaseReference.child(keys).removeValue();
                                    Toast.makeText(itemView.getContext(), "Appointment Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });

            approveappt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Appointments");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                                String keys=dataSnapshot.getKey();
                                if (dataSnapshot.child("doctornumber").getValue().toString().equals(finaldocmobile)&& (dataSnapshot.child("patientnumber").getValue().toString().equals(rmobile.getText().toString()))) {
                                    databaseReference.child(keys).child("apptstatus").setValue("Approved");
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(itemView.getContext(), "Appointment Approved Sucessfuly", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
