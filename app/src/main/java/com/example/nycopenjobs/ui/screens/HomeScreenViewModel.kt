package com.example.nycopenjobs.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.nycopenjobs.NYCOpenJobsApp
import com.example.nycopenjobs.data.AppRepository
import com.example.nycopenjobs.model.JobPost
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface HomeScreenUIState {
    data class Success(val data: List<JobPost>) : HomeScreenUIState
    data object Error : HomeScreenUIState
    data object Loading : HomeScreenUIState
    data object Ready : HomeScreenUIState
}

class HomeScreenViewModel(private val appRepository: AppRepository) : ViewModel() {

    var uiState: HomeScreenUIState by mutableStateOf(HomeScreenUIState.Ready)
        private set

    init {
        getJobPosting()
    }

    fun getJobPosting() {
        viewModelScope.launch {
            uiState = HomeScreenUIState.Loading
            uiState = try {
                HomeScreenUIState.Success(appRepository.getJobPostings())
            } catch (e: IOException) {
                Log.e("HomeScreenViewModel", "Error fetching job postings: ${e.message}")
                HomeScreenUIState.Error
            } catch (e: HttpException) {
                Log.e("HomeScreenViewModel", "HTTP error: ${e.message}")
                HomeScreenUIState.Error
            }
        }
    }

    fun getScrollPosition(): Int {
        return appRepository.getScrollPosition()
    }

    fun setScrollPosition(position: Int) {
        appRepository.setScrollPosition(position)
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
                return HomeScreenViewModel(appContainer.appRepository) as T
            }
        }
    }
}
