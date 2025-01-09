@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.example.jettipjc

import android.graphics.Paint.Align
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettipjc.ui.theme.JetTipJCTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTipJCTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {


                    var totalBillAmount = remember {
                        mutableStateOf("")
                    }

                    var numberOfPeople = remember {
                        mutableStateOf(0)
                    }

                    var tipPercentage = remember {
                        mutableStateOf(0.0)
                    }

                    fun getTotalPerPerson(): String {
                        if (totalBillAmount.value.isEmpty()) {
                            return "0.0"
                        }
                        return ((totalBillAmount.value.toDouble() + ((tipPercentage.value / 100) * totalBillAmount.value.toDouble())) / numberOfPeople.value).toString()
                    }
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TotalPerPersonCard(amount = getTotalPerPerson())
                        SplitAmountCard(
                            totalBillAmount = totalBillAmount.value,
                            numberOfPeople = numberOfPeople.value,
                            tipPercentage = tipPercentage.value.toDouble(),
                            onAmountChange = {
                                totalBillAmount.value = it
                            },
                            onNumberOfPeopleChange = {
                                numberOfPeople.value = it
                            },
                            onTipPercentageChange = {
                                tipPercentage.value = it
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetTipJCTheme {
        Greeting("Android")
    }
}


@Composable
fun TotalPerPersonCard(amount: String) {
    Card(colors  = CardDefaults.cardColors(containerColor = Color(0xFFE9D7F7), contentColor = Color.Black),
        shape = RoundedCornerShape(12.dp)
    ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement  = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Total Per Person",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(
                    "$${amount}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
    }
}


@Composable
fun BillAmountCard(totalBillAmount: String, onAmountChange: (String) -> Unit) {

    var textFieldState = remember {
        mutableStateOf(totalBillAmount)
    }

    // This is similar to Computed Property in SwiftUI the Main Diff is
    // In SwiftUI the computed property will be reevaluated every time it's accessed and it's dependencies have changed whereas,
    // In JC remember ensures that the value is recalculated only when the specified dependencies(textFieldState.value) is changed, and is retained across the recompositions.
    // We can also write something like this
    // val validState = textFieldState.value.isEmpty()  :: But this would recalculate on every recomposition, where as `remember` optimises to avoid unnecessary recalculations.

    var validState = remember(textFieldState.value) {
        textFieldState.value.isNotEmpty()
    }

    var keyBoardController = LocalSoftwareKeyboardController.current

    var focusManager = LocalFocusManager.current



    Column {
        OutlinedTextField(
            value = textFieldState.value,
            onValueChange = {
                textFieldState.value = it
                onAmountChange(it)
            },
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text("Enter Bill")
            },
            placeholder = {
                Text("Please enter the Bill Amount")
            },
            leadingIcon = {
//                Icon(imageVector = Icons.Rounded.AttachMoney, contentDescription = "Money Icon")
                Image(painter = painterResource(id = R.drawable.baseline_attach_money_24), contentDescription = "Dollar Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color(0xFFA020F0),
                textColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = Color(0xFFA020F0)
            ),
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = {
                if (validState) {
                    keyBoardController?.hide()
                    focusManager.clearFocus()
                } else {
                    null
                }
            })
            
        )
    }
}

@Composable
fun SplitCard(
    totalBillAmount: String,
    numberOfPeople: Int, tipPercentage: Double, onNumberOfPeopleChange: (Int) -> Unit, onTipPercentageChange: (Double) -> Unit) {

    var tipPercentage = remember {
        mutableStateOf(tipPercentage)
    }

    var number = remember {
        mutableStateOf(numberOfPeople)
    }

    fun getTipAmount() : Double {
        if (totalBillAmount.isEmpty()) {
            return 0.0
        }
        return (tipPercentage.value / 100.0) * (totalBillAmount.toDouble())
    }


    Column {
        Row() {
            Text(
                modifier = Modifier.weight(weight = 1f),
                text = "Split"
            )
            Row(
                modifier = Modifier.weight(weight = 3f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                IncrementAndDecrementButton(resourceID = R.drawable.baseline_horizontal_rule_24) {
                    number.value = number.value - 1
                    onNumberOfPeopleChange(number.value)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(numberOfPeople.toString())
                Spacer(modifier = Modifier.width(20.dp))

                IncrementAndDecrementButton(resourceID = R.drawable.baseline_add_24) {
                    number.value = number.value + 1
                    onNumberOfPeopleChange(number.value)
                }
            }
        }
        
        
        Row {
            Text(
                modifier = Modifier.weight(weight = 1f),
                text = "Tip")

            Text(
                modifier = Modifier.weight(weight = 3f),
                text = getTipAmount().toString(),
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(10.dp))
        
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(tipPercentage.value.toInt().toString() + "%")
        }

        Slider(
            value = tipPercentage.value.toFloat(),
            onValueChange = {
                tipPercentage.value = it.toDouble()
                onTipPercentageChange(tipPercentage.value)
            },
            steps = 5,
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                inactiveTrackColor = Color(0x69A020F0),
                activeTrackColor = Color(0xFFA020F0),
                thumbColor = Color(0xFFA020F0)
            )
        )

    }
}

@Composable
fun IncrementAndDecrementButton(resourceID: Int, onTap: () -> Unit) {

    Card(
        shape = CircleShape,
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Black)) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onTap()
                },
            contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = resourceID),
                contentDescription = null
            )
        }
    }
}


@Composable
fun SplitAmountCard(
    totalBillAmount: String,
    numberOfPeople: Int,
    tipPercentage: Double,
    onAmountChange: (String) -> Unit,
    onNumberOfPeopleChange: (Int) -> Unit,
    onTipPercentageChange: (Double) -> Unit
) {
    Column(
        modifier = Modifier
            .border(
                color = Color.Gray,
                shape = RoundedCornerShape(corner = CornerSize(size = 12.dp)),
                width = 1.5.dp
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BillAmountCard(totalBillAmount = totalBillAmount, onAmountChange = onAmountChange)
        SplitCard(
            totalBillAmount = totalBillAmount,
            numberOfPeople = numberOfPeople,
            tipPercentage = tipPercentage,
            onNumberOfPeopleChange = onNumberOfPeopleChange,
            onTipPercentageChange = onTipPercentageChange
        )
    }
}