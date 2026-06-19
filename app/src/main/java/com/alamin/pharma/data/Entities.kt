package com.alamin.pharma.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val priceYer: Int,
    val oldPriceYer: Int,
    val mainCategoryId: String,
    val subCategoryId: String,
    val imageUrl: String,
    val stock: Int,
    val rating: Double,
    val isFeatured: Boolean,
    val isNew: Boolean,
    val isBestseller: Boolean,
    val soldCount: Int,
    val active: Boolean,
    val createdAt: Long,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "subcategories")
data class SubCategoryEntity(
    @PrimaryKey val id: String,
    val mainCategoryId: String,
    val nameAr: String,
    val iconName: String,
    val order: Int,
    val active: Boolean
)

@Entity(tableName = "banners")
data class BannerEntity(
    @PrimaryKey val id: String,
    val titleAr: String,
    val subtitleAr: String,
    val imageUrl: String,
    val bgColor: String,
    val active: Boolean,
    val order: Int
)

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val imageUrl: String,
    val category: String,
    val author: String,
    val publishedAt: Long,
    val active: Boolean
)

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val productId: String,
    val productName: String,
    val priceYer: Int,
    val imageUrl: String,
    val qty: Int
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val productId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "pending_orders")
data class PendingOrderEntity(
    @PrimaryKey val id: String,
    val payload: String,         // JSON
    val createdAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)
