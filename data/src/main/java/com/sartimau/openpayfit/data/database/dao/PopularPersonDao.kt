package com.sartimau.openpayfit.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sartimau.openpayfit.data.database.entity.PopularPersonEntity
import com.sartimau.openpayfit.data.database.entity.PopularPersonEntityWithKnownForEntity

@Dao
interface PopularPersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPopularPerson(popularPersonEntity: PopularPersonEntity)

    @Transaction
    @Query("SELECT * FROM popular_person")
    fun getDBPopularPersons(): List<PopularPersonEntityWithKnownForEntity>
}
