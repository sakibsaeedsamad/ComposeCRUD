package com.scube.composecrud

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModelProvider
import com.example.registrationservlet.viewmodel.UserViewModel
import com.scube.composecrud.model.Gender
import com.scube.composecrud.model.InsertModel
import com.scube.composecrud.model.Role
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    var roleList = ArrayList<Any>()
    var genderList = ArrayList<Any>()

    var roleDropdown: ArrayList<Role> = ArrayList<Role>()
    var roleDesc: ArrayList<String> = ArrayList<String>()

    var gendDesc: ArrayList<String> = ArrayList<String>()

    var roleCode: String = ""

    var genderString: String = ""

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        getRoleData()
        getGenderData()
        observeViewModel()

        setContent {

            MaterialUIApp(this)
            //showDatePicker(this)

        }
    }

    private fun observeViewModel() {
        userViewModel.userInsert_response_error.observe(this, androidx.lifecycle.Observer {
            it?.let {


                Log.e("Insert-->", "Error Found")

            }
        })

        userViewModel.userInsert_response.observe(this, androidx.lifecycle.Observer {
            it?.let {

                if ("E".equals(it.errorCode)) {

                    Log.d("MessageD ", it.errorMessage.toString())
                    Toast.makeText(
                        this,
                        "Message : " + it.errorMessage.toString(),
                        Toast.LENGTH_LONG
                    ).show()


                }
                else{
                   // cleanFields()
                    Toast.makeText(
                        this,
                        "User Inserted Successfully. " ,
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
        })

        userViewModel.roleGet_response_error.observe(this, androidx.lifecycle.Observer {
            it?.let {


                Log.e("RoleList-->", "Error Found")

            }
        })

        userViewModel.roleGet_response.observe(this, androidx.lifecycle.Observer {
            it?.let {


                if (it.size >= 1) {

                    for (i in it.indices) {

                        val code = it[i].code
                        val desc = it[i].desc
                        roleList.add(code!!)
                        roleList.add(desc!!)

                    }

                    //addDropDownList()

                } else {
                    Log.e("RoleList-->", "Error Found")
                }


            }
        })


        userViewModel.genderGet_response_error.observe(this, androidx.lifecycle.Observer {
            it?.let {


                Log.e("Gender-->", "Error Found")

            }
        })

        userViewModel.genderGet_response.observe(this, androidx.lifecycle.Observer {
            it?.let {


                if (it.size >= 1) {

                    for (i in it.indices) {

                        val genCode = it[i].genCode
                        val genDesc = it[i].genDesc
                        genderList.add(genCode!!)
                        genderList.add(genDesc!!)

                    }

                    getGenderList()


                } else {
                    Log.e("Gender-->", "Error Found")
                }


            }
        })


    }

    private fun getGenderData() {
        var model = InsertModel()

        model.requestCode = ("3")

        this.let { it1 ->
            userViewModel.doGetGenderList(model)
        }
    }

    private fun getRoleData() {
        var model = InsertModel()

        model.requestCode = ("2")

        this.let { it1 -> userViewModel.doGetAllRoleList(model) }
    }

    fun getGenderList() {


        if (genderList != null) {
            roleDropdown.clear()
            val i: Iterator<Any> = genderList.iterator()
            while (i.hasNext()) {
                val roleModel = Gender(i.next().toString(), i.next().toString())
                gendDesc.add(roleModel.genDesc.toString())

            }
            //addRadioButtons(gendDesc.size)

        }
    }
}


@Composable
fun MaterialUIApp(context: Context) {
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
                                "Setting Button Clicked"
                            )
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
fun Registration(context: Context) {
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val date = remember { mutableStateOf("") }
    var name: String by remember { mutableStateOf("") }
    var mobile: String by remember { mutableStateOf("") }
    var email: String by remember { mutableStateOf("") }
    var address: String by remember { mutableStateOf("") }
    var dobVal: String by remember { mutableStateOf("") }

    var selected by remember {
        mutableStateOf("Male")
    }

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Item1","Item2","Item3")
    var selectedText by remember { mutableStateOf("") }

    var textfieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown



    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth/${month + 1}/$year"
        }, year, month, day
    )

    LazyColumn(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {

        item {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Name") },
                placeholder = { Text(text = "Enter your name") },
            )
        }
        item {
            OutlinedTextField(
                value = mobile,
                onValueChange = { mobile = it },
                label = { Text(text = "Mobile No") },
                placeholder = { Text(text = "Enter your mobile no") },
            )
        }

        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                placeholder = { Text(text = "Enter your email") },
            )
        }


        item {
            dobVal = date.value;

            OutlinedTextField(
                value = dobVal,
                onValueChange = { dobVal = it },
                readOnly = true,
                singleLine = true,
                label = { Text(text = "Date of Birth") },
                trailingIcon = {
                    IconButton(onClick = {
                        datePickerDialog.show()

                    }) {
                        Icon(
                            Icons.Filled.DateRange,
                            "contentDescription",
                            tint = Color.Blue
                        )

                    }
                },
            )


        }
        item {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Gender:")

                //defining radio buttons
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {

                    RadioButton(selected = selected == "Male", onClick = { selected = "Male" })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Male",
                        modifier = Modifier.clickable(onClick = { selected = "Male" })
                    )


                    Spacer(modifier = Modifier.width(24.dp))

                    RadioButton(selected = selected == "Female", onClick = { selected = "Female" })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Female",
                        modifier = Modifier.clickable(onClick = { selected = "Female" })
                    )
                }

            }


        }
        item {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(text = "Address") },
                visualTransformation = PasswordVisualTransformation(),

                placeholder = { Text(text = "Enter your address") },
            )
        }

        item {
            OutlinedTextField(
                readOnly=true,
                value = selectedText,
                onValueChange = { selectedText = it },
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textfieldSize = coordinates.size.toSize()
                    },
                label = { Text("Role") },
                trailingIcon = {
                    Icon(icon, "contentDescription",
                        Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textfieldSize.width.toDp() }
                    )
            ) {
                suggestions.forEach { label ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        selectedText = label
                    }) {
                        Text(text = label)
                    }
                }
            }
        }
        item{
            CountrySelection()
        }



        item {
            Row {
                Button(
                    onClick = {
                        Log.d(
                            "SubmitButton",
                            "Name: $name Email: $email Address: $address"
                        )
                    },
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 10.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Color.DarkGray
                    ),
                    shape = CircleShape,
                ) {
                    Text(text = "Register")
                }

                Button(
                    onClick = {
                        Log.d(
                            "SubmitButton",
                            "Name: $name Email: $email Address: $address"
                        )
                    },
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 10.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Color.DarkGray
                    ),
                    shape = CircleShape,
                ) {
                    Text(text = "Show User")
                }
            }
        }
    }


}
/////////////////////////////////////////
@Composable
fun DropDownList(
    requestToOpen: Boolean = false,
    list: List<String>,
    request: (Boolean) -> Unit,
    selectedString: (String) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(),
//        toggle = {
//            // Implement your toggle
//        },
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        list.forEach {
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    selectedString(it)
                }
            ) {
                Text(it, modifier = Modifier.wrapContentWidth().align(Alignment.CenterVertically))
            }
        }
    }
}
@Composable
fun CountrySelection() {
    val countryList = listOf(
        "United state",
        "Australia",
        "Japan",
        "India",
    )
    val text = remember { mutableStateOf("") } // initial value
    val isOpen = remember { mutableStateOf(false) } // initial value
    val openCloseOfDropDownList: (Boolean) -> Unit = {
        isOpen.value = it
    }
    val userSelectedString: (String) -> Unit = {
        text.value = it
    }
    Box {
        Column {
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text(text = "TextFieldTitle") },
                modifier = Modifier.fillMaxWidth()
            )
            DropDownList(
                requestToOpen = isOpen.value,
                list = countryList,
                openCloseOfDropDownList,
                userSelectedString
            )
        }
        Spacer(
            modifier = Modifier.matchParentSize().background(Color.Transparent).padding(10.dp)
                .clickable(
                    onClick = { isOpen.value = true }
                )
        )
    }
}


