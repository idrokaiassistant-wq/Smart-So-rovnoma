package com.smartsorovnoma.admin.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val currentUser get() = auth.currentUser

    suspend fun signInWithGoogle(idToken: String): Result<Boolean> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            checkAdminStatus()
        } catch (e: Exception) {
            Result.failure(Exception("Google orqali kirishda xatolik yuz berdi"))
        }
    }

    suspend fun checkAdminStatus(): Result<Boolean> {
        val user = auth.currentUser ?: return Result.failure(Exception("Tizimga kirilmagan"))
        
        return try {
            val snapshot = firestore.collection("users").document(user.uid).get().await()
            
            if (!snapshot.exists()) {
                 return Result.failure(Exception("Foydalanuvchi ma'lumotlari topilmadi"))
            }

            val role = snapshot.getString("role")
            val enabled = snapshot.getBoolean("enabled") ?: false
            
            if (role == "admin" && enabled) {
                Result.success(true)
            } else {
                Result.failure(Exception("Ruxsat berilmadi: Sizda administrator huquqi yo'q."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Administrator tekshiruvini bajarib bo'lmadi"))
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
