package com.mossco.todoapp

import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mossco.todoapp.database.TodoItem


@BindingAdapter("todoItemTitle")
fun TextView.setTitle(todoItem: TodoItem) {
    todoItem.let {
        text = todoItem.title
    }
}

@BindingAdapter("todoItemDescription")
fun TextView.setDescription(todoItem: TodoItem) {
    todoItem.let {
        text = todoItem.description
    }
}

@BindingAdapter("isTodoItemCompleted")
fun CheckBox.isItemCompleted(todoItem: TodoItem) {
    todoItem.let {
        isChecked = todoItem.isComplete
    }
}