package com.example.lab06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lab06.ui.theme.AndroidIntroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidIntroTheme {
                MPView()
            }
        }
    }
}

@Composable
fun MPView(modifier: Modifier = Modifier) {
    var mp: ParliamentMember by remember { mutableStateOf(getRandomMP()) }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "https://dummyimage.com/300x400/208ac7/ffffff.jpg&text=Some+Image",
            contentDescription = "${mp.firstname}'s photo",
            modifier = Modifier.size(300.dp)
        )
        MPInfo(mp, modifier = modifier.padding(16.dp))
        Button(onClick = { mp = getRandomMP() }) { Text("Another MP") }
    }
}

@Composable
fun MPInfo(mp: ParliamentMember, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (mp.minister) Text("Minister", modifier = Modifier.align(Alignment.CenterHorizontally))
        Text("Name: ${mp.firstname} ${mp.lastname}")
        Text("MP Id: ${mp.hetekaId}")
        Text("Seat no.: ${mp.seatNumber}")
        Text("Party: ${mp.party}")
    }
}

fun getRandomMP(): ParliamentMember = ParliamentMembersData.members.random()

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidIntroTheme {
        MPView()
    }
}