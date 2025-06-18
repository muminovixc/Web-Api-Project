package com.np.apiaplikacija.homepage

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // prazno jer koristiš centralni drawer u AppNavigation
        },
        content = {
            Scaffold(
                content = { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Dobrodošli nazad! 👋",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ovdje možete pregledati podatke i koristiti opcije aplikacije.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = { /* TODO: Add action */ },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Istraži više")
                        }
                    }
                }
            )
        }
    )
}
