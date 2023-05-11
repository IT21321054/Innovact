package com.example.Innovact.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Innovact.R
import com.example.Innovact.activities.UpdateActivity
import com.example.Innovact.models.CommentModel
import com.example.Innovact.models.TopicModel
import com.google.firebase.database.FirebaseDatabase

class TopicAdapter(private  val feedList:ArrayList<TopicModel>,private val context: Context) :
    RecyclerView.Adapter<TopicAdapter.ViewHolder>(){

    private  lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener =clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val itemView =LayoutInflater.from(parent.context).inflate(R.layout.topic_list_item,parent,false)
        return  ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentfeed= feedList[position]
        holder.lblTopic.text=currentfeed.topicTitle
        holder.lblDescription.text=currentfeed.topicDescription

        holder.commentRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.commentRecyclerView.setHasFixedSize(true)
        holder.lblAvatar.setText(currentfeed.topicTitle?.substring(0,1).toString())

        val dbRef= FirebaseDatabase.getInstance().getReference("Topic").child(currentfeed.topicId.toString()).child("Comment")

        val feedList = arrayListOf<CommentModel>()
        holder.lblCommentCount.setText(feedList.size.toString());



        var mAdapter = CommentAdapter(feedList!!)
        holder.commentRecyclerView.adapter = mAdapter;

        holder.btnSend.setOnClickListener{
            val comment = holder.txtComment.text.toString();

            if(comment.isEmpty()){
                holder.txtComment.setError("Enter comment first!")
            }else{
                val database = FirebaseDatabase.getInstance().getReference("Topic").child(currentfeed.topicId.toString()).child("Comment");
                val id = database.push().key!!

                val task= CommentModel(currentfeed.topicId.toString(),id,"0001",comment)


                dbRef.child(id).setValue(task)
                    .addOnCompleteListener{
                        Toast.makeText(context,"comment insert successfully", Toast.LENGTH_LONG).show()
                        holder.txtComment.text.clear()

                    }.addOnFailureListener { err->
                        Toast.makeText(context,"Error ${err.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        /**
         * delete item from database
         */
        holder.btnDelete.setOnClickListener{
            val dbRef = FirebaseDatabase.getInstance().getReference("Topic").child(currentfeed.topicId.toString())
            val mTask = dbRef.removeValue()
            feedList.drop(position);
            Toast.makeText(context, "Item has been deleted!!",Toast.LENGTH_LONG).show();
        }

        /**
         *  Intent to edit page
         */
        holder.btnEdit.setOnClickListener{
            context.startActivity(Intent(context, UpdateActivity::class.java).apply {
                putExtra("id", currentfeed.topicId)
                putExtra("title", currentfeed.topicTitle)
                putExtra("description", currentfeed.topicDescription)
            })
        }

    }


    override fun getItemCount(): Int {
       return feedList.size
    }
    class ViewHolder(itemView: View, clickListener: onItemClickListener):RecyclerView.ViewHolder(itemView) {
        val lblTopic : TextView =itemView.findViewById(R.id.lblTopic)
        val commentRecyclerView : RecyclerView = itemView.findViewById(R.id.listViewComment)
        val lblDescription : TextView =itemView.findViewById(R.id.lblDescription)
        val lblAvatar : TextView =itemView.findViewById(R.id.lblAvatar)
        val btnEdit : ImageButton =itemView.findViewById(R.id.btnEdit)
        val btnDelete : ImageButton =itemView.findViewById(R.id.btnDelete)
        val txtComment : EditText = itemView.findViewById(R.id.txtComment)
        val lblCommentCount : TextView = itemView.findViewById(R.id.lblCommentCount)
        val btnSend : ImageButton = itemView.findViewById(R.id.btnSend)
        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}