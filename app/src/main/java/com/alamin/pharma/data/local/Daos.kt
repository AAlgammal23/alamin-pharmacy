package com.alamin.pharma.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE active = 1 ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE mainCategoryId = :mainId AND active = 1 ORDER BY createdAt DESC")
    fun observeByMainCategory(mainId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE subCategoryId = :subId AND active = 1 ORDER BY createdAt DESC")
    fun observeBySubCategory(subId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isFeatured = 1 AND active = 1 ORDER BY createdAt DESC")
    fun observeFeatured(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isNew = 1 AND active = 1 ORDER BY createdAt DESC")
    fun observeNew(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isBestseller = 1 AND active = 1 ORDER BY soldCount DESC")
    fun observeBestsellers(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<ProductEntity?>

    @Query("""
        SELECT * FROM products
        WHERE active = 1 AND (name LIKE '%' || :q || '%' OR description LIKE '%' || :q || '%')
        ORDER BY createdAt DESC
    """)
    fun search(q: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clear()
}

@Dao
interface SubCategoryDao {
    @Query("SELECT * FROM subcategories WHERE mainCategoryId = :mainId AND active = 1 ORDER BY `order`")
    fun observeByMain(mainId: String): Flow<List<SubCategoryEntity>>

    @Query("SELECT * FROM subcategories WHERE active = 1 ORDER BY mainCategoryId, `order`")
    fun observeAll(): Flow<List<SubCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<SubCategoryEntity>)

    @Query("DELETE FROM subcategories")
    suspend fun clear()
}

@Dao
interface BannerDao {
    @Query("SELECT * FROM banners WHERE active = 1 ORDER BY `order`")
    fun observeAll(): Flow<List<BannerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<BannerEntity>)

    @Query("DELETE FROM banners")
    suspend fun clear()
}

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles WHERE active = 1 ORDER BY publishedAt DESC")
    fun observeAll(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<ArticleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    suspend fun clear()
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    fun observe(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartEntity)

    @Query("DELETE FROM cart WHERE productId = :productId")
    suspend fun remove(productId: String)

    @Query("DELETE FROM cart")
    suspend fun clear()

    @Query("UPDATE cart SET qty = :qty WHERE productId = :productId")
    suspend fun updateQty(productId: String, qty: Int)
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun observe(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE productId = :productId)")
    fun isFavorite(productId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(item: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE productId = :productId")
    suspend fun remove(productId: String)
}

@Dao
interface PendingOrderDao {
    @Query("SELECT * FROM pending_orders WHERE synced = 0")
    suspend fun getUnsynced(): List<PendingOrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: PendingOrderEntity)

    @Query("UPDATE pending_orders SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: String)
}
