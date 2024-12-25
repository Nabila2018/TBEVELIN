@file:Suppress("DEPRECATION")

package com.example.evelin.ui.registerEvent

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.evelin.R
import com.example.evelin.ui.theme.Green
import com.example.evelin.ui.theme.LightGreen
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evelin.ViewModelFactory

@Composable
fun RegisterEventScreen(
    onBackClick: () -> Unit = {},
    eventId: String?,
    navController: NavController,
    viewModel: RegisterEventViewModel = viewModel(factory = ViewModelFactory.getInstance(LocalContext.current))
) {
    val context = LocalContext.current
    val userData by viewModel.user.collectAsState()
    val registrationStatus by viewModel.registrationStatus.collectAsState()
    val navigateToHome by viewModel.navigateToHome.observeAsState()

    LaunchedEffect(eventId) {
        viewModel.fetchUser()
    }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var noHp by remember { mutableStateOf("") }
    var institusi by remember { mutableStateOf("") }

    LaunchedEffect(userData) {
        name = userData?.name ?: ""
        email = userData?.email ?: ""
        noHp = userData?.noHp ?: ""
        institusi = userData?.institusi ?: ""
    }

    LaunchedEffect(navigateToHome) {
        if (navigateToHome == true) {
            Toast.makeText(context, "Register event berhasil", Toast.LENGTH_SHORT).show()
            navController.navigate("home")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button (Optional)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Green
                )
            }
        }

        // Register Now Title
        Text(
            text = "Register Now!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Green,
            textAlign = TextAlign.Center
        )
        Text(
            text = "to be a part of the Event.\nFill the information carefully",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        DisplayField(label = "Nama", value = name)
        Spacer(modifier = Modifier.height(8.dp))
        DisplayField(label = "Email*", value = email)
        Spacer(modifier = Modifier.height(8.dp))
        DisplayField(label = "No. Telepon*", value = noHp)
        Spacer(modifier = Modifier.height(8.dp))
        DisplayField(label = "Asal Institusi*", value = institusi)
        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(32.dp))

        // Submit Button
        Button(
            onClick = {
                eventId?.let { id ->
                    viewModel.submitEventRegistration(eventId = id)
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Green),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "SUBMIT",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun DisplayField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(LightGreen, RoundedCornerShape(8.dp))
                .padding(8.dp)
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterEventScreen() {
    val navController = rememberNavController()
    RegisterEventScreen(eventId = "1", navController = navController)
}