package com.example.lab270924

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab270924.ui.theme.AndroidIntroTheme

enum class Screens {
    Start, A, B
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidIntroTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = Screens.Start.name) {
                        composable(route = Screens.Start.name) {
                            Greeting1(nc = navController, modifier = Modifier.padding(innerPadding))
                        }
                        composable(route = Screens.A.name) {
                            Greeting2(nc = navController, modifier = Modifier.padding(innerPadding))
                        }
                        composable(route = Screens.B.name) {
                            Greeting3(nc = navController, context = this@MainActivity, modifier = Modifier.padding(innerPadding))
                        }
                        composable(route = "yetanother/{hereitis}") {
                            val argument = it.arguments?.getString("hereitis", "mydefault") ?: "oh my"
                            Greeting4(nc = navController, extra = argument, modifier = Modifier.padding(innerPadding))
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting1(nc: NavController, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello Android!",
            modifier = modifier
        )
        Button(onClick = {
            nc.navigate(Screens.A.name)
        }) {
            Text(text = "to iOS view")
        }
        Button(onClick = {
            nc.navigate(Screens.B.name)
        }) {
            Text(text = "to other view")
        }
        Button(onClick = {
            nc.navigate("yetanother/${(1..10).random().toString()}")
        }) {
            Text(text = "to example detail view")
        }
    }
}

@Composable
fun Greeting2(nc: NavController, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello iOS!",
            modifier = modifier
        )
        Button(onClick = {
            nc.navigate(Screens.B.name)
        }) {
            Text(text = "to other view")
        }
        Button(onClick = {
            nc.navigate("yetanother/fromGreeting2")
        }) {
            Text(text = "to example detail view")
        }
    }
}

@Composable
fun Greeting3(nc: NavController, context: Context, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello other!",
            modifier = modifier
        )
        Button(onClick = {
            nc.popBackStack()
        }) {
            Text(text = "back")
        }
        Button(onClick = {
            nc.navigate(Screens.Start.name)
        }) {
            Text(text = "to start view")
        }
        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://metropolia.fi"))
            context.startActivity(intent)
        }) {
            Text(text = "launch")
        }
    }
}

@Composable
fun Greeting4(nc: NavController, extra: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello other! $extra",
            modifier = modifier
        )
        Button(onClick = {
            nc.popBackStack()
        }) {
            Text(text = "back")
        }
    }
}


