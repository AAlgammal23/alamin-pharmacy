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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alamin.pharma.data.Product
import com.alamin.pharma.ui.PharmacyViewModel

@Composable
fun SearchScreen(
    vm: PharmacyViewModel,
    onBack: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onProductClick: (String) -> Unit
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var selectedMain by remember { mutableStateOf<String?>(null) }
    val allProducts by vm.products.collectAsState()
    val subs by vm.subCategories.collectAsState()

    val filtered = remember(query, selectedMain, allProducts) {
        allProducts.filter { p ->
            val matchesQuery = query.isBlank() ||
                p.name.contains(query, ignoreCase = true) ||
                p.description.contains(query, ignoreCase = true)
            val matchesCat = selectedMain == null || p.mainCategoryId == selectedMain
            matchesQuery && matchesCat
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.Clear, contentDescription = "close", tint = MaterialTheme.colorScheme.onBackground)
            }
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("ابحث عن منتج أو دواء...") },
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = if (query.isNotEmpty()) {
                    { IconButton(onClick = { query = "" }) { Icon(Icons.Filled.Clear, contentDescription = "clear") } }
                } else null,
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
            )
        }

        // فلاتر التصنيفات
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedMain == null,
                    onClick = { selectedMain = null },
                    label = { Text("الكل") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedMain == "otc",
                    onClick = { selectedMain = "otc" },
                    label = { Text("أدوية ذاتية") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedMain == "rx",
                    onClick = { selectedMain = "rx" },
                    label = { Text("أدوية وصفية") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
            item {
                FilterChip(
                    selected = selectedMain == "vitamins",
                    onClick = { selectedMain = "vitamins" },
                    label = { Text("فيتامينات") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(72.dp))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (query.isBlank() && selectedMain == null) "ابدأ بكتابة اسم المنتج"
                        else "لا توجد نتائج لـ \"$query\"",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Text("${filtered.size} نتيجة", modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            Spacer(Modifier.height(4.dp))
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { p ->
                    SearchResultCard(p, onClick = { onProductClick(p.id) }, onAddToCart = { onAddToCart(p) })
                }
            }
        }
    }
}

@Composable
private fun SearchResultCard(p: Product, onClick: () -> Unit, onAddToCart: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = p.imageUrl,
                contentDescription = p.name,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFE6F7F4)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(p.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground, maxLines = 2)
                if (p.description.isNotBlank()) {
                    Text(p.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                }
                Spacer(Modifier.height(4.dp))
                Text("${p.priceYer} ر.ي", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.primary).clickable { onAddToCart() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Add, contentDescription = "add", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
