package com.example.nycopenjobs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import com.example.nycopenjobs.ui.screens.DetailScreen
import com.example.nycopenjobs.ui.screens.DetailScreenViewModel
import com.example.nycopenjobs.ui.screens.HomeScreen
import com.example.nycopenjobs.ui.screens.HomeScreenViewModel
import com.example.nycopenjobs.ui.theme.NYCOpenJobsTheme
import com.example.nycopenjobs.model.JobPost

val favoriteJobs = mutableStateListOf<JobPost>() // Shared list of favorite jobs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NYCOpenJobsTheme {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate("home") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                    label = { Text("Favorites") },
                    selected = false,
                    onClick = { navController.navigate("favorites") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                val homeViewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.Factory)
                HomeScreen(
                    viewModel = homeViewModel,
                    onJobClick = { jobPost ->
                        navController.navigate("details/${jobPost.jobId}")
                    }
                )
            }

            composable("favorites") {
                FavoriteScreen(navController)
            }

            composable("details/{jobId}") { backStackEntry ->
                val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable
                val detailViewModel: DetailScreenViewModel = viewModel(factory = DetailScreenViewModel.Factory)
                val jobPostState = detailViewModel.jobPost.collectAsState()

                LaunchedEffect(jobId) {
                    detailViewModel.fetchJobById(jobId)
                }

                val jobPost = jobPostState.value
                if (jobPost != null) {
                    DetailScreen(
                        jobPost = jobPost,
                        onBackClick = { navController.popBackStack() },
                        onFavoriteToggle = { job ->
                            if (favoriteJobs.contains(job)) favoriteJobs.remove(job) else favoriteJobs.add(job)
                        },
                        isFavorite = favoriteJobs.contains(jobPost)
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Select Job", fontWeight = FontWeight.Bold) },
                actions = {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
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
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (favoriteJobs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Your Favorites List is Empty!", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(favoriteJobs) { jobPost ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            onClick = { navController.navigate("details/${jobPost.jobId}") }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(jobPost.agency, fontWeight = FontWeight.Bold)
                                Text(jobPost.businessTitle)
                            }
                        }
                    }
                }
            }
        }
    }
}
