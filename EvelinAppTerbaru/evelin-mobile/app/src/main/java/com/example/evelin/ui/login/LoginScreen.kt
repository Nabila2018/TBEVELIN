package com.example.evelin.ui.login

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evelin.R
import com.example.evelin.ViewModelFactory
import com.example.evelin.data.pref.UserModel
import com.example.evelin.ui.theme.Green
import com.example.evelin.ui.theme.LightGreen

@Composable
fun LoginScreen(context: Context, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginViewModel: LoginViewModel = viewModel(factory = ViewModelFactory.getInstance(context))

    var loginResult by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.evelin_logo), // Replace with Logo
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sign In Title
        Text(
            text = "Sign In",
            fontSize = 28.sp,
            color = Green, // Green Color
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(painterResource(id = R.drawable.ic_email), contentDescription = "Email") }, // Replace
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Your password") },
            leadingIcon = { Icon(painterResource(id = R.drawable.ic_lock), contentDescription = "Lock") }, // Replace
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Text(
            text = "Forgot Password?",
            color = Green, // Green
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.End)
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clickable { /* Navigate to Forgot Password */ }
        )

        Spacer(modifier = Modifier.height(12.dp))

        val loginState by loginViewModel.loginResult.observeAsState()

        // Sign In Button
        Button(
            onClick = {
                loginViewModel.login(email, password)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green), // Green Button
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Sign In", color = Color.White, fontSize = 16.sp)
        }

        LaunchedEffect(loginState) {
            loginState?.let { result ->
                result.onSuccess { response ->
                    // Simpan session pengguna
                    loginViewModel.saveSession(
                        UserModel(
                            token = response.dataUser.token,
                            isLogin = true
                        )
                    )

                    // Navigasi ke halaman home
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }.onFailure {
                    // Tangani kesalahan login (misalnya, tampilkan Snackbar)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // OR Divider
        Text("OR", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(12.dp))

        // Unand Sign In
        Button(
            onClick = { /* Handle Google Sign In */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen), // Light Green
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.unand_logo), // Replace with Google Icon
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Universitas Andalas Email", color = Green, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // google sign in
        Button(
            onClick = { /* Handle Google Sign In */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen), // Light Green
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google), // Replace with Google Icon
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sign In with Google", color = Green, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign In Text
        Row {
            Text(text = "Don't have an account?", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Don't have an account? Sign Up",
                color = Green,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }

        // Display login result
        loginResult?.let {
            Text(
                text = if (it) "Login successful" else "Login failed",
                color = if (it) Color.Green else Color.Red,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(context = LocalContext.current, navController = navController)
}