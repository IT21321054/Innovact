package com.example.projectmad

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val productNameTextView: TextView = itemView.findViewById(R.id.textViewName)
    val productDateTextView: TextView = itemView.findViewById(R.id.textViewDate)
    val removeButton: Button = itemView.findViewById(R.id.removeBtn)
}