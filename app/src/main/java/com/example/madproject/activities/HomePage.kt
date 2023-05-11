package com.example.madproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.HomePageDetails
import com.example.madproject.R
import com.example.madproject.adapters.HomeAdapter
import com.example.madproject.models.HomeModel
import com.google.firebase.database.*

class HomePage : AppCompatActivity() {

    private lateinit var proRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var proList: ArrayList<HomeModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        proRecyclerView = findViewById(R.id.rvproduct)
        proRecyclerView.layoutManager = LinearLayoutManager(this)
        proRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        proList = arrayListOf<HomeModel>()

        getProductsData()

    }

    private fun getProductsData(){
        proRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Products")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                proList.clear()
                if (snapshot.exists()){
                    for (proSnap in snapshot.children){
                        val proData = proSnap.getValue(HomeModel::class.java)
                        proList.add(proData!!)
                    }
                    val pAdapter = HomeAdapter(proList)
                    proRecyclerView.adapter = pAdapter

                    pAdapter.setOnItemClickListener(object : HomeAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@HomePage, HomePageDetails::class.java)

                            //put extras
                            intent.putExtra("proId", proList[position].proId)
                            intent.putExtra("proname", proList[position].proname)
                            intent.putExtra("procat", proList[position].procat)
                            intent.putExtra("innoname", proList[position].innoname)
                            intent.putExtra("adddes", proList[position].adddes)
                            intent.putExtra("proprice", proList[position].proprice)
                            startActivity(intent)

                        }

                    })

                    proRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}