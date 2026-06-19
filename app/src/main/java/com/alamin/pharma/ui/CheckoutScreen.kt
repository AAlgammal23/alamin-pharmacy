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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alamin.pharma.data.Address
import com.alamin.pharma.ui.PharmacyViewModel
import com.alamin.pharma.utils.ContactUtils

@Composable
fun CheckoutScreen(
    vm: PharmacyViewModel,
    onBack: () -> Unit,
    onOrderComplete: () -> Unit
) {
    val context = LocalContext.current
    val cart by vm.cart.collectAsState()
    val total = vm.cartTotal()
    val isOnline by vm.isOnline.collectAsState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var addressLabel by remember { mutableStateOf("البيت") }
    var fullAddress by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("إب") }
    var notes by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("cod") }  // cod = عند الاستلام
    var submitting by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf(false) }

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
            Text("إتمام الطلب", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
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
            }
        }

        if (success) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(96.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("تم استلام طلبك!", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(8.dp))
                    Text(message ?: "", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = onOrderComplete,
                        modifier = Modifier.height(48.dp).padding(horizontal = 32.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("حسناً", fontWeight = FontWeight.Bold)
                    }
                }
            }
            return
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // المنتجات
            item {
                Text("المنتجات (${cart.size})", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            }
            items(cart) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.productName, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                            Text("${item.priceYer} ر.ي", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(28.dp).clip(androidx.compose.foundation.shape.CircleShape).background(Color(0xFFE6F7F4))
                                    .clickable { vm.updateCartQty(item.productId, item.qty - 1) },
                                contentAlignment = Alignment.Center
                            ) { Icon(Icons.Filled.Remove, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp)) }
                            Spacer(Modifier.width(8.dp))
                            Text("${item.qty}", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier.size(28.dp).clip(androidx.compose.foundation.shape.CircleShape).background(MaterialTheme.colorScheme.primary)
                                    .clickable { vm.updateCartQty(item.productId, item.qty + 1) },
                                contentAlignment = Alignment.Center
                            ) { Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp)) }
                        }
                    }
                }
            }

            // معلومات التوصيل
            item {
                Spacer(Modifier.height(8.dp))
                Text("معلومات التوصيل", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            }
            item {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("الاسم الكامل") },
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = phone, onValueChange = { phone = it },
                    label = { Text("رقم الجوال") },
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = addressLabel, onValueChange = { addressLabel = it },
                        label = { Text("تسمية") },
                        leadingIcon = { Icon(Icons.Outlined.Home, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true
                    )
                    OutlinedTextField(
                        value = city, onValueChange = { city = it },
                        label = { Text("المدينة") },
                        modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true
                    )
                }
            }
            item {
                OutlinedTextField(
                    value = fullAddress, onValueChange = { fullAddress = it },
                    label = { Text("العنوان التفصيلي") },
                    modifier = Modifier.fillMaxWidth().height(80.dp), shape = RoundedCornerShape(12.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = notes, onValueChange = { notes = it },
                    label = { Text("ملاحظات (اختياري)") },
                    modifier = Modifier.fillMaxWidth().height(80.dp), shape = RoundedCornerShape(12.dp)
                )
            }

            // طريقة الدفع
            item {
                Spacer(Modifier.height(8.dp))
                Text("طريقة الدفع", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            }
            item {
                PaymentOption(
                    icon = Icons.Outlined.LocalShipping,
                    title = "الدفع عند الاستلام",
                    subtitle = "ادفع نقداً للمندوب",
                    isSelected = paymentMethod == "cod",
                    onClick = { paymentMethod = "cod" }
                )
            }
        }

        // الشريط السفلي
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("المجموع:", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text("$total ر.ي", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        submitting = true
                        val addr = Address(
                            id = "addr-${System.currentTimeMillis()}",
                            label = addressLabel,
                            fullAddress = fullAddress,
                            city = city,
                            phone = phone
                        )
                        vm.submitOrder(name, phone, addr, notes) { ok, msg ->
                            submitting = false
                            if (ok) {
                                success = true
                                message = msg
                                // إرسال نسخة عبر واتساب (اختياري)
                                val orderText = buildString {
                                    append("طلب جديد من تطبيق صيدلية الأمين الحديثة:\n")
                                    append("الاسم: $name\nالهاتف: $phone\nالعنوان: $fullAddress, $city")
                                    if (notes.isNotBlank()) append("\nملاحظات: $notes")
                                }
                                ContactUtils.openWhatsApp(context, com.alamin.pharma.data.ContactInfo().whatsapp, orderText)
                            } else {
                                message = msg
                            }
                        }
                    },
                    enabled = !submitting && cart.isNotEmpty() && name.isNotBlank() && phone.isNotBlank() && fullAddress.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (submitting) "جاري الإرسال..." else "تأكيد الطلب", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PaymentOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFDFF7F2) else Color.White
        ),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
                Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (isSelected) Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}
