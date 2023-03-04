package com.sartimau.openpayfit.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.sartimau.openpayfit.data.database.entity.KnownForEntity

@Dao
interface KnownForDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKnownFor(knownForEntity: KnownForEntity)
}
