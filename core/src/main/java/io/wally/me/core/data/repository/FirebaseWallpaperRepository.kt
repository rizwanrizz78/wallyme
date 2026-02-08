package io.wally.me.core.data.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.FirebaseStorage
import io.wally.me.core.data.local.WallpaperDao
import io.wally.me.core.data.local.toDomain
import io.wally.me.core.data.local.toEntity
import io.wally.me.core.model.Category
import io.wally.me.core.model.Wallpaper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseWallpaperRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val wallpaperDao: WallpaperDao
) : WallpaperRepository {

    override fun getWallpapers(): Flow<List<Wallpaper>> {
        return firestore.collection("wallpapers")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Wallpaper::class.java)?.copy(id = doc.id)
                }
            }
    }

    override fun getWallpaper(id: String): Flow<Wallpaper?> {
        return firestore.collection("wallpapers").document(id)
            .snapshots()
            .map { snapshot ->
                snapshot.toObject(Wallpaper::class.java)?.copy(id = snapshot.id)
            }
    }

    override fun getCategories(): Flow<List<Category>> {
        return firestore.collection("categories")
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Category::class.java)?.copy(id = doc.id)
                }
            }
    }

    override suspend fun addWallpaper(wallpaper: Wallpaper, imageData: ByteArray): Result<Unit> {
        return try {
            val fileName = "wallpapers/${UUID.randomUUID()}.jpg"
            val ref = storage.reference.child(fileName)

            Log.d("FirebaseRepo", "Starting image upload for wallpaper: ${wallpaper.title}")

            // Upload image
            ref.putBytes(imageData).await()
            val downloadUrl = ref.downloadUrl.await().toString()

            Log.d("FirebaseRepo", "Image uploaded successfully. URL: $downloadUrl")

            val newWallpaper = wallpaper.copy(
                url = downloadUrl,
                timestamp = System.currentTimeMillis()
            )

            Log.d("FirebaseRepo", "Saving wallpaper to Firestore...")
            firestore.collection("wallpapers").add(newWallpaper).await()
            Log.d("FirebaseRepo", "Wallpaper saved to Firestore.")

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error adding wallpaper. Message: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun updateWallpaper(wallpaper: Wallpaper): Result<Unit> {
        return try {
            firestore.collection("wallpapers").document(wallpaper.id).set(wallpaper).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error updating wallpaper", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteWallpaper(id: String): Result<Unit> {
        return try {
            val doc = firestore.collection("wallpapers").document(id).get().await()
            val wallpaper = doc.toObject(Wallpaper::class.java)

            if (wallpaper?.url != null) {
                try {
                    val storageRef = storage.getReferenceFromUrl(wallpaper.url)
                    storageRef.delete().await()
                } catch (e: Exception) {
                    Log.w("FirebaseRepo", "Could not delete image file: ${e.message}")
                }
            }

            firestore.collection("wallpapers").document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error deleting wallpaper", e)
            Result.failure(e)
        }
    }

    override suspend fun addCategory(category: Category, imageData: ByteArray): Result<Unit> {
        return try {
            val fileName = "categories/${UUID.randomUUID()}.jpg"
            val ref = storage.reference.child(fileName)

            Log.d("FirebaseRepo", "Starting image upload for category: ${category.name}")

            // Upload image
            ref.putBytes(imageData).await()
            val downloadUrl = ref.downloadUrl.await().toString()

            Log.d("FirebaseRepo", "Image uploaded successfully. URL: $downloadUrl")

            val newCategory = category.copy(
                coverUrl = downloadUrl
            )

            Log.d("FirebaseRepo", "Saving category to Firestore...")
            firestore.collection("categories").add(newCategory).await()
            Log.d("FirebaseRepo", "Category saved to Firestore.")

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error adding category. Message: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(id: String): Result<Unit> {
         return try {
            val doc = firestore.collection("categories").document(id).get().await()
            val category = doc.toObject(Category::class.java)

            if (category?.coverUrl != null) {
                try {
                    val storageRef = storage.getReferenceFromUrl(category.coverUrl)
                    storageRef.delete().await()
                } catch (e: Exception) {
                     Log.w("FirebaseRepo", "Could not delete image file: ${e.message}")
                }
            }

            firestore.collection("categories").document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error deleting category", e)
            Result.failure(e)
        }
    }

    override suspend fun incrementViewCount(id: String) {
        try {
            firestore.collection("wallpapers").document(id)
                .update("views", FieldValue.increment(1))
                .await()
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error incrementing view count", e)
        }
    }

    override suspend fun incrementDownloadCount(id: String) {
        try {
            firestore.collection("wallpapers").document(id)
                .update("downloads", FieldValue.increment(1))
                .await()
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error incrementing download count", e)
        }
    }

    override fun getFavorites(): Flow<List<Wallpaper>> {
        return wallpaperDao.getFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun isFavorite(id: String): Flow<Boolean> {
        return wallpaperDao.isFavorite(id)
    }

    override suspend fun addToFavorites(wallpaper: Wallpaper) {
        wallpaperDao.insert(wallpaper.toEntity())
    }

    override suspend fun removeFromFavorites(wallpaper: Wallpaper) {
        wallpaperDao.delete(wallpaper.toEntity())
    }

    override suspend fun testFirestoreConnection(): Result<Unit> {
        return try {
            val testData = hashMapOf(
                "timestamp" to System.currentTimeMillis(),
                "message" to "Test connection from Admin App"
            )
            Log.d("FirebaseRepo", "Attempting to write test document...")
            firestore.collection("test_connection").add(testData).await()
            Log.d("FirebaseRepo", "Test document written successfully.")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Test connection failed: ${e.message}", e)
            Result.failure(e)
        }
    }
}
