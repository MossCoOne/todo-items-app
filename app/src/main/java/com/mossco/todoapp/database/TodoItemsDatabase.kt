package com.mossco.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoItem::class],version = 1,exportSchema = false)
abstract class TodoItemsDatabase : RoomDatabase(){

    abstract val todoItemDatabaseDao: TodoItemsDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: TodoItemsDatabase? = null

        fun getInstance(context: Context): TodoItemsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoItemsDatabase::class.java,
                        "todo_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}