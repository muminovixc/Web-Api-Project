package com.np.apiaplikacija.frontend.w.homepage

import android.R.string
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.np.apiaplikacija.viewmodel.HomePageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(viewModel: HomePageViewModel = viewModel(), navController: NavController,token:String) {
    val categories = listOf("Novorođeni", "Umrli")
    val entities = listOf("FBiH", "RS", "BD")
    val cantons = listOf("Sarajevo", "Tuzla", "Zenica")
    val cantonMunicipalities = mapOf(
        "Sarajevo" to listOf("Centar", "Stari Grad", "Novi Grad", "Ilidža"),
        "Tuzla" to listOf("Tuzla", "Živinice", "Lukavac"),
        "Zenica" to listOf("Zenica", "Zavidovići", "Vareš")
    )
    val years = (2010..2024).map { it.toString() }

    var selectedCategory by remember { mutableStateOf(categories.first()) }
    var selectedEntity by remember { mutableStateOf(entities.first()) }
    var selectedCanton by remember { mutableStateOf(cantons.first()) }
    var selectedMunicipality by remember { mutableStateOf(cantonMunicipalities[selectedCanton]?.first() ?: "") }
    var selectedYear by remember { mutableStateOf(years.first()) }

    val results by viewModel.filteredResults.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pretraga Podataka", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuSelector("Kategorija", categories, selectedCategory) { selectedCategory = it }
        DropdownMenuSelector("Entitet", entities, selectedEntity) { selectedEntity = it }
        DropdownMenuSelector("Kanton", cantons, selectedCanton) {
            selectedCanton = it
            selectedMunicipality = cantonMunicipalities[it]?.first() ?: ""
        }
        DropdownMenuSelector(
            "Općina",
            cantonMunicipalities[selectedCanton] ?: emptyList(),
            selectedMunicipality
        ) { selectedMunicipality = it }
        DropdownMenuSelector("Godina", years, selectedYear) { selectedYear = it }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.loadFilteredData(
                    type = selectedCategory,
                    entity = selectedEntity,
                    canton = selectedCanton,
                    municipality = selectedMunicipality,
                    year = selectedYear,
                    token
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pretraži")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(results) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(text = item, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuSelector(label: String, options: List<String>, selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
