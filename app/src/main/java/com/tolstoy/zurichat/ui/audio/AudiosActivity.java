package com.tolstoy.zurichat.ui.audio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.View;

import java.io.File;
import com.tolstoy.zurichat.R;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AudiosActivity extends AppCompatActivity {

    ImageView audio;
    private int IMG = 31;
    String filepath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audios);
        audio = findViewById(R.id.link_btn);

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMG);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG &&  resultCode == RESULT_OK &&  data != null && data.getData()  != null){
            sendAudio();
        }

    }
    private void sendAudio() {
        File file = new File(filepath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("messagemedia", file.getName(),requestBody);

        RequestBody fomData = RequestBody.create(MediaType.parse("text/plain"), "This is a new audio");
        Retrofit retrofit = AudioClients.getRetrofit();
        SendAudio sendAudio = (SendAudio) retrofit.create(SendAudio.class);
        Call call = sendAudio.sendAudio(part, fomData);
        call.enqueue(new Callback() {
            @java.lang.Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Toast.makeText(AudiosActivity.this, "Sent Successfully", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 400){
                    Toast.makeText(AudiosActivity.this, "Error 400, invalid", Toast.LENGTH_SHORT).show();

                }else if (response.code() == 404){
                    Toast.makeText(AudiosActivity.this, "Error 404, Not found", Toast.LENGTH_SHORT).show();

                }else if (response.code() == 401){
                    Toast.makeText(AudiosActivity.this, "Error 400, invalid", Toast.LENGTH_SHORT).show();

                }
            }

            @java.lang.Override
            public void onFailure(Call call, java.lang.Throwable t) {

            }
        });



    }

}