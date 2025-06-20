package com.np.apiaplikacija.frontend.w.statistics

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.google.gson.Gson
import com.google.gson.JsonObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatistikaScreen(backStackEntry: NavBackStackEntry) {
    val encodedData = backStackEntry.arguments?.getString("data") ?: ""

    val decodedJson = remember(encodedData) {
        try {
            val jsonString = Uri.decode(encodedData)
            Gson().fromJson(jsonString, JsonObject::class.java)
        } catch (e: Exception) {
            null
        }
    }

    if (decodedJson == null) {
        Text("Greška prilikom dekodiranja podataka.")
        return
    }

    val male = decodedJson["maleTotal"]?.asInt ?: 0
    val female = decodedJson["femaleTotal"]?.asInt ?: 0

    val maleColor = Color(0xFF2196F3)
    val femaleColor = Color(0xFFE91E63)

    val chartData = listOf(
        BarChartData.Bar(value = male.toFloat(), label = "Muški", color = maleColor),
        BarChartData.Bar(value = female.toFloat(), label = "Ženski", color = femaleColor)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Statistika | M: $male | Ž: $female")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Ukupno: ${male + female}", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))

            BarChart(
                barChartData = BarChartData(bars = chartData),
                barDrawer = SimpleBarDrawer(),
                labelDrawer = SimpleValueDrawer(drawLocation = SimpleValueDrawer.DrawLocation.Outside)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(maleColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Muški", style = MaterialTheme.typography.bodyMedium)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(femaleColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ženski", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}