package com.example.pawsomepals.dataclass

data class RecievedOrder(
    val buyerID: String? = null,
    val buyerName: String? = null,
    val address: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val note: String? = null,
    val itemID: String? = null,
    val itemName: String? = null,
    val itemImage: String? = null,
    val qty: String? = null,
    val paymentAmount: String? = null,
    val date: String? = null,
    val status: String? = null,
)
