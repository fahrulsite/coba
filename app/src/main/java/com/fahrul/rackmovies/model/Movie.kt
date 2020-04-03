package com.fahrul.rackmovies.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fahrul.rackmovies.util.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = Constants.TABLE_MOVIE)
data class Movie(
    @PrimaryKey
    @ColumnInfo
    val id: String,

    @ColumnInfo
    val title: String?,

    @ColumnInfo
    val poster_path: String?,

    @ColumnInfo
    val release_date: String,

    @ColumnInfo
    val overview: String?

) : Parcelable