package com.example.nycopenjobs.data

import android.content.SharedPreferences
import android.util.Log
import com.example.nycopenjobs.api.NycOpenDataApi
import com.example.nycopenjobs.model.JobPost
import com.example.nycopenjobs.util.Tag
import kotlinx.coroutines.flow.first

interface AppRepository {
    fun getScrollPosition(): Int
    fun setScrollPosition(position: Int)
    suspend fun getJobPostings(): List<JobPost>
    suspend fun getJobPost(jobId: Int): JobPost
}

class AppRepositoryImpl(
    private val nycOpenDataApi: NycOpenDataApi,
    private val sharedPreferences: SharedPreferences,
    private val dao: JobPostDao
) : AppRepository {

    private val scrollPositionKey = "scroll_position"
    private val offsetKey = "offset"

    private var offset = sharedPreferences.getInt(offsetKey, 0)
    private var totalJobs = 0

    private fun updateOffset() {
        offset += (totalJobs - offset)
        Log.i(Tag, "offset: $offset")
        sharedPreferences.edit().putInt(offsetKey, offset).apply()
    }

    private fun updateTotalJobs(newTotal: Int) {
        totalJobs = newTotal
        Log.i(Tag, "total jobs: $totalJobs")
    }

    override suspend fun getJobPostings(): List<JobPost> {
        Log.i(Tag, "Getting job posting")
        updateOffset()

        val localData = dao.getAll().first()
        updateTotalJobs(localData.size)

        if (offset == totalJobs) {
            Log.i(Tag, "getting job posting via API")
            val jobs = nycOpenDataApi.getJobPosting(offset)

            Log.i(Tag, "API returned ${jobs.size} jobs. Updating local database")
            dao.upsert(jobs)

            val updatedJobs = dao.getAll().first()
            updateTotalJobs(updatedJobs.size)

            return updatedJobs
        }
        return localData
    }

    override suspend fun getJobPost(jobId: Int): JobPost {
        Log.i(Tag, "getting job post id $jobId")
        return dao.get(jobId)
    }

    override fun getScrollPosition(): Int {
        val scrollPosition = sharedPreferences.getInt(scrollPositionKey, 0)
        Log.i(Tag, "scroll position: $scrollPosition")
        return scrollPosition
    }

    override fun setScrollPosition(position: Int) {
        Log.i(Tag, "setting scroll position: $position")
        sharedPreferences.edit().putInt(scrollPositionKey, position).apply()
    }
}
