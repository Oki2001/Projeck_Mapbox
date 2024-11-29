package com.example.tugas_maps

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tugas_maps.ui.screen.AdminScreen
import com.example.tugas_maps.ui.theme.Tugas_MapsTheme
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContent {
            Tugas_MapsTheme {
                AdminScreen(
                    onLogout = {
                        auth.signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}
