package com.example.pawsomepals

data class PetInformationData(
    val petId: String? = null,
    val petName: String? = null,
    val PetCategory: String? = null,
    val breed: String? = null,
    val age: String? = null,
    val gender: String? = null,
    val petDescription:String? = null,

    val organizationName: String? = null,
    val address: String? = null,
    val contact: String? = null,
    var imageURL: String? = null
)
