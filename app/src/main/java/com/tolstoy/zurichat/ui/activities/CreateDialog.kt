package com.tolstoy.zurichat.ui.activities

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.tolstoy.zurichat.R

class CreateDialog(private val layoutInflater: LayoutInflater,
                private val context:Context) {

    fun createEditNameDialog(savedName: TextView): AlertDialog {
        val view = layoutInflater.inflate(R.layout.edit_dialog,null)
        val name = view.findViewById<EditText>(R.id.editName)
        val cancelBtn = view.findViewById<Button>(R.id.cancelBtn)
        val saveBtn = view.findViewById<Button>(R.id.saveBtn)
        val textCounter = view.findViewById<TextView>(R.id.name_text_counter)

        name.setText(savedName.text.toString())

        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()

        textCounter.text = (20-savedName.text.length).toString()

        val textListener = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textCounter.text = (20 - name.text.length).toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        name.addTextChangedListener(textListener)

        cancelBtn.setOnClickListener {
            if(dialog.isShowing){
                dialog.dismiss()
                name.removeTextChangedListener(textListener)
            }
        }

        saveBtn.setOnClickListener {
            if(name.text.isNotEmpty()){
                savedName.text = name.text.toString()
                dialog.dismiss()
            }
        }
        return dialog
    }


    // Create an edit about dialog
    fun createEditAboutDialog(savedAbout: TextView): AlertDialog {
        val aboutView = layoutInflater.inflate(R.layout.edit_about_dialog,null)
        val about = aboutView.findViewById<EditText>(R.id.editAbout)
        val cancelAbtBtn = aboutView.findViewById<Button>(R.id.cancelAbtBtn)
        val saveAbtBtn = aboutView.findViewById<Button>(R.id.saveAbtBtn)
        val dialog1 = AlertDialog.Builder(context)
            .setView(aboutView)
            .setCancelable(false)
            .create()
        about.setText(savedAbout.text.toString())

        cancelAbtBtn.setOnClickListener {
            if(dialog1.isShowing){
                dialog1.dismiss()
            }
        }
        saveAbtBtn.setOnClickListener {
            if(about.text.isNotEmpty()){
                savedAbout.text = about.text.toString()
                dialog1.dismiss()
            }
        }
        return dialog1
    }

}