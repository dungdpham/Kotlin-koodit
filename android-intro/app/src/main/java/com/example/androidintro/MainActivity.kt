package com.example.androidintro

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidintro.ui.theme.AndroidIntroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidIntroTheme {
                Column(
                    Modifier.fillMaxSize().padding(top = 32.dp)
                ) {
                    Greeting(
                        //greetingViewModel = viewModel(),
                        name = "Android"
                    )

                    Another()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, greetingViewModel: GreetingViewModel = viewModel()) {
    Log.d("XXX", "Greeting()")
    val nShow = greetingViewModel.n.collectAsState()

    Column {
        Text(text = "Hello $name!")

        Button(greetingViewModel::inc) {
            Text("Hit me!")
        }

        Text("GreetingViewModel counts ${nShow.value}")
    }
}

@Composable
fun Another() {
    Log.d("XXX", "Another()")

    val gvm: GreetingViewModel = viewModel()

    Column {
        Text("Hi!")

        Button(gvm::bigInc) {
            Text("Hit me too!")
        }
    }
}

@Composable
fun NumberLab(modifier: Modifier = Modifier) {
    var numbers by rememberSaveable { mutableStateOf((0..99).toList()) }
    var clicked by rememberSaveable { mutableStateOf(listOf<Int>()) }
    var checkClickStatus by rememberSaveable { mutableStateOf(false) }
    var result by rememberSaveable { mutableStateOf<List<Int>?>(null) }

    val secret = (1..100).toList().shuffled().take(7)

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Clicked: $clicked")

        Button(
            onClick = {
                checkClickStatus = true
                result = secret.intersect(clicked.toSet()).toList()
            },
            enabled = clicked.size == 7,
            colors = ButtonColors(
                containerColor = Color.Red,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
        ) {
            Text("Check!")
        }

        result?.let {
            Text("${it.size} correct: $it")
        }

//        result?.apply {
//            Text("${this.size} correct: $this")
//        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(64.dp)
        ) {
            items(numbers) { n ->
                Log.d("XXX", "item $n")

                Button(
                    {
                        Log.d("XXX", "$n clicked")
                        clicked = clicked.plus(n)
                        numbers = numbers.minus(n)
                        Log.d("XXY", clicked.toString())
                        Log.d("XXY", numbers.toString())
                    },
                    modifier = Modifier.padding(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (n % 2 == 0) Color.Gray else Color.Blue)
                ) {
                    Text("$n")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidIntroTheme {
        Greeting("Android")
    }
}
