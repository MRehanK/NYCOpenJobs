package com.example.nycopenjobs.data

import android.content.Context
import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import com.example.nycopenjobs.model.JobPost
import com.example.nycopenjobs.util.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface JobPostDao {
    // Get all job posts
    @Query("SELECT * FROM JobPost ORDER BY postingLastUpdated DESC")
    fun getAll(): Flow<List<JobPost>>

    // Get a specific job post
    @Query("SELECT * FROM JobPost WHERE jobId = :id")
    suspend fun get(id: Int): JobPost

    // Update or insert job posts
    @Upsert(entity = JobPost::class)
    suspend fun upsert(jobPosting: List<JobPost>)
}

@Database(entities = [JobPost::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    // DAO instance
    abstract fun jobPostDao(): JobPostDao

    companion object {
        private const val DATABASE = "local_database"

        @Volatile
        private var Instance: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            Log.i(Tag, "Getting database instance")
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    DATABASE
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
