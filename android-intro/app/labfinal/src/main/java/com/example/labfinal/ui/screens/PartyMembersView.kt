package com.example.labfinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.labfinal.data.database.MPDatabase
import com.example.labfinal.data.repository.MPRepository
import com.example.labfinal.ui.viewmodels.PartyMembersViewModel
import com.example.labfinal.ui.viewmodels.PartyMembersViewModelFactory

// Dung Pham 2217963 12.10.24
// Composable that shows the list of members of the "party" provided in the function's parameter,
// retrieved through the PartyMembersViewModel
@Composable
fun PartyMembersView(
    navController: NavHostController,
    party: String
) {
    val database = MPDatabase.getDatabase()
    val repository = MPRepository(database.mpDataDao(), database.mpDataExtraDao(), database.mpGradingDao())
    val viewModel: PartyMembersViewModel = viewModel(factory = PartyMembersViewModelFactory(repository, party))

    val partyMPs by viewModel.partyMPs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // Back Icon and Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "$party party members",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(partyMPs) { member ->
                Text(
                    text = member.firstname + " " + member.lastname,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .background(Color.LightGray.copy(alpha = 0.1f))
                        .clickable {
                            navController.navigate("mp_screen/${member.hetekaId}")
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}