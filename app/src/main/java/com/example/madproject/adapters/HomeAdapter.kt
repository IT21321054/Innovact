package com.example.madproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.models.HomeModel

class HomeAdapter (private val proList: ArrayList<HomeModel>):
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private lateinit var pListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        pListener = clickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false)
        return ViewHolder(itemView, pListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = proList[position]
        holder.tvproname.text = currentProduct.proname
    }

    override fun getItemCount(): Int {
        return proList.size
    }

    class ViewHolder (itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvproname : TextView = itemView.findViewById(R.id.tvproname)

        init{
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}