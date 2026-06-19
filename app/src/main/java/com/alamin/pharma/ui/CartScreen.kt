package com.alamin.pharma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alamin.pharma.data.CartItem
import com.alamin.pharma.ui.PharmacyViewModel

@Composable
fun CartScreen(
    vm: PharmacyViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val cart by vm.cart.collectAsState()
    val total = vm.cartTotal()
    val isOnline by vm.isOnline.collectAsState()

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
            Spacer(Modifier.weight(1f))
            Text("سلة المشتريات", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.weight(1f))
            if (!isOnline) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFA000))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("أوفلاين", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Spacer(Modifier.width(8.dp))
            }
        }

        if (cart.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.ShoppingCart, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(72.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("السلة فارغة", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                    Text("أضف منتجات للسلة لرؤيتها هنا", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cart) { item ->
                    CartItemRow(
                        item = item,
                        onInc = { vm.updateCartQty(item.productId, item.qty + 1) },
                        onDec = { vm.updateCartQty(item.productId, item.qty - 1) },
                        onRemove = { vm.removeFromCart(item.productId) }
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("المجموع:", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text("$total ر.ي", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onCheckout,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("متابعة الطلب", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.productName,
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFE6F7F4)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.productName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                Text("${item.priceYer} ر.ي", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(28.dp).clip(androidx.compose.foundation.shape.CircleShape).background(MaterialTheme.colorScheme.primary).clickable { onInc() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(4.dp))
                    Text("${item.qty}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.width(4.dp))
                    Box(
                        modifier = Modifier.size(28.dp).clip(androidx.compose.foundation.shape.CircleShape).background(Color(0xFFE6F7F4)).clickable { onDec() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("−", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(4.dp))
                IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Filled.Delete, contentDescription = "remove", tint = Color.Red, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
