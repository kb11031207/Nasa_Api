package com.example.nasa_api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NasaAdapter(private var nasaImages: List<NasaImage>) : RecyclerView.Adapter<NasaAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.nasaImageView)
        val titleText: TextView = view.findViewById(R.id.nasaTitleText)
        val dateText: TextView = view.findViewById(R.id.nasaDateText)
        val explanationText: TextView = view.findViewById(R.id.nasaExplanationText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.nasa_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nasaImage = nasaImages[position]
        
        // Load image with Glide
        Glide.with(holder.imageView.context)
            .load(nasaImage.url)
            .placeholder(android.R.drawable.ic_menu_rotate)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(holder.imageView)
        
        // Set text data
        holder.titleText.text = nasaImage.title
        holder.dateText.text = nasaImage.date
        holder.explanationText.text = nasaImage.explanation
    }

    override fun getItemCount() = nasaImages.size
    
    // Method to update the data
    fun updateData(newImages: List<NasaImage>) {
        nasaImages = newImages
        notifyDataSetChanged()
    }
} 