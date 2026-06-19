package com.alamin.pharma.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

object ContactUtils {

    fun openWhatsApp(context: Context, phone: String, message: String = "") {
        val cleanPhone = phone.replace("+", "").replace(" ", "")
        val url = if (message.isBlank()) {
            "https://wa.me/$cleanPhone"
        } else {
            "https://wa.me/$cleanPhone?text=${Uri.encode(message)}"
        }
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    .setPackage("com.whatsapp")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (e: Exception) {
            try {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            } catch (e2: Exception) {
                Toast.makeText(context, "تطبيق واتساب غير مثبت", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openDialer(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun openEmail(context: Context, email: String, subject: String = "") {
        val uri = Uri.parse("mailto:$email" + if (subject.isNotBlank()) "?subject=${Uri.encode(subject)}" else "")
        context.startActivity(
            Intent(Intent.ACTION_SENDTO, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun openUrl(context: Context, url: String) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun openLocation(context: Context, query: String) {
        val uri = Uri.parse("geo:0,0?q=${Uri.encode(query)}")
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (e: Exception) {
            openUrl(context, "https://www.google.com/maps/search/${Uri.encode(query)}")
        }
    }

    fun sharePrescription(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "إرسال الوصفة"))
    }

    fun shareImageUri(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "إرسال صورة الوصفة"))
    }
}
