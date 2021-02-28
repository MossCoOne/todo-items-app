package com.mossco.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mossco.todoapp.database.TodoItem
import com.mossco.todoapp.databinding.TodoItemLayoutBinding

class TodoItemsAdapter(private val itemClickListener: ListItemClickListener) :
    ListAdapter<TodoItem, TodoItemsAdapter.TodoItemViewHolder>(NoteListDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        return TodoItemViewHolder.fromParent(parent)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.bindData(todoItem, itemClickListener)
    }

    class TodoItemViewHolder private constructor(private val binding: TodoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(todoItem: TodoItem, itemClickListener: ListItemClickListener) {
            binding.todoItem = todoItem
            binding.executePendingBindings()
            binding.isCompleteCheckBox.setOnClickListener {
                todoItem.apply {
                    isComplete = binding.isCompleteCheckBox.isChecked
                }
                itemClickListener.onCheckBoxToggled(todoItem)
            }

            binding.deleteButton.setOnClickListener {
                itemClickListener.onDeleteButtonClicked(todoItem.id)
            }
        }

        companion object {
            fun fromParent(parent: ViewGroup): TodoItemViewHolder {
                val binding = TodoItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return TodoItemViewHolder(binding)
            }
        }

    }

    interface ListItemClickListener {
        fun onCheckBoxToggled(todoItem: TodoItem)
        fun onDeleteButtonClicked(itemId: Int)
    }
}

class NoteListDiffCallBack : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }

}