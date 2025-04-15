package com.example.pupilmesh.Ui_Layer.Screens
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pupilmesh.Common.State.State
import com.example.pupilmesh.Ui_Layer.Viewmodel.PupilVm

@Composable
fun SignupScreen(PopScreen: () -> Unit) {
    val context= LocalContext.current
    val viewModel:PupilVm = hiltViewModel()
    val insertState = viewModel.insertState.collectAsState()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)

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

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if(password.value==confirmPassword.value && password.value!="" && confirmPassword.value!="" &&email.value!=""){
                    viewModel.insertUser(email.value,password.value)
                }
                else{
                    Toast.makeText(context, "Password doesn't match", Toast.LENGTH_SHORT).show()
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = {
            PopScreen()
        }) {
            Text("Already have an account? Login")
        }
    }
    when(insertState.value){
        is State.Error -> {
            Toast.makeText(context, "Unable to Signup ", Toast.LENGTH_SHORT).show()
        }
        is State.Loading -> {

        }
        is State.Success -> {
            Toast.makeText(context, "Account Created SuccessFully", Toast.LENGTH_SHORT).show()
        }
    }
}