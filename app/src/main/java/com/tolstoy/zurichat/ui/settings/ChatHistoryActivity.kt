package com.tolstoy.zurichat.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.tolstoy.zurichat.R

class ChatHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_history)

        // use arrayadapter and define an array
        val arrayAdapter: ArrayAdapter<*>
        val options = arrayOf(
            "Export Chat", "Archive Chat", "Clear Chat",
            "Delete All Chats", "Backup All Chats"
        )

        // access the listView from xml file
        var mListView = findViewById<ListView>(R.id.options_view)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, options)
        mListView.adapter = arrayAdapter

        mListView.setDivider(null)
        mListView.setDividerHeight(0)



    }


    }

