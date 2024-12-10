package com.example.nycopenjobs.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.nycopenjobs.NYCOpenJobsApp
import com.example.nycopenjobs.data.AppRepository
import com.example.nycopenjobs.model.JobPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailScreenViewModel(private val appRepository: AppRepository) : ViewModel() {

    private val _jobPost = MutableStateFlow<JobPost?>(null)
    val jobPost: StateFlow<JobPost?> get() = _jobPost

    fun fetchJobById(jobId: String) {
        viewModelScope.launch {
            try {
                val fetchedJob = appRepository.getJobPost(jobId.toInt())
                _jobPost.value = fetchedJob
            } catch (e: Exception) {
                _jobPost.value = null // Handle the exception gracefully
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as NYCOpenJobsApp
                val appContainer = application.container
                return DetailScreenViewModel(appContainer.appRepository) as T
            }
        }
    }
}