package com.tech.building.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemRequestModel(
    var materialModel: MaterialModel,
    var qtdRequested: Int,
    var unit: String,
    var quantityAttended: Int = 0
) : Parcelable