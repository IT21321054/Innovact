package com.example.projectmad

import java.util.Date

class Lists {
    companion object{
        private val list = mutableListOf<Prod>()

        fun addProduct(product: Prod) {
            list.add(product)
        }

        fun removeProduct(product: Prod) {
            list.remove(product)
        }

        fun getWishlist(): MutableList<Prod> {
            println(list)
            return list
        }

        fun clearWishlist() {
            list.clear()
        }
    }
}

class Prod(
    val id: String,
    val name: String,
    val date: Date,
) {
}