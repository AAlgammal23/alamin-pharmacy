package com.alamin.pharma.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alamin.pharma.R
import com.alamin.pharma.ui.screens.AccountScreen
import com.alamin.pharma.ui.screens.ArticlesScreen
import com.alamin.pharma.ui.screens.CartScreen
import com.alamin.pharma.ui.screens.CategoriesScreen
import com.alamin.pharma.ui.screens.CheckoutScreen
import com.alamin.pharma.ui.screens.ConsultationScreen
import com.alamin.pharma.ui.screens.HomeScreen
import com.alamin.pharma.ui.screens.AuthScreen
import com.alamin.pharma.ui.screens.PrescriptionScreen
import com.alamin.pharma.ui.screens.ProductDetailsScreen
import com.alamin.pharma.ui.screens.ReminderScreen
import com.alamin.pharma.ui.screens.SearchScreen
import com.alamin.pharma.ui.screens.SubCategoryProductsScreen
import com.google.firebase.auth.FirebaseAuth

sealed class Tab(val route: String, val icon: ImageVector, val labelRes: Int) {
    data object Account : Tab("account", Icons.Filled.AccountCircle, R.string.tab_account)
    data object Reminder : Tab("reminder", Icons.Filled.Alarm, R.string.tab_reminder)
    data object Home : Tab("home", Icons.Filled.Home, R.string.tab_home)
    data object Prescription : Tab("prescription", Icons.Filled.UploadFile, R.string.tab_prescription)
    data object Categories : Tab("categories", Icons.Filled.GridView, R.string.tab_drugs)
}

val Tabs = listOf(Tab.Account, Tab.Reminder, Tab.Home, Tab.Prescription, Tab.Categories)

@Composable
fun AlAminAppRoot() {
    val navController = rememberNavController()
    val vm: PharmacyViewModel = viewModel()
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    // الاستماع لحالة تسجيل الدخول
    LaunchedEffect(Unit) {
        auth.addAuthStateListener { firebaseAuth ->
            isLoggedIn = firebaseAuth.currentUser != null
        }
    }

    if (!isLoggedIn) {
        AuthScreen(onAuthSuccess = { isLoggedIn = true })
        return
    }

    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: Tab.Home.route
    val isMainTab = Tabs.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (isMainTab) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    Tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(stringResource(tab.labelRes)) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Tab.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Tab.Home.route) {
                HomeScreen(
                    vm = vm,
                    onConsultation = { navController.navigate("consultation") },
                    onPrescription = { navController.navigate("prescription_form") },
                    onSearch = { navController.navigate("search") },
                    onCart = { navController.navigate("cart") },
                    onArticleList = { navController.navigate("articles") },
                    onProductClick = { id -> navController.navigate("product/$id") }
                )
            }
            composable(Tab.Categories.route) {
                CategoriesScreen(
                    vm = vm,
                    onSubcategoryClick = { id -> navController.navigate("subcategory/$id") }
                )
            }
            composable(Tab.Prescription.route) {
                PrescriptionScreen(onBack = { navController.popBackStack() })
            }
            composable(Tab.Reminder.route) {
                ReminderScreen()
            }
            composable(Tab.Account.route) {
                AccountScreen(
                    vm = vm,
                    onCart = { navController.navigate("cart") },
                    onNavigate = { route ->
                        when (route) {
                            "articles" -> navController.navigate("articles")
                            "favorites" -> navController.navigate("favorites")
                            "addresses" -> navController.navigate("addresses")
                            "balance" -> navController.navigate("balance")
                            "orders" -> navController.navigate("orders")
                            else -> {}
                        }
                    }
                )
            }
            // صفحات إضافية
            composable("consultation") { ConsultationScreen(onBack = { navController.popBackStack() }) }
            composable("prescription_form") { PrescriptionScreen(onBack = { navController.popBackStack() }) }
            composable("search") {
                SearchScreen(
                    vm = vm,
                    onBack = { navController.popBackStack() },
                    onAddToCart = { vm.addToCart(it) },
                    onProductClick = { id -> navController.navigate("product/$id") }
                )
            }
            composable("cart") {
                CartScreen(
                    vm = vm,
                    onBack = { navController.popBackStack() },
                    onCheckout = { navController.navigate("checkout") }
                )
            }
            composable("checkout") {
                CheckoutScreen(
                    vm = vm,
                    onBack = { navController.popBackStack() },
                    onOrderComplete = {
                        navController.popBackStack(Tab.Home.route, false)
                    }
                )
            }
            composable("product/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                ProductDetailsScreen(
                    vm = vm,
                    productId = id,
                    onBack = { navController.popBackStack() },
                    onCart = { navController.navigate("cart") }
                )
            }
            composable("subcategory/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                SubCategoryProductsScreen(
                    vm = vm,
                    subCategoryId = id,
                    onBack = { navController.popBackStack() },
                    onProductClick = { pid -> navController.navigate("product/$pid") }
                )
            }
            composable("articles") { ArticlesScreen(vm = vm, onBack = { navController.popBackStack() }) }
            composable("favorites") { FavoritesScreen(vm = vm, onBack = { navController.popBackStack() }, onProductClick = { id -> navController.navigate("product/$id") }) }
            composable("orders") { OrdersScreen(vm = vm, onBack = { navController.popBackStack() }) }
            composable("addresses") { AddressesScreen(onBack = { navController.popBackStack() }) }
            composable("balance") { BalanceScreen(onBack = { navController.popBackStack() }) }
        }
    }
}
