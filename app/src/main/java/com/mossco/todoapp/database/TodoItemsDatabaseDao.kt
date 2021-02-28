package com.mossco.todoapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoItemsDatabaseDao {
    @Query("SELECT * FROM todoitem")
    fun getAllItems(): LiveData<List<TodoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(todoItem: TodoItem)

    @Query("DELETE FROM todoitem WHERE id = :itemId")
    suspend fun removeItem(itemId: Int)

    @Query("DELETE FROM todoitem")
    suspend fun deleteAllItems()

    @Query("UPDATE todoitem SET isComplete = :isComplete WHERE id = :itemId")
    suspend fun updateItem(isComplete: Boolean, itemId: Int)
}