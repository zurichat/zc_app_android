package com.zurichat.app.ui.documents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zurichat.app.R;
import com.zurichat.app.ui.audio.AudioClients;
import com.zurichat.app.ui.audio.AudiosActivity;
import com.zurichat.app.ui.audio.SendAudio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DocumentActivity extends AppCompatActivity {

    ImageView docs;
    private int IMG = 33;
    String filepath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        docs = findViewById(R.id.link_doc);

        docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMG);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG &&  resultCode == RESULT_OK &&  data != null && data.getData()  != null){
            sendDocument();
        }

    }
    private void sendDocument() {
        File file = new File(filepath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("messagemedia", file.getName(),requestBody);

        RequestBody fomData = RequestBody.create(MediaType.parse("text/plain"), "This is a new audio");
        Retrofit retrofit = DocumentClients.getRetrofit();
        SendDocs sendDocs = (SendDocs) retrofit.create(SendDocs.class);
        Call call = sendDocs.sendDocs(part, fomData);
        call.enqueue(new Callback() {
            @java.lang.Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Toast.makeText(DocumentActivity.this, "Sent Successfully", Toast.LENGTH_SHORT).show();
                }else if (response.code() == 400){
                    Toast.makeText(DocumentActivity.this, "Error 400, invalid", Toast.LENGTH_SHORT).show();

                }else if (response.code() == 404){
                    Toast.makeText(DocumentActivity.this, "Error 404, Not found", Toast.LENGTH_SHORT).show();

                }else if (response.code() == 401){
                    Toast.makeText(DocumentActivity.this, "Error 400, invalid", Toast.LENGTH_SHORT).show();

                }
            }

            @java.lang.Override
            public void onFailure(Call call, java.lang.Throwable t) {

            }
        });



    }


}