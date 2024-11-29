package com.example.tugas_maps

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tugas_maps.ui.screen.RegisterScreen
import com.example.tugas_maps.ui.theme.Tugas_MapsTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setContent {
            Tugas_MapsTheme {
                RegisterScreen { username, email, password ->
                    registerUser(username, email, password)
                }
            }
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        val userMap = mapOf(
                            "username" to username,
                            "role" to "user" // Default role, bisa diubah sesuai kebutuhan
                        )
                        database.child("users").child(userId).setValue(userMap)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    showToast("Pendaftaran berhasil")
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                } else {
                                    showToast("Gagal menyimpan data pengguna: ${dbTask.exception?.message}")
                                }
                            }
                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Pendaftaran gagal, coba lagi"
                    showToast(errorMessage)
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
