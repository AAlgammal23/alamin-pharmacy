package com.alamin.pharma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.WhatsApp
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alamin.pharma.data.ContactInfo
import com.alamin.pharma.utils.ContactUtils

@Composable
fun ConsultationScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var symptoms by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val canSubmit = name.isNotBlank() && symptoms.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(Modifier.weight(1f))
            Text(
                "استشارة طبية",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(48.dp))
        }

        // Doctor card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.MedicalServices,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "فريق صيدلية الأمين الحديثة",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "متاحون للرد على استفساراتكم الطبية",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Info
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F7F4)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "📋 املأ النموذج وسيتم التواصل معك",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "نقوم بإرسال استفسارك إلى صيادلة مؤهلين للرد عليك عبر الواتساب في أقرب وقت ممكن.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("الاسم الكامل") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("العمر (اختياري)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("رقم الهاتف (اختياري)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = symptoms,
                onValueChange = { symptoms = it },
                label = { Text("اشرح الأعراض أو الاستفسار") },
                placeholder = { Text("مثال: أعاني من صداع متكرر منذ أسبوع...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Submit button
        Button(
            onClick = {
                val msg = buildString {
                    append("السلام عليكم، أرغب باستشارة طبية من صيدلية الأمين الحديثة.")
                    append("\n\nالاسم: $name")
                    if (age.isNotBlank()) append("\nالعمر: $age")
                    if (phone.isNotBlank()) append("\nالهاتف: $phone")
                    append("\n\nالاستفسار:\n$symptoms")
                }
                ContactUtils.openWhatsApp(context, ContactInfo().whatsapp, msg)
            },
            enabled = canSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(Icons.Outlined.Send, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                "إرسال الاستفسار عبر واتساب",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Spacer(Modifier.height(40.dp))
    }
}
