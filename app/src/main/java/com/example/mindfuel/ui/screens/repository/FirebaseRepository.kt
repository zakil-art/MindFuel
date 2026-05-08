package com.example.mindfuel.ui.screens.repository

import com.example.mindfuel.data.Entry
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository {

    private val db = FirebaseDatabase.getInstance().getReference("entries")

    fun saveEntry(entry: Entry) {
        val id = db.push().key ?: return
        db.child(id).setValue(entry)
    }
}