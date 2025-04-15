package com.example.pupilmesh.Ui_Layer.Screens
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Ui_Layer.Viewmodel.PupilVm
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(SignUp: () -> Unit, Home: () -> Unit) {
    val viewmodel :PupilVm = hiltViewModel()
    val loginstatus = viewmodel.loginState.collectAsState()
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        val scope = rememberCoroutineScope()
        Button(
            onClick = {
                scope.launch {
                viewmodel.loginUser(email.value,password.value)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = {
            SignUp()
        }) {
            Text("Don't have an account? Sign Up")
        }
    }
    when (val state = loginstatus.value) {
        is State.Error -> {
            Toast.makeText(context, "Unable to login", Toast.LENGTH_SHORT).show()
        }
        is State.Loading -> {}
        is State.Success -> {
            if (state.data == true) {
                Home()
            } else {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}