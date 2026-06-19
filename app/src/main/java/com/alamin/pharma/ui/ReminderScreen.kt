package com.alamin.pharma.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Plus
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alamin.pharma.R
import com.alamin.pharma.ui.components.AlAminTopBar

data class Reminder(
    val id: Int,
    val medicine: String,
    val time: String,
    val enabled: Boolean = true
)

@Composable
fun ReminderScreen(modifier: Modifier = Modifier) {
    val reminders = remember {
        mutableStateListOf(
            Reminder(1, "باراسيتامول 500mg", "08:00 ص"),
            Reminder(2, "فيتامين د", "12:30 م"),
            Reminder(3, "دواء الضغط", "09:00 م")
        )
    }
    var showDialog by remember { mutableStateOf(false) }
    var medName by remember { mutableStateOf("") }
    var medTime by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AlAminTopBar()
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.reminder_title),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))

            if (reminders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.Alarm,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "لا توجد منبهات بعد. اضغط + لإضافة منبه جديد.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = 16.dp, end = 16.dp, bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(reminders.toList()) { r ->
                        ReminderRow(
                            reminder = r,
                            onToggle = { checked ->
                                val i = reminders.indexOfFirst { it.id == r.id }
                                if (i >= 0) reminders[i] = r.copy(enabled = checked)
                            },
                            onDelete = {
                                reminders.removeAll { it.id == r.id }
                            }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Outlined.Plus, contentDescription = "add")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.add_reminder)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = medName,
                        onValueChange = { medName = it },
                        label = { Text("اسم الدواء") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = medTime,
                        onValueChange = { medTime = it },
                        label = { Text("الوقت (مثال: 08:00 ص)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (medName.isNotBlank() && medTime.isNotBlank()) {
                        reminders.add(
                            Reminder(
                                id = (reminders.maxOfOrNull { it.id } ?: 0) + 1,
                                medicine = medName,
                                time = medTime
                            )
                        )
                        medName = ""
                        medTime = ""
                        showDialog = false
                    }
                }) { Text("حفظ", color = MaterialTheme.colorScheme.primary) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("إلغاء") }
            }
        )
    }
}

@Composable
private fun ReminderRow(
    reminder: Reminder,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F7F4)),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Medication,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    reminder.medicine,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    reminder.time,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(checked = reminder.enabled, onCheckedChange = onToggle)
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
