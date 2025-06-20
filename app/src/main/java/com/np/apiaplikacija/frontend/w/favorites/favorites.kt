package com.np.apiaplikacija.frontend.w.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.np.apiaplikacija.data.db.FavoriteEntity
import com.np.apiaplikacija.viewmodel.ApiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(viewModel: ApiViewModel, navController: NavController) {
    // 🚀 Poziv učitavanja pri otvaranju ekrana
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Omiljeni Dataseti") })
        }
    ) { padding ->
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "Nema omiljenih datasetova.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(24.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(favorites) { dataset: FavoriteEntity ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clickable {
                                val encodedUrl = java.net.URLEncoder.encode(dataset.url, "UTF-8")
                                navController.navigate("dataset_detail/$encodedUrl")
                            },
                        elevation = CardDefaults.cardElevation()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = dataset.name, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}
