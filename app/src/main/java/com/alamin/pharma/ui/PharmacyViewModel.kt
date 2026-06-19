package com.alamin.pharma.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alamin.pharma.data.Address
import com.alamin.pharma.data.Article
import com.alamin.pharma.data.Banner
import com.alamin.pharma.data.CartItem
import com.alamin.pharma.data.ContactInfo
import com.alamin.pharma.data.Order
import com.alamin.pharma.data.Product
import com.alamin.pharma.data.SubCategory
import com.alamin.pharma.data.SyncRepository
import com.alamin.pharma.sync.NetworkObserver
import com.alamin.pharma.sync.SyncWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class PharmacyViewModel(app: Application) : AndroidViewModel(app) {
    val repo = SyncRepository(app)

    val isOnline: StateFlow<Boolean> = NetworkObserver.observe(app)
        .stateIn(viewModelScope, SharingStarted.Eagerly, NetworkObserver.isOnline(app))

    val products: StateFlow<List<Product>> = repo.productsFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val featured: StateFlow<List<Product>> = repo.featuredFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val newProducts: StateFlow<List<Product>> = repo.newFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val bestsellers: StateFlow<List<Product>> = repo.bestsellersFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val subCategories: StateFlow<List<SubCategory>> = repo.subCategoriesFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val banners: StateFlow<List<Banner>> = repo.bannersFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val articles: StateFlow<List<Article>> = repo.articlesFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val contact: StateFlow<ContactInfo> = repo.contactInfoFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, ContactInfo())
    val cart: StateFlow<List<CartItem>> = repo.cartFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Sync status
    private val _lastSyncAt = MutableStateFlow<Long?>(null)
    val lastSyncAt: StateFlow<Long?> = _lastSyncAt.asStateFlow()

    init {
        sync()
        SyncWorker.schedule(app)
    }

    fun sync() {
        viewModelScope.launch {
            try {
                repo.syncAll()
                _lastSyncAt.value = System.currentTimeMillis()
            } catch (_: Exception) {}
        }
    }

    // ============== السلة ==============
    fun addToCart(p: Product, qty: Int = 1) {
        viewModelScope.launch {
            val current = cart.value.firstOrNull { it.productId == p.id }
            val newQty = (current?.qty ?: 0) + qty
            repo.addToCart(CartItem(p.id, p.name, p.priceYer, p.imageUrl, newQty))
        }
    }

    fun updateCartQty(productId: String, qty: Int) {
        viewModelScope.launch { repo.updateCartQty(productId, qty) }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch { repo.removeFromCart(productId) }
    }

    fun clearCart() {
        viewModelScope.launch { repo.clearCart() }
    }

    fun cartTotal(): Int = cart.value.sumOf { it.priceYer * it.qty }

    // ============== الطلبات ==============
    fun submitOrder(
        name: String,
        phone: String,
        address: Address,
        notes: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val order = Order(
                id = UUID.randomUUID().toString(),
                userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: "",
                userName = name,
                userPhone = phone,
                address = address,
                items = cart.value,
                total = cartTotal(),
                notes = notes
            )
            val result = repo.submitOrder(order)
            result.onSuccess {
                clearCart()
                onResult(true, it)
            }.onFailure { onResult(false, it.message ?: "خطأ غير معروف") }
        }
    }

    // ============== المفضلة ==============
    fun isFavorite(productId: String) =
        repo.isFavoriteFlow(productId)
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun toggleFavorite(productId: String, currentlyFav: Boolean) {
        viewModelScope.launch { repo.toggleFavorite(productId, currentlyFav) }
    }
}
