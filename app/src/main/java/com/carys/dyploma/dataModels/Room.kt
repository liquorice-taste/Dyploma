package com.carys.dyploma.dataModels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room (var id: Int, var name: String, var system: Int): Parcelable