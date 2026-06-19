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
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alamin.pharma.data.CAT_OTC
import com.alamin.pharma.data.CAT_RX
import com.alamin.pharma.data.CAT_VITAMINS
import com.alamin.pharma.data.MainCategories
import com.alamin.pharma.data.Product
import com.alamin.pharma.data.SubCategory
import com.alamin.pharma.ui.PharmacyViewModel

@Composable
fun CategoriesScreen(
    vm: PharmacyViewModel,
    onSubcategoryClick: (String) -> Unit
) {
    val allSubs by vm.subCategories.collectAsState()
    var selectedMain by remember { mutableStateOf(CAT_OTC) }
    val subsForMain = allSubs.filter { it.mainCategoryId == selectedMain }.sortedBy { it.order }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ===== 3 بطاقات رئيسية =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MainCategoryCard(
                title = "الأدوية الذاتية",
                isSelected = selectedMain == CAT_OTC,
                color = MaterialTheme.colorScheme.primary,
                onClick = { selectedMain = CAT_OTC },
                modifier = Modifier.weight(1f)
            )
            MainCategoryCard(
                title = "الأدوية الوصفية",
                isSelected = selectedMain == CAT_RX,
                color = MaterialTheme.colorScheme.primary,
                onClick = { selectedMain = CAT_RX },
                modifier = Modifier.weight(1f)
            )
            MainCategoryCard(
                title = "الفيتامينات والمكملات",
                isSelected = selectedMain == CAT_VITAMINS,
                color = MaterialTheme.colorScheme.primary,
                onClick = { selectedMain = CAT_VITAMINS },
                modifier = Modifier.weight(1f)
            )
        }

        // ===== شبكة الأصناف الفرعية =====
        LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(subsForMain) { sub ->
                SubCategoryCard(sub = sub, onClick = { onSubcategoryClick(sub.id) })
            }
        }
    }
}

@Composable
private fun MainCategoryCard(
    title: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(70.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color else Color(0xFFE6F7F4)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.Center) {
            Text(
                title,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun SubCategoryCard(sub: SubCategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F7F4)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    sub.icon(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                sub.nameAr,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

// helper: grid items
private fun androidx.compose.foundation.lazy.LazyGridScope.items(
    items: List<SubCategory>,
    content: @Composable (SubCategory) -> Unit
) {
    items(items.size) { i -> content(items[i]) }
}
