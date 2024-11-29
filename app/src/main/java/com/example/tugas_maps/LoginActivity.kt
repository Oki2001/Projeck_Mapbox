package com.example.tugas_maps

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.tugas_maps.ui.screen.LoginScreen
import com.example.tugas_maps.ui.theme.Tugas_MapsTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class  LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setContent {
            Tugas_MapsTheme {
                LoginScreen(
                    onLogin = { username, email, password -> // Menggunakan username
                        loginUser(username, email, password)
                    },
                    onRegister = {
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    private fun loginUser(username: String, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        checkUserRoleAndUsername(user.uid, username)
                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Login gagal, coba lagi"
                    showToast(errorMessage)
                }
            }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun checkUserRoleAndUsername(userId: String, username: String) {
        database.child("users").child(userId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                val role = dataSnapshot?.child("role")?.value.toString()

                if (role == "admin") {
                    showToast("Selamat datang, $username (Admin)!")
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showToast("Selamat datang, $username!")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                showToast("Gagal memuat data pengguna: ${task.exception?.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
