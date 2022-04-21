package com.mypc.jettipapp.utils

fun calcTotalPerPerson(bill:Float,split:Int,tip:Float):Double{
    return (bill+(tip*bill/100))/split.toDouble()
}