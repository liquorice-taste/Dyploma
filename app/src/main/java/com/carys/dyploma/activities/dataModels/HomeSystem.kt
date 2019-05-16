package com.carys.dyploma.activities.dataModels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeSystem(val id: String, val name: String, val ip: String, val address: String): Parcelable