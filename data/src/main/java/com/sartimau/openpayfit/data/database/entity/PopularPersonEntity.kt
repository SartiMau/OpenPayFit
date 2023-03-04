package com.sartimau.openpayfit.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "popular_person")
class PopularPersonEntity(
    @PrimaryKey val id: Int,
    val adult: Boolean,
    val gender: Int,
    val knownForDepartment: String,
    val name: String,
    val popularity: Double,
    val profilePath: String
)

@Entity(
    tableName = "known_for",
    foreignKeys = [
        ForeignKey(
            entity = PopularPersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class KnownForEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val adult: Boolean,
    val backdropPath: String,
    val firstAirDate: String,
    val mediaType: String,
    val name: String,
    val originalLanguage: String,
    val originalName: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)

data class PopularPersonEntityWithKnownForEntity(
    @Embedded val popularPersonEntity: PopularPersonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val knownForEntity: List<KnownForEntity>
)
