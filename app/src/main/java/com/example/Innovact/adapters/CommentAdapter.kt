package com.example.Innovact.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Innovact.R
import com.example.Innovact.models.CommentModel

class CommentAdapter(private  val feedList:ArrayList<CommentModel>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    private  lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener =clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val itemView =LayoutInflater.from(parent.context).inflate(R.layout.comment_list_item,parent,false)
        return  ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentfeed= feedList[position]
        holder.lblComment.text=currentfeed.comment.toString()

    }


    override fun getItemCount(): Int {
       return feedList.size
    }
    class ViewHolder(itemView: View, clickListener: onItemClickListener):RecyclerView.ViewHolder(itemView) {
        val lblComment : TextView =itemView.findViewById(R.id.lblComment)


        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}