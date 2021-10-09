package com.zurichat.app.ui.documents;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zurichat.app.R;

import java.io.File;
import java.util.List;


public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>{
    Context context;
    List<File> fileList;

    public DocumentAdapter(Context context, List<File> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DocumentViewHolder(LayoutInflater.from(context).inflate(R.layout.document, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentAdapter.DocumentViewHolder holder, int position) {
        //to get pdf name
        holder.pdfname.setText(fileList.get(position).getName());
        holder.pdfname.setSelected(true);

        //to get pdf size
        holder.pdfsize.setText(Formatter.formatShortFileSize(context,fileList.get(position).length()));

        //to determine pdf icon
        if (fileList.get(position).getName().toLowerCase().contains(".pdf")){
            holder.pdficon.setImageResource(R.drawable.pdf);
        }else if (fileList.get(position).getName().toLowerCase().contains(".pptx")){
            holder.pdficon.setImageResource(R.drawable.pptx);
        }else if (fileList.get(position).getName().toLowerCase().contains(".docx")){
            holder.pdficon.setImageResource(R.drawable.docx);
        }else if (fileList.get(position).getName().toLowerCase().contains(".doc")){
            holder.pdficon.setImageResource(R.drawable.docx);
        }else if (fileList.get(position).getName().toLowerCase().contains(".apk")){
            holder.pdficon.setImageResource(R.drawable.ic_android);
        }else {
            holder.pdficon.setImageResource(R.drawable.ic_file);
        }


    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView  pdfname, pdfsize;
        ImageView pdficon;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfname = itemView.findViewById(R.id.pdfname);
            pdfsize = itemView.findViewById(R.id.pdfsize);
            pdficon = itemView.findViewById(R.id.pdficon);

        }
    }
}
