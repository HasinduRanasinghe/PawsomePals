package com.example.pawsomepals

import android.os.Parcel
import android.os.Parcelable

data class LostPet(
    val petName: String,
    val gender: String,
    val breed: String,
    val location: String,
    var imageURL: String,
    val age: String,
    val description: String,
    val ownerName: String,
    val contactNumber: String,
    val type: String
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "", "", "", "")

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(petName)
        parcel.writeString(gender)
        parcel.writeString(breed)
        parcel.writeString(location)
        parcel.writeString(imageURL)
        parcel.writeString(age)
        parcel.writeString(description)
        parcel.writeString(ownerName)
        parcel.writeString(contactNumber)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LostPet> {
        override fun createFromParcel(parcel: Parcel): LostPet {
            return LostPet(parcel)
        }

        override fun newArray(size: Int): Array<LostPet?> {
            return arrayOfNulls(size)
        }
    }
}
