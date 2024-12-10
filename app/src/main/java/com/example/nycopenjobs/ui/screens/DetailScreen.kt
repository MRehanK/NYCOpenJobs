package com.example.nycopenjobs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nycopenjobs.model.JobPost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    jobPost: JobPost?,
    onBackClick: () -> Unit,
    onFavoriteToggle: (JobPost) -> Unit,
    isFavorite: Boolean
) {
    if (jobPost == null) {
        Text("Job details unavailable.")
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = jobPost.agency) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onFavoriteToggle(jobPost) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        DetailContent(
            jobPost = jobPost,
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        )
    }
}


@Composable
fun DetailContent(jobPost: JobPost, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Job Id: ${jobPost.jobId}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Posting Date: ${jobPost.postingDate}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Business Title:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = jobPost.businessTitle,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Career Level: ${jobPost.careerLevel}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Salary range: ${jobPost.salaryRangeFrom} - ${jobPost.salaryRangeTo} ${jobPost.salaryFrequency}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Job Category: ${jobPost.jobCategory}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Work location: ${jobPost.workLocation}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Division: ${jobPost.divisionWorkUnit}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Job Description:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = jobPost.jobDescription,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
