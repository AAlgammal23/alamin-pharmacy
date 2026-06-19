package com.alamin.pharma.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.LocalDining
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Soap
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

// ============== التصنيفات الرئيسية ==============
// 3 فئات رئيسية
const val CAT_OTC = "otc"            // الأدوية الذاتية
const val CAT_RX = "rx"              // الأدوية الوصفية
const val CAT_VITAMINS = "vitamins"  // الفيتامينات والمكملات

@Serializable
data class MainCategory(
    val id: String = "",
    val nameAr: String = "",
    val order: Int = 0
)

// ============== الأصناف الفرعية (22 صنف) ==============
@Serializable
data class SubCategory(
    val id: String = "",
    val mainCategoryId: String = "",  // otc / rx / vitamins
    val nameAr: String = "",
    val iconName: String = "MedicalServices",
    val order: Int = 0,
    val active: Boolean = true
) {
    fun icon(): ImageVector = when (iconName) {
        "MedicalServices" -> Icons.Outlined.MedicalServices
        "Face" -> Icons.Outlined.Face
        "ChildCare" -> Icons.Outlined.ChildCare
        "Soap" -> Icons.Outlined.Soap
        "LocalDining" -> Icons.Outlined.LocalDining
        else -> Icons.Outlined.MedicalServices
    }
}

val DefaultSubCategories = listOf(
    // ============== الأدوية الذاتية (OTC) ==============
    SubCategory(id = "otc-eye-ear", mainCategoryId = CAT_OTC, nameAr = "أدوية العين والأذن", order = 1),
    SubCategory(id = "otc-cold", mainCategoryId = CAT_OTC, nameAr = "أدوية الزكام والسعال", order = 2),
    SubCategory(id = "otc-skin", mainCategoryId = CAT_OTC, nameAr = "أدوية الجلدية", order = 3),
    SubCategory(id = "otc-stomach", mainCategoryId = CAT_OTC, nameAr = "أدوية المعدة", order = 4),
    SubCategory(id = "otc-sexual", mainCategoryId = CAT_OTC, nameAr = "أدوية الصحة الجنسية", order = 5),
    SubCategory(id = "otc-baby", mainCategoryId = CAT_OTC, nameAr = "أدوية الأطفال والمواليد", order = 6),
    SubCategory(id = "otc-painkiller", mainCategoryId = CAT_OTC, nameAr = "مسكنات الألم", order = 7),

    // ============== الأدوية الوصفية (Rx) ==============
    SubCategory(id = "rx-hormones", mainCategoryId = CAT_RX, nameAr = "أدوية الهرمونات", order = 1),
    SubCategory(id = "rx-bp", mainCategoryId = CAT_RX, nameAr = "ضغط الدم", order = 2),
    SubCategory(id = "rx-antibiotics", mainCategoryId = CAT_RX, nameAr = "المضادات الحيوية", order = 3),
    SubCategory(id = "rx-inflammatory", mainCategoryId = CAT_RX, nameAr = "الالتهابات", order = 4),
    SubCategory(id = "rx-diabetes", mainCategoryId = CAT_RX, nameAr = "السكري", order = 5),
    SubCategory(id = "rx-cholesterol", mainCategoryId = CAT_RX, nameAr = "الكوليسترول", order = 6),
    SubCategory(id = "rx-depression", mainCategoryId = CAT_RX, nameAr = "الاكتئاب", order = 7),

    // ============== الفيتامينات والمكملات ==============
    SubCategory(id = "vit-general", mainCategoryId = CAT_VITAMINS, nameAr = "صحة عامة", order = 1),
    SubCategory(id = "vit-sleep", mainCategoryId = CAT_VITAMINS, nameAr = "نوم", order = 2),
    SubCategory(id = "vit-minerals", mainCategoryId = CAT_VITAMINS, nameAr = "معادن", order = 3),
    SubCategory(id = "vit-slimming", mainCategoryId = CAT_VITAMINS, nameAr = "تنحيف", order = 4),
    SubCategory(id = "vit-skin", mainCategoryId = CAT_VITAMINS, nameAr = "بشرة", order = 5),
    SubCategory(id = "vit-immunity", mainCategoryId = CAT_VITAMINS, nameAr = "مناعة", order = 6),
    SubCategory(id = "vit-sports", mainCategoryId = CAT_VITAMINS, nameAr = "رياضيين", order = 7),
    SubCategory(id = "vit-kids", mainCategoryId = CAT_VITAMINS, nameAr = "صحة الطفل", order = 8),
    SubCategory(id = "vit-women", mainCategoryId = CAT_VITAMINS, nameAr = "صحة المرأة", order = 9)
)

val MainCategories = listOf(
    MainCategory(id = CAT_OTC, nameAr = "الأدوية الذاتية", order = 1),
    MainCategory(id = CAT_RX, nameAr = "الأدوية الوصفية", order = 2),
    MainCategory(id = CAT_VITAMINS, nameAr = "الفيتامينات والمكملات الغذائية", order = 3)
)

@Serializable
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val priceYer: Int = 0,
    val oldPriceYer: Int = 0,
    val mainCategoryId: String = "",   // otc / rx / vitamins
    val subCategoryId: String = "",    // مثل otc-eye-ear
    val imageUrl: String = "",
    val stock: Int = 0,
    val rating: Double = 0.0,
    val isFeatured: Boolean = false,
    val isNew: Boolean = false,
    val isBestseller: Boolean = false,
    val soldCount: Int = 0,
    val active: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class Banner(
    val id: String = "",
    val titleAr: String = "",
    val subtitleAr: String = "",
    val imageUrl: String = "",
    val bgColor: String = "#1FBFB0",
    val active: Boolean = true,
    val order: Int = 0
)

@Serializable
data class PromoOffer(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val discountPercent: Int = 0,
    val validUntil: Long = 0L,
    val active: Boolean = true
)

// ============== المستخدم ==============
@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val balance: Int = 0,         // الرصيد
    val points: Int = 0,          // النقاط
    val addresses: List<Address> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class Address(
    val id: String = "",
    val label: String = "",       // البيت، العمل...
    val fullAddress: String = "",
    val city: String = "",
    val phone: String = ""
)

// ============== الطلبات ==============
enum class OrderStatus(val ar: String) {
    PENDING("قيد المراجعة"),
    CONFIRMED("مؤكد"),
    PREPARING("قيد التجهيز"),
    DELIVERED("تم التوصيل"),
    CANCELLED("ملغي")
}

@Serializable
data class Order(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhone: String = "",
    val address: Address = Address(),
    val items: List<CartItem> = emptyList(),
    val total: Int = 0,
    val status: String = OrderStatus.PENDING.name,
    val paymentMethod: String = "cod",  // cod = الدفع عند الاستلام
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

// ============== المفضلة ==============
@Serializable
data class Favorite(
    val userId: String = "",
    val productId: String = ""
)

// ============== السلة ==============
@Serializable
data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val priceYer: Int = 0,
    val imageUrl: String = "",
    val qty: Int = 1
)

// ============== معلومات التواصل ==============
@Serializable
data class ContactInfo(
    val whatsapp: String = "+967774973636",
    val whatsappRx: String = "+967784332800",
    val phone: String = "+967774973636",
    val email: String = "alaminmodern.ph@gmail.com",
    val address: String = "اليمن - إب - مدينة القاعدة",
    val facebook: String = "https://www.facebook.com/share/18BNE6VzVK/",
    val workingHours: String = "يومياً من 8 صباحاً - 11 مساءً"
)

// ============== المقالات الصحية ==============
@Serializable
data class Article(
    val id: String = "",
    val title: String = "",
    val summary: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val category: String = "",        // nutrition / kids / diseases / general
    val author: String = "صيدلية الأمين الحديثة",
    val publishedAt: Long = System.currentTimeMillis(),
    val active: Boolean = true
)
