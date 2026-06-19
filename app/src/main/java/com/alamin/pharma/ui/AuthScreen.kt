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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alamin.pharma.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    var mode by remember { mutableStateOf(0) }  // 0 = login, 1 = signup
    var input by remember { mutableStateOf("") }  // email or phone
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var showForgot by remember { mutableStateOf(false) }

    val isPhone = input.startsWith("+") || input.all { it.isDigit() } && input.isNotEmpty() && !input.contains("@")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))

        // الشعار
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text("ص", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 36.sp)
        }

        Spacer(Modifier.height(16.dp))
        Text("صيدلية الأمين الحديثة", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)
        Text("صحتك أمانة عندنا", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)

        Spacer(Modifier.height(32.dp))

        // Tabs
        TabRow(
            selectedTabIndex = mode,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = mode == 0,
                onClick = { mode = 0; error = null },
                text = { Text("تسجيل الدخول") }
            )
            Tab(
                selected = mode == 1,
                onClick = { mode = 1; error = null },
                text = { Text("حساب جديد") }
            )
        }

        Spacer(Modifier.height(24.dp))

        // الاسم (للتسجيل فقط)
        if (mode == 1) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("الاسم الكامل") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
        }

        // البريد أو الجوال
        OutlinedTextField(
            value = input,
            onValueChange = { input = it; error = null },
            label = { Text("البريد الإلكتروني أو رقم الجوال") },
            leadingIcon = {
                Icon(
                    if (isPhone) Icons.Outlined.Phone else Icons.Outlined.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPhone) KeyboardType.Phone else KeyboardType.Email
            )
        )

        Spacer(Modifier.height(12.dp))

        // كلمة المرور
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; error = null },
            label = { Text("كلمة المرور") },
            leadingIcon = {
                Icon(Icons.Outlined.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        // تذكرني + نسيت كلمة المرور
        if (mode == 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                    Text("تذكرني", fontSize = 13.sp)
                }
                TextButton(onClick = { showForgot = true }) {
                    Text("نسيت كلمة السر؟", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                }
            }
        }

        // رسالة الخطأ
        if (error != null) {
            Text(error!!, color = MaterialTheme.colorScheme.error, fontSize = 13.sp, modifier = Modifier.padding(vertical = 8.dp))
        }

        Spacer(Modifier.height(16.dp))

        // زر الدخول
        Button(
            onClick = {
                error = null
                loading = true
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        if (isPhone) {
                            // في هذا المثال: سنعالج الهاتف عبر البريد كـ placeholder
                            // للتفعيل الكامل يلزم Firebase Phone Auth + Recaptcha
                            error = "تسجيل الدخول برقم الجوال يتطلب إعداد Firebase Phone Authentication. استخدم البريد للحين."
                        } else {
                            if (mode == 0) {
                                auth.signInWithEmailAndPassword(input, password).await()
                            } else {
                                auth.createUserWithEmailAndPassword(input, password).await()
                                // إنشاء ملف المستخدم في Firestore
                                val uid = auth.currentUser?.uid
                                if (uid != null) {
                                    com.google.firebase.firestore.FirebaseFirestore.getInstance()
                                        .collection("users").document(uid)
                                        .set(User(id = uid, name = name, email = input))
                                }
                            }
                            onAuthSuccess()
                        }
                    } catch (e: Exception) {
                        error = when {
                            e.message?.contains("password") == true -> "كلمة المرور خاطئة"
                            e.message?.contains("email") == true -> "البريد الإلكتروني غير صحيح"
                            e.message?.contains("already") == true -> "البريد مستخدم بالفعل"
                            else -> e.message ?: "حدث خطأ"
                        }
                    } finally {
                        loading = false
                    }
                }
            },
            enabled = !loading && input.isNotBlank() && password.isNotBlank() && (mode == 0 || name.isNotBlank()),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(if (loading) "جاري..." else if (mode == 0) "تسجيل الدخول" else "إنشاء الحساب",
                fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(Modifier.height(40.dp))
    }

    if (showForgot) {
        ForgotPasswordDialog(
            onDismiss = { showForgot = false },
            onReset = { email ->
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        auth.sendPasswordResetEmail(email).await()
                        error = "تم إرسال رابط إعادة التعيين إلى بريدك"
                        showForgot = false
                    } catch (e: Exception) {
                        error = e.message
                    }
                }
            }
        )
    }
}

@Composable
private fun ForgotPasswordDialog(onDismiss: () -> Unit, onReset: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إعادة تعيين كلمة المرور") },
        text = {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("البريد الإلكتروني") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onReset(email) }) { Text("إرسال", color = MaterialTheme.colorScheme.primary) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء") }
        }
    )
}
