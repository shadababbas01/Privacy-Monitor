package com.privacymonitor.android.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Query("SELECT * FROM score_snapshots ORDER BY timestamp DESC LIMIT 1")
    fun observeLatestScore(): Flow<ScoreSnapshotEntity?>

    @Query("SELECT * FROM score_snapshots ORDER BY timestamp DESC")
    fun observeScoreHistory(): Flow<List<ScoreSnapshotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScoreSnapshot(snapshot: ScoreSnapshotEntity)
}
