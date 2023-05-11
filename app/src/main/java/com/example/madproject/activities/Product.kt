package com.example.madproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madproject.R
import com.example.madproject.adapters.ProductAdapter
import com.example.madproject.models.ProductModel
import com.google.firebase.database.*

class Product : AppCompatActivity() {

    private lateinit var proRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var proList: ArrayList<ProductModel>
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        proRecyclerView = findViewById(R.id.rvpro)
        proRecyclerView.layoutManager = LinearLayoutManager(this)
        proRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        proList = arrayListOf<ProductModel>()

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
                        val proData = proSnap.getValue(ProductModel::class.java)
                        proList.add(proData!!)
                    }
                    val pAdapter = ProductAdapter(proList)
                    proRecyclerView.adapter = pAdapter

                    pAdapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@Product, ProductDetails::class.java)

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