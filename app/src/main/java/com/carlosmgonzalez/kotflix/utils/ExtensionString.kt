package com.carlosmgonzalez.kotflix.utils

fun String.truncate(): String {
    return if (this.length > 20) {
         this.take(18) + "..."
    } else {
         this
    }
}