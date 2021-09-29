package com.tolstoy.zurichat.ui.documents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Bundle;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tolstoy.zurichat.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DocumentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<File> fileList;
    private DocumentAdapter documentAdapter;
    File storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        String document = System.getenv("EXTERNAL_STORAGE");

        storage = new File(document);

        String data;
        try {
            data = getString(Integer.parseInt("path"));
            File file = new File(data);
            storage = file;
        }catch (Exception e){
            e.printStackTrace();
        }
        runtimepermission();
    }

    private void runtimepermission() {
        Dexter.withContext(this).withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked( MultiplePermissionsReport multiplePermissionsReport){
                displayFiles();
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken){
                permissionToken.continuePermissionRequest();
            }

        }).check();
    }
    private ArrayList<File> findFiles(File storage) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = storage.listFiles();

        //to get all document files
        for (File singleFile : files){
            if (singleFile.getName().toLowerCase().endsWith(".pdf") || singleFile.getName().toLowerCase().endsWith(".pptx")
                    || singleFile.getName().toLowerCase().endsWith(".docx") || singleFile.getName().toLowerCase().endsWith(".apk")){
                arrayList.add(singleFile);

            }
        }
        return arrayList;

    }
    private void displayFiles(){
        recyclerView = findViewById(R.id.docrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileList = new ArrayList<>();
        fileList.addAll(findFiles(storage));
        documentAdapter = new DocumentAdapter(this, fileList);
        recyclerView.setAdapter(documentAdapter);
    }


}