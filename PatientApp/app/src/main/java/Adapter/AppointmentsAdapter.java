package Adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientapp.DetailsforAppointment;
import com.example.patientapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.Appointmentdata;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.MyViewHolder> {

    String finalpatmobile="";

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
        holder.rname.setText(appointmentdata.getDoctorname());
        holder.rappointdate.setText(appointmentdata.getAppointmentdate());
        holder.rmobile.setText(appointmentdata.getDoctornumber());
        holder.statusAPPT.setText(appointmentdata.getApptstatus());
        String patmobile=appointmentdata.getPatientnumber();
        finalpatmobile=patmobile;

    }

    @Override
    public int getItemCount() {
        return appointmentdataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rname,rappointdate,rmobile,statusAPPT;
        ImageView delete,edit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rname=(TextView) itemView.findViewById(R.id.readDocname);
            rappointdate=(TextView) itemView.findViewById(R.id.readappointdate);
            rmobile=(TextView) itemView.findViewById(R.id.readmobile);
            statusAPPT=(TextView) itemView.findViewById(R.id.statusAPPT);

            delete=(ImageView) itemView.findViewById(R.id.delete);
            edit =(ImageView) itemView.findViewById(R.id.edit);

            edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View view) {

                    final String appointmentdate=rappointdate.getText().toString();
                    final String docmobile=rmobile.getText().toString();
                    ViewGroup viewGroup= view.findViewById(android.R.id.content);

                    View v =LayoutInflater.from(context).inflate(R.layout.updateappointmentdate,viewGroup,false);

                    final TextView uappointmentdate=v.findViewById(R.id.currentdate);
                    Button udatepicer=v.findViewById(R.id.newdatepicker);

                    Button update=v.findViewById(R.id.updatedata);
                    Button cancel=v.findViewById(R.id.canceldata);

                    final BottomSheetDialog dialog=new BottomSheetDialog(context);
                    dialog.setContentView(v);
                    dialog.setCancelable(false);

                    uappointmentdate.setText(appointmentdate);

                    udatepicer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            int c_date;
                            int c_month;
                            int c_year;

                            Calendar c=Calendar.getInstance();
                            c_year=c.get(Calendar.YEAR);
                            c_month=c.get(Calendar.MONTH);
                            c_date =c.get(Calendar.DATE);

                            DatePickerDialog pickerDialog=new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                    String appointmentnewdate="";


                                    appointmentnewdate=appointmentnewdate+dayOfMonth+"-"+(month+1)+"-"+year;
                                    uappointmentdate.setText(appointmentnewdate);

                                }
                            },c_year,c_month, c_date);

                            pickerDialog.show();

                        }
                    });

                    update.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View v) {

                            final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Appointments");
                            final String appointmentupdate=uappointmentdate.getText().toString();

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                                        String keys=dataSnapshot.getKey();
                                        if ((dataSnapshot.child("doctornumber").getValue().toString().equals(docmobile))&& (dataSnapshot.child("patientnumber").getValue().toString().equals(finalpatmobile))) {
                                            databaseReference.child(keys).child("appointmentdate").setValue(appointmentupdate);
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Toast.makeText(view.getContext(), "Date Updated Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Appointments");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()) {

                                String keys=dataSnapshot.getKey();
                                if (dataSnapshot.child("doctornumber").getValue().toString().equals(rmobile.getText().toString()) &&(dataSnapshot.child("patientnumber").getValue().toString().equals(finalpatmobile))) {
                                    databaseReference.child(keys).removeValue();
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
        }
    }
}
