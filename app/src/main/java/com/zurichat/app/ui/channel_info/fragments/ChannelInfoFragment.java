package com.zurichat.app.ui.channel_info.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zurichat.app.R;
import com.zurichat.app.models.Media;
import com.zurichat.app.models.OrganizationMember;
import com.zurichat.app.models.Participant;
import com.zurichat.app.ui.adapters.MediaAdapter;
import com.zurichat.app.ui.adapters.ParticipantAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChannelInfoFragment extends Fragment {

    private NavController navController;
    private RecyclerView media_recyclerView;
    private RecyclerView participants_recyclerView;
    private List<Media> mediaList;
    //private List<Participant> participantList;
    private MediaAdapter mediaAdapter;
    private ParticipantAdapter participantAdapter;
    private ConstraintLayout mediaDocs;
    private TextView channel_name;
    private Bundle bundle;
    private ImageView moreMenuIcon;
    private TextView participantView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_channel_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        String channelName = Objects.requireNonNull(bundle).getString("channel_name");
        List<OrganizationMember> members =(List<OrganizationMember>) Objects.requireNonNull(bundle).get("members");

        navController = Navigation.findNavController(view);
        media_recyclerView = view.findViewById(R.id.recyclerView);
        participants_recyclerView = view.findViewById(R.id.p_recyclerView);
        mediaDocs = view.findViewById(R.id.media_docs);
        channel_name = view.findViewById(R.id.textView_channel_name);
        moreMenuIcon = view.findViewById(R.id.moreMenuIconView);

        channel_name.setText(channelName);
        participantView = view.findViewById(R.id.participants);
        if (members != null){
            int size = members.size();
            participantView.setText(size+" Participant(s)");
        }

        mediaDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bundle.putString("title", channel_name.getText().toString());
                //navController.navigate(R.id.action_channelInfoFragment_to_mediaDocsFragment, bundle);
            }

        });

        initMedia();
        if (members != null){
            initParticipant(members);
        }


        //Launch More Menu Popup
        View moreOptions = LayoutInflater.from(getActivity()).inflate(R.layout.channel_info_more_menu, null);
        final PopupWindow popupWindow = new PopupWindow(moreOptions, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

//        moreMenuIcon.setOnClickListener(v -> popupWindow.showAtLocation(moreOptions, Gravity.RIGHT, 0,-450));

        //Navigate to the add to channel Fragment on-click
        moreMenuIcon.setOnClickListener(v -> {
            //navController.navigate(R.id.action_channelInfoFragment_to_addToChannel);
        });

        View thePopUpWindow = popupWindow.getContentView();
        TextView muteChannel = (TextView)thePopUpWindow.findViewById(R.id.muteChannelOption);
        TextView leaveChannel = (TextView)thePopUpWindow.findViewById(R.id.leaveChannelOption);

        //leave channel Dialog
        View leaveChannelView = LayoutInflater.from(requireContext()).inflate(R.layout.leave_channel_confirmation, null);
        TextView cancel = leaveChannelView.findViewById(R.id.leaveChannelWarningCancelTextView);
        TextView leave = leaveChannelView.findViewById(R.id.leaveChannelWarningLeaveTextView);


        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireActivity());
        builder1.setCancelable(true)
                .setView(leaveChannelView);
        AlertDialog leaveChannelWarningDialog = builder1.create();
        cancel.setOnClickListener(view1 -> leaveChannelWarningDialog.dismiss());
        leave.setOnClickListener(view2 -> {
            leaveChannelWarningDialog.dismiss();
            Toast.makeText(requireContext(), "You successfully left the channel", Toast.LENGTH_SHORT).show();
        });

        //when leave channel is clicked
        leaveChannel.setOnClickListener(v -> leaveChannelWarningDialog.show());

        //Mute Channel Dialog
//        View muteChanelView = LayoutInflater.from(requireContext()).inflate(R.layout.mute_channel_notifications, null);
//        TextView cancelMuting = muteChanelView.findViewById(R.id.muteChannelCancelOption);
//        TextView oKMuting = muteChanelView.findViewById(R.id.muteChannelOkOption);
//        RadioGroup mutingOptionsGroup = muteChanelView.findViewById(R.id.muteChannelNotificationsRadioGroup);
//        mutingOptionsGroup.clearCheck();
//        AlertDialog.Builder builder2 = new AlertDialog.Builder(requireActivity());
//        builder2.setCancelable(true)
//                .setView(muteChanelView);
//        AlertDialog muteChannelNotificationsDialog = builder2.create();
//        cancelMuting.setOnClickListener(v -> muteChannelNotificationsDialog.dismiss());
//
//
//
//        //When mute channel is clicked
//        muteChannel.setOnClickListener(v -> muteChannelNotificationsDialog.show());

    }

    public void initMedia(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        media_recyclerView.setLayoutManager(layoutManager);
        media_recyclerView.setHasFixedSize(true);
        mediaAdapter = new MediaAdapter();
        mediaList = new ArrayList<>();
        setMediaListData();
        mediaAdapter.setMediaList(mediaList);
        media_recyclerView.setAdapter(mediaAdapter);
    }

    public void initParticipant(List<OrganizationMember> members){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        participants_recyclerView.setLayoutManager(layoutManager);
        participants_recyclerView.setHasFixedSize(true);
        participantAdapter = new ParticipantAdapter();
//        participantList = new ArrayList<>();
//        setParticipantListData();
        participantAdapter.setParticipants(getParticipantList(members));
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
//        participantList.add(new Participant("Cephas", "I am a Person of the word, speaking life", R.drawable.img_user_profile));
//        participantList.add(new Participant("Hamid", "I am a Person of the word, speaking life", R.drawable.img_user_profile));
//        participantList.add(new Participant("Afhamou", "I am a Person of the word, speaking life", R.drawable.img_user_profile));
//        participantList.add(new Participant("Joseph", "I am a Person of the word, speaking life", R.drawable.img_user_profile));
//        participantList.add(new Participant("Kagiri", "I am a Person of the word, speaking life", R.drawable.img_user_profile));
    }

    private List<Participant> getParticipantList(List<OrganizationMember> memberList){
        List<Participant> participants = new ArrayList<>();
        for (OrganizationMember member:memberList) {
            participants.add(new Participant(member.getEmail(),"",R.drawable.img_user_profile));
        }
        return participants;
    }
    @Override
    public void onResume() {
        super.onResume();
       // ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

}