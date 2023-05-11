package com.example.Innovact.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Innovact.R
import com.example.Innovact.adapters.TopicAdapter
import com.example.Innovact.models.CommentModel
import com.example.Innovact.models.TopicModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class FetchingActivity : AppCompatActivity() {
    private lateinit var topicRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var feedList: ArrayList<TopicModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        topicRecyclerView = findViewById(R.id.rvTask)
        topicRecyclerView.layoutManager = LinearLayoutManager(this)
        topicRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        feedList = arrayListOf<TopicModel>()

        getFeedBackData()

    }

    private fun getFeedBackData( ) {
        topicRecyclerView.visibility= View.GONE
        tvLoadingData.visibility= View.VISIBLE
        dbRef= FirebaseDatabase.getInstance().getReference("Topic")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                feedList.clear()
                if(snapshot.exists()) {
                    for (taskSnap in snapshot.children) {
                        val taskData = taskSnap.getValue(TopicModel::class.java)
                        feedList.add(taskData!!)

                        getComments(taskData)

                    }
                    val mAdapter = TopicAdapter(feedList, this@FetchingActivity)
                    topicRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : TopicAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent= Intent(this@FetchingActivity, UpdateActivity::class.java)

                            //put extra
                            intent.putExtra("id", feedList[position].topicId)
                            intent.putExtra("title", feedList[position].topicTitle)
                            intent.putExtra("description", feedList[position].topicDescription)
                            intent.putExtra("comments", feedList[position].comment)

                            startActivity(intent)
                        }

                    })

                    topicRecyclerView.visibility= View.VISIBLE
                    tvLoadingData.visibility= View.GONE


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }


    private fun getComments(topic : TopicModel){



        val commentList = arrayListOf<TopicModel>()

       val  dbRef= FirebaseDatabase.getInstance().getReference("Topic").child("-NUluLySTB4NUReq2QTM").child("Comment")
       dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                commentList.clear()
                if(snapshot.exists()) {
                    for (taskSnap in snapshot.children) {
                        val taskData = taskSnap.getValue(CommentModel::class.java)
                        print("id is : " + topic)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}