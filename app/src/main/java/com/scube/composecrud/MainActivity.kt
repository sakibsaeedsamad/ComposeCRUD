package com.scube.composecrud

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.scube.composecrud.ui.theme.ComposeCRUDTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MaterialUIApp(this)
            //showDatePicker(this)
            }
        }
    }


@Composable
fun MaterialUIApp(context: Context){
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
        Registration(context)
    }
}

@Composable
fun Registration(context: Context){
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val date = remember { mutableStateOf("") }
    var name:String by remember { mutableStateOf("") }
    var mobile:String by remember { mutableStateOf("") }
    var email:String by remember { mutableStateOf("") }
    var address:String by remember { mutableStateOf("") }
    var dobVal: String by remember { mutableStateOf("") }

    var selected by remember {
        mutableStateOf("Kotlin")
    }

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Item1","Item2","Item3")
    var selectedText by remember { mutableStateOf("") }

    var dropDownWidth by remember { mutableStateOf(0) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
    Icons.Filled.KeyboardArrowDown



    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth/${month+1}/$year"
        }, year, month, day
    )

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
                placeholder = { Text(text = "Enter your name")},
            )
        }
        item {
            OutlinedTextField(
                value = mobile,
                onValueChange = {mobile = it},
                label = { Text(text = "Mobile No")},
                placeholder = { Text(text = "Enter your mobile no")},
            )
        }

        item {
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = { Text(text = "Email")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        placeholder = { Text(text = "Enter your email")},
            )
        }


        item {
            dobVal= date.value;

            OutlinedTextField(
                value = dobVal,
                onValueChange = { dobVal = it },
                readOnly= true,
                singleLine= true,
                label = { Text(text = "Date of Birth") },
                trailingIcon = {
                    IconButton(onClick = {
                        datePickerDialog.show()

                    }) {
                        Icon(
                            Icons.Filled.DateRange,
                            "contentDescription",
                            tint = Color.Blue)

                    }
                },
            )


        }
        item{

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,){
                Text(text = "Gender:")

                //defining radio buttons
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {

                    RadioButton(selected = selected == "Kotlin", onClick = { selected = "Kotlin" })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Kotlin", modifier = Modifier.clickable(onClick = { selected = "Kotlin" }))


                    Spacer(modifier = Modifier.width(24.dp))

                    RadioButton(selected = selected == "Java", onClick = { selected = "Java" })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Java", modifier = Modifier.clickable(onClick = { selected = "Java" }))
                }

            }


        }
        item {
            OutlinedTextField(
                value = address,
                onValueChange = {address = it},
                label = { Text(text = "Address")},
                visualTransformation = PasswordVisualTransformation(),

                placeholder = { Text(text = "Enter your address")},
            )
        }

        item{

            OutlinedTextField(
                readOnly= true,
                value = selectedText,
                onValueChange = { selectedText = it },
                modifier = Modifier.fillMaxWidth()
                    .onSizeChanged {
                        dropDownWidth = it.width
                    },
                label = {Text("Label")},
                trailingIcon = {
                    Icon(icon,"contentDescription", Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current){dropDownWidth.toDp()})
            ) {
                suggestions.forEach { label ->
                    DropdownMenuItem(onClick = {
                        selectedText = label
                    }) {
                        Text(text = label)
                    }
                }
            }
        }

        item {
            Row{
                Button(
                    onClick = { Log.d("SubmitButton", "Name: $name Email: $email Address: $address") },
                    contentPadding = PaddingValues(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp),
                    colors = ButtonDefaults.buttonColors(contentColor =  Color.White, backgroundColor = Color.DarkGray),
                    shape = CircleShape,
                ) {
                    Text(text = "Register")
                }

                Button(
                    onClick = { Log.d("SubmitButton", "Name: $name Email: $email Address: $address") },
                    contentPadding = PaddingValues(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp),
                    colors = ButtonDefaults.buttonColors(contentColor =  Color.White, backgroundColor = Color.DarkGray),
                    shape = CircleShape,
                ) {
                    Text(text = "Show User")
                }
            }
        }
    }


}


@Composable
fun SimpleRadioButtonComponent() {
    val radioOptions = listOf("Male", "Female", "Common")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[2]) }
    Column(
        // we are using column to align our
        // imageview to center of the screen.
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

        // below line is used for
        // specifying vertical arrangement.
        verticalArrangement = Arrangement.Center,

        // below line is used for
        // specifying horizontal arrangement.
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // we are displaying all our
        // radio buttons in column.
        Row {
            // below line is use to set data to
            // each radio button in colums.
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        // using modifier to add max
                        // width to our radio button.
                        .fillMaxWidth()
                        // below method is use to add
                        // selectable to our radio button.
                        .selectable(
                            // this method is called when
                            // radio button is selected.
                            selected = (text == selectedOption),
                            // below method is called on
                            // clicking of radio button.
                            onClick = { onOptionSelected(text) }
                        )
                        // below line is use to add
                        // padding to radio button.
                        .padding(horizontal = 16.dp)
                ) {
                    // below line is use to get context.
                    val context = LocalContext.current

                    // below line is use to
                    // generate radio button
                    RadioButton(
                        // inside this method we are
                        // adding selected with a option.
                        selected = (text == selectedOption),modifier = Modifier.padding(all = Dp(value = 8F)),
                        onClick = {
                            // inide on click method we are setting a
                            // selected option of our radio buttons.
                            onOptionSelected(text)

                            // after clicking a radio button
                            // we are displaying a toast message.
                            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                        }
                    )
                    // below line is use to add
                    // text to our radio buttons.
                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}