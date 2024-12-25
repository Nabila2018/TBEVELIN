@file:Suppress("DEPRECATION")

package com.example.evelin.ui.userInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.evelin.R
import com.example.evelin.ViewModelFactory
import com.example.evelin.ui.profile.ProfileViewModel
import com.example.evelin.ui.theme.Blue
import com.example.evelin.ui.theme.Green
import com.example.evelin.ui.theme.LightGreen

@Composable
fun UserInfoScreen(onBackClick: () -> Unit,viewModel: UserInfoViewModel = viewModel(factory = ViewModelFactory.getInstance(
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



    Scaffold(
        backgroundColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            // Header Text
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Informasi\nProfile",
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            // Profile Information Section
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(LightGreen, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    ProfileInfoItem(label = "Full Name", value = name)
                    ProfileInfoItem(label = "Email", value = email, isLink = true)
                    ProfileInfoItem(label = "Nomor Telepon", value = noHp)
                    ProfileInfoItem(label = "Asal Institusi", value = institusi)
                }
            }
        }
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String, isLink: Boolean = false, changeAction: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLink) {
                ClickableText(
                    text = AnnotatedString(value),
                    onClick = {},
                    style = LocalTextStyle.current.copy(
                        color = Blue,
                        fontSize = 14.sp
                    )
                )
            } else {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )
            }
            if (changeAction != null) {
                ClickableText(
                    text = AnnotatedString(changeAction),
                    onClick = {},
                    style = LocalTextStyle.current.copy(
                        color = Green,
                        fontSize = 12.sp
                    )
                )
            }
        }
        Divider(
            color = Color.LightGray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoScreenPreview() {
    UserInfoScreen(onBackClick = {})
}