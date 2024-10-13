package com.example.labfinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.labfinal.ui.viewmodels.PartiesViewModel
import com.example.labfinal.ui.viewmodels.PartiesViewModelFactory

// Dung Pham 2217963 12.10.24
// Composable that shows the list of parties from the database, retrieved through the PartiesViewModel
@Composable
fun PartiesView(navController: NavHostController) {
    val database = MPDatabase.getDatabase()
    val repository = MPRepository(database.mpDataDao(), database.mpDataExtraDao(), database.mpGradingDao())
    val viewModel: PartiesViewModel = viewModel(factory = PartiesViewModelFactory(repository))

    val parties by viewModel.parties.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "List of Parties",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(parties) { party ->
                Text(
                    text = party,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .background(Color.LightGray.copy(alpha = 0.1f))
                        .clickable {
                            navController.navigate("partymembers_screen/$party")
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}