package com.tolstoy.zurichat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.model.slide

class sliderAdapter(private val slides: List<slide>) :
    RecyclerView.Adapter<sliderAdapter.sliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sliderViewHolder {
        return sliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_container,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: sliderViewHolder, position: Int) {
        holder.bind(slides[position])
    }

    override fun getItemCount(): Int {
        return slides.size
    }

    inner class sliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textTitle =
            view.findViewById<TextView>(R.id.textTitle)
        private val textDescription =
            view.findViewById<TextView>(R.id.textDescription)
        private val imageIcon =
            view.findViewById<ImageView>(R.id.imageSlideIcon)

        fun bind(Slider: slide) {
            textTitle.text = Slider.title
            textDescription.text = Slider.description
            imageIcon.setImageResource(Slider.icon)
        }
    }
}