package com.example.evelin.ui.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evelin.ui.component.bar.BottomNavBar
import com.example.evelin.R
import com.example.evelin.ViewModelFactory
import com.example.evelin.ui.addEvent.AddEventViewModel
import com.example.evelin.ui.theme.Green
import com.example.evelin.ui.theme.LightGreen
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController,
                  viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory.getInstance(
                      LocalContext.current))
) {
    val userData by viewModel.user.collectAsState()


    viewModel.fetchUser()


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


    val coroutineScope = rememberCoroutineScope()
    Log.d("ProfileScreen", "Entering ProfileScreen")
    Scaffold(
        bottomBar = {
            Log.d("ProfileScreen", "Setting up BottomNavBar")
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFDFF3DC))
                    .padding(top = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.file), // Gambar profil
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                //name
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                //email
                Text(
                    text = "{$email} | ${noHp}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button Section
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { navController.navigate("userInfo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Informasi Profil", color = Color.White)
                }
                Button(
                    onClick = { navController.navigate("eventHistory") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Riwayat Kegiatan", color = Color.White)
                }
                Button(
                    onClick = { navController.navigate("myEvent") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Kegiatan yang Diselenggarakan", color = Color.White)
                }
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.logout()
                        }
                    },

                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = LightGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Logout", color = Green)
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileScreen(navController = navController)
}