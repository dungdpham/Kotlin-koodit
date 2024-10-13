package com.example.labfinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.labfinal.data.database.MPDatabase
import com.example.labfinal.data.repository.MPRepository
import com.example.labfinal.ui.components.StarRating
import com.example.labfinal.ui.viewmodels.MPInfoViewModel
import com.example.labfinal.ui.viewmodels.MPInfoViewModelFactory

// Dung Pham 2217963 12.10.24
// Composable that shows the detailed information of MP with specified hetekaId passed in the parameter,
// retrieved through the MPInfoViewModel. Data are combined from standard MP data and MP extra data,
// as well as recorded grades and comments
@Composable
fun MPInfoView(
    navController: NavHostController,
    hetekaId: Int
) {
    val database = MPDatabase.getDatabase()
    val repository =
        MPRepository(database.mpDataDao(), database.mpDataExtraDao(), database.mpGradingDao())
    val viewModel: MPInfoViewModel =
        viewModel(factory = MPInfoViewModelFactory(repository, hetekaId))

    val mpInfo by viewModel.mpDetailInfo.collectAsState(null)
    var commentText by remember { mutableStateOf("") }
    var grade by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Back Icon and Title
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                Text(
                    text = "MP information",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        mpInfo?.let { detailInfo ->
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // load and display MP's image using Coil
                        Image(
                            painter = rememberAsyncImagePainter(
                                "https://avoindata.eduskunta.fi/${detailInfo.mpData.pictureUrl}"
                            ),
                            contentDescription = "MP Image",
                            modifier = Modifier
                                .size(256.dp)
                                .padding(vertical = 16.dp)
                        )

                        Text(
                            text = "${detailInfo.mpData.firstname} ${detailInfo.mpData.lastname}",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                Text(
                    text = "Heteka ID: ${detailInfo.mpData.hetekaId}",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "Seat Number: ${detailInfo.mpData.seatNumber ?: "N/A"}",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "Party: ${detailInfo.mpData.party ?: "N/A"}",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "Minister: ${if (detailInfo.mpData.minister == true) "Yes" else "No"}",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // show additional MP information from MPDataExtra if available
                detailInfo.mpDataExtra?.let { extra ->
                    Text(
                        text = "Constituency: ${extra.constituency}",
                        style = TextStyle(
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    extra.twitter?.let {
                        Text(
                            text = "Twitter: $it",
                            style = TextStyle(
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    extra.bornYear?.let {
                        Text(
                            text = "Born Year: $it",
                            style = TextStyle(
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = String.format("Average grade: %.1f", detailInfo.averageGrade),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Input for grade and comment
            item {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text("Add a comment") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                // Use StarRating component for grade input
                StarRating(rating = grade) { newRating ->
                    grade = newRating
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.addGrading(grade, commentText)
                        commentText = ""
                        grade = 0
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Submit")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Comments:",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                detailInfo.gradings.forEach { grading ->
                    Text(
                        text = "(Grade: ${grading.grade}) ${grading.comment}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .background(Color.LightGray.copy(alpha = 0.1f))
                            .padding(16.dp)
                    )
                }
            }
        } ?: run {
            // handle loading state or empty state if mpDetailInfo is null
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}