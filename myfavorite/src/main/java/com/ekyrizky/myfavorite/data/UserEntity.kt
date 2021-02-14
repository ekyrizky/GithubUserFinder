package com.ekyrizky.myfavorite.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    var username: String = "",
    var name: String = "",
    var avatar: String = "",
    var bio: String = "",
    var company: String = "",
    var location: String = "",
    var email: String = "",
    var url: String = "",
    var repository: Int = 0,
    var following: Int = 0,
    var followers: Int = 0,
): Parcelable
