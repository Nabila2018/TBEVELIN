package com.example.evelin.ui.component.bar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evelin.R
import com.example.evelin.ui.theme.BackGround
import com.example.evelin.ui.theme.Green

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Log.d("BottomNavBar", "Rendering BottomNavBar with NavController: $navController")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp)
            .background(BackGround),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                tint = Green,
            )
        }


        IconButton(
            onClick = { navController.navigate("addEvent") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_24),
                contentDescription = "Add_Event",
                tint = Green
            )
        }

        IconButton(
            onClick = {
                Log.d("BottomNavBar", "Profile button clicked")
                try {
                    navController.navigate("profile")
                } catch (e: Exception) {
                    Log.e("BottomNavBar", "Error navigating to profile", e)
                    throw e
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_user),
                contentDescription = "Profile",
                tint = Green
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val navController = rememberNavController()
    BottomNavBar(navController = navController)
}
