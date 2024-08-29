package com.example.dunipool.apiManager.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoinAboutItem(
    var coinWebsite: String? = "no data",
    var coinTwitter: String? = "no data",
    var coinReddit: String? = "no data",
    val coinGithub: String? = "no data",
    var coinDescription: String? = "no data"
):Parcelable