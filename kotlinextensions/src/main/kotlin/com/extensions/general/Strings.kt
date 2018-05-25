package com.extensions.general

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.webkit.URLUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Pattern

fun String?.isUrl() :Boolean {
    return this.isNotNull() && URLUtil.isValidUrl(this)
}

fun random(length :Int = 8) :String {
    val base = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()-_=+"
    var randomString :String = ""

    for(i in 1..length) {
        val randomValue = (0..base.count()).random()
        randomString += "${base[randomValue]}"
    }
    return randomString
}

fun String.toBitmap() :Bitmap {
    val decoded = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decoded, 0, decoded.count())
}

fun String?.ellipsize(at :Int) :String {
    return if(this.isNotNull() && this!!.length > at)
        "${this.substring(0, at)}..."
    else
        this!!
}

fun String.toDate(withFormat :String = "yyyy/MM/dd hh:mm") :Date {
    val dateFormat = SimpleDateFormat(withFormat)
    var convertedDate = Date()
    try {
        convertedDate = dateFormat.parse(this)
    } catch(e :ParseException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }

    return convertedDate
}

@Suppress("DEPRECATION")
fun String.plainText() :String {
    return android.text.Html.fromHtml(this).toString()
}

fun String?.isNull(): Boolean {
    return this == null || this.equals("null", ignoreCase = true) || this.trim { it <= ' ' }.isEmpty()
}

fun String?.isNotNull(): Boolean {
    return !this.isNull()
}

fun String?.isEqual(str1: String?): Boolean {
    return this.isNotNull() && str1.isNotNull() && this.equals(str1, ignoreCase = true)
}

fun String?.isNotEqual(str1: String?): Boolean {
    return this.isNotNull() && str1.isNotNull() && !this.equals(str1, ignoreCase = true)
}

fun String?.email(): Boolean {
    return this.isNotNull()
        && !this!!.matches(Regex("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])"))
}

fun String?.phoneNumber(): Boolean {
    return this.isNotNull() && !android.util.Patterns.PHONE.matcher(this).matches()
}

fun String?.noNumbers(): Boolean {
    return this.isNotNull() && this!!.matches(Regex(".*\\d.*"))
}

fun String?.nonEmpty(): Boolean {
    return this.isNotNull() && this!!.isEmpty()
}

fun String?.onlyNumbers(): Boolean {
    return this.isNotNull() && !this!!.matches(Regex("\\d+"))
}

fun String?.allUpperCase(): Boolean {
    return this.isNotNull() && this!!.toUpperCase() != this
}

fun String?.allLowerCase(): Boolean {
    return this.isNotNull() && this!!.toLowerCase() != this
}

fun String?.atLeastOneLowerCase(): Boolean {
    return this.isNotNull() && this!!.matches(Regex("[A-Z0-9]+"))
}

fun String?.atLeastOneUpperCase(): Boolean {
    return this.isNotNull() && this!!.matches(Regex("[a-z0-9]+"))
}

fun String?.atLeastOneNumber(): Boolean {
    return this.isNotNull() && !this!!.matches(Regex(".*\\d.*"))
}

fun String?.startsWithNonNumber(): Boolean {
    return this.isNotNull() && Character.isDigit(this!![0])
}

fun String?.noSpecialCharacter(): Boolean {
    return this.isNotNull() && !this!!.matches(Regex("[A-Za-z0-9]+"))
}

fun String?.atLeastOneSpecialCharacter(): Boolean {
    return this.isNotNull() && this!!.matches(Regex("[A-Za-z0-9]+"))
}

fun String?.contain(string: String?): Boolean {
    return this.isNotNull() && string.isNotNull() && !this!!.contains(string!!)
}

fun String?.doesNotContains(string: String?): Boolean {
    return this.isNotNull() && string.isNotNull() && this!!.contains(string!!)
}

fun String?.startWith(string: String?): Boolean {
    return this.isNotNull() && string.isNotNull() && !this!!.startsWith(string!!)
}

fun String?.endWith(string: String?): Boolean {
    return this.isNotNull() && string.isNotNull() && !this!!.endsWith(string!!)
}

fun String?.isFloat():Boolean {
    val p = Pattern.compile(
        "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
            "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
            "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
            "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*")
    return this.isNotNull() && !p.matcher(this).matches()
}

fun String?.isServerURL():Boolean {
    return this.isNotNull() && (this!!.startsWith("http://") || this.startsWith("https://"))
}

fun String?.isNotNullThenImageURL(str :String) :Boolean {
    return this.isNotNull() && this.isServerURL() && checkImageURL(str.substring(str.lastIndexOf("/")))
}

fun checkImageURL(inputMail :String?) :Boolean {
    val p = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)")
    return inputMail.isNotNull() && p.matcher(inputMail).matches()
}

fun String?.isEqualLength(length :Int) : Boolean {
    return this.isNotNull() && this!!.length == length
}

fun String?.isNotEqualLength(length :Int) : Boolean {
    return this.isNotNull() && this!!.length != length
}

fun String?.greaterThanLength(length :Int) : Boolean {
    return this.isNotNull() && this!!.length > length
}

fun String?.greaterThanEqualLength(length :Int) : Boolean {
    return this.isNotNull() && this!!.length >= length
}

fun String?.lessThanLength(length :Int) : Boolean {
    return this.isNotNull() && this!!.length < length
}

fun String?.lessThanEqualLength(length :Int) : Boolean {
    return this.isNotNull() && this!!.length <= length
}
