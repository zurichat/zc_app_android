package com.zurichat.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.models.organization_model.RecipientEmail

class RecipientEmailAdapter(private val mList: List<RecipientEmail>) : RecyclerView.Adapter<RecipientEmailAdapter.ViewHolder>() {
    private var listData: MutableList<RecipientEmail> = mList as MutableList<RecipientEmail>
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipient_email, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = listData[position]

        // sets the image to the imageview from our itemHolder class
        //holder.imgEmail.setImageResource(R.drawable.ic_baseline_email_24)
        //holder.btnRemove.setImageResource(R.drawable.ic_cancel)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.email

        holder.btnRemove.setOnClickListener(View.OnClickListener {
            listData.removeAt(position)
            notifyDataSetChanged()
        })

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return listData.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imgEmail: ImageView = itemView.findViewById(R.id.ic_email)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btn_remove);
        val textView: TextView = itemView.findViewById(R.id.textView_recipient_email)
    }
}