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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alamin.pharma.data.User
import com.alamin.pharma.ui.PharmacyViewModel
import com.alamin.pharma.ui.components.AlAminTopBar
import com.alamin.pharma.utils.ContactUtils
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AccountScreen(
    vm: PharmacyViewModel,
    onCart: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val contact by vm.contact.collectAsState()
    val isOnline by vm.isOnline.collectAsState()

    val user = remember {
        FirebaseAuth.getInstance().currentUser
    }
    val userName = user?.displayName ?: user?.email?.substringBefore("@") ?: "مستخدم"
    val userEmail = user?.email ?: ""

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item { AlAminTopBar(onCart = onCart) }
        item { Spacer(Modifier.height(8.dp)) }

        // بطاقة المستخدم
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(userName.first().toString(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(userName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(userEmail, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                    }
                }
            }
        }

        // الرصيد والنقاط
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(icon = Icons.Filled.AccountBalanceWallet, title = "الرصيد", value = "0 ر.ي", modifier = Modifier.weight(1f))
                StatCard(icon = Icons.Filled.Star, title = "النقاط", value = "0", modifier = Modifier.weight(1f))
            }
        }

        // القائمة الرئيسية
        item {
            Spacer(Modifier.height(8.dp))
            Text("خدماتي", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccountRow(icon = Icons.Filled.ShoppingBag, title = "طلباتي", subtitle = "تتبع وإدارة طلباتك") { onNavigate("orders") }
                AccountRow(icon = Icons.Filled.Favorite, title = "المفضلة", subtitle = "منتجاتك المفضلة") { onNavigate("favorites") }
                AccountRow(icon = Icons.Filled.LocationOn, title = "عناويني", subtitle = "إدارة عناوين التوصيل") { onNavigate("addresses") }
                AccountRow(icon = Icons.Filled.AccountBalanceWallet, title = "رصيدي ونقاطي", subtitle = "عرض الرصيد وكسب النقاط") { onNavigate("balance") }
                AccountRow(icon = Icons.Outlined.Notifications, title = "الإشعارات", subtitle = "إشعارات الطلبات والعروض") { onNavigate("notifications") }
                AccountRow(icon = Icons.Filled.SupportAgent, title = "الدعم والتواصل", subtitle = "تواصل مع خدمة العملاء") { ContactUtils.openWhatsApp(context, contact.whatsapp, "السلام عليكم، أحتاج دعم من تطبيق صيدلية الأمين الحديثة.") }
            }
        }

        // معلومات وإعدادات
        item {
            Spacer(Modifier.height(8.dp))
            Text("المزيد", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccountRow(icon = Icons.Outlined.Article, title = "المقالات الصحية", subtitle = "نصائح ومقالات طبية") { onNavigate("articles") }
                AccountRow(icon = Icons.Outlined.Info, title = "معلومات عنا", subtitle = contact.address) { onNavigate("about") }
                AccountRow(icon = Icons.Outlined.Language, title = "اللغة", subtitle = "العربية") { }
                AccountRow(icon = Icons.Filled.Settings, title = "الإعدادات", subtitle = "إعدادات الحساب والتطبيق") { onNavigate("settings") }
                AccountRow(icon = Icons.Outlined.Description, title = "سياسة التطبيق والشروط", subtitle = "") { onNavigate("terms") }
                if (user != null) {
                    AccountRow(icon = Icons.Outlined.Logout, title = "تسجيل الخروج", subtitle = userEmail, isDanger = true) {
                        FirebaseAuth.getInstance().signOut()
                    }
                }
            }
        }

        // مؤشر الاتصال
        item {
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isOnline) "🟢 متصل" else "🟡 أوفلاين — التغييرات تُحفظ محلياً",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatCard(icon: ImageVector, title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F7F4)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(value, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}

@Composable
private fun AccountRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isDanger: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape)
                    .background(if (isDanger) Color(0xFFFFE0E0) else MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = if (isDanger) Color.Red else Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                    color = if (isDanger) Color.Red else MaterialTheme.colorScheme.onBackground)
                if (subtitle.isNotBlank()) {
                    Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text("›", fontSize = 22.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
