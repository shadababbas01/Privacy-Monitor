package com.privacymonitor.android.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM privacy_events ORDER BY timestamp DESC")
    fun observeAllEvents(): Flow<List<PrivacyEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: PrivacyEventEntity)

    @Query("UPDATE privacy_events SET isResolved = 1 WHERE id = :eventId")
    suspend fun markResolved(eventId: String)

    @Query("DELETE FROM privacy_events WHERE timestamp < :cutoffTimestamp")
    suspend fun deleteOlderThan(cutoffTimestamp: Long)
}
