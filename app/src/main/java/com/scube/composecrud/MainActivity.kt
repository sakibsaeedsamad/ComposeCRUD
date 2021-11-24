package com.scube.composecrud

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scube.composecrud.ui.theme.ComposeCRUDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialUIApp()
            }
        }
    }


@Composable
fun MaterialUIApp(){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Compose CRUD App")
                },
                actions = {
                    IconButton(
                        onClick = {
                            Log.d(
                                "ButtonClicked",
                                "Setting Button Clicked")
                        }) {
                        Icon(Icons.Filled.Settings, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Registration()
    }
}

@Composable
fun Registration(){
    var name:String by remember { mutableStateOf("") }
    var email:String by remember { mutableStateOf("") }
    var password:String by remember { mutableStateOf("") }

    LazyColumn(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 20.dp),
        verticalArrangement =  Arrangement.spacedBy(10.dp),
    ) {

        item {
            OutlinedTextField(
                value = name,
                onValueChange = {name = it},
                label = { Text(text = "Name")},
            )
        }

        item {
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = { Text(text = "Email")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }
        item {
            OutlinedTextField(
                value = password,
                onValueChange = {password = it},
                label = { Text(text = "Password")},
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        item {
            Button(
                onClick = { Log.d("SubmitButton", "Name: $name Email: $email Password: $password") },
                contentPadding = PaddingValues(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(contentColor =  Color.White, backgroundColor = Color.DarkGray),
                shape = CircleShape,
            ) {
                Text(text = "Submit")
            }
        }
    }
}