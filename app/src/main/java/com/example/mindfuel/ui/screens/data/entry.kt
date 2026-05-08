package com.example.mindfuel.data

data class Entry(
    val id: String = "",
    val userId: String = "",
    val text: String = "",
    val mood: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
