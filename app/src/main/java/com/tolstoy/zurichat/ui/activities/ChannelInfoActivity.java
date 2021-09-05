package com.tolstoy.zurichat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.tolstoy.zurichat.R;
import com.tolstoy.zurichat.models.Media;
import com.tolstoy.zurichat.models.Participant;
import com.tolstoy.zurichat.ui.adapters.MediaAdapter;
import com.tolstoy.zurichat.ui.adapters.ParticipantAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChannelInfoActivity extends AppCompatActivity {

    private RecyclerView media_recyclerView;
    private RecyclerView participants_recyclerView;
    private List<Media> mediaList;
    private List<Participant> participantList;
    private MediaAdapter mediaAdapter;
    private ParticipantAdapter participantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_info);

        media_recyclerView = findViewById(R.id.recyclerView);
        participants_recyclerView = findViewById(R.id.p_recyclerView);

        initMedia();
        initParticipant();
    }

    public void initMedia(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        media_recyclerView.setLayoutManager(layoutManager);
        media_recyclerView.setHasFixedSize(true);
        mediaAdapter = new MediaAdapter();
        mediaList = new ArrayList<>();
        setMediaListData();
        mediaAdapter.setMediaList(mediaList);
        media_recyclerView.setAdapter(mediaAdapter);
    }

    public void initParticipant(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        participants_recyclerView.setLayoutManager(layoutManager);
        participants_recyclerView.setHasFixedSize(true);
        participantAdapter = new ParticipantAdapter();
        participantList = new ArrayList<>();
        setParticipantListData();
        participantAdapter.setParticipants(participantList);
        participants_recyclerView.setAdapter(participantAdapter);
    }

    public void setMediaListData(){
        mediaList.add(new Media(R.drawable.media_1));
        mediaList.add(new Media(R.drawable.media_2));
        mediaList.add(new Media(R.drawable.media_3));
        mediaList.add(new Media(R.drawable.media_4));
        mediaList.add(new Media(R.drawable.media_5));
    }

    public void setParticipantListData(){
        participantList.add(new Participant("Cephas", "I am a Person of the word, speaking life", R.drawable.img_user_profile));
        participantList.add(new Participant("Hamid", "I am a Person of the word, speaking life", R.drawable.img_user_profile));
        participantList.add(new Participant("Afhamou", "I am a Person of the word, speaking life", R.drawable.img_user_profile));
        participantList.add(new Participant("Joseph", "I am a Person of the word, speaking life", R.drawable.img_user_profile));
        participantList.add(new Participant("Kagiri", "I am a Person of the word, speaking life", R.drawable.img_user_profile));
    }

}