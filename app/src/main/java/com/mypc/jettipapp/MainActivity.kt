package com.mypc.jettipapp

import android.content.res.Resources
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mypc.jettipapp.components.InputField
import com.mypc.jettipapp.ui.theme.JetTipAppTheme
import com.mypc.jettipapp.utils.calcTotalPerPerson
import com.mypc.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}



@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        MyApp {
           MainContent()
        }
    }
}

@Composable
fun MyApp(content:@Composable () -> Unit) {
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colors.background
        ) {
            content()

        }
    }


}
//@Preview
@Composable
fun TopHeader(totalPerPerson:Double = 134.0){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(15.dp)
        //.clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))))
        .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
    color = Color(color = 0xFFE9D7f7)
    )
        {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(text = "Total Per Person",
            style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold

            )
            val total  = "%.2f".format(totalPerPerson)
            Text(text = "$$total",
            style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold)
        }
    }
}
@ExperimentalComposeUiApi
@Preview
@Composable
fun MainContent(){



    BillForm(){ billAmt ->
        Log.d("AMT","MAinContent:$billAmt")


    }

}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier = Modifier,
            onValChanged:(String) -> Unit = {}
             ){


    val totalBillState = remember{
        mutableStateOf(value = "")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val totalSplit = remember {
        mutableStateOf(value = "")
    }

    val keyBoardController =  LocalSoftwareKeyboardController.current

    val sliderPosition = remember {
        mutableStateOf(value = 0f)
    }

    val totalAmountPerPerson = remember {
        mutableStateOf(value = 0.0)
    }

    TopHeader(totalAmountPerPerson.value)
    Surface(modifier = Modifier
        .padding(2.dp)
        .padding(top = 150.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp,color = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(all = 6.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start){
            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions{
                    if(!validState)return@KeyboardActions
                    onValChanged(totalBillState.value.trim())
                    val totalText = calcTotalPerPerson(
                        bill = totalBillState.value.toFloat(),
                        split = totalSplit.value.toInt(),
                        tip = sliderPosition.value*100)
                    totalAmountPerPerson.value = totalText
                    keyBoardController?.hide()
                })

            if(validState){
                Row(modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start) {
                    Text(text = "Split",
                    modifier = Modifier.align(
                        alignment = Alignment.CenterVertically
                    ))
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End) {
                        RoundIconButton(//minus
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                val currentV = Integer.valueOf(totalSplit.value) - 1
                                if(currentV >= 1) {
                                    totalSplit.value = currentV.toString()
                                    val totalText = calcTotalPerPerson(
                                        bill = totalBillState.value.toFloat(),
                                        split = totalSplit.value.toInt(),
                                        tip = sliderPosition.value*100)
                                    totalAmountPerPerson.value = totalText
                                }
                            })

                        }

                    Text(modifier = Modifier
                        .padding(start = 9.dp, end = 9.dp)
                        .align(Alignment.CenterVertically)
                        ,text = totalSplit.value.trim())
                    RoundIconButton(//plus
                        imageVector = Icons.Default.Add,
                        onClick = {
                            val currentV = Integer.valueOf(totalSplit.value) + 1
                            totalSplit.value = currentV.toString()
                            val totalText = calcTotalPerPerson(
                                bill = totalBillState.value.toFloat(),
                                split = totalSplit.value.toInt(),
                                tip = sliderPosition.value*100)
                            totalAmountPerPerson.value = totalText
                        })

                    }

                Row(modifier = Modifier.padding(horizontal = 3.dp,vertical = 12.dp)) {
                    Text(text = "Tip",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(200.dp))
                    val tempTip = sliderPosition.value * totalBillState.value.toDouble()

                    Text(text = "%.2f".format(tempTip))
                }

                Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                    var finalTextSlider = (sliderPosition.value*100).toInt().toString() + " %"
                    if(sliderPosition.value*100 > 99.5)
                        finalTextSlider = "100"
                    Log.d("SliderVal","${sliderPosition.value*100}" )
                    Text(text = finalTextSlider)
                    Spacer(modifier = Modifier.height(14.dp))

                    //slider
                    Slider(value = sliderPosition.value,
                        onValueChange = { newVal ->
                            sliderPosition.value = newVal
                            val totalText = calcTotalPerPerson(
                                bill = totalBillState.value.toFloat(),
                                split = totalSplit.value.toInt(),
                                tip = newVal*100)
                            totalAmountPerPerson.value = totalText

                        },
                    modifier = Modifier.padding(start = 16.dp,
                                                end = 16.dp),
                    steps = 5)

                }


                    if(totalSplit.value.trim().isEmpty()){
                        totalSplit.value = "1"
                    }

                }


       else{
                Box(){}

               }
        }
    }


}
