package com.np.apiaplikacija.frontend.w.dataset

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.np.apiaplikacija.data.db.FavoriteEntity
import com.np.apiaplikacija.viewmodel.ApiViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatasetListScreen(
    viewModel: ApiViewModel,
    token: String,
    navController: NavController
) {
    val datasets by viewModel.datasets.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        viewModel.loadDatasets(token)
        viewModel.loadFavorites()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Dataseti") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(datasets) { dataset ->
                val isFavorite = favorites.any { it.url == dataset.apiUrl }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable {
                            val encodedUrl = java.net.URLEncoder.encode(dataset.apiUrl, "UTF-8")
                            navController.navigate("dataset_detail/$encodedUrl")
                        },
                    elevation = CardDefaults.cardElevation()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = dataset.name, style = MaterialTheme.typography.titleMedium)
                        }

                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    if (isFavorite) {
                                        viewModel.removeFavoriteByUrl(dataset.apiUrl)
                                    } else {

                                        viewModel.addToFavorites(name = dataset.name, apiUrl = dataset.apiUrl)

                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (isFavorite) "Ukloni iz favorita" else "Dodaj u favorite",
                                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
