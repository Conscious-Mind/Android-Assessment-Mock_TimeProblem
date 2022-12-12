package com.davidson.mock01

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*


@Composable
fun TimeProblem() {

    val myContext = LocalContext.current

    val myCalendar = Calendar.getInstance()
    val currentHour = myCalendar[Calendar.HOUR_OF_DAY]
    val currentMinute = myCalendar[Calendar.MINUTE]
    val currentYear = myCalendar[Calendar.YEAR]
    val currentMonth = myCalendar[Calendar.MONTH]
    val currentDayOfMonth = myCalendar[Calendar.DAY_OF_MONTH]

    val selectedTimeHour = remember { mutableStateOf("") }
    val selectedTimeMinute = remember { mutableStateOf("") }
    val selectedTimeAmOrPm = remember { mutableStateOf("") }

    val selectedYear = remember { mutableStateOf("") }
    val selectedMonth = remember { mutableStateOf("") }
    val selectedDay = remember { mutableStateOf("") }


    val timePickerDialog = TimePickerDialog(
        myContext,
        { tp, selectedHour: Int, selectedMinute: Int ->
            selectedTimeHour.value = "$selectedHour"
            selectedTimeMinute.value = "$selectedMinute"
            selectedTimeAmOrPm.value = getAMorPm(tp)
        }, currentHour, currentMinute, false
    )

    val datePickerDialog = DatePickerDialog(
        myContext,
        { dp, _, _, _ ->
            selectedYear.value = "${dp.year}"
            selectedMonth.value = "${dp.month}"
            selectedDay.value = "${dp.dayOfMonth}"
        }, currentYear, currentMonth, currentDayOfMonth
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { timePickerDialog.show() },
            ) {
                Text(text = "Open Time Picker", color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { datePickerDialog.show() },
            ) {
                Text(text = "Open Date Picker", color = Color.White)
            }
        }



        Text(
            text = "Selected Time:    ${
                getSelectedTime(
                    selectedTimeHour.value,
                    selectedTimeMinute.value,
                    selectedTimeAmOrPm.value
                )
            }", fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp)
        )


        Text(
            text = "Military Time:    ${
                getMilitaryTime(
                    selectedTimeHour.value,
                    selectedTimeMinute.value
                )
            }", fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp)
        )


        Text(
            text = "Relative Time:    ${
                getRelativeTime(
                    selectedTimeHour.value,
                    selectedTimeMinute.value,
                    selectedYear.value,
                    selectedMonth.value,
                    selectedDay.value
                )
            }", fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp)
        )
    }
}

private fun getAMorPm(timePicker: TimePicker): String {
    return if (timePicker.hour < 12) "AM" else "PM"
}

private fun getSelectedTime(
    selectedHour: String,
    selectedMinute: String,
    selectedAmOrPm: String
): String {
    if (selectedHour == "" || selectedMinute == "" || selectedAmOrPm == "")
        return ""
    if (selectedAmOrPm == "AM")
        return "${if(selectedHour.toInt() == 0) "12" else selectedHour} : $selectedMinute $selectedAmOrPm"
    if (selectedAmOrPm == "PM")
        return "${if(selectedHour.toInt() > 12) (selectedHour.toInt() - 12) else selectedHour} : $selectedMinute $selectedAmOrPm"
    return ""
}


private fun getMilitaryTime(
    selectedHour: String,
    selectedMinute: String,
): String {
    if (selectedHour == "" || selectedMinute == "")
        return ""
    return "$selectedHour : $selectedMinute Hours"
}

private fun getRelativeTime(
    selectedHour: String,
    selectedMinute: String,
    selectedYear: String,
    selectedMonth: String,
    selectedDay: String
): String {
    if (selectedHour == "" || selectedMinute == "" || selectedDay == "" ||
        selectedMonth == "" || selectedYear == ""
    )
        return ""
    val timeSelectedInMillis = Calendar.getInstance().also {
        it.set(
            selectedYear.toInt(),
            selectedMonth.toInt(),
            selectedDay.toInt(),
            selectedHour.toInt(),
            selectedMinute.toInt()
        )
    }.timeInMillis

    val currentTimeInMillis = Calendar.getInstance().timeInMillis

    Log.d("Tired", "getDate: $timeSelectedInMillis")


    val differenceTimeInMillis = currentTimeInMillis - timeSelectedInMillis

    if (differenceTimeInMillis < 0)
        return "Invalid Input"

    val differenceTimeInSeconds = differenceTimeInMillis / 1000

    if (differenceTimeInSeconds < 60)
        return "Just Now"

    val differenceTimeInMinutes = differenceTimeInSeconds / 60

    if (differenceTimeInMinutes < 60)
        return "$differenceTimeInMinutes ${if (differenceTimeInMinutes == 1L) "Minute" else "Minutes"} Ago"

    val differenceTimeInHours = differenceTimeInMinutes / 60

    if (differenceTimeInHours < 24)
        return "$differenceTimeInHours ${if (differenceTimeInHours == 1L) "Hour" else "Hours"} Ago"

    val differenceTimeInDays = differenceTimeInHours / 24

    if (differenceTimeInDays < 7)
        return "$differenceTimeInDays ${if (differenceTimeInDays == 1L) "Day" else "Days"} Ago"

    val differenceTimeInWeeks = differenceTimeInDays / 7

    if (differenceTimeInWeeks < 5)
        return "$differenceTimeInWeeks ${if (differenceTimeInWeeks == 1L) "Week" else "Weeks"} Ago"

    val differenceTimeInMonths = differenceTimeInDays / 30

    if (differenceTimeInMonths < 12)
        return "$differenceTimeInMonths ${if (differenceTimeInMonths == 1L) "Month" else "Months"} Ago"

    val differenceTimeInYears = differenceTimeInDays / 365

    if (differenceTimeInYears < 15)
        return "$differenceTimeInYears ${if (differenceTimeInYears == 1L) "Year" else "Years"} Ago"
    return "Long Time Ago"
}

private fun getSelectedDate(
    selectedYear: String,
    selectedMonth: String,
    selectedDay: String
): String {
    return "$selectedDay $selectedMonth $selectedYear"
}

// For displaying preview in
// the Android Studio IDE emulator
@Preview(showBackground = true)
@Composable
fun DefaultPreviewMock() {
    TimeProblem()
}
