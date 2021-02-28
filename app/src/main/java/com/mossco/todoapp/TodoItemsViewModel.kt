package com.mossco.todoapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mossco.todoapp.database.TodoItem
import com.mossco.todoapp.database.TodoItemsDatabase
import com.mossco.todoapp.database.TodoItemsDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TodoItemsViewModel(application: Application) : AndroidViewModel(application) {
    private val todoItemDao: TodoItemsDatabaseDao =
        TodoItemsDatabase.getInstance(application).todoItemDatabaseDao
    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val todoItemList: LiveData<List<TodoItem>> = todoItemDao.getAllItems()
    val percentageOfCompletedItems: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val showFeedbackMessageEvent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private suspend fun insertItem(todoItem: TodoItem) {
        todoItemDao.insertItem(todoItem)
    }

    private suspend fun updateItem(todoItem: TodoItem) {
        todoItemDao.updateItem(todoItem.isComplete, todoItem.id)
    }

    private suspend fun deleteItem(todoItemId: Int) {
        todoItemDao.removeItem(todoItemId)
    }

    private suspend fun deleteAllItems() {
        todoItemDao.deleteAllItems()
    }

    fun onCheckBoxToggled(todoItem: TodoItem) {
        viewModelScope.launch {
            updateItem(todoItem)
        }
    }

    fun onAddItemClicked(item: TodoItem) {
        viewModelScope.launch {
            insertItem(item)
        }
    }

    fun updateProgressBar(it: List<TodoItem>?) {
        val totalNumberOfItems: Int = it?.size ?: 0
        val completedItems: Int? = it?.filter { it.isComplete }?.size
        if (completedItems == 0) {
            percentageOfCompletedItems.value = 0
        } else {
            percentageOfCompletedItems.value = completedItems?.times(100)?.div(totalNumberOfItems)
        }
    }

    fun onDeleteButtonClicked(itemId: Int) {
        viewModelScope.launch {
            deleteItem(itemId)
        }
    }

    fun onDeleteAllClicked() {
        viewModelScope.launch {
            deleteAllItems()
            showFeedbackMessageEvent.value =
                getApplication<Application>().getString(R.string.item_deleted)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
