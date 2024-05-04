package com.example.prison_app.ui.stuff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prison_app.R

private fun checkIncreaseNumber(num : Int, maxValue : Int, minValue : Int, addingValue : Int) : Int{
    return if(num + addingValue > maxValue) minValue else num + addingValue
}
private fun checkDecreaseNumber(num : Int, maxValue : Int, minValue : Int, reducingVal : Int) : Int{
    return if(num - reducingVal < minValue) maxValue else num - reducingVal
}
private fun checkIncreaseNumber(num : Double, maxValue : Double, minValue : Double, addingValue : Double) : Double {
    return if (num + addingValue > maxValue) minValue else num + addingValue
}
private fun checkDecreaseNumber(num : Double, maxValue : Double, minValue : Double, reducingVal : Double) : Double {
    return if (num - reducingVal < minValue) maxValue else num - reducingVal
}

@Composable
fun  IntNumberPicker(
    modifier : Modifier = Modifier,
    maxVal : Int,
    minVal : Int,
    defaultVal : Int,
    addingVal : Int,
    reducingVal : Int,
    onNumberChanged : (number : Int) -> Unit
) {
    var selectedNumber by rememberSaveable {
        mutableStateOf(defaultVal)
    }
    var stringNum by rememberSaveable {
        mutableStateOf(defaultVal.toString())
    }
    var width = (selectedNumber.toString().length * 20).dp

    Row(modifier = modifier) {
        Text(modifier = Modifier
            .align(Alignment.CenterVertically)
            .width(width),
            text = selectedNumber.toString(),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center)

        Column(
            modifier = Modifier
                .wrapContentSize(),
        ) {
            IconButton(modifier = Modifier
                .size(28.dp),
                onClick = {
                    selectedNumber = checkIncreaseNumber(selectedNumber, maxVal, minVal, addingVal)
                        .also(onNumberChanged)
                        .also{ stringNum = it.toString() }
                }) {
                Icon(painter = painterResource(R.drawable.increase_button_ic),
                    contentDescription = stringResource(id = R.string.increase_button_desc, addingVal))
            }
            IconButton(modifier = Modifier
                .size(28.dp),
                onClick = {
                    selectedNumber = checkDecreaseNumber(selectedNumber, maxVal, minVal, reducingVal)
                        .also(onNumberChanged)
                        .also{ stringNum = it.toString() }
                }) {
                Icon(painter = painterResource(R.drawable.decrease_button_ic,),
                    contentDescription = stringResource(id = R.string.decrease_button_desc, reducingVal))
            }

        }
    }
}

@Composable
fun  DoubleNumberPicker(
    modifier : Modifier = Modifier,
    maxVal : Double,
    minVal : Double,
    defaultVal : Double,
    addingVal : Double,
    extraAddingVal : Double,
    reducingVal : Double,
    extraReducingVal : Double,
    onNumberChanged : (number : Double) -> Unit
) {
    var selectedNumber by rememberSaveable {
        mutableStateOf(defaultVal)
    }
    var stringNum by rememberSaveable {
        mutableStateOf(defaultVal.toString())
    }
    val width = (selectedNumber.toString().length * 20).dp

    Row(modifier = modifier) {
        Text(modifier = Modifier
            .align(Alignment.CenterVertically)
            .width(width),
            text = selectedNumber.toString(),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center)

        Column(
            modifier = Modifier
                .wrapContentSize(),
        ) {
            IconButton(modifier = Modifier
                .size(28.dp),
                onClick = {
                    selectedNumber = checkIncreaseNumber(selectedNumber, maxVal, minVal, addingVal)
                        .also(onNumberChanged)
                        .also{ stringNum = it.toString() }
                }) {
                Icon(painter = painterResource(R.drawable.increase_button_ic),
                    contentDescription = stringResource(id = R.string.increase_button_desc, addingVal.toInt()))
            }
            IconButton(modifier = Modifier
                .size(28.dp),
                onClick = {
                    selectedNumber = checkDecreaseNumber(selectedNumber, maxVal, minVal, reducingVal)
                        .also(onNumberChanged)
                        .also{ stringNum = it.toString() }
                }) {
                Icon(painter = painterResource(R.drawable.decrease_button_ic,),
                    contentDescription = stringResource(id = R.string.decrease_button_desc, reducingVal.toInt()))
            }

        }
        Column(
            modifier = Modifier
                .wrapContentSize(),
        ) {
            IconButton(modifier = Modifier
                .size(28.dp),
                onClick = {
                    selectedNumber = checkIncreaseNumber(selectedNumber, maxVal, minVal, extraAddingVal)
                        .also(onNumberChanged)
                        .also{ stringNum = it.toString() }
                }) {
                Icon(painter = painterResource(R.drawable.extra_increase_ic),
                    contentDescription = stringResource(id = R.string.increase_button_desc, extraAddingVal.toInt()))
            }
            IconButton(modifier = Modifier
                .size(28.dp),
                onClick = {
                    selectedNumber = checkDecreaseNumber(selectedNumber, maxVal, minVal, extraReducingVal)
                        .also(onNumberChanged)
                        .also{ stringNum = it.toString() }
                }) {
                Icon(painter = painterResource(R.drawable.extra_decrease_ic,),
                    contentDescription = stringResource(id = R.string.decrease_button_desc, extraReducingVal.toInt()))
            }

        }
    }
}

@Preview
@Composable
private fun NumberPickerPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        IntNumberPicker(
            maxVal = 0,
            minVal =2,
            addingVal = 1,
            reducingVal = 1,
            onNumberChanged = {},
            defaultVal = 1
        )
    }
}