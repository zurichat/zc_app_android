package com.tolstoy.zurichat.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.models.Participant;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private List<Participant> participants;

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant, parent, false);
        return new ParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        Participant participant = participants.get(position);
        holder.bind(participant);
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
        notifyDataSetChanged();
    }

    class ParticipantViewHolder extends RecyclerView.ViewHolder{

        private TextView username, status;
        private ImageView profilePhoto;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.textView_username);
            status = itemView.findViewById(R.id.textView_status);
            profilePhoto = itemView.findViewById(R.id.imageView_user_profile);

        }

        public void bind(Participant participant){
            username.setText(participant.getUsername());
            status.setText(participant.getStatus());
            profilePhoto.setImageResource(participant.getProfile_photo());
        }
    }
}
