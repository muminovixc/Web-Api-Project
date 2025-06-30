package com.np.apiaplikacija.frontend.w.dataset

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.np.apiaplikacija.viewmodel.DatasetDetailViewModel
import com.np.apiaplikacija.BuildConfig
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatasetDetailScreen(
    url: String,
    viewModel: DatasetDetailViewModel = viewModel(),
    navController: NavController,
) {
    val decodedUrl = URLDecoder.decode(url, "UTF-8")
    val json by viewModel.json.collectAsState()

    val gradovi = listOf(
        "Sarajevo", "Tuzla", "Zenica", "Mostar", "Bihać",
       "Prijedor", "Travnik",
        "Goražde", "Gradačac", "Cazin", "Zavidovići", "Konjic"
    )

    val godine = (2010..2025).map { it.toString() }

    var selectedMunicipality by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }

    var expandedMunicipality by remember { mutableStateOf(false) }
    var expandedYear by remember { mutableStateOf(false) }

    LaunchedEffect(decodedUrl) {
        viewModel.loadData(
            decodedUrl
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Podaci iz API-ja") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (json == null) {
                CircularProgressIndicator()
            } else {
                Text("Izaberi općinu", style = MaterialTheme.typography.labelLarge)
                ExposedDropdownMenuBox(
                    expanded = expandedMunicipality,
                    onExpandedChange = { expandedMunicipality = !expandedMunicipality }
                ) {
                    TextField(
                        value = selectedMunicipality,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Općina") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMunicipality)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedMunicipality,
                        onDismissRequest = { expandedMunicipality = false }
                    ) {
                        gradovi.forEach { grad ->
                            DropdownMenuItem(
                                text = { Text(grad) },
                                onClick = {
                                    selectedMunicipality = grad
                                    expandedMunicipality = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Izaberi godinu", style = MaterialTheme.typography.labelLarge)
                ExposedDropdownMenuBox(
                    expanded = expandedYear,
                    onExpandedChange = { expandedYear = !expandedYear }
                ) {
                    TextField(
                        value = selectedYear,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Godina") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYear)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedYear,
                        onDismissRequest = { expandedYear = false }
                    ) {
                        godine.forEach { god ->
                            DropdownMenuItem(
                                text = { Text(god) },
                                onClick = {
                                    selectedYear = god
                                    expandedYear = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val resultList = try {
                    val result = json?.asJsonObject?.get("result")?.asJsonArray
                    result?.mapNotNull { it.asJsonObject }
                } catch (e: Exception) {
                    null
                }

                if (!resultList.isNullOrEmpty()) {
                    val filteredList = resultList.filter {
                        val municipality = it["municipality"]?.asString?.lowercase() ?: ""
                        val year = it["year"]?.asString ?: ""
                        (selectedMunicipality.isBlank() || municipality.contains(selectedMunicipality.lowercase())) &&
                                (selectedYear.isBlank() || year.contains(selectedYear))
                    }


                    val context = LocalContext.current

                    LazyColumn {
                        items(filteredList) { item: JsonObject ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                elevation = CardDefaults.cardElevation(6.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    item.entrySet().forEach { entry: Map.Entry<String, JsonElement> ->
                                        Text(
                                            text = "${entry.key}: ${entry.value.asString}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Button(
                                            onClick = {
                                                val encodedData = Uri.encode(item.toString())
                                                navController.navigate("statistika/$encodedData")
                                            }
                                        ) {
                                            Text("Pogledaj statistiku")
                                        }

                                        Button(
                                            onClick = {
                                                val prettyText = item.entrySet().joinToString(separator = "\n") { entry ->
                                                    "${entry.key}: ${entry.value.asString}"
                                                }

                                                val shareIntent = Intent().apply {
                                                    action = Intent.ACTION_SEND
                                                    putExtra(Intent.EXTRA_TEXT, prettyText)
                                                    type = "text/plain"
                                                }
                                                context.startActivity(Intent.createChooser(shareIntent, "Podijeli podatke putem:"))
                                            }
                                        ) {
                                            Text("Podijeli")
                                        }

                                    }
                                }
                            }
                        }
                    }


                } else {
                    Text("Nema podataka za prikaz ili format nije podržan.")
                }
            }
        }
    }
}
