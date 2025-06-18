package com.np.apiaplikacija.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

data class DrawerItem(val label: String, val route: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideBar(navController: NavController, content: @Composable () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("home") }

    val items = listOf(
        DrawerItem("Home", "home", Icons.Default.Home),
        DrawerItem("Profile", "profile", Icons.Default.Person),
        DrawerItem("Settings", "settings", Icons.Default.Settings)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Navigacija",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
                )
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(text = item.label) },
                        selected = selectedItem == item.route,
                        onClick = {
                            selectedItem = item.route
                            navController.navigate(item.route)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("OpenDataApp") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                            }
                        }
                    )
                },
                content = { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        content()
                    }
                }
            )
        }
    )
}
