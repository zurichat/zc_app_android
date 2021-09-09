package com.tolstoy.zurichat.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.fragment.DataAndStorageFragment



class LargerItemsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_larger_items)


//        // Begin the transaction
//        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
//        // Replace the contents of the container with the new fragment
//        ft.replace(R.id.fragment_data_and_storage_container, DataAndStorageFragment())
//        // Complete the changes added above
//        ft.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.data_and_storage_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){

            R.id.action_save -> {
                Toast.makeText(applicationContext, "click to save", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_delete -> {
                Toast.makeText(applicationContext, "click to delete", Toast.LENGTH_SHORT).show()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }

    }
}