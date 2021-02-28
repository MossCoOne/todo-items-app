package com.mossco.todoapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoItem constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var description: String,
    var isComplete: Boolean
) {
    constructor(title: String, description: String, isComplete: Boolean) : this(
        0,
        title,
        description,
        isComplete
    )
}