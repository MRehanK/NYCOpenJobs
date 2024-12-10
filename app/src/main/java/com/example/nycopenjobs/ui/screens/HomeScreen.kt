package com.example.nycopenjobs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nycopenjobs.model.JobPost
import com.example.nycopenjobs.util.LoadingSpinner
import com.example.nycopenjobs.util.ToastMessage

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    onJobClick: (JobPost) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    when (val uiState = viewModel.uiState) {
        is HomeScreenUIState.Loading -> LoadingSpinner()
        is HomeScreenUIState.Success -> {
            Column(modifier = modifier.fillMaxSize()) {
                // Section 1: Top Bar
                HomeTopBar(
                    searchQuery = searchQuery,
                    onQueryChanged = { searchQuery = it }
                )

                // Section 2: Scrollable List of Job Postings
                val filteredJobs = uiState.data.filter {
                    it.businessTitle.contains(searchQuery, ignoreCase = true) ||
                            it.agency.contains(searchQuery, ignoreCase = true)
                }
                JobList(
                    jobs = filteredJobs,
                    onJobSelected = onJobClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        is HomeScreenUIState.Error -> ToastMessage("Job listing not available at this time")
        else -> ToastMessage("Job listing loaded")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(searchQuery: String, onQueryChanged: (String) -> Unit) {
    TopAppBar(
        title = { Text(text = "Select Job", fontWeight = FontWeight.Bold) },
        actions = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChanged,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .width(200.dp),
                singleLine = true,
                placeholder = { Text("Search jobs") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            )
        }
    )
}

@Composable
fun JobList(
    jobs: List<JobPost>,
    onJobSelected: (JobPost) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        items(jobs) { jobPost ->
            JobCard(jobPost, onJobSelected)
        }
    }
}

@Composable
fun JobCard(jobPost: JobPost, onJobSelected: (JobPost) -> Unit) {
    Card(
        onClick = { onJobSelected(jobPost) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = jobPost.careerLevel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = jobPost.agency,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = jobPost.businessTitle,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
