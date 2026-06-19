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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddressesScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.onBackground)
                }
                Text("عناويني", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }
            Box(modifier = Modifier.fillMaxSize().padding(40.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(72.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("ستُضاف العناوين تلقائياً عند أول طلب", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(4.dp))
                    Text("يمكنك إضافة عناوين متعددة من شاشة الطلب", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun BalanceScreen(onBack: () -> Unit) {
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
            Text("رصيدي ونقاطي", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("الرصيد الحالي", color = Color.White, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("0 ر.ي", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                    }
                }
            }
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFA000)),
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("النقاط", color = Color.White, fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("0 نقطة", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                        Spacer(Modifier.height(4.dp))
                        Text("كل 100 نقطة = خصم 5%", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                    }
                }
            }
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("كيف تكسب النقاط؟", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                        Spacer(Modifier.height(8.dp))
                        Text("• 1 نقطة لكل 100 ر.ي إنفاق", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("• 50 نقطة عند تسجيلك لأول مرة", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("• 100 نقطة لكل صديق تدعوه", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
