package com.alamin.pharma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alamin.pharma.data.CartItem
import com.alamin.pharma.data.Product
import com.alamin.pharma.ui.PharmacyViewModel
import com.alamin.pharma.utils.ContactUtils

@Composable
fun ProductDetailsScreen(
    vm: PharmacyViewModel,
    productId: String,
    onBack: () -> Unit,
    onCart: () -> Unit
) {
    val context = LocalContext.current
    val productFlow = remember(productId) { vm.repo.productByIdFlow(productId) }
    val product by productFlow.collectAsState(initial = null)
    val cart by vm.cart.collectAsState()
    val currentInCart = cart.firstOrNull { it.productId == productId }?.qty ?: 0
    val fav by vm.isFavorite(productId).collectAsState()

    val p = product
    if (p == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("جاري التحميل...")
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { vm.toggleFavorite(p.id, fav) }) {
                Icon(
                    if (fav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "fav",
                    tint = if (fav) Color.Red else MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = onCart) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = "cart", tint = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(Modifier.width(40.dp))
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                // صورة المنتج
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE6F7F4))
                ) {
                    AsyncImage(
                        model = p.imageUrl,
                        contentDescription = p.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(p.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFB400), modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("${p.rating}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(12.dp))
                        Text("• ${p.soldCount} مبيعات", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${p.priceYer} ر.ي", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        if (p.oldPriceYer > p.priceYer) {
                            Spacer(Modifier.width(8.dp))
                            Text("${p.oldPriceYer} ر.ي", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                            Spacer(Modifier.width(8.dp))
                            val off = ((1 - p.priceYer.toDouble() / p.oldPriceYer) * 100).toInt()
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.tertiary)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("خصم $off%", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // الوصف
                    Text("الوصف", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        p.description.ifBlank { "لا يوجد وصف لهذا المنتج." },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(16.dp))

                    // توفر
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (p.stock > 0) Color(0xFF2BB673) else Color.Red)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            if (p.stock > 0) "متوفر (${p.stock} قطعة)" else "غير متوفر",
                            fontSize = 13.sp,
                            color = if (p.stock > 0) Color(0xFF2BB673) else Color.Red
                        )
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }

        // شريط الإضافة للسلة
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // عداد الكمية
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE6F7F4))
                        .clickable { vm.updateCartQty(p.id, currentInCart - 1) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Remove, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.width(12.dp))
                Text("$currentInCart", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { vm.addToCart(p) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
                }
            }
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = {
                    vm.addToCart(p)
                    onCart()
                },
                enabled = p.stock > 0,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("أضف للسلة", fontWeight = FontWeight.Bold)
            }
        }
    }
}
