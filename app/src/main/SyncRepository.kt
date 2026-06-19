package com.alamin.pharma.data

import android.content.Context
import com.alamin.pharma.data.local.AppDatabase
import com.alamin.pharma.data.local.ArticleEntity
import com.alamin.pharma.data.local.BannerEntity
import com.alamin.pharma.data.local.CartEntity
import com.alamin.pharma.data.local.FavoriteEntity
import com.alamin.pharma.data.local.PendingOrderEntity
import com.alamin.pharma.data.local.ProductEntity
import com.alamin.pharma.data.local.SubCategoryEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * مستودع هجين:
 * - القراءة من Room (offline-first)
 * - الكتابة في Firestore + Room (online)
 * - الطلبات المعلقة تُحفظ في Room وتُرفع عند توفر الإنترنت
 */
class SyncRepository(context: Context) {
    private val db = AppDatabase.get(context)
    private val firestore = FirebaseFirestore.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)

    private val productDao = db.productDao()
    private val subCategoryDao = db.subCategoryDao()
    private val bannerDao = db.bannerDao()
    private val articleDao = db.articleDao()
    private val cartDao = db.cartDao()
    private val favoriteDao = db.favoriteDao()
    private val pendingOrderDao = db.pendingOrderDao()

    // ============== المزامنة ==============
    suspend fun syncAll() {
        syncSubCategories()
        syncProducts()
        syncBanners()
        syncArticles()
        syncPendingOrders()
    }

    private suspend fun syncSubCategories() {
        val snap = firestore.collection("subcategories")
            .whereEqualTo("active", true)
            .orderBy("order", Query.Direction.ASCENDING)
            .get().await()
        val list = snap.documents.mapNotNull { d ->
            d.toObject(SubCategoryEntity::class.java)?.copy(id = d.id)
        }
        subCategoryDao.upsertAll(list)
    }

    private suspend fun syncProducts() {
        val snap = firestore.collection("products")
            .whereEqualTo("active", true)
            .get().await()
        val list = snap.documents.mapNotNull { d ->
            d.toObject(ProductEntity::class.java)?.copy(id = d.id)
        }
        productDao.upsertAll(list)
    }

    private suspend fun syncBanners() {
        val snap = firestore.collection("banners")
            .whereEqualTo("active", true)
            .get().await()
        val list = snap.documents.mapNotNull { d ->
            d.toObject(BannerEntity::class.java)?.copy(id = d.id)
        }
        bannerDao.upsertAll(list)
    }

    private suspend fun syncArticles() {
        val snap = firestore.collection("articles")
            .whereEqualTo("active", true)
            .get().await()
        val list = snap.documents.mapNotNull { d ->
            d.toObject(ArticleEntity::class.java)?.copy(id = d.id)
        }
        articleDao.upsertAll(list)
    }

    private suspend fun syncPendingOrders() {
        val pending = pendingOrderDao.getUnsynced()
        for (order in pending) {
            try {
                // نرفع الـ payload كما هو مع timestamp
                firestore.collection("orders").document(order.id).set(
                    mapOf(
                        "payload" to order.payload,
                        "syncedAt" to System.currentTimeMillis(),
                        "status" to "PENDING"
                    )
                ).await()
                pendingOrderDao.markSynced(order.id)
            } catch (e: Exception) {
                // سنتحاول مرة أخرى لاحقاً
            }
        }
    }

    // ============== تدفقات البيانات (من Room) ==============
    fun productsFlow(): Flow<List<Product>> = productDao.observeAll().map { list ->
        list.map { it.toDomain() }
    }
    fun featuredFlow(): Flow<List<Product>> = productDao.observeFeatured().map { it.map { e -> e.toDomain() } }
    fun newFlow(): Flow<List<Product>> = productDao.observeNew().map { it.map { e -> e.toDomain() } }
    fun bestsellersFlow(): Flow<List<Product>> = productDao.observeBestsellers().map { it.map { e -> e.toDomain() } }
    fun productsByMainFlow(mainId: String): Flow<List<Product>> =
        productDao.observeByMainCategory(mainId).map { it.map { e -> e.toDomain() } }
    fun productsBySubFlow(subId: String): Flow<List<Product>> =
        productDao.observeBySubCategory(subId).map { it.map { e -> e.toDomain() } }
    fun productByIdFlow(id: String): Flow<Product?> =
        productDao.observeById(id).map { it?.toDomain() }
    fun searchFlow(q: String): Flow<List<Product>> =
        productDao.search(q).map { it.map { e -> e.toDomain() } }

    fun subCategoriesFlow(): Flow<List<SubCategory>> = subCategoryDao.observeAll().map { list ->
        list.map { it.toDomain() }
    }
    fun subCategoriesByMainFlow(mainId: String): Flow<List<SubCategory>> =
        subCategoryDao.observeByMain(mainId).map { it.map { e -> e.toDomain() } }

    fun bannersFlow(): Flow<List<Banner>> = bannerDao.observeAll().map { list ->
        list.map { it.toDomain() }
    }
    fun articlesFlow(): Flow<List<Article>> = articleDao.observeAll().map { list ->
        list.map { it.toDomain() }
    }
    fun articleByIdFlow(id: String): Flow<Article?> = articleDao.observeById(id).map { it?.toDomain() }

    // ============== السلة ==============
    fun cartFlow(): Flow<List<CartItem>> = cartDao.observe().map { list ->
        list.map { CartItem(it.productId, it.productName, it.priceYer, it.imageUrl, it.qty) }
    }
    suspend fun addToCart(item: CartItem) {
        cartDao.upsert(CartEntity(item.productId, item.productName, item.priceYer, item.imageUrl, item.qty))
    }
    suspend fun updateCartQty(productId: String, qty: Int) {
        if (qty <= 0) cartDao.remove(productId) else cartDao.updateQty(productId, qty)
    }
    suspend fun removeFromCart(productId: String) = cartDao.remove(productId)
    suspend fun clearCart() = cartDao.clear()

    // ============== المفضلة ==============
    fun favoritesFlow(): Flow<List<FavoriteEntity>> = favoriteDao.observe()
    fun isFavoriteFlow(productId: String): Flow<Boolean> = favoriteDao.isFavorite(productId)
    suspend fun toggleFavorite(productId: String, currentlyFav: Boolean) {
        if (currentlyFav) favoriteDao.remove(productId) else favoriteDao.add(FavoriteEntity(productId))
        // مزامنة إلى Firestore في الخلفية
        scope.launch {
            try {
                val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    val doc = firestore.collection("users").document(user.uid)
                        .collection("favorites").document(productId)
                    if (currentlyFav) doc.delete().await() else doc.set(mapOf("addedAt" to System.currentTimeMillis())).await()
                }
            } catch (_: Exception) {}
        }
    }

    // ============== الطلبات ==============
    suspend fun submitOrder(order: Order): Result<String> {
        return try {
            firestore.collection("orders").add(order).await()
            Result.success("تم إرسال الطلب بنجاح")
        } catch (e: Exception) {
            // حفظ محلي للمزامنة لاحقاً — نبني JSON بسيط يدوياً
            val payload = buildString {
                append("{")
                append("\"id\":\"${order.id}\",")
                append("\"userId\":\"${order.userId}\",")
                append("\"userName\":\"${order.userName}\",")
                append("\"userPhone\":\"${order.userPhone}\",")
                append("\"total\":${order.total},")
                append("\"status\":\"${order.status}\",")
                append("\"paymentMethod\":\"${order.paymentMethod}\",")
                append("\"notes\":\"${order.notes}\",")
                append("\"createdAt\":${order.createdAt},")
                append("\"itemsCount\":${order.items.size}")
                append("}")
            }
            pendingOrderDao.insert(PendingOrderEntity(order.id, payload))
            Result.success("تم حفظ الطلب — سيُرفع تلقائياً عند توفر الإنترنت")
        }
    }

    // ============== معلومات التواصل (مباشرة من Firestore) ==============
    fun contactInfoFlow(): Flow<ContactInfo> = kotlinx.coroutines.flow.callbackFlow {
        val reg = firestore.collection("settings").document("contact")
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                trySend(snap?.toObject(ContactInfo::class.java) ?: ContactInfo())
            }
        awaitClose { reg.remove() }
    }
}

// ============== Mappers ==============
private fun ProductEntity.toDomain() = Product(
    id, name, description, priceYer, oldPriceYer, mainCategoryId, subCategoryId,
    imageUrl, stock, rating, isFeatured, isNew, isBestseller, soldCount, active, createdAt
)
private fun SubCategoryEntity.toDomain() = SubCategory(id, mainCategoryId, nameAr, iconName, order, active)
private fun BannerEntity.toDomain() = Banner(id, titleAr, subtitleAr, imageUrl, bgColor, active, order)
private fun ArticleEntity.toDomain() = Article(id, title, summary, content, imageUrl, category, author, publishedAt, active)
