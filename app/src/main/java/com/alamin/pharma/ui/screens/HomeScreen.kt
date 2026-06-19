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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material.icons.outlined.LocalPharmacy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alamin.pharma.R
import com.alamin.pharma.data.Banner
import com.alamin.pharma.data.Product
import com.alamin.pharma.ui.PharmacyViewModel
import com.alamin.pharma.ui.components.AlAminTopBar
import com.alamin.pharma.ui.components.SectionHeader

@Composable
fun HomeScreen(
    vm: PharmacyViewModel,
    onConsultation: () -> Unit,
    onPrescription: () -> Unit,
    onSearch: () -> Unit,
    onCart: () -> Unit,
    onArticleList: () -> Unit,
    onProductClick: (String) -> Unit
) {
    val banners by vm.banners.collectAsState()
    val featured by vm.featured.collectAsState()
    val newProducts by vm.newProducts.collectAsState()
    val bestsellers by vm.bestsellers.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { AlAminTopBar(onCart = onCart, onSearch = onSearch) }
        item { BannerCarousel(banners) }
        item { Spacer(Modifier.height(8.dp)) }

        // أزرار الخدمات
        item {
            ServiceRow(
                onConsultation = onConsultation,
                onPrescription = onPrescription,
                onArticles = onArticleList
            )
        }

        // الأكثر مبيعاً
        if (bestsellers.isNotEmpty()) {
            item { Spacer(Modifier.height(8.dp)) }
            item { SectionHeader(title = "الأكثر مبيعاً", actionLabel = stringResource(R.string.view_all)) }
            item { BestsellersRow(bestsellers, onClick = onProductClick, onAdd = { vm.addToCart(it) }) }
        }

        // مميزة
        if (featured.isNotEmpty()) {
            item { Spacer(Modifier.height(8.dp)) }
            item { SectionHeader(title = stringResource(R.string.featured_products), actionLabel = stringResource(R.string.view_all)) }
            item { FeaturedRow(featured, onClick = onProductClick, onAdd = { vm.addToCart(it) }) }
        }

        // جديدة
        if (newProducts.isNotEmpty()) {
            item { Spacer(Modifier.height(8.dp)) }
            item { SectionHeader(title = stringResource(R.string.new_products), actionLabel = stringResource(R.string.view_all)) }
            items(newProducts) { p ->
                NewProductRow(p, onClick = { onProductClick(p.id) }, onAdd = { vm.addToCart(p) })
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun BannerCarousel(banners: List<Banner>) {
    if (banners.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1FBFB0))
        ) {
            Column(modifier = Modifier.padding(20.dp).align(Alignment.CenterStart)) {
                Text("قريباً", color = Color(0xFFCFFF5B), fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)
                Spacer(Modifier.height(8.dp))
                Text("احصل على المكملات والأدوية العالمية بأسعار مفاجئة", color = Color.White, fontSize = 14.sp)
            }
        }
        return
    }
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(banners) { banner ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(runCatching { Color(android.graphics.Color.parseColor(banner.bgColor)) }.getOrElse { Color(0xFF1FBFB0) })
            ) {
                Column(modifier = Modifier.padding(20.dp).align(Alignment.CenterStart)) {
                    Text(banner.titleAr, color = Color(0xFFCFFF5B), fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(banner.subtitleAr, color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun ServiceRow(
    onConsultation: () -> Unit,
    onPrescription: () -> Unit,
    onArticles: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ServiceCard(
            title = "استشارة الطبيب",
            icon = Icons.Outlined.MedicalServices,
            onClick = onConsultation,
            modifier = Modifier.weight(1f)
        )
        ServiceCard(
            title = "رفع وصفة",
            icon = Icons.Outlined.UploadFile,
            onClick = onPrescription,
            modifier = Modifier.weight(1f)
        )
        ServiceCard(
            title = "المقالات",
            icon = Icons.Outlined.Article,
            onClick = onArticles,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ServiceCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDFF7F2)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            }
            Text(title, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
    }
}

@Composable
private fun FeaturedRow(products: List<Product>, onClick: (String) -> Unit, onAdd: (Product) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products) { p -> ProductCard(p, onClick = { onClick(p.id) }, onAdd = { onAdd(p) }) }
    }
}

@Composable
private fun BestsellersRow(products: List<Product>, onClick: (String) -> Unit, onAdd: (Product) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products) { p -> ProductCard(p, onClick = { onClick(p.id) }, onAdd = { onAdd(p) }) }
    }
}

@Composable
private fun ProductCard(p: Product, onClick: () -> Unit, onAdd: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.width(180.dp).clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = p.imageUrl,
                    contentDescription = p.name,
                    modifier = Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE6F7F4)),
                    contentScale = ContentScale.Crop
                )
                if (p.isBestseller) {
                    Box(
                        modifier = Modifier.align(Alignment.TopStart).padding(4.dp)
                            .clip(RoundedCornerShape(6.dp)).background(Color(0xFFFFA000)).padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("🔥 الأكثر مبيعاً", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
                if (p.oldPriceYer > p.priceYer) {
                    Box(
                        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                            .clip(RoundedCornerShape(6.dp)).background(MaterialTheme.colorScheme.tertiary).padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        val off = ((1 - p.priceYer.toDouble() / p.oldPriceYer) * 100).toInt()
                        Text("خصم $off%", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFB400), modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("${p.rating}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(4.dp))
            Text(p.name, fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground, maxLines = 2)
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("${p.priceYer} ر.ي", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    if (p.oldPriceYer > p.priceYer) {
                        Text("${p.oldPriceYer} ر.ي", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                    }
                }
                Box(
                    modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.primary).clickable { onAdd() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "add", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
private fun NewProductRow(p: Product, onClick: () -> Unit, onAdd: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = p.imageUrl,
                contentDescription = p.name,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE6F7F4)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(p.name, fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                Text("${p.priceYer} ر.ي", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.primary).clickable { onAdd() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
