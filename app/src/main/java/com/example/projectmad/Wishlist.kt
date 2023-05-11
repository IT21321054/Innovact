package com.example.projectmad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmad.Lists.Companion.getWishlist

class Wishlist : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_wishlist)

        val recycler: RecyclerView = findViewById(R.id.recycler)

        recycler.layoutManager = LinearLayoutManager(this)
        val wishlistAdapter = WishlistAdapter(Lists.getWishlist())
        recycler.adapter = wishlistAdapter

    }
}