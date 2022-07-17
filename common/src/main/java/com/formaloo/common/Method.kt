
package com.formaloo.common

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

fun converDateStrToStr(time: String): String {

    val read = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    read.timeZone = TimeZone.getTimeZone("GMT")

    val writeDay = SimpleDateFormat("EEEE-MMM-dd", Locale.getDefault())
    writeDay.timeZone = TimeZone.getTimeZone("GMT")
//
//    val writeMonth = SimpleDateFormat("MMM", Locale.getDefault())
//    writeMonth.timeZone = TimeZone.getTimeZone("GMT")
//
    val str: String = writeDay.format(read.parse(time)?: Date())
//    val strMonth: String = writeMonth.format(read.parse(time)?: Date())


    return str
}


fun convertTimeStrToDate(time: String): Date? {
    //time example=> 20:20
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.US)
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    val sdf2 = SimpleDateFormat("HH:mm", Locale.US)
    sdf2.timeZone = TimeZone.getTimeZone("GMT")

    return try {
        sdf.parse(time)

    } catch (e: Exception) {
        try {
            sdf2.parse(time)
        } catch (e: Exception) {
            Log.e("TAG", "convertTimeStrToLong: $e")
            Date()
        }

    }

}

fun convertTimeToStr(time: Date): String? {
    //time example=> 20:20
    val sdf = SimpleDateFormat("hh:mm aa", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("GMT");
    return sdf.format(time)

}

