package com.alamin.pharma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alamin.pharma.data.Order
import com.alamin.pharma.data.OrderStatus
import com.alamin.pharma.ui.PharmacyViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun OrdersScreen(vm: PharmacyViewModel, onBack: () -> Unit) {
    val user = remember { FirebaseAuth.getInstance().currentUser }
    val orders = remember { MutableStateFlow<List<Order>>(emptyList()) }
    val list by orders.asStateFlow().collectAsState()
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (user != null) {
            FirebaseFirestore.getInstance().collection("orders")
                .whereEqualTo("userId", user.uid)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snap, _ ->
                    val items = snap?.documents?.mapNotNull { it.toObject(Order::class.java) } ?: emptyList()
                    orders.value = items
                    loading = false
                }
        } else {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text("طلباتي", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("جاري التحميل...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else if (list.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(40.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.ShoppingBag, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(72.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("لا توجد طلبات بعد", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
            ) {
                items(list) { order ->
                    OrderCard(order)
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order) {
    val statusText = runCatching { OrderStatus.valueOf(order.status).ar }.getOrDefault(order.status)
    val statusColor = when (order.status) {
        "DELIVERED" -> androidx.compose.ui.graphics.Color(0xFF2BB673)
        "CANCELLED" -> androidx.compose.ui.graphics.Color.Red
        else -> MaterialTheme.colorScheme.tertiary
    }
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("طلب #${order.id.take(8)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(statusText, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("${order.items.size} منتج", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Text("${order.total} ر.ي", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            if (order.address.fullAddress.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text("العنوان: ${order.address.fullAddress}, ${order.address.city}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
