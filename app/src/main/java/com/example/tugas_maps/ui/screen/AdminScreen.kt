package com.example.tugas_maps.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.database.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onLogout: () -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    val userList by viewModel.userList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel") },
                actions = {
                    Button(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(userList) { user ->
                    UserCard(
                        user = user,
                        onEdit = { updatedUser ->
                            viewModel.editUser(userId = user.id, updatedUser = updatedUser)
                        },
                        onDelete = {
                            viewModel.deleteUser(user.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UserCard(
    user: User,
    onEdit: (User) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Username: ${user.username}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Role: ${user.role}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    val updatedUser = user.copy(role = if (user.role == "admin") "user" else "admin")
                    onEdit(updatedUser)
                }) {
                    Text("Edit Role")
                }

                Button(onClick = { onDelete() }) {
                    Text("Delete")
                }
            }
        }
    }
}
