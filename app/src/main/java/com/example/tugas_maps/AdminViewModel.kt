package com.example.tugas_maps.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class User(
    val id: String = "",
    val username: String = "",
    val role: String = ""
)

class AdminViewModel : ViewModel() {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { it.toUser() }
                _userList.value = users
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun editUser(userId: String, updatedUser: User) {
        database.child(userId).setValue(updatedUser)
    }

    fun deleteUser(userId: String) {
        database.child(userId).removeValue()
    }

    private fun DataSnapshot.toUser(): User? {
        return try {
            val id = this.key ?: return null
            val username = this.child("username").value.toString()
            val role = this.child("role").value.toString()
            User(id = id, username = username, role = role)
        } catch (e: Exception) {
            null
        }
    }
}
