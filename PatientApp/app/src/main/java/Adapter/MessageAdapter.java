package Adapter;

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
import com.example.patientapp.Doctor;
import com.example.patientapp.MessageActivity;
import com.example.patientapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import Models.Chat;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;


    private Context mContext;
    private List<Chat> mChat;
    private String patientmobile;
    private String imageURL;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> mChat,String patientmobile,String imageURL){

        this.mContext=mContext;
        this.mChat=mChat;
        this.patientmobile=patientmobile;
        this.imageURL=imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat=mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        if(imageURL.equals("default")){

            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{

            Glide.with(holder.itemView.getContext()).load(imageURL).into(holder.profile_image);

        }

        if(position==mChat.size()-1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            }

            else{
                holder.txt_seen.setText("Delivered");
            }
        }

        else{
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image;
        TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            txt_seen=itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(mChat.get(position).getSender().equals(patientmobile)){
            return MSG_TYPE_LEFT;
        }

        else{
            return MSG_TYPE_RIGHT;
        }
    }
}
